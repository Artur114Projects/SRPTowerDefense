package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.math.m2d.vec.Vec2I;
import com.artur114.bananalib.mc.cap.BananaCapProv;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
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
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@Mod.EventBusSubscriber
public class TowerDefenceEventHandler {

    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() != null && e.getObject() instanceof EntityParasiteBase && e.getObject().world != null && !e.getObject().world.isRemote) e.addCapability(new ResourceLocation(SRPTDMain.MODID, "tower_defence_entity"), new BananaCapProv<>(new TowerDefenceEntity((EntityParasiteBase) e.getObject()), InitCapabilities.TD_ENTITY_DATA));
    }

    @SubscribeEvent
    public static void attachCapabilitiesWorld(AttachCapabilitiesEvent<World> e) {
        if (e.getObject() != null && !e.getObject().isRemote) e.addCapability(new ResourceLocation(SRPTDMain.MODID, "waves_system"), new BananaCapProv<>(new TowerDefenceManager((WorldServer) e.getObject()), InitCapabilities.TOWER_DEFENCE_SYSTEM));
    }

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load e) {
        if (e.getWorld() != null && !e.getWorld().isRemote) {
            TowerDefenceManager manager = e.getWorld().getCapability(InitCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.load();
            }
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.START && !e.world.isRemote) {
            TowerDefenceManager manager = e.world.getCapability(InitCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.update();
            }
        }
    }

    @SubscribeEvent
    public static void livingDead(LivingDeathEvent e) {
        if (e.getEntity().world != null && !e.getEntity().world.isRemote) {
            TowerDefenceManager manager = e.getEntity().world.getCapability(InitCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.entityDead(e.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void canDeSpawn(LivingSpawnEvent.AllowDespawn e) {
        if (!e.getWorld().isRemote && e.getEntity() != null) {
            TowerDefenceEntity data = e.getEntity().getCapability(InitCapabilities.TD_ENTITY_DATA, null);
            if (data != null && !data.canDespawn()) {
                e.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load e) {
        if (!e.getWorld().isRemote) {
            TowerDefenceManager manager = e.getWorld().getCapability(InitCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.chunkLoad(e.getChunk());
            }
        }
    }

    @SubscribeEvent
    public static void chunkSave(ChunkDataEvent.Save e) {
        if (!e.getWorld().isRemote) {
            TowerDefenceManager manager = e.getWorld().getCapability(InitCapabilities.TOWER_DEFENCE_SYSTEM, null);

            if (manager != null) {
                manager.chunkSave(e.getData());
            }
        }
    }
}
