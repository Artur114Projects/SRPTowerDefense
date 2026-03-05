package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.vec.IVec2I;
import net.minecraft.util.math.BlockPos;

public interface IWaveTarget {
    BlockPos[] causalBlocks();
    BlockPos causePos();
    IVec2I causeChunk();
    boolean isValide();
}
