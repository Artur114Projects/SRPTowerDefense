package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.nbt.NBTTagCompound;

public interface ITDEntityManager {
    NBTTagCompound modifyEntityData(NBTTagCompound entity);
    void onEntityDied(TowerDefenceEntity entity);
    void onEntityEvolved(TowerDefenceEntity oldEntity, TowerDefenceEntity newEntity);
}
