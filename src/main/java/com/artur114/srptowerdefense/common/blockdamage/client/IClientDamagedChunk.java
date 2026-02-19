package com.artur114.srptowerdefense.common.blockdamage.client;

import com.artur114.srptowerdefense.common.blockdamage.IDamagedChunk;
import net.minecraft.nbt.NBTTagCompound;

public interface IClientDamagedChunk extends IDamagedChunk {
    void processSyncData(NBTTagCompound dataIn);
    void unloadChunk();
    void update();

    default boolean isRemote() {return true;}
}
