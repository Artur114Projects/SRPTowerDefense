package com.artur114.srptowerdefense.common.pathfinding;

import com.artur114.srptowerdefense.common.worldstate.blockdamage.BlockDamageHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class BreakArea implements Iterable<BlockPos> {
    private BlockPos[] area;

    public BreakArea(BlockPos[] area) {
        this.area = area;
    }

    public BreakArea(BlockPos pos) {
        this.area = new BlockPos[] {pos.toImmutable()};
    }

    public void add(BreakArea area) {
        BlockPos[] copy = new BlockPos[this.area.length + area.area.length];

        System.arraycopy(this.area, 0, copy, 0, this.area.length);
        System.arraycopy(area.area, 0, copy, this.area.length, area.area.length);

        this.area = copy;
    }

    public BreakArea copy() {
        return new BreakArea(this.area);
    }

    public boolean entityDamage(EntityLiving entity, int damagePerSecondToOneBlock) {
        boolean flag = false;
        for (int i = 0; i != this.area.length; i++) {
            BlockPos pos = this.area[i];
            if (!entity.world.isAirBlock(pos)) {
                BlockDamageHandler.entityDamage(entity, pos, damagePerSecondToOneBlock);
                if (entity.world.isAirBlock(pos)) {
                    flag = true;
                } else {
                    return true;
                }
            }
        }

        return flag;
    }

    @NotNull
    @Override
    public Iterator<BlockPos> iterator() {
        return Arrays.asList(this.area).iterator();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.area);
    }
}
