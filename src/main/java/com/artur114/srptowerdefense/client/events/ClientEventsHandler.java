package com.artur114.srptowerdefense.client.events;

import com.artur114.srptowerdefense.client.events.managers.MalletInteractManager;
import com.artur114.srptowerdefense.client.events.managers.ToolTipManager;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventsHandler {
    public static final MalletInteractManager MALLET_INTERACT_MANAGER = new MalletInteractManager();
    public static final ToolTipManager TOOL_TIP_MANAGER = new ToolTipManager();

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent e) {
        TOOL_TIP_MANAGER.itemTooltipEvent(e);
    }

    @SubscribeEvent
    public static void mouseEvent(MouseEvent e) {
        MALLET_INTERACT_MANAGER.mouseEvent(e);
    }
}
