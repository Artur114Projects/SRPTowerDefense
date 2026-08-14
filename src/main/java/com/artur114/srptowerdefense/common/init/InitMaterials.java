package com.artur114.srptowerdefense.common.init;

import com.artur114.bananalib.mc.base.MaterialArray;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class InitMaterials {
    public static final MaterialArray STONE_BLOCK = MaterialArray.from(SoundType.STONE, Material.ROCK, 25.0F, 1.5F);
    public static final MaterialArray IRON_BLOCK = MaterialArray.from(SoundType.METAL, Material.IRON, 250.0F, 3.0F);
}
