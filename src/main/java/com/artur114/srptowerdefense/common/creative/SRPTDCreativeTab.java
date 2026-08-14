package com.artur114.srptowerdefense.common.creative;

import com.artur114.srptowerdefense.common.init.InitItems;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

public class SRPTDCreativeTab extends CreativeTabs {
    public SRPTDCreativeTab(String label) {
        super(label);
    }

    @Override
    public void displayAllRelevantItems(@NotNull NonNullList<ItemStack> list) {
        for (Item item : SRPTDMain.REGISTER_BUS.items()) {
            if (item == InitItems.DEBUGGING_FISH) {
                continue;
            }
            item.getSubItems(this, list);
        }
        list.add(new ItemStack(InitItems.DEBUGGING_FISH));
    }

    @Override
    public @NotNull ItemStack getTabIconItem() {
        return new ItemStack(InitItems.IRON_MALLET);
    }
}
