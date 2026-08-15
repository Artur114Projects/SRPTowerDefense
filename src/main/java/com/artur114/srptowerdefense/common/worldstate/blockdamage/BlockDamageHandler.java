package com.artur114.srptowerdefense.common.worldstate.blockdamage;

import com.artur114.srptowerdefense.common.network.client.CPacketCreateFX;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.server.IServerDamagedChunk;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;


public class BlockDamageHandler {
    public static int getDamage(World world, BlockPos pos) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);

        if (chunk.isEmpty()) {
            return 0;
        }

        IDamagedChunk damagedChunk = chunk.getCapability(InitCapabilities.BLOCK_DAMAGE, null);

        if (damagedChunk != null) {
            return damagedChunk.getDamage(pos);
        }

        return 0;
    }

    public static void setDamage(World world, BlockPos pos, int damage) {
        if (damage >= 0) {
            damage(world, pos, damage);
        } else {
            repair(world, pos, -damage);
        }
    }

    public static void entityDamage(EntityLiving entity, BlockPos pos, int damagePerSecond) {
        Chunk chunk = entity.world.getChunkFromBlockCoords(pos);
        IDamagedChunk damagedChunk = chunk.getCapability(InitCapabilities.BLOCK_DAMAGE, null);

        if (damagedChunk != null && !damagedChunk.isRemote()) {
            if (entity.ticksExisted % 20 == 0) {
                IBlockState state = chunk.getBlockState(pos);

                if (state.getMaterial() != Material.AIR) {
                    SoundType type = state.getBlock().getSoundType(state, entity.world, pos, entity);
                    entity.world.playSound(null, pos, type.getHitSound(), SoundCategory.NEUTRAL, (type.getVolume() + 1.0F) / 8.0F, type.getPitch() * 0.5F);
                    EnumFacing facing = EnumFacing.getFacingFromVector((float) (entity.posX - (pos.getX() + 0.5F)), (float) ((entity.posY + entity.height / 2) - (pos.getY() + 0.5F)), (float) (entity.posZ - (pos.getZ() + 0.5F)));
                    CPacketCreateFX.sendBlockPunch(entity.world, pos, facing, new Vec3d(0.5 + facing.getFrontOffsetX() * 0.5, 0.5 + facing.getFrontOffsetY() * 0.5, 0.5 + facing.getFrontOffsetZ() * 0.5).addVector(pos.getX(), pos.getY(), pos.getZ()), Math.max(1, damagePerSecond / 6000));
                }

                ((IServerDamagedChunk) damagedChunk).damage(pos, damagePerSecond);
            }
        }
    }

    public static void damage(World world, BlockPos pos, int amount) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        IDamagedChunk damagedChunk = chunk.getCapability(InitCapabilities.BLOCK_DAMAGE, null);

        if (damagedChunk != null && !damagedChunk.isRemote()) {
            ((IServerDamagedChunk) damagedChunk).damage(pos, amount);
        }
    }

    public static void repair(World world, BlockPos pos, int amount) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        IDamagedChunk damagedChunk = chunk.getCapability(InitCapabilities.BLOCK_DAMAGE, null);

        if (damagedChunk != null && !damagedChunk.isRemote()) {
            ((IServerDamagedChunk) damagedChunk).repair(pos, amount);
        }
    }
}
