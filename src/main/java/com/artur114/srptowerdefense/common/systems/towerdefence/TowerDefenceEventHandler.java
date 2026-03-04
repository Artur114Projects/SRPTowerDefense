package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.vec.Vec2I;
import com.artur114.srptowerdefense.common.capabilities.GenericCapProviderS;
import com.artur114.srptowerdefense.common.capabilities.SRPTDCapabilities;
import com.artur114.srptowerdefense.main.SRPTDMain;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@Mod.EventBusSubscriber
public class TowerDefenceEventHandler {
    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() != null && e.getObject() instanceof EntityParasiteBase && e.getObject().world != null && !e.getObject().world.isRemote) e.addCapability(new ResourceLocation(SRPTDMain.MODID, "wave_data"), new GenericCapProviderS<>(new WaveEntityData((EntityParasiteBase) e.getObject()), SRPTDCapabilities.WAVE_ENTITY_DATA));
    }

    @SubscribeEvent
    public static void attachCapabilitiesWorld(AttachCapabilitiesEvent<World> e) {
        if (e.getObject() != null && !e.getObject().isRemote) e.addCapability(new ResourceLocation(SRPTDMain.MODID, "waves_system"), new GenericCapProviderS<>(new TowerDefenceManager((WorldServer) e.getObject()), SRPTDCapabilities.TOWER_DEFENCE_SYSTEM));
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.START && !e.world.isRemote && e.world.getTotalWorldTime() % 8 == 0) {
            TowerDefenceManager manager = e.world.getCapability(SRPTDCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.update();


                if (e.world.getTotalWorldTime() % 1200 == 0) { // Debug
                    Random rand = new Random();
                    manager.addWave(new WaveTest(new Vec2I(20, 0)), rand.nextInt());
                }
            }
        }
    }

    @SubscribeEvent
    public static void livingDead(LivingDeathEvent e) {
        if (e.getEntity().world != null && !e.getEntity().world.isRemote) {
            TowerDefenceManager manager = e.getEntity().world.getCapability(SRPTDCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.entityDead(e.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void canDeSpawn(LivingSpawnEvent.AllowDespawn e) {
        if (!e.getWorld().isRemote && e.getEntity() != null) {
            WaveEntityData data = e.getEntity().getCapability(SRPTDCapabilities.WAVE_ENTITY_DATA, null);
            if (data != null && data.isBindToWave()) {
                e.setResult(Event.Result.DENY);
            }
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
    public static void chunkSave(ChunkDataEvent.Save e) {
        if (!e.getWorld().isRemote) {
            TowerDefenceManager manager = e.getWorld().getCapability(SRPTDCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.chunkSave(e.getData());
            }
        }
    }
}
