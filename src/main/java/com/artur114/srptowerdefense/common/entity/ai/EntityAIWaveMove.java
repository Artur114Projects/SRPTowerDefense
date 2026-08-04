package com.artur114.srptowerdefense.common.entity.ai;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.IWave;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceEntity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIWaveMove extends EntityAIBase {
    private final PosMc3IM prevTarget = new PosMc3IM();
    private final TowerDefenceEntity waveData;
    private final EntityCreature creature;

    public EntityAIWaveMove(TowerDefenceEntity data) {
        this.creature = data.entity;
        this.waveData = data;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return this.waveData.isBindToTDObj() && this.waveData.isObjInstanceOf(IWave.class) && this.creature.getAttackTarget() == null;
    }

    @Override
    public void updateTask() {
        if (this.waveData.moveSpeed() != -1.0F) {
            this.creature.getNavigator().setSpeed(this.waveData.moveSpeed());
            if (this.creature.ticksExisted % 8 == 0 && (!this.prevTarget.equals(this.waveData.moveTarget()) || this.creature.getNavigator().noPath())) {
                BlockPos pos = this.waveData.moveTarget();
                if (pos != null) {
                    this.creature.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY() + 1, pos.getZ(), this.waveData.moveSpeed());
                    this.prevTarget.set(pos);
                }
            }
        }
    }

    @Override
    public void resetTask() {
        this.creature.getNavigator().clearPath();
    }
}
