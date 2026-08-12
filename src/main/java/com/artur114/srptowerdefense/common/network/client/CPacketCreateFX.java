package com.artur114.srptowerdefense.common.network.client;

import com.artur114.bananalib.math.m3d.vec.IVec3D;
import com.artur114.srptowerdefense.client.fx.FXBlockPunch;
import com.artur114.srptowerdefense.common.network.base.NBTPacketBase;
import com.artur114.srptowerdefense.main.SRPTDMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class CPacketCreateFX extends NBTPacketBase {
    private static final Logger log = LogManager.getLogger("SRPTD/Network");
    private BlockPos pos;
    private FXType type;

    public CPacketCreateFX() {}

    public CPacketCreateFX(BlockPos pos, FXType type) {
        this(pos, type, null);
    }

    public CPacketCreateFX(BlockPos pos, FXType type, @Nullable NBTTagCompound nbt) {
        super(nbt);
        this.type = type;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        int id = buf.readInt();
        if (id >= 0 && id < FXType.values().length) {
            this.type = FXType.values()[id];
        } else {
            log.warn("Unknown fx id {}", id);
        }
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(this.type.ordinal());
        buf.writeLong(this.pos.toLong());
    }

    public enum FXType {
        BLOCK_PUNCH;
    }

    public static class HandlerCFX implements IMessageHandler<CPacketCreateFX, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(CPacketCreateFX message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                switch (message.type) {
                    case BLOCK_PUNCH:
                        FXBlockPunch.draw(message.pos, message.nbt);
                        break;
                    default:
                        log.warn("Unknown fx type {}", message.type);
                }
            });
            return null;
        }
    }

    public static void sendBlockPunch(World world, BlockPos pos, EnumFacing facing, Vec3d vec, int power) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("facing", (byte) facing.ordinal());
        nbt.setDouble("vecX", vec.x);
        nbt.setDouble("vecY", vec.y);
        nbt.setDouble("vecZ", vec.z);
        nbt.setInteger("power", power);
        send(world, pos, FXType.BLOCK_PUNCH, nbt);
    }

    public static void send(World world, BlockPos pos, FXType type) {
        send(world, pos, type, null);
    }

    public static void send(World world, BlockPos pos, FXType type, @Nullable NBTTagCompound nbt) {
        NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 32);
        SRPTDMain.NETWORK.sendToAllAround(new CPacketCreateFX(pos, type, nbt), point);
    }
}