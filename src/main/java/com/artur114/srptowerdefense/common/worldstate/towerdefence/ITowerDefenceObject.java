package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.mc.nbt.INBTSerializable;
import com.artur114.bananalib.mc.nbt.IReadFromNBT;
import com.artur114.bananalib.mc.nbt.IWriteToNBT;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public interface ITowerDefenceObject extends INBTSerializable {
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
