package com.artur114.srptowerdefense.common.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;

public enum PathNodeTypeForced implements IForcedPathNodeType {
    BLOCKED(-1.0F),
    OPEN(0.0F),
    WALKABLE(0.0F),
    TRAPDOOR(0.0F),
    FENCE(-1.0F),
    LAVA(-1.0F),
    WATER(8.0F),
    RAIL(0.0F),
    DANGER_FIRE(8.0F),
    DAMAGE_FIRE(16.0F),
    DANGER_CACTUS(8.0F),
    DAMAGE_CACTUS(-1.0F),
    DANGER_OTHER(8.0F),
    DAMAGE_OTHER(-1.0F),
    DOOR_OPEN(0.0F),
    DOOR_WOOD_CLOSED(-1.0F),
    DOOR_IRON_CLOSED(-1.0F);

    private final float priority;

    PathNodeTypeForced(float priorityIn) {
        this.priority = priorityIn;
    }

    public float getPriority() {
        return this.priority;
    }

    @Override
    public float getPriority(EntityLiving entity) {
        return entity.getPathPriority(this.toMc());
    }

    @Override
    public PathNodeType toMc() {
        return PathNodeType.valueOf(this.name());
    }

    @Override
    public int ord() {
        return this.ordinal();
    }

    public static IForcedPathNodeType fromMc(PathNodeType pathNodeType) {
        return PathNodeTypeForced.valueOf(pathNodeType.name());
    }
}
