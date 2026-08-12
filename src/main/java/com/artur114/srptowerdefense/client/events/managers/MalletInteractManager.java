package com.artur114.srptowerdefense.client.events.managers;

import com.artur114.srptowerdefense.common.items.ItemMallet;
import com.artur114.srptowerdefense.common.network.server.SPacketMalletClick;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.MouseEvent;

public class MalletInteractManager {
    public void mouseEvent(MouseEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || (e.getButton() != 0 && e.getButton() != 1)) {
            return;
        }

        EnumHand hand = e.getButton() == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        ItemStack held = mc.player.getHeldItem(hand);

        if (held.getItem() instanceof ItemMallet) {
            if (e.isButtonstate()) {
                mc.player.swingArm(hand);
                SRPTDMain.NETWORK.sendToServer(new SPacketMalletClick(hand));
                ((ItemMallet) held.getItem()).onClicked(hand);
            }
            e.setCanceled(true);
        }
    }
}
