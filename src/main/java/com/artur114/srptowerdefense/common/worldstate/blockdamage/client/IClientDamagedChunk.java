package com.artur114.srptowerdefense.common.worldstate.blockdamage.client;

import com.artur114.srptowerdefense.common.worldstate.blockdamage.IDamagedChunk;
import net.minecraft.nbt.NBTTagCompound;

public interface IClientDamagedChunk extends IDamagedChunk {
    void processSyncData(NBTTagCompound dataIn);
    void draw();

    default boolean isRemote() {return true;}
}
