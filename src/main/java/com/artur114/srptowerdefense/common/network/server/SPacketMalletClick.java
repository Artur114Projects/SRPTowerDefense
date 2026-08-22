package com.artur114.srptowerdefense.common.network.server;

import com.artur114.srptowerdefense.common.items.ItemMallet;
import com.artur114.srptowerdefense.common.network.client.CPacketCreateFX;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketMalletClick implements IMessage {
    public EnumHand side;

    public SPacketMalletClick() {}

    public SPacketMalletClick(EnumHand side) {
        this.side = side;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.side = EnumHand.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.side.ordinal());
    }

    public static class HandlerPMC implements IMessageHandler<SPacketMalletClick, IMessage> {

        @Override
        public IMessage onMessage(SPacketMalletClick message, MessageContext ctx) {
            ctx.getServerHandler().player.mcServer.addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                ItemStack stack = player.getHeldItem(message.side);

                if (!(stack.getItem() instanceof ItemMallet)) {
                    return;
                }

                RayTraceResult ray = this.rayTrace(player);
                if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
                    if (!player.isCreative()) {
                        stack.damageItem(1, player);
                    }

                    if (!stack.isEmpty()) {
                        ((ItemMallet) stack.getItem()).onBlockClick(player, player.world, ray.getBlockPos(), ray.sideHit, stack);
                        CPacketCreateFX.sendBlockPunch(player.world, ray.getBlockPos(), ray.sideHit, ray.hitVec, ((ItemMallet) stack.getItem()).punchPower());
                    }
                }
            });
            return null;
        }

        public RayTraceResult rayTrace(EntityPlayerMP player) {
            double reachDist = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
            Vec3d look = player.getLook(1);
            Vec3d start = player.getPositionEyes(1);
            Vec3d end = start.addVector(look.x * reachDist, look.y * reachDist, look.z * reachDist);
            return player.world.rayTraceBlocks(start, end, false, false, true);
        }
    }
}
