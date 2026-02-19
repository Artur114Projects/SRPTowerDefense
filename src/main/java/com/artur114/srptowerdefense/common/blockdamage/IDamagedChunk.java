package com.artur114.srptowerdefense.common.blockdamage;

import net.minecraft.util.math.BlockPos;

public interface IDamagedChunk {
    int MAX_DAMAGE = 127;

    boolean isRemote();
    int getDamage(BlockPos pos);
    int getDamage(int x, int y, int z);
    boolean isEmpty();
}
