package com.artur114.srptowerdefense.common.blockdamage;

import com.artur114.srptowerdefense.common.util.interfaces.IReadFromNBT;
import com.artur114.srptowerdefense.common.util.interfaces.IWriteToNBT;
import net.minecraft.util.math.BlockPos;

public interface IExtendedDamageStorage extends IWriteToNBT, IReadFromNBT {

    int getDamage(int x, int y, int z);

    int getDamage(BlockPos pos);

    boolean setDamage(int x, int y, int z, int amount);

    boolean setDamage(BlockPos pos, int amount);

    boolean isEmpty();
}
