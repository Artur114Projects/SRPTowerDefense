package com.artur114.srptowerdefense.common.init;

import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.srptowerdefense.common.blocks.BlockAreaProtector;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

@RegistryContainer
public class InitBlocks {
    public static final Block AREA_PROTECTOR = new BlockAreaProtector("area_protector");
}
