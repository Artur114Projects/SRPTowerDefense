package com.artur114.srptowerdefense.common.events.managers;

import com.artur114.srptowerdefense.common.entity.ai.EntityAIBreakGears;
import com.artur114.srptowerdefense.common.entity.ai.EntityAIWaveMove;
import com.artur114.srptowerdefense.common.pathfinding.PathNavigateGroundForced;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ParasiteAIRebuildManager {
    public void entityJoinWorldEvent(EntityJoinWorldEvent e) {
        Entity entity = e.getEntity();

        if (!e.getWorld().isRemote && entity instanceof EntityParasiteBase) {
            EntityParasiteBase parasite = (EntityParasiteBase) entity;

//            IAttributeInstance attribute = parasite.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
//            attribute.setBaseValue(attribute.getAttributeValue() * 2);
            if (parasite.navigator instanceof PathNavigateGround) {
                parasite.navigator = new PathNavigateGroundForced(parasite, parasite.world);
            }
            parasite.setSkillBreakBlocksValues(0.0F, 0, 0);
            parasite.tasks.addTask(4, new EntityAIBreakGears(parasite, parasite.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            parasite.tasks.addTask(4, new EntityAIWaveMove(parasite, parasite.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
        }
    }
}
