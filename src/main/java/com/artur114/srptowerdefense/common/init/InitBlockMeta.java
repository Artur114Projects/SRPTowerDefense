package com.artur114.srptowerdefense.common.init;

import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePost;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.registry.BlockMetaRegistry;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

@AutoInstantiate
public class InitBlockMeta implements ILoadStagePost {
    @Override
    public void onPostInit() {
        BlockMetaRegistry.bindResistanceMul(Blocks.DIRT, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.STONE, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.GRASS, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.MYCELIUM, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.MELON_BLOCK, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.SAND, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.GRAVEL, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.SANDSTONE, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.RED_SANDSTONE, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.LEAVES, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.LEAVES2, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.LOG, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.LOG2, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.OBSIDIAN, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.END_STONE, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.NETHERRACK, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.COBBLESTONE, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.CACTUS, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.PUMPKIN, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.SOUL_SAND, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.TALLGRASS, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindResistanceMul(Blocks.MOSSY_COBBLESTONE, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
        BlockMetaRegistry.bindRegenPower(Blocks.SAND, 2000.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.GRAVEL, 2000.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.GRASS, 400.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.DIRT, 400.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.LEAVES, 600.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.LEAVES2, 600.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.CACTUS, 600.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.PUMPKIN, 400.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.LOG, 500.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.LOG2, 500.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.SOUL_SAND, 1000.0F);
        BlockMetaRegistry.bindRegenPower(Blocks.MELON_BLOCK, 400.0F);

        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfuserFurnace, BlockMetaRegistry.ResMulCause.SEMI_ORGANIC_BLOCK, 0.5F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.SemiorganicBlock, BlockMetaRegistry.ResMulCause.SEMI_ORGANIC_BLOCK, 0.5F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.RELAY_CONTROLLER, BlockMetaRegistry.ResMulCause.SEMI_ORGANIC_BLOCK, 0.5F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.NODE_RELAY, BlockMetaRegistry.ResMulCause.SEMI_ORGANIC_BLOCK, 0.5F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.RelayBase, BlockMetaRegistry.ResMulCause.SEMI_ORGANIC_BLOCK, 0.5F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.RelayMiddle, BlockMetaRegistry.ResMulCause.SEMI_ORGANIC_BLOCK, 0.5F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.RelayRoof, BlockMetaRegistry.ResMulCause.SEMI_ORGANIC_BLOCK, 0.5F);

        BlockMetaRegistry.bindResistanceMul(SRPBlocks.INFESTED_FURNACE, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStain, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedTrunk, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedRubble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedBush, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedLeaves, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedLeavesFast, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedPlanks, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStoneBricks, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedTerracotta, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.PolishedInfestedStone, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedColumn, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedSandstone, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedSandstoneChiseled, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedSandstoneCut, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedGlass, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.INFESTED_GLASS_PANE, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedWorkbench, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.INFESTED_POT, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedCobblestone, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStainStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedRubbleStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedTrunkStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedFence, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedSandstoneStairs, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedPlanksStairs, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStoneBricksStairs, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedPolishedStoneBricksStairs, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStoneStairs, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedSand, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedCobblestoneSlab, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedCobblestoneSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStoneSlab, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStoneSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedDirtSlab, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedDirtSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStoneBrickSlab, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStoneBrickSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedTerracottaSlab, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedTerracottaSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.PolishedInfestedStoneSlab, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.PolishedInfestedStoneSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedSandstoneSlab, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedSandstoneSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedPlankSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedPlankSlab, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedRubbleWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStainWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedPlankWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.PolishedInfestedStoneWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedStoneBrickWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedSandstoneWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.InfestedOre, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteCactus, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteBush, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteCanister, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteTrunk, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasitePlank, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStain, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteLoot, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStructure, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteThin, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteSapling, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteMouth, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleDense, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleFleshWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleBoneStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleFleshStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleStoneStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleStoneDebrisStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleWoodStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleBrickStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleMetalStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleObsidianStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleFungusStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStainFleshStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStainDirtStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStainMudStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStainFeelerStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleDenseWallStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleDenseNodeStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleDenseColonyStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteTrunkBallStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteTrunkTreeStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteTrunkPlantStair, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleSlabHalf, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStainSlabHalf, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStainSlabDouble, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteVine, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteFog, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasitePlankDeadheadWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleWeathbWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleWeathfsWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleWeathbcWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleBricksWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleMetalWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleDenseColonyWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteRubbleDenseBiomeWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteCanisterBagWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteStainFleshWall, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.ParasiteCanisterActive, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.BloodyIce, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.goreSim, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.gorePri, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.goreAda, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.gorePur, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.goreFer, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);
        BlockMetaRegistry.bindResistanceMul(SRPBlocks.goreMar, BlockMetaRegistry.ResMulCause.PARASITE_BLOCK, 0.1F);

        this.registerGenericMeta();
    }

    private void registerGenericMeta() {
        for (Block block : Block.REGISTRY) {
            if (BlockMetaRegistry.hasMetaFor(block)) {
                continue;
            }

            if (block instanceof IPlantable) {
                BlockMetaRegistry.bindResistanceMul(block, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
            }

            if (block instanceof IGrowable) {
                BlockMetaRegistry.bindRegenPower(block, 200.0F);
            }

            if (block instanceof BlockLeaves) {
                BlockMetaRegistry.bindResistanceMul(block, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
                BlockMetaRegistry.bindRegenPower(block, 600.0F);
            }
        }

        for (String oreName : OreDictionary.getOreNames()) {
            for (ItemStack stack : OreDictionary.getOres(oreName, false)) {
                if (stack.getItem() instanceof ItemBlock) {
                    Block block = ((ItemBlock) stack.getItem()).getBlock();

                    if (BlockMetaRegistry.hasMetaFor(block)) {
                        continue;
                    }

                    if (oreName.startsWith("ore")) {
                        BlockMetaRegistry.bindResistanceMul(block, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
                    }

                    if (oreName.startsWith("stone")) {
                        BlockMetaRegistry.bindResistanceMul(block, BlockMetaRegistry.ResMulCause.NATURAL_BLOCK, 0.25F);
                    }
                }
            }
        }
    }
}
