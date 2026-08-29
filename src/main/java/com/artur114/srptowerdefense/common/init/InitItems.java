package com.artur114.srptowerdefense.common.init;

import com.artur114.bananalib.mc.base.BItemBase;
import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.srptowerdefense.common.items.ItemDebugFish;
import com.artur114.srptowerdefense.common.items.ItemMallet;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

@RegistryContainer
public class InitItems {
    public static final BItemBase WOODEN_MALLET = new ItemMallet("wooden_mallet", 256, 16000, 1, 1);
    public static final BItemBase STONE_MALLET = new ItemMallet("stone_mallet", 4096, 32000, 2, 1).setRarity(EnumRarity.UNCOMMON);
    public static final BItemBase IRON_MALLET = new ItemMallet("iron_mallet", 16384, 64000, 4, 3).setRarity(EnumRarity.RARE);
    public static final BItemBase DEBUGGING_FISH = new ItemDebugFish("debug_fish");
}
