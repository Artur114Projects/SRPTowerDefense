package com.artur114.srptowerdefense.common.worldstate.blockdamage;

import com.artur114.srptowerdefense.common.worldstate.blockdamage.server.IServerDamagedChunk;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
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
            float multiplier = ((IServerDamagedChunk) damagedChunk).damageMultiplierFor(pos);
            int tick;

            if (damagePerSecond * multiplier >= 1) {
                tick = 20;
            } else {
                tick = Math.round((1.2F / (damagePerSecond * multiplier))) * 20;
            }

            if (entity.ticksExisted % tick == 0) {
                ((IServerDamagedChunk) damagedChunk).damage(pos, (int) ((damagePerSecond / 20.0F) * tick));
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
