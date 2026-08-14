package com.artur114.srptowerdefense.common.init;

import com.artur114.bananalib.mc.base.BBlockBase;
import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.srptowerdefense.common.blocks.BlockAreaProtector;
import com.artur114.srptowerdefense.common.blocks.BlockWall;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

@RegistryContainer
public class InitBlocks {
    public static final BBlockBase AREA_PROTECTOR = new BlockAreaProtector("area_protector");
    public static final BBlockBase ARMORED_BRICKS = new BlockWall("armored_bricks", InitMaterials.IRON_BLOCK, 4000000, 4000, false);
    public static final BBlockBase REINFORCED_BRICKS = new BlockWall("reinforced_bricks", InitMaterials.STONE_BLOCK, 2500000);
    public static final BBlockBase REINFORCED_CONCRETE = new BlockWall("reinforced_concrete", InitMaterials.STONE_BLOCK, 400000);
    public static final BBlockBase ORGANIC_CONCRETE = new BlockWall("organic_concrete", InitMaterials.STONE_BLOCK, 400000, 4000, true);
}
