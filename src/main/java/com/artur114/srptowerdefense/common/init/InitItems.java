package com.artur114.srptowerdefense.common.init;

import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.srptowerdefense.common.items.ItemDebugFish;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

@RegistryContainer
public class InitItems {
    public static final Item DEBUGGING_FISH = new ItemDebugFish("debug_fish");
}
