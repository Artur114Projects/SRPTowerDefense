package com.artur114.srptowerdefense.common.events;

import com.artur114.srptowerdefense.common.events.managers.*;
import jdk.nashorn.internal.ir.Block;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonEventsHandler {
    public static final ParasiteAIRebuildManager PARASITE_AI_REBUILD_MANAGER = new ParasiteAIRebuildManager();
    public static final ParasiteSpawnManager PARASITE_SPAWN_MANAGER = new ParasiteSpawnManager();
    public static final BlockBreakManager BLOCK_BREAK_MANAGER = new BlockBreakManager();

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent e) {
        PARASITE_AI_REBUILD_MANAGER.entityJoinWorldEvent(e);
    }

    @SubscribeEvent
    public static void spawnEvent(LivingSpawnEvent.CheckSpawn e) {
        PARASITE_SPAWN_MANAGER.livingSpawnEventCheckSpawn(e);
    }

    @SubscribeEvent
    public static void livingBreak(LivingDestroyBlockEvent e) {
        BLOCK_BREAK_MANAGER.livingDestroyBlockEvent(e);
    }
}
