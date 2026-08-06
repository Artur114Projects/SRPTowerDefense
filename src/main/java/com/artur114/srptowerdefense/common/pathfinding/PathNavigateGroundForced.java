package com.artur114.srptowerdefense.common.pathfinding;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PathNavigateGroundForced extends PathNavigateGround {
    private double speedInWater;
    private double speedOnGround;

    public PathNavigateGroundForced(EntityLiving entityLiving, World world) {
        super(entityLiving, world);
    }

    @Override
    public void setSpeed(double speedIn) {
        this.speedInWater = speedIn * 6;
        this.speedOnGround = speedIn;
    }

    @Override
    public boolean setPath(@Nullable Path pathentityIn, double speedIn) {
        boolean flag = super.setPath(pathentityIn, speedIn);
        this.setSpeed(speedIn);
        return flag;
    }

    @Override
    protected @NotNull PathFinder getPathFinder() {
        this.nodeProcessor = new WalkNodeProcessorForced();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinderForced((WalkNodeProcessorForced) this.nodeProcessor);
    }

    @Override
    public void onUpdateNavigation() {
        if (this.entity.isInWater()) {
            this.speed = this.speedInWater;
        } else {
            this.speed = this.speedOnGround;
        }

        super.onUpdateNavigation();

        if (this.entity instanceof EntityParasiteBase) {
            ((EntityParasiteBase) this.entity).setSkillBreakBlocksValues(0.0F, 0, 0);
        }


        Path path = this.getPath();
        if (this.entity.ticksExisted % 8 == 0 && !this.noPath() && path != null) {
            if (path.getPathPointFromIndex(path.getCurrentPathIndex()).y > this.entity.posY) {
                PathPoint point = path.getPathPointFromIndex(path.getCurrentPathIndex());
                if (this.entity.getDistanceSq(point.x, point.y, point.z) < 1.5F * 1.5F) {
                    this.entity.getJumpHelper().setJumping();
                }
            }
            for (int i = -1; i != 1; i++) {
                PathPoint point = path.getPathPointFromIndex(Math.max(0, path.getCurrentPathIndex() + i));

                if (point instanceof PathPointForced) {
                    BreakArea area = ((PathPointForced) point).posToBreak;

                    if (area != null) {
                        if (area.entityDamage(this.entity, 128 * 8)) {
                            this.ticksAtLastPos = this.totalTicks;
                            this.timeoutTimer = 0;
                        }
                    }
                }
            }
        }
    }
}
