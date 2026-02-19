package com.artur114.srptowerdefense.common.init;

import com.artur114.srptowerdefense.common.items.ItemDebugCarrot;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class InitItems {
    public static final List<Item> ITEMS_REGISTER_BUSS = new ArrayList<>();

    public static final Item DEBUGGING_CARROT = new ItemDebugCarrot("debug_carrot");
}
