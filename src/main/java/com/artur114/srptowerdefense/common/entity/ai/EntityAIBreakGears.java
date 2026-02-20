package com.artur114.srptowerdefense.common.entity.ai;

import com.artur114.srptowerdefense.common.blockdamage.BlockDamageHandler;
import com.artur114.srptowerdefense.common.util.math.AdvancedBlockPos;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;

public class EntityAIBreakGears extends EntityAIBase {
    private final IAttributeInstance pathSearchRange;
    private final EntityCreature creature;
    private final double movementSpeed;
    private BlockPos foundedGear;
    private final World world;

    public EntityAIBreakGears(EntityCreature theCreatureIn, double movementSpeedIn) {
        this.pathSearchRange = theCreatureIn.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        this.movementSpeed = Math.max(1.0D, movementSpeedIn);
        this.creature = theCreatureIn;
        this.world = theCreatureIn.world;
        this.setMutexBits(15);
    }

    public boolean shouldExecute() {
        return this.creature.getAttackTarget() == null && (this.foundedGear = this.findGear()) != null;
    }

    public boolean shouldContinueExecuting() {
        return this.creature.getAttackTarget() == null && this.foundedGear != null && this.world.getTileEntity(this.foundedGear) != null;
    }

    public void startExecuting() {
        EnumFacing facing = EnumFacing.VALUES[this.creature.getRNG().nextInt(EnumFacing.VALUES.length)];
        this.creature.getNavigator().tryMoveToXYZ(this.foundedGear.getX() + facing.getFrontOffsetX(), this.foundedGear.getY() + facing.getFrontOffsetY(), this.foundedGear.getZ() + facing.getFrontOffsetZ(), this.movementSpeed);
    }

    @Override
    public void updateTask() {
        double d = this.creature.getDistanceSq(this.foundedGear.getX() + 0.5, this.foundedGear.getY(), this.foundedGear.getZ() + 0.5);

        if (this.creature.ticksExisted % 8 == 0 && this.creature.getNavigator().noPath() && d > 4D) {
            this.startExecuting();
        }

        this.creature.getLookHelper().setLookPosition(this.foundedGear.getX() + 0.5, this.foundedGear.getY() + 0.5, this.foundedGear.getZ() + 0.5, 30F, 30F);

        if (this.creature.ticksExisted % 8 == 0 && d <= 4D) {
            BlockDamageHandler.entityDamage(this.creature, this.foundedGear, 256);
            ((EntityParasiteBase) this.creature).setAttackCooldownAni(100);
        }
    }

    @Nullable
    private BlockPos findGear() {
        int range = (int) (this.pathSearchRange.getAttributeValue() + 8);
        int rangeChunk = (range >> 4) + 1;
        AdvancedBlockPos blockPos = AdvancedBlockPos.obtain().setPos(this.creature.posX, this.creature.getEntityBoundingBox().minY, this.creature.posZ);
        ChunkPos center = blockPos.toChunkPos();
        AdvancedBlockPos.release(blockPos);

        ArrayList<TileEntity> entities = new ArrayList<>();

        for (int x = center.x - rangeChunk; x != center.x + rangeChunk + 1; x++) {
            for (int z = center.z - rangeChunk; z != center.z + rangeChunk + 1; z++) {
                if (!((WorldServer) this.world).getChunkProvider().chunkExists(x, z)) {
                    continue;
                }

                Chunk chunk = this.world.getChunkFromChunkCoords(x,z);
                entities.addAll(chunk.getTileEntityMap().values());
            }
        }

        entities.sort(Comparator.comparingDouble((t) -> this.creature.getDistanceSq(t.getPos())));

        for (TileEntity tile : entities) {
            if (this.world.isBlockLoaded(tile.getPos()) && tile instanceof IInventory && tile.getPos().getY() > this.creature.posY - range && tile.getPos().getY() < this.creature.posY + range) {
                return tile.getPos();
            }
        }

        return null;
    }

    @Override
    public void resetTask() {
        this.creature.getNavigator().clearPath();
        this.foundedGear = null;
    }
}
