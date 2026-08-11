package com.artur114.srptowerdefense.common.pathfinding;

import com.artur114.srptowerdefense.common.worldstate.blockdamage.registry.EntityDamageRegistry;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PathNavigateGroundForced extends PathNavigateGround {
    public int timeFromLastDamage = 0;
    private double speedInWater;
    private double speedOnGround;

    public PathNavigateGroundForced(EntityLiving entityLiving, World world) {
        super(entityLiving, world);
    }

    @Override
    public float getPathSearchRange() {
        return 48.0F;
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
        if (!this.noPath() && path != null) {
            if (this.entity.ticksExisted % 8 == 0) {
                PathPoint point = path.getPathPointFromIndex(path.getCurrentPathIndex());
                if (point.y > this.entity.posY && this.entity.getDistanceSq(point.x + 0.5, point.y + 0.5, point.z + 0.5) < 2.0F * 2.0F) {
                    this.entity.getJumpHelper().setJumping();
                }
                this.entity.getLookHelper().setLookPosition(point.x, point.y, point.z, (float) this.entity.getHorizontalFaceSpeed(), (float) this.entity.getVerticalFaceSpeed());
            }
            if (this.entity.ticksExisted % 20 == 0) {
                this.timeFromLastDamage += 20;
                for (int i = -1; i != 1; i++) {
                    PathPoint point = path.getPathPointFromIndex(Math.max(0, path.getCurrentPathIndex() + i));

                    if (point instanceof PathPointForced) {
                        BreakArea area = ((PathPointForced) point).posToBreak;

                        if (area != null) {
                            if (area.entityDamage(this.entity, EntityDamageRegistry.damageOf(this.entity))) {
                                this.ticksAtLastPos = this.totalTicks;
                                this.timeFromLastDamage = 0;
                                this.timeoutTimer = 0;
                            }
                        }
                    }
                }
            }
        }
    }
}
