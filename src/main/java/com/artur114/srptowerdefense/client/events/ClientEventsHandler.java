package com.artur114.srptowerdefense.client.events;

import com.artur114.srptowerdefense.client.events.managers.EntityPathDrawManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ClientEventsHandler {
    public static final EntityPathDrawManager ENTITY_PATH_DRAW_MANAGER = new EntityPathDrawManager();

    @SubscribeEvent
    public static void renderWorldLast(RenderWorldLastEvent e) {
        ENTITY_PATH_DRAW_MANAGER.renderWorldLastEvent(e);
    }
}
