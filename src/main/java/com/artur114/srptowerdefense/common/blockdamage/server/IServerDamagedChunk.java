package com.artur114.srptowerdefense.common.blockdamage.server;

import com.artur114.srptowerdefense.common.blockdamage.IDamagedChunk;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IServerDamagedChunk extends IDamagedChunk, INBTSerializable<NBTTagCompound> {
    void damage(BlockPos pos, int amount);
    void repair(BlockPos pos, int amount);
    void damage(int x, int y, int z, int amount);
    void repair(int x, int y, int z, int amount);
    float damageMultiplierFor(BlockPos pos);
    float repairMultiplierFor(BlockPos pos);
    void syncToClient(EntityPlayerMP clientIn);
    void sendInitialDataToClient(EntityPlayerMP clientIn);
    void onBlockBreak(BlockPos pos);
    ChunkPos getPos();
    int getDimension();

    default boolean isRemote() {return false;}
}
