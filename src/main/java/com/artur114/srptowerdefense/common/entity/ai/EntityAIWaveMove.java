package com.artur114.srptowerdefense.common.entity.ai;

import com.artur114.srptowerdefense.common.util.math.AdvancedBlockPos;
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
    private final IAttributeInstance pathSearchRange;
    private final EntityCreature creature;
    private final double movementSpeed;
    private final World world;

    public EntityAIWaveMove(EntityCreature theCreatureIn, double movementSpeedIn) {
        this.pathSearchRange = theCreatureIn.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        this.movementSpeed = Math.max(1.0D, movementSpeedIn);
        this.creature = theCreatureIn;
        this.world = theCreatureIn.world;
        this.setMutexBits(16);
    }

    @Override
    public boolean shouldExecute() {
        return this.creature.getAttackTarget() == null;
    }

    @Override
    public void updateTask() {
        if (this.creature.getNavigator().noPath()) {
            AdvancedBlockPos blockPos = AdvancedBlockPos.obtain();
            if (Math.sqrt(this.creature.getDistanceSq(this.target())) < this.pathSearchRange.getAttributeValue() + 8) {
                blockPos.setPos(this.target());
                this.creature.getNavigator().tryMoveToXYZ(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), this.movementSpeed);
            } else {
                blockPos.setPos(target()).subtract(this.creature.getPosition()).setY(0);
                Vec3d vec = new Vec3d(blockPos).normalize();
                Random rand = this.creature.getRNG();

                double range = this.pathSearchRange.getAttributeValue();

                for (int i = 0; i != 4; i++) {
                    if (!this.creature.getNavigator().noPath()) {
                        break;
                    }

                    int r = 4;

                    int x = MathHelper.floor((range * 0.8) * vec.x) + (rand.nextInt(r * 2) - r);
                    int z = MathHelper.floor((range * 0.8) * vec.z) + (rand.nextInt(r * 2) - r);

                    blockPos.setPos(this.creature).add(x, 0, z).setWorldY(this.world, state -> state.getMaterial().isReplaceable(), false);

                    this.creature.getNavigator().tryMoveToXYZ(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), this.movementSpeed);
                }
            }
            AdvancedBlockPos.release(blockPos);
        }
    }

    @Override
    public void resetTask() {
        this.creature.getNavigator().clearPath();
    }

    private BlockPos target() {
        return new BlockPos(8, 100, 8);
    }
}
