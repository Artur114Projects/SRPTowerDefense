package com.artur114.srptowerdefense.client.events;

import com.artur114.srptowerdefense.client.events.managers.EntityPathDrawManager;
import com.artur114.srptowerdefense.client.events.managers.ToolTipManager;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientEventsHandler {
    public static final EntityPathDrawManager ENTITY_PATH_DRAW_MANAGER = new EntityPathDrawManager();
    public static final ToolTipManager TOOL_TIP_MANAGER = new ToolTipManager();

    @SubscribeEvent
    public static void renderWorldLast(RenderWorldLastEvent e) {
        ENTITY_PATH_DRAW_MANAGER.renderWorldLastEvent(e);
    }

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent e) {
        TOOL_TIP_MANAGER.itemTooltipEvent(e);
    }
}
