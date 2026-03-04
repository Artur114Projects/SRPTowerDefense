package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.util.nbt.IReadFromNBT;
import com.artur114.bananalib.util.nbt.IWriteToNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public interface IWave extends IReadFromNBT, IWriteToNBT {
    void init(WorldServer world, TowerDefenceManager owner, int id);
    void onEntryToLoadedChunk(Chunk chunk);
    void onEntityDied(WaveEntityData entity);
    void ondChunkLoaded(Chunk chunk);
    void move(IVec2D vec);
    boolean isAlive();
    float speed();
    int id();
    IVec2D targetChunk();
    BlockPos target();
    IVec2D pos();
    IBox2I box();
}
