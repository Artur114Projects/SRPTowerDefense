package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m3d.vec.AdvancedBlockPos;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class WaveEntityData implements INBTSerializable<NBTTagCompound> {
    public final EntityParasiteBase entity;
    public NBTTagCompound data;
    private AdvancedBlockPos blockPos;
    private float speed;
    private IWave wave;

    public WaveEntityData(EntityParasiteBase entity) {
        this.blockPos = new AdvancedBlockPos();
        this.data = new NBTTagCompound();
        this.entity = entity;
        this.speed = -1.0F;
    }

    public WaveEntityData kill() {
        this.data = new NBTTagCompound();
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

    public void bindWave(IWave wave) {
        this.entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, Integer.MAX_VALUE, 0, false, false));
        this.entity.cannotDespawn(false);
        this.entity.setWait(0);
        this.wave = wave;
    }

    public boolean isBindToWave() {
        return this.wave != null;
    }

    public int waveId() {
        return this.wave.id();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("bindToWave", this.isBindToWave());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {}
}
