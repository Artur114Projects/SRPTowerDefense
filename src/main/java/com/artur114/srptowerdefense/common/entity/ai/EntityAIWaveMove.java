package com.artur114.srptowerdefense.common.entity.ai;

import com.artur114.bananalib.math.m3d.vec.AdvancedBlockPos;
import com.artur114.srptowerdefense.common.systems.towerdefence.WaveEntityData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class EntityAIWaveMove extends EntityAIBase {
    private final EntityCreature creature;
    private final WaveEntityData waveData;

    public EntityAIWaveMove(WaveEntityData data) {
        this.creature = data.entity;
        this.waveData = data;
        this.setMutexBits(16);
    }

    @Override
    public boolean shouldExecute() {
        return this.waveData.isBindToWave() && this.creature.getAttackTarget() == null;
    }

    @Override
    public void updateTask() {
        if (this.waveData.moveSpeed() != -1.0F) {
            this.creature.getNavigator().setSpeed(this.waveData.moveSpeed());
            if (this.creature.ticksExisted % 8 == 0) {
                BlockPos pos = this.waveData.moveTarget();
                if (pos != null) this.creature.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY() + 1, pos.getZ(), this.waveData.moveSpeed());
            }
        }
    }

    @Override
    public void resetTask() {
        this.creature.getNavigator().clearPath();
    }
}
