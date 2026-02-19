package com.artur114.srptowerdefense.common.items;

import com.artur114.srptowerdefense.common.init.InitItems;
import com.artur114.srptowerdefense.common.util.interfaces.IHasModel;
import com.artur114.srptowerdefense.main.TowerDefence;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public abstract class BaseItem extends Item implements IHasModel {
    protected BaseItem(String name) {
        this.setRegistryName(name);
        this.setUnlocalizedName(name);

        InitItems.ITEMS_REGISTER_BUSS.add(this);
    }


    protected void setModCreativeTab() {
        this.setCreativeTab(TowerDefence.CREATIVE_TAB);
    }

    protected void addForCreativeOnlyTooltip(List<String> tooltip) {
        tooltip.add(TextFormatting.RED + I18n.format("srptowerdefense.for_creative"));
    }

    @Override
    public void registerModels() {
        TowerDefence.PROXY.registerItemRenderer(this, 0, "inventory");
    }
}
