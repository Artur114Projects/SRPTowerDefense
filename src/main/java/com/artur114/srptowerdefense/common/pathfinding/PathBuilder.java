package com.artur114.srptowerdefense.common.pathfinding;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;

import java.util.Arrays;

public class PathBuilder {
    private final Int2ObjectMap<BreakArea> breakAreas = new Int2ObjectOpenHashMap<>();
    private PathPointForced[] points = new PathPointForced[16];
    private int cursor = 0;
    public PathPointForced lastPoint = null;
    public PathPointForced target = null;
    public float lpDistanceFromOrigin;
    public float lpDistanceToTarget;
    public float totalPathDistance;
    public float totalBreakCost;
    public float totalPathCost;

    protected PathBuilder() {}

    protected PathBuilder setTarget(PathPointForced target) {
        this.target = target;
        return this;
    }

    protected PathBuilder clear() {
        this.cursor = 0;
        this.lastPoint = null;

        this.lpDistanceFromOrigin = 0;
        this.lpDistanceToTarget = 0;

        this.totalPathDistance = 0;
        this.totalBreakCost = 0;
        this.totalPathCost = 0;
        this.breakAreas.clear();
        Arrays.fill(this.points, null);

        return this;
    }

    protected PathBuilder copyFrom(PathBuilder parent) {
        if (parent.cursor > 0) {
            if (this.points.length < parent.points.length) {
                this.points = Arrays.copyOf(parent.points, parent.points.length);
            } else {
                System.arraycopy(parent.points, 0, this.points, 0, parent.cursor);
            }
        }
        this.lastPoint = parent.lastPoint;
        this.cursor = parent.cursor;

        this.lpDistanceFromOrigin = parent.lpDistanceFromOrigin;
        this.lpDistanceToTarget = parent.lpDistanceToTarget;

        this.totalPathDistance = parent.totalPathDistance;
        this.totalBreakCost = parent.totalBreakCost;
        this.totalPathCost = parent.totalPathCost;
        this.breakAreas.putAll(parent.breakAreas);

        return this;
    }

    public boolean isEnded() {
        return this.target.equals(this.lastPoint);
    }

    public PathBuilder addPoint(PathPointForced point) {
        if (this.cursor >= this.points.length) {
            this.points = Arrays.copyOf(this.points, this.points.length * 2);
        }
        this.points[this.cursor++] = point;
        point.bestTotalCost = this.totalPathCost;
        if (this.lastPoint != null) {
            float distanceToNew = this.lastPoint.distanceManhattan(point);
            float costedDistance = distanceToNew + point.costMalus;


            this.totalPathDistance += costedDistance;

            this.lpDistanceFromOrigin += distanceToNew;
        }
        if (point.posToBreak != null) {
            this.breakAreas.put(point.hashCode(), point.posToBreak.copy());
        }
        this.lastPoint = point;
        this.totalBreakCost += point.breakCost;
        this.totalPathCost = this.totalPathDistance + this.totalBreakCost;
        this.lpDistanceToTarget = this.lastPoint.distanceManhattan(this.target) * 4.0F + this.totalPathDistance + (0.2F * this.totalBreakCost);
        return this;
    }

    public Path build() {
        PathPoint[] points = new PathPoint[this.cursor];
        PathPoint prev = null;
        for (int i = 0; i != this.cursor; i++) {
            PathPointForced point = this.points[i];

            if (point == null) {
                continue;
            }

            point.index = i;
            point.posToBreak = this.breakAreas.get(point.hashCode());

            if (prev != null) {
                point.previous = prev;

                float f = prev.distanceManhattan(point);
                point.distanceFromOrigin = prev.distanceFromOrigin + f;
                point.cost = f + point.costMalus;
                point.totalPathDistance = prev.totalPathDistance + point.cost;
                point.distanceToNext = point.distanceManhattan(this.target) + point.costMalus;
                point.distanceToTarget = point.totalPathDistance + point.distanceToNext;
            } else {
                point.totalPathDistance = 0.0F;
                point.distanceToNext = point.distanceManhattan(this.target);
                point.distanceToTarget = point.distanceToNext;
            }

            points[i] = point;
            prev = point;
        }

        if (points.length == 0) {
            return null;
        } else {
            return new Path(points);
        }
    }
}
