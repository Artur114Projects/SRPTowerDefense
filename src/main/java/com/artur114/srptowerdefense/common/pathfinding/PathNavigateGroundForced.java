package com.artur114.srptowerdefense.common.pathfinding;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PathNavigateGroundForced extends PathNavigateGround {
    public PathNavigateGroundForced(EntityLiving entityLiving, World world) {
        super(entityLiving, world);
    }

    @Override
    protected @NotNull PathFinder getPathFinder() {
        this.nodeProcessor = new WalkNodeProcessorForced();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinderForced((WalkNodeProcessorForced) this.nodeProcessor);
    }

    @Override
    public void onUpdateNavigation() {
        super.onUpdateNavigation();

        if (this.entity instanceof EntityParasiteBase) {
            ((EntityParasiteBase) this.entity).setSkillBreakBlocksValues(0.0F, 0, 0);
        }

        Path path = this.getPath();
        if (this.entity.ticksExisted % 8 == 0 && !this.noPath() && path != null) {
            for (int i = -1; i != 1; i++) {
                PathPoint point = path.getPathPointFromIndex(Math.max(0, path.getCurrentPathIndex() + i));

                if (point instanceof PathPointForced) {
                    BreakArea area = ((PathPointForced) point).posToBreak;

                    if (area != null) {
                        if (area.entityDamage(this.entity, 128)) {
                            this.ticksAtLastPos = this.totalTicks;
                            this.timeoutTimer = 0;
                        }
                    }
                }
            }
        }
    }
}
