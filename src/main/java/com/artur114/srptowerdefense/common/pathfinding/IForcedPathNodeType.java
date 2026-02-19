package com.artur114.srptowerdefense.common.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;

public interface IForcedPathNodeType {
    float getPriority();
    float getPriority(EntityLiving entity);
    PathNodeType toMc();
    int ord();
}
