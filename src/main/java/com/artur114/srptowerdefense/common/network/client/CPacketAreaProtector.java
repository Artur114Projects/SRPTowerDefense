package com.artur114.srptowerdefense.common.network.client;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.srptowerdefense.client.gui.GuiAreaProtector;
import com.artur114.srptowerdefense.common.network.base.NBTPacketBase;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketAreaProtector extends NBTPacketBase {
    public CPacketAreaProtector() {}

    public CPacketAreaProtector(NBTTagCompound nbt) {
        super(nbt);
    }

    public static class HandlerCAP implements IMessageHandler<CPacketAreaProtector, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(CPacketAreaProtector message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                if (message.nbt.getInteger("action") == -1) {
                    NBTTagList list = message.nbt.getTagList("prot", 4);
                    long[] prot = new long[list.tagCount()];
                    for (int i = 0; i != list.tagCount(); i++) prot[i] = ((NBTTagLong) list.get(i)).getLong();
                    mc.displayGuiScreen(new GuiAreaProtector(BlockPos.fromLong(message.nbt.getLong("pos")), prot, message.nbt.getBoolean("active"), message.nbt.getInteger("range")));
                } else {
                    if (mc.currentScreen instanceof GuiAreaProtector) {
                        ((GuiAreaProtector) mc.currentScreen).messageFromServer(message.nbt);
                    }
                }

            });
            return null;
        }
    }

    public static void sendOpenGui(EntityPlayerMP player, BlockPos pos, long[] prot, boolean active, int range) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("action", -1);
        nbt.setLong("pos", pos.toLong());
        NBTTagList list = new NBTTagList();
        for (long chunk : prot) list.appendTag(new NBTTagLong(chunk));
        nbt.setTag("prot", list);
        nbt.setBoolean("active", active);
        nbt.setInteger("range", range);
        SRPTDMain.NETWORK.sendTo(new CPacketAreaProtector(nbt), player);
    }

    public static void sendAcceptActivateRequest(EntityPlayerMP player) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("action", 0);
        SRPTDMain.NETWORK.sendTo(new CPacketAreaProtector(nbt), player);
    }

    public static void sendAcceptProtectRequest(EntityPlayerMP player, ChunkPos pos, boolean state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("action", 1);
        nbt.setLong("pos", BananaMC.chunkPosAsLong(pos));
        nbt.setBoolean("state", state);
        SRPTDMain.NETWORK.sendTo(new CPacketAreaProtector(nbt), player);
    }
}
