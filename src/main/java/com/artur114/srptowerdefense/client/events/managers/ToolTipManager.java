package com.artur114.srptowerdefense.client.events.managers;

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

            inserter.accept("Ударная вязкость: " + TextFormatting.YELLOW + formatJoules(resilience));
            BlockMetaRegistry.BlockMeta meta = BlockMetaRegistry.metaOf(itemBlock.getBlock());

            if (meta.regenPower() != -1) {
                inserter.accept("Мощность регенерации: " + TextFormatting.GREEN + this.formatWatts(meta.regenPower()));
            }

            if (meta.resistanceMulCause() != BlockMetaRegistry.ResMulCause.DEFAULT) {
                TextFormatting color = meta.resistanceMul() >= 1 ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED;
                inserter.accept(color + "×" + (1 / meta.resistanceMul()) + " урон (" + I18n.format(meta.resistanceMulCause().causeTranslationKey()) + ")");
            }
        }
    }

    private float calculateResilience(Block block) {
        return BlockMetaRegistry.solidResistanceOf(block, block.getBlockHardness(block.getDefaultState(), Minecraft.getMinecraft().world, BlockPos.ORIGIN));
    }

    private String formatJoules(float val) {
        String[] vals = new String[] {"%.1f §fДж/м²", "%.1f §fкДж/м²", "%.1f §fМДж/м²", "%.1f §fГДж/м²"};

        if (val < 0) {
            return "∞ §fДж/м²";
        }

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000.0F >= 0.5) {
                j /= 1000.0F;
            } else {
                return String.format(vals[i], j);
            }
        }

        return String.format(vals[3], j);
    }

    private String formatWatts(float val) {
        String[] vals = new String[] {"%.1f §fВт/м²", "%.1f §fкВт/м²", "%.1f §fМВт/м²", "%.1f §fГВт/м²"};

        if (val < 0) {
            return "∞ §fВт/м²";
        }

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000.0F >= 0.5) {
                j /= 1000.0F;
            } else {
                return String.format(vals[i], j);
            }
        }

        return String.format(vals[3], j);
    }
}
