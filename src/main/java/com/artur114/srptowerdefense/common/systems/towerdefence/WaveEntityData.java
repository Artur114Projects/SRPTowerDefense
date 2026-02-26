package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class WaveEntityData {
    public final EntityParasiteBase entity;
    public NBTTagCompound data;

    public WaveEntityData(EntityParasiteBase entity) {
        this.data = new NBTTagCompound();
        this.entity = entity;
    }

    public void bindWave(IWave wave) {

    }

    public boolean isBindToWave() {
        return false;
    }

    public int waveId() {
        return 0;
    }
}
