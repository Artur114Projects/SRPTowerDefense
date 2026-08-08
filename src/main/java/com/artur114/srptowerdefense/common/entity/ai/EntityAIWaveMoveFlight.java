package com.artur114.srptowerdefense.common.entity.ai;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.IWave;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceEntity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIWaveMoveFlight extends EntityAIBase {
    private final TowerDefenceEntity waveData;
    private final EntityCreature creature;

    public EntityAIWaveMoveFlight(TowerDefenceEntity data) {
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
            BlockPos pos = this.waveData.moveTarget();
            if (pos != null) {
                this.creature.getMoveHelper().setMoveTo(pos.getX(), pos.getY() + 1 + 4, pos.getZ(), this.waveData.moveSpeed());
                this.creature.getLookHelper().setLookPosition(pos.getX(), pos.getY() + 1 + 4, pos.getZ(), 180.0F, 20.0F);
            }
        }
    }
}
