package com.artur114.srptowerdefense.client.events.managers;

import com.artur114.srptowerdefense.common.util.CIMetricsUtils;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.registry.BlockMetaRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.function.Consumer;

public class ToolTipManager {

    public void itemTooltipEvent(ItemTooltipEvent e) {
        if (GuiScreen.isShiftKeyDown() && e.getItemStack().getItem() instanceof ItemBlock && Minecraft.getMinecraft().world != null) {
            ItemBlock itemBlock = (ItemBlock) e.getItemStack().getItem();

            List<String> tt = e.getToolTip();
            float resilience = calculateResilience(itemBlock.getBlock());
            Consumer<String> inserter = (tt::add);

            if (e.getFlags().isAdvanced()) {
                inserter = ((s) -> tt.add(tt.size() - 1, s));
            }

            inserter.accept(I18n.format("srptowerdefense.info.resistance_power") + " " + TextFormatting.YELLOW + CIMetricsUtils.formatJoules(resilience));
            BlockMetaRegistry.BlockMeta meta = BlockMetaRegistry.metaOf(itemBlock.getBlock());

            if (meta.regenPower() != -1) {
                inserter.accept(I18n.format("srptowerdefense.info.regen_power") + " " + TextFormatting.GREEN + CIMetricsUtils.formatWatts(meta.regenPower()));
            }

            if (meta.resistanceMulCause() != BlockMetaRegistry.ResMulCause.DEFAULT) {
                TextFormatting color = meta.resistanceMul() >= 1 ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED;
                inserter.accept(color + "×" + (1 / meta.resistanceMul()) + " " + I18n.format("srptowerdefense.info.damage") + " (" + I18n.format(meta.resistanceMulCause().causeTranslationKey()) + ")");
            }
        }
    }

    private float calculateResilience(Block block) {
        return BlockMetaRegistry.solidResistanceOf(block, block.getBlockHardness(block.getDefaultState(), Minecraft.getMinecraft().world, BlockPos.ORIGIN));
    }
}
