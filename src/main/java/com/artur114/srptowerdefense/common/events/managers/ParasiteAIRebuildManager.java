package com.artur114.srptowerdefense.common.events.managers;

import com.artur114.bananalib.mc.cap.BananaCaps;
import com.artur114.srptowerdefense.common.entity.ai.EntityAIWaveMoveFlight;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.common.entity.ai.EntityAIBreakGears;
import com.artur114.srptowerdefense.common.entity.ai.EntityAIWaveMove;
import com.artur114.srptowerdefense.common.pathfinding.PathNavigateGroundForced;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceEntity;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParasiteAIRebuildManager {
    public void entityJoinWorldEvent(EntityJoinWorldEvent e) {
        Entity entity = e.getEntity();

        if (!e.getWorld().isRemote && entity instanceof EntityParasiteBase && !(entity instanceof EntityPStationary)) {
            EntityParasiteBase parasite = (EntityParasiteBase) entity;

            BananaCaps.capability(parasite, InitCapabilities.TD_ENTITY_DATA).ifPresent(data -> {
                if (data.isBindToTDObj()) {
                    parasite.setSkillBreakBlocksValues(0.0F, 0, 0);
                    if (parasite.navigator instanceof PathNavigateGround) {
                        parasite.navigator = new PathNavigateGroundForced(parasite, parasite.world);
                    }
                    this.openPriority(parasite.tasks, 4);
                    if (parasite.navigator instanceof PathNavigateFlying) {
                        parasite.tasks.addTask(4, new EntityAIWaveMoveFlight(data));
                    } else {
                        parasite.tasks.addTask(4, new EntityAIBreakGears(parasite, parasite.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                        parasite.tasks.addTask(4, new EntityAIWaveMove(data));
                    }
                }
            });
        }
    }

    private void openPriority(EntityAITasks tasks, int priority) {
        Iterator<EntityAITasks.EntityAITaskEntry> iterator = tasks.taskEntries.iterator();
        List<EntityAITasks.EntityAITaskEntry> ret = new ArrayList<>();
        while (iterator.hasNext()) {
            EntityAITasks.EntityAITaskEntry entry = iterator.next();

            if (entry.priority >= priority) {
                ret.add(tasks.new EntityAITaskEntry(entry.priority + 1, entry.action));
            } else {
                ret.add(entry);
            }

            iterator.remove();
        }
        tasks.taskEntries.addAll(ret);
    }
}
