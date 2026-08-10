package com.artur114.srptowerdefense.common.events.managers;

import com.artur114.bananalib.math.m2d.box.Box2I;
import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.mc.math.m2d.vec.PosMc2I;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ProtectedZone;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceEntity;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceManager;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ParasiteSpawnManager {
    public void livingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn e) {
        TowerDefenceEntity data = e.getEntityLiving().getCapability(InitCapabilities.TD_ENTITY_DATA, null);

        if (data == null) {
            return;
        }

        if (data.isBindToTDObj()) {
            return;
        }

        TowerDefenceManager manager = e.getWorld().getCapability(InitCapabilities.TOWER_DEFENCE_SYSTEM, null);

        if (manager != null) {
            for (ProtectedZone zone : manager.tdObjects(ProtectedZone.class)) {
                if (!zone.canParasiteLocateIn(((int) e.getX()) >> 4, ((int) e.getZ()) >> 4)) {
                    e.setResult(Event.Result.DENY); return;
                }
            }
        }
    }
}
