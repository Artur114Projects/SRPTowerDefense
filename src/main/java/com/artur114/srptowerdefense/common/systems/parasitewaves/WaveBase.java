package com.artur114.srptowerdefense.common.systems.parasitewaves;

import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaveBase implements IWave {

    @Override
    public void move(IVec2D vec) {

    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public float speed() {
        return 0;
    }

    @Override
    public IVec2D targetChunk() {
        return null;
    }

    @Override
    public BlockPos target() {
        return null;
    }

    @Override
    public IVec2D pos() {
        return null;
    }

    @Override
    public IBox2I box() {
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return null;
    }
}
