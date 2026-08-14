package com.artur114.srptowerdefense.common.worldstate.blockdamage.registry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BlockMetaRegistry {
    private static final Map<Block, BlockMeta> META_MAP = new HashMap<>();
    private static final float DEFAULT_REGEN_POWER = 50.0F;

    public static boolean canBeDamaged(World world, BlockPos pos) {
        return canBeDamaged(world, world.getBlockState(pos), pos);
    }

    public static boolean canBeDamaged(World world, IBlockState state, BlockPos pos) {
        Block block = state.getBlock();
        return !state.getMaterial().isLiquid() && !block.isAir(state, world, pos) && !(block.getBlockHardness(state, world, pos) < 0.0F);
    }

    public static float regenPowerOf(World world, BlockPos pos) {
        BlockMeta meta = metaOf(world.getBlockState(pos).getBlock());

        if (meta.regenPower == -1) {
            return DEFAULT_REGEN_POWER;
        } else {
            return meta.regenPower;
        }
    }

    public static float regenPowerOf(Block block) {
        BlockMeta meta = metaOf(block);

        if (meta.regenPower == -1) {
            return DEFAULT_REGEN_POWER;
        } else {
            return meta.regenPower;
        }
    }

    public static float resistanceOf(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        float hardness = block.getBlockHardness(state, world, pos);
        if (!canBeDamaged(world, state, pos)) {
            return -1.0F;
        } else {
            return resistanceOf(block, hardness);
        }
    }

    public static float solidResistanceOf(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        float hardness = block.getBlockHardness(state, world, pos);
        if (!canBeDamaged(world, state, pos)) {
            return -1.0F;
        } else {
            return solidResistanceOf(block, hardness);
        }
    }

    public static float resistanceOf(Block block, float hardness) {
        return solidResistanceOf(block, hardness) * metaOf(block).resistanceMul;
    }

    public static float solidResistanceOf(Block block, float hardness) {
        BlockMeta meta = metaOf(block);
        if (meta.isOverrideResistance) {
            return meta.resistanceOverride;
        }
        return hardness * 80_000.0F;
    }

    public static BlockMeta metaOf(Block block) {
        return META_MAP.getOrDefault(block, BlockMeta.DEFAULT);
    }

    public static boolean hasMetaFor(Block block) {
        return META_MAP.containsKey(block);
    }

    public static void bindRegenPower(Block block, float strength) {
        BlockMeta meta = META_MAP.computeIfAbsent(block, b -> BlockMeta.createDef());
        meta.regenPower = strength;
    }

    public static void bindResistanceMul(Block block, ResMulCause cause, float mul) {
        BlockMeta meta = META_MAP.computeIfAbsent(block, b -> BlockMeta.createDef());
        meta.resistanceMulCause = cause;
        meta.resistanceMul = mul;
    }

    public static void bindResistance(Block block, float resistance) {
        BlockMeta meta = META_MAP.computeIfAbsent(block, b -> BlockMeta.createDef());
        meta.resistanceOverride = resistance;
        meta.isOverrideResistance = true;
    }

    public static class BlockMeta {
        private static final BlockMeta DEFAULT = BlockMeta.createDef();
        private boolean isOverrideResistance = false;
        private float resistanceOverride = 0.0F;
        private ResMulCause resistanceMulCause;
        private float resistanceMul;
        private float regenPower;

        private BlockMeta(ResMulCause resistanceMulCause, float regenPower, float resistanceMul) {
            this.resistanceMulCause = resistanceMulCause;
            this.resistanceMul = resistanceMul;
            this.regenPower = regenPower;
        }

        public ResMulCause resistanceMulCause() {
            return this.resistanceMulCause;
        }

        public float resistanceMul() {
            return this.resistanceMul;
        }

        public float regenPower() {
            return this.regenPower;
        }

        public float resistanceOverride() {
            return resistanceOverride;
        }

        public boolean isOverrideResistance() {
            return isOverrideResistance;
        }

        private static BlockMeta createDef() {
            return new BlockMeta(ResMulCause.DEFAULT, -1.0F, 1.0F);
        }
    }

    public enum ResMulCause {
        DEFAULT, NATURAL_BLOCK, PARASITE_BLOCK, SEMI_ORGANIC_BLOCK;

        public String causeTranslationKey() {
            switch (this) {
                case NATURAL_BLOCK:
                    return "blockdamage.resistance_cause.natural";
                case PARASITE_BLOCK:
                    return "blockdamage.resistance_cause.parasite";
                case SEMI_ORGANIC_BLOCK:
                    return "blockdamage.resistance_cause.organic";
                case DEFAULT:
                    return "";
                default:
                    throw new IllegalStateException("Unknown cause: " + this);
            }
        }
    }
}
