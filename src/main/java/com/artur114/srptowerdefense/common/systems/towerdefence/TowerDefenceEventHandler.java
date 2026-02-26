package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.srptowerdefense.common.capabilities.GenericCapProviderS;
import com.artur114.srptowerdefense.common.capabilities.SRPTDCapabilities;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class TowerDefenceEventHandler {
    @SubscribeEvent
    public static void attachCapabilitiesWorld(AttachCapabilitiesEvent<World> e) {
        if (e.getObject() != null && !e.getObject().isRemote) e.addCapability(new ResourceLocation(SRPTDMain.MODID, "waves_system"), new GenericCapProviderS<>(new TowerDefenceManager((WorldServer) e.getObject()), SRPTDCapabilities.TOWER_DEFENCE_SYSTEM));
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.START && !e.world.isRemote && e.world.getTotalWorldTime() % 20 == 0) {
            TowerDefenceManager manager = e.world.getCapability(SRPTDCapabilities.TOWER_DEFENCE_SYSTEM, null);
            if (manager != null) manager.update();
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load e) {
        if (!e.getWorld().isRemote) {
            TowerDefenceManager manager = e.getWorld().getCapability(SRPTDCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.chunkLoad(e.getChunk());
            }
        }
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkEvent.Unload e) {
        if (!e.getWorld().isRemote) {
            TowerDefenceManager manager = e.getWorld().getCapability(SRPTDCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.chunkUnload(e.getChunk());
            }
        }

    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload e) {
        if (!e.getWorld().isRemote) {
            TowerDefenceManager manager = e.getWorld().getCapability(SRPTDCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.unload();
            }
        }
    }

}
