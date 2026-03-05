package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.util.nbt.IReadFromNBT;
import com.artur114.bananalib.util.nbt.IWriteToNBT;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public interface ITowerDefenceObject extends IReadFromNBT, IWriteToNBT {
    void init(WorldServer world, TowerDefenceManager owner, int id);
    void onChunkLoaded(Chunk chunk);
    int ticksToUpdate();
    boolean isAlive();
    void onRemove();
    void update();
    int id();
    IVec2D pos();
    IBox2I box();
}
