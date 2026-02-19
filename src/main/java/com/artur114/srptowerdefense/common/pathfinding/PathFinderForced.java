package com.artur114.srptowerdefense.common.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class PathFinderForced extends PathFinder {
    public static final int MAX_PATH_FINDING_ITR = 200;
    private final PathPointForced[] pathOptions = new PathPointForced[32];
    private final PathBuildHeap pathHeap = new PathBuildHeap();
    private final WalkNodeProcessorForced nodeProcessor;

    public PathFinderForced(WalkNodeProcessorForced processor) {
        super(processor);
        this.nodeProcessor = processor;
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, EntityLiving entity, Entity targetEntity, float maxDistance) {
        return this.findPath(worldIn, entity, targetEntity.posX, targetEntity.getEntityBoundingBox().minY, targetEntity.posZ, maxDistance);
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, EntityLiving entity, BlockPos targetPos, float maxDistance) {
        return this.findPath(worldIn, entity, (float)targetPos.getX() + 0.5F, (float)targetPos.getY() + 0.5F, (float)targetPos.getZ() + 0.5F, maxDistance);
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, EntityLiving entity, double x, double y, double z, float maxDistance) {
        this.nodeProcessor.init(worldIn, entity);
        PathPointForced start = this.nodeProcessor.getStart();
        PathPointForced end = this.nodeProcessor.getPathPointToCoords(x, y, z);
        Path path = this.findPath(start, end, maxDistance);
        this.nodeProcessor.postProcess();
        this.pathHeap.clearPath();
        return path;
    }

    @Nullable
    private Path findPath(PathPointForced pathFrom, PathPointForced pathTo, float maxDistance) {
        this.pathHeap.init(pathTo);
        PathBuilder path = this.pathHeap.copy(this.pathHeap.addPath(this.pathHeap.createBuilder().addPoint(pathFrom)));
        path.lpDistanceToTarget = Float.MAX_VALUE;
        int iterationCount = 0;

        while (!this.pathHeap.isPathEmpty()) {
            iterationCount++;

            if (iterationCount >= 200) {
                break;
            }

            PathBuilder current = this.pathHeap.dequeue();

            if (current.lastPoint == null) {
                this.pathHeap.releaseBuilder(current);
                continue;
            }

            if (current.isEnded()) {
                this.pathHeap.releaseBuilder(path);
                path = current;

                if (path.totalBreakCost > 0) {
                    continue;
                } else {
                    break;
                }
            }

            if (path.lastPoint.distanceManhattan(pathTo) > current.lastPoint.distanceManhattan(pathTo) || path.lpDistanceToTarget > current.lpDistanceToTarget) {
                this.pathHeap.releaseBuilder(path);
                path = this.pathHeap.copy(current);
            }

            if (path.lastPoint.equals(current.lastPoint) && (path.totalPathCost > current.totalPathCost)) {
                this.pathHeap.releaseBuilder(path);
                path = this.pathHeap.copy(current);
            }

            int optionsCount = this.nodeProcessor.findPathOptions(this.pathOptions, current.lastPoint, pathTo, Float.MAX_VALUE);

            for (int i = 0; i != optionsCount; i++) {
                PathPointForced option = this.pathOptions[i];

                if (current.totalPathCost < option.bestTotalCost) {
                    if (current.lpDistanceFromOrigin + current.lastPoint.distanceManhattan(option) < maxDistance) {
                        this.pathHeap.addPath(this.pathHeap.copy(current).addPoint(option));
                    }
                }
            }

            this.pathHeap.releaseBuilder(current);
        }

        this.pathHeap.releaseAll();
        Path build = path.build();
        this.pathHeap.releaseBuilder(path);

        return build;
    }
}