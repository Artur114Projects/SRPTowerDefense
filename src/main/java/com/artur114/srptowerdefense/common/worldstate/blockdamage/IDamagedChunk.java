package com.artur114.srptowerdefense.common.worldstate.blockdamage;

import net.minecraft.util.math.BlockPos;

public interface IDamagedChunk {
    int MAX_DAMAGE = 65535;

    boolean isRemote();
    int getDamage(BlockPos pos);
    int getDamage(int x, int y, int z);
    boolean isEmpty();
}
