package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.math.m2d.box.Box2I;
import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.Vec2D;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

public interface IWaveTarget extends ITowerDefenceObject {
    boolean isForcedChunk(IVec2D pos);
    BlockPos[] causalBlocks();
    BlockPos causePos();

    IWaveTarget INVALID = new IWaveTarget() {
        @Override
        public boolean isForcedChunk(IVec2D pos) {return false;}
        @Override
        public BlockPos[] causalBlocks() {return new BlockPos[0];}
        @Override
        public BlockPos causePos() {return BlockPos.ORIGIN;}
        @Override
        public void init(WorldServer world, TowerDefenceManager owner, int id) {}
        @Override
        public void onChunkLoaded(Chunk chunk) {}
        @Override
        public int ticksToUpdate() {return 1;}
        @Override
        public boolean isAlive() {return false;}
        @Override
        public void onRemove() {}
        @Override
        public void update() {}
        @Override
        public int id() {return -1;}
        @Override
        public IVec2D pos() {return Vec2D.ZERO;}
        @Override
        public IBox2I box() {return new Box2I(0, 0, 0, 0);}
        @Override
        public void readFromNBT(@NotNull NBTTagCompound nbt) {}
        @Override
        public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {return nbt;}
    };
}
