package com.artur114.srptowerdefense.common.systems.towerdefence;

import net.minecraft.nbt.NBTTagCompound;

public interface ITDEntityManager {
    NBTTagCompound modifyEntityData(NBTTagCompound entity);
    void onEntityDied(TowerDefenceEntity entity);
}
