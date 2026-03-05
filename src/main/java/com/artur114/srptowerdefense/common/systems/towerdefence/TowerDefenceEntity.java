package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m3d.vec.AdvancedBlockPos;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class TowerDefenceEntity implements INBTSerializable<NBTTagCompound> {
    public final EntityParasiteBase entity;
    public NBTTagCompound data;
    private Object2BooleanMap<Class<? extends ITowerDefenceObject>> instanceOfMap = null;
    private AdvancedBlockPos blockPos;
    private ITowerDefenceObject tdObj;
    private float speed;

    public TowerDefenceEntity(EntityParasiteBase entity) {
        this.blockPos = new AdvancedBlockPos();
        this.data = new NBTTagCompound();
        this.entity = entity;
        this.speed = -1.0F;
    }

    public TowerDefenceEntity kill() {
        this.data = new NBTTagCompound();
        this.instanceOfMap = null;
        this.entity.isDead = true;
        this.blockPos = null;
        this.speed = -1.0F;
        return null;
    }

    public float moveSpeed() {
        return this.speed;
    }

    public void setMoveSpeed(float speed) {
        this.speed = speed;
    }

    public BlockPos moveTarget() {
        return this.blockPos;
    }

    public void setMoveTarget(BlockPos pos) {
        this.blockPos.setPos(pos);
    }

    public void bind(ITowerDefenceObject tdObj) {
        this.entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, Integer.MAX_VALUE, 0, false, false));
        this.entity.cannotDespawn(false);
        this.entity.setWait(0);
        this.tdObj = tdObj;
    }

    public boolean isObjInstanceOf(Class<? extends ITowerDefenceObject> clazz) {
        if (this.instanceOfMap == null) {
            this.instanceOfMap = new Object2BooleanOpenHashMap<>();
        }
        if (this.instanceOfMap.containsKey(clazz)) {
            return this.instanceOfMap.get(clazz);
        }
        boolean flag = clazz.isInstance(this.tdObj);
        this.instanceOfMap.put(clazz, flag);
        return flag;
    }

    public boolean isBindToTDObj() {
        return this.tdObj != null;
    }

    public int objectId() {
        return this.tdObj.id();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("bindToObject", this.objectId());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {}
}
