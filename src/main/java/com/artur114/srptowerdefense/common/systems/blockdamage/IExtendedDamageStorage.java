package com.artur114.srptowerdefense.common.systems.blockdamage;

import com.artur114.bananalib.util.IReadFromNBT;
import com.artur114.bananalib.util.IWriteToNBT;
import net.minecraft.util.math.BlockPos;

public interface IExtendedDamageStorage extends IWriteToNBT, IReadFromNBT {

    int getDamage(int x, int y, int z);

    int getDamage(BlockPos pos);

    boolean setDamage(int x, int y, int z, int amount);

    boolean setDamage(BlockPos pos, int amount);

    boolean isEmpty();
}
