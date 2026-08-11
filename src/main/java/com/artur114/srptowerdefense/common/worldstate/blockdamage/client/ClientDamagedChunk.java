package com.artur114.srptowerdefense.common.worldstate.blockdamage.client;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.DamagedChunk;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.ExtendedDamageStorageMapped;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.IExtendedDamageStorage;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ClientDamagedChunk extends DamagedChunk implements IClientDamagedChunk {
    private static final TextureAtlasSprite[] destroyBlockIcons = grabDestroyStageArray();

    public ClientDamagedChunk(ChunkPos pos, int dimension) {
        super(pos, dimension);
    }

    @Override
    public void processSyncData(NBTTagCompound dataIn) {
        if (dataIn.hasKey("dataChange")) {
            this.processDataChange(dataIn.getTagList("dataChange", 10));
        } else if (dataIn.hasKey("storages")) {
            this.processInitialData(dataIn.getTagList("storages", 10));
        } else {
            this.crearChunkData();
        }
    }

    @Override
    public void draw() {
        Minecraft mc = Minecraft.getMinecraft();
        int from = MathHelper.clamp(((int) mc.player.posY - 64) >> 4, 0, this.storages.length - 1);
        int to = MathHelper.clamp(((int) mc.player.posY + 64) >> 4, 0, this.storages.length - 1);

        for (int i = from; i != to + 1; i++) {
            ExtendedDamageStorageMapped storage = (ExtendedDamageStorageMapped) this.storages[i];
            if (storage != null && !storage.isEmpty()) {
                this.drawStorage(storage, storage.allBlocksWithData(this.pos, i));
            }
        }
    }

    public void drawStorage(IExtendedDamageStorage storage, Iterable<PosMc3IM> iterable) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;

        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.preRenderDamagedBlocks();
        bufferBuilder.begin(7, DefaultVertexFormats.BLOCK);
        bufferBuilder.setTranslation(-Particle.interpPosX, -Particle.interpPosY, -Particle.interpPosZ);
        bufferBuilder.noColor();


        for (PosMc3IM pos : iterable) {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            TileEntity te = world.getTileEntity(pos);
            boolean hasBreak = block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull;
            if (!hasBreak) hasBreak = te != null && te.canRenderBreaking();

            if (!hasBreak && state.getMaterial() != Material.AIR) {
                TextureAtlasSprite textureatlassprite = destroyBlockIcons[BananaMath.round(9.0F * ((float) storage.getDamage(pos) / MAX_DAMAGE))];
                mc.getBlockRendererDispatcher().renderBlockDamage(state, pos.toImmutable(), textureatlassprite, world);
            }
        }

        tessellator.draw();
        bufferBuilder.setTranslation(0.0D, 0.0D, 0.0D);
        this.postRenderDamagedBlocks();
    }


    private void preRenderDamagedBlocks() {
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        GlStateManager.doPolygonOffset(-1.0F, -10.0F);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
    }

    private void postRenderDamagedBlocks() {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0F, 0.0F);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    private void processDataChange(NBTTagList list) {
        int[] posBuff = new int[3];
        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            int storageIndex = data.getByte("storage");
            int packedPos = data.getShort("packedPos");
            int newDamage = data.getShort("damage") & 0xFFFF;

            IExtendedDamageStorage storage = this.storages[storageIndex];

            if (storage == null) {
                storage = new ExtendedDamageStorageMapped();
                this.storages[storageIndex] = storage;
                this.initStoragesCount++;
            }

            int[] buff = this.unpackPos(posBuff, (short) packedPos);

            storage.setDamage(buff[0], buff[1], buff[2], newDamage);

            if (storage.isEmpty()) {
                this.storages[storageIndex] = null;
                this.initStoragesCount--;
            }
        }
    }

    private void processInitialData(NBTTagList list) {
        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            ExtendedDamageStorageMapped storage = new ExtendedDamageStorageMapped();
            storage.readFromNBT(data);
            int index = data.getInteger("storageIndex");
            this.storages[index] = storage;
            this.initStoragesCount++;
        }
    }

    private void crearChunkData() {
        Arrays.fill(this.storages, null);
        this.initStoragesCount = 0;
    }

    private int[] unpackPos(int[] posBuf, short packedPos) {
        posBuf[0] = (packedPos >> 8) & 15;
        posBuf[1] = (packedPos >> 4) & 15;
        posBuf[2] = packedPos & 15;
        return posBuf;
    }

    private static TextureAtlasSprite[] grabDestroyStageArray() {
        try {
            String name = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "destroyBlockIcons" : "field_94141_F";
            Field field = RenderGlobal.class.getDeclaredField(name);
            field.setAccessible(true);
            return (TextureAtlasSprite[]) field.get(Minecraft.getMinecraft().renderGlobal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return this.pos.toString() + " isEmpty:" + this.isEmpty();
    }
}
