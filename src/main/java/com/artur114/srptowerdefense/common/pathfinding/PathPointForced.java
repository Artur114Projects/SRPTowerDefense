package com.artur114.srptowerdefense.common.pathfinding;

import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathPointForced extends PathPoint {
    public IForcedPathNodeType nodeTypeForced = PathNodeTypeForced.BLOCKED;
    public float bestTotalCost = Float.MAX_VALUE;
    public BreakArea posToBreak = null;
    public float breakCost;
    public PathPointForced(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    public @NotNull PathPointForced cloneMove(int x, int y, int z) {
        PathPointForced point = new PathPointForced(x, y, z);
        point.index = this.index;
        point.totalPathDistance = this.totalPathDistance;
        point.distanceToNext = this.distanceToNext;
        point.distanceToTarget = this.distanceToTarget;
        if (this.previous != null) {
            point.previous = this.previous.cloneMove(this.previous.x, this.previous.y, this.previous.z);
        }
        point.visited = this.visited;
        point.distanceFromOrigin = this.distanceFromOrigin;
        point.cost = this.cost;
        point.costMalus = this.costMalus;
        point.nodeType = this.nodeType;
        point.nodeTypeForced = this.nodeTypeForced;
        point.posToBreak = this.posToBreak == null ? null : this.posToBreak.copy();
        point.breakCost = this.breakCost;
        return point;
    }

    public void setNodeTypeForced(IForcedPathNodeType nodeTypeForced) {
        this.nodeTypeForced = nodeTypeForced;
        this.nodeType = nodeTypeForced.toMc();

        if (nodeTypeForced instanceof PathNodeTypeBreakage) {
            this.posToBreak = ((PathNodeTypeBreakage) nodeTypeForced).getBreakArea();
            this.breakCost = ((PathNodeTypeBreakage) nodeTypeForced).getBreakPriority();
        }
    }
}
