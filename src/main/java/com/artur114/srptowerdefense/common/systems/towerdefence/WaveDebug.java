package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.vec.IVec2I;
import com.artur114.bananalib.math.m2d.vec.Vec2I;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import net.minecraft.entity.EntityList;
import net.minecraft.util.math.BlockPos;

public class WaveDebug extends WaveAbstract {
    public WaveDebug(IVec2I pos) {
        super(pos, new WaveTargetDebug(), 1.4F);

        int v0 = 24;
        int v1 = this.rand.nextInt(v0);

        this.addEntities(new EntityCreatorRl("srparasites:rupter"), v1);
        this.addEntities(new EntityCreatorClass(EntityInfHuman.class), v0 - v1);
    }

    public static class WaveTargetDebug implements IWaveTarget {
        private static final BlockPos cause = new BlockPos(7, 80, 7);
        private static final BlockPos[] blocks = new BlockPos[] {cause};
        private static final IVec2I chunk = new Vec2I(0, 0);

        @Override
        public BlockPos[] causalBlocks() {
            return blocks;
        }

        @Override
        public BlockPos causePos() {
            return cause;
        }

        @Override
        public IVec2I causeChunk() {
            return chunk;
        }

        @Override
        public boolean isValide() {
            return true;
        }
    }
}
