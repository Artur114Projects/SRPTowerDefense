package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import net.minecraft.nbt.NBTTagCompound;

public interface ITDEntityManager {
    NBTTagCompound modifyEntityData(NBTTagCompound entity);
    void onEntityDied(TowerDefenceEntity entity);
}
