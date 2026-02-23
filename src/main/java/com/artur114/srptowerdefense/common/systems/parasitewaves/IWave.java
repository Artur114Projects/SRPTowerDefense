package com.artur114.srptowerdefense.common.systems.parasitewaves;

import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.util.IReadFromNBT;
import com.artur114.bananalib.util.IWriteToNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IWave extends IReadFromNBT, IWriteToNBT {
    void move(IVec2D vec);
    boolean isAlive();
    float speed();
    IVec2D targetChunk();
    BlockPos target();
    IVec2D pos();
    IBox2I box();
}
