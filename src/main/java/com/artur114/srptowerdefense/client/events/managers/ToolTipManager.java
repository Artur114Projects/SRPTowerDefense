package com.artur114.srptowerdefense.client.events.managers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipManager {
    private static final float HARDNESS_TO_JM2 = 80_000.0F;


    public void itemTooltipEvent(ItemTooltipEvent e) {
        if (GuiScreen.isShiftKeyDown() && e.getItemStack().getItem() instanceof ItemBlock && Minecraft.getMinecraft().world != null) {
            ItemBlock itemBlock = (ItemBlock) e.getItemStack().getItem();

            float resilience = calculateResilience(itemBlock.getBlock());

            if (e.getFlags().isAdvanced()) {
                e.getToolTip().add(e.getToolTip().size() - 1, "Ударная вязкость: " + TextFormatting.YELLOW + formatJoules(resilience));
            } else {
                e.getToolTip().add("Ударная вязкость: " + TextFormatting.YELLOW + formatJoules(resilience));
            }
        }
    }

    private float calculateResilience(Block block) {
        return block.getBlockHardness(block.getDefaultState(), Minecraft.getMinecraft().world, BlockPos.ORIGIN) * HARDNESS_TO_JM2;
    }

    private String formatJoules(float val) {
        String[] vals = new String[] {"%.1f §fДж/м²", "%.1f §fкДж/м²", "%.1f §fМДж/м²", "%.1f §fГДж/м²"};

        if (val < 0) {
            return "∞ §fДж/м²";
        }

        float j = val;
        for (int i = 0; i != vals.length; i++) {
            if (j / 1000.0F > 0.5) {
                j /= 1000.0F;
            } else {
                return String.format(vals[i], j);
            }
        }

        return String.format(vals[3], j);
    }
}
