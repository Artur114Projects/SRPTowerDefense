package com.artur114.srptowerdefense.common.pathfinding;

import com.artur114.srptowerdefense.common.blockdamage.BlockDamageHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        Path path = this.getPath();
        if (this.entity.ticksExisted % 8 == 0 && !this.noPath() && path != null) {
            for (int i = -1; i != 1; i++) {
                PathPoint point = path.getPathPointFromIndex(Math.max(0, path.getCurrentPathIndex() + i));

                if (point instanceof PathPointForced) {
                    BreakArea area = ((PathPointForced) point).posToBreak;

                    if (area != null) {
                        if (area.entityDamage(this.entity, 64)) {
                            this.timeoutTimer = 0;
                        }
                    }
                }
            }
        }
    }
}
