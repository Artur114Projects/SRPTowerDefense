package com.artur114.srptowerdefense.common.network.server;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.srptowerdefense.client.gui.GuiAreaProtector;
import com.artur114.srptowerdefense.common.network.base.NBTPacketBase;
import com.artur114.srptowerdefense.common.network.client.CPacketAreaProtector;
import com.artur114.srptowerdefense.common.tileentity.TileEntityAreaProtector;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SPacketAreaProtector extends NBTPacketBase {
    public SPacketAreaProtector() {}

    public SPacketAreaProtector(NBTTagCompound nbt) {
        super(nbt);
    }

    public static class HandlerSAP implements IMessageHandler<SPacketAreaProtector, IMessage> {

        @Override
        public IMessage onMessage(SPacketAreaProtector message, MessageContext ctx) {
            ctx.getServerHandler().player.mcServer.addScheduledTask(() -> {
                BlockPos pos = BlockPos.fromLong(message.nbt.getLong("pos"));
                int dimension = message.nbt.getInteger("dimension");
                World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
                if (!world.isBlockLoaded(pos)) {
                    return;
                }

                TileEntity tileRaw = world.getTileEntity(pos);
                if (tileRaw instanceof TileEntityAreaProtector) {
                    ((TileEntityAreaProtector) tileRaw).messageFromGui(ctx.getServerHandler().player, message.nbt);
                }
            });
            return null;
        }
    }


    public static void sendRadiusChange(TileEntityAreaProtector tile, int radius) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("action", 0);
        nbt.setLong("pos", tile.getPos().toLong());
        nbt.setInteger("dimension", tile.getWorld().provider.getDimension());
        nbt.setInteger("range", radius);
        SRPTDMain.NETWORK.sendToServer(new SPacketAreaProtector(nbt));
    }

    public static void sendActivateRequest(TileEntityAreaProtector tile) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("action", 1);
        nbt.setLong("pos", tile.getPos().toLong());
        nbt.setInteger("dimension", tile.getWorld().provider.getDimension());
        SRPTDMain.NETWORK.sendToServer(new SPacketAreaProtector(nbt));
    }

    public static void sendProtectRequest(TileEntityAreaProtector tile, ChunkPos pos, boolean state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("action", 2);
        nbt.setLong("pos", tile.getPos().toLong());
        nbt.setInteger("dimension", tile.getWorld().provider.getDimension());
        nbt.setLong("chunk", BananaMC.chunkPosAsLong(pos));
        nbt.setBoolean("state", state);
        SRPTDMain.NETWORK.sendToServer(new SPacketAreaProtector(nbt));
    }
}
