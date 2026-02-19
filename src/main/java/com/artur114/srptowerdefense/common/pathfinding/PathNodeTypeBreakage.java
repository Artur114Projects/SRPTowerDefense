package com.artur114.srptowerdefense.common.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;

public class PathNodeTypeBreakage implements IForcedPathNodeType {
    private final BreakArea breakArea;
    private float priority;

    public PathNodeTypeBreakage(BlockPos nodePos, float priority) {
        this.priority = priority;

        this.breakArea = new BreakArea(nodePos);
    }

    public void applyBreakageNode(PathNodeTypeBreakage node) {
        this.breakArea.add(node.breakArea);
        this.priority += node.priority;
    }

    public BreakArea getBreakArea() {
        return this.breakArea;
    }

    public float getBreakPriority() {
        return this.priority;
    }

    @Override
    public float getPriority() {
        return 4.0F;
    }

    @Override
    public float getPriority(EntityLiving entity) {
        return 4.0F;
    }

    @Override
    public PathNodeType toMc() {
        return PathNodeType.WALKABLE;
    }

    @Override
    public int ord() {
        return -1;
    }
}
