package com.artur114.srptowerdefense.common.worldstate.blockdamage.client;

import com.artur114.bananalib.mc.cap.BananaCapProvNoSave;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;

public class ClientBlockDamageManager {
    public void renderWorldLastEvent(RenderWorldLastEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        WorldClient world = mc.world;
        int range = 5;

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

        for (int x = mc.player.chunkCoordX - range; x != mc.player.chunkCoordX + range + 1; x++) {
            for (int z = mc.player.chunkCoordZ - range; z != mc.player.chunkCoordZ + range + 1; z++) {
                Chunk chunk = world.getChunkProvider().getLoadedChunk(x, z);

                if (chunk != null) {
                    IDamagedChunk damagedChunk = chunk.getCapability(InitCapabilities.BLOCK_DAMAGE, null);

                    if (damagedChunk != null) {
                        ((IClientDamagedChunk) damagedChunk).draw();
                    }
                }
            }
        }

        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.disableBlend();
    }

    public void attachCapabilitiesEventChunk(AttachCapabilitiesEvent<Chunk> e) {
        e.addCapability(new ResourceLocation(SRPTDMain.MODID, "blocks_damage"), new BananaCapProvNoSave<>(new ClientDamagedChunk(e.getObject().getPos(), e.getObject().getWorld().provider.getDimension()), InitCapabilities.BLOCK_DAMAGE));
    }
}
