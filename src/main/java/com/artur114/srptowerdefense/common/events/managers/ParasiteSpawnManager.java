package com.artur114.srptowerdefense.common.events.managers;

import com.artur114.bananalib.math.m2d.box.Box2I;
import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.mc.math.m2d.vec.PosMc2I;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ParasiteSpawnManager {
    public void livingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn e) {
        IBox2I box = new Box2I(0, 0, 0, 0).grow(4);

        if (box.contains(((int) e.getX()) >> 4, ((int) e.getZ()) >> 4)) {
            TowerDefenceEntity data = e.getEntityLiving().getCapability(InitCapabilities.TD_ENTITY_DATA, null);
            if (data != null && !data.isBindToTDObj()) {
                e.setResult(Event.Result.DENY);
            }
        }
    }

    public void livingSpawnEventAllowDespawn(LivingSpawnEvent.AllowDespawn e) {
        TowerDefenceEntity data = e.getEntityLiving().getCapability(InitCapabilities.TD_ENTITY_DATA, null);
        if (data != null && !data.canDespawn()) {
            e.setResult(Event.Result.DENY);
        }
    }
}
