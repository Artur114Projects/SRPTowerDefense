package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.mc.cap.BananaCaps;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class TowerDefenceEntity implements INBTSerializable<NBTTagCompound> {
    private Object2BooleanMap<Class<?>> instanceOfMap = null;
    private int unnaturalLocationCounter = 0;
    public final EntityParasiteBase entity;
    private ITowerDefenceObject tdObj;
    private boolean isDirectTarget;
    public NBTTagCompound data;
    private PosMc3IM blockPos;
    private float speed;

    public TowerDefenceEntity(EntityParasiteBase entity) {
        this.blockPos = new PosMc3IM();
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

    public void setDirectTarget(boolean directTarget) {
        this.isDirectTarget = directTarget;
    }

    public BlockPos moveTarget() {
        return this.blockPos;
    }

    public void setMoveTarget(BlockPos pos) {
        this.blockPos.set(pos);
    }

    public boolean isDirectTarget() {
        return this.isDirectTarget;
    }

    public void tickOnUnnaturalLocation() {
        if (this.unnaturalLocationCounter > 16 * 60 * 20) {
            this.entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 1);
        }

        if (this.unnaturalLocationCounter > 20 * 60 * 20) {
            this.entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 3);
        }

        if (this.unnaturalLocationCounter > 24 * 60 * 20) {
            this.entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, 8);
            this.entity.setFire(2);
        }

        this.unnaturalLocationCounter += 32;
    }

    public void bind(ITowerDefenceObject tdObj) {
        if (tdObj != null) {
//            this.entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, Integer.MAX_VALUE, 0, false, false));
            this.entity.cannotDespawn(false);
            this.entity.setWait(0);
        } else {
//            this.entity.clearActivePotions();
            this.entity.cannotDespawn(true);
            this.entity.setWait(0);
        }
        this.tdObj = tdObj;
    }

    public boolean isObjInstanceOf(Class<?> clazz) {
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

    public void onEvolved(EntityParasiteBase newEntity) {
        if (this.isBindToTDObj() && this.isObjInstanceOf(ITDEntityManager.class)) {
            BananaCaps.capability(newEntity, InitCapabilities.TD_ENTITY_DATA).ifPresent(data -> {
                ((ITDEntityManager) this.tdObj).onEntityEvolved(this, data);
            });
        }
    }

    public boolean canDespawn() {
        return !this.isBindToTDObj();
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
        if (this.isBindToTDObj()) compound.setInteger("bindToObject", this.objectId());
        compound.setInteger("unnaturalLocationCounter", this.unnaturalLocationCounter);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.unnaturalLocationCounter = nbt.getInteger("unnaturalLocationCounter");
    }
}
