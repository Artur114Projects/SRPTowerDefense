package com.artur114.srptowerdefense.common.network.client;

import com.artur114.srptowerdefense.common.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.blockdamage.client.IClientDamagedChunk;
import com.artur114.srptowerdefense.common.capabilities.TowerDefenceCapabilities;
import com.artur114.srptowerdefense.common.util.math.MathUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketSyncBlocksDamage implements IMessage {
    private NBTTagCompound data;
    private ChunkPos chunk;
    private int dimension;

    public CPacketSyncBlocksDamage() {}

    public CPacketSyncBlocksDamage(int dimensionIn, ChunkPos chunkIn, NBTTagCompound dataIn) {
        this.dimension = dimensionIn;
        this.chunk = chunkIn;
        this.data = dataIn;

        this.data.setLong("chunkPos", MathUtils.chunkPosAsLong(chunkIn));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.dimension = buf.readInt();

        this.data = ByteBufUtils.readTag(buf);

        this.chunk = MathUtils.chunkPosFromLong(this.data.getLong("chunkPos"));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.dimension);

        ByteBufUtils.writeTag(buf, this.data);
    }

    @SideOnly(Side.CLIENT)
    public static class HandlerSPC implements IMessageHandler<CPacketSyncBlocksDamage, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(CPacketSyncBlocksDamage message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.world.provider.getDimension() == message.dimension) {
                    Chunk chunk = mc.world.getChunkFromChunkCoords(message.chunk.x, message.chunk.z);
                    IDamagedChunk protectedChunk = chunk.getCapability(TowerDefenceCapabilities.BLOCK_DAMAGE, null);
                    if (protectedChunk instanceof IClientDamagedChunk) {
                        ((IClientDamagedChunk) protectedChunk).processSyncData(message.data);
                    }
                }
            });
            return null;
        }
    }
}
