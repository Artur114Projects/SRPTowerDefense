package com.artur114.srptowerdefense.common.events.managers;

import com.artur114.srptowerdefense.common.init.InitBlocks;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.world.BlockEvent;

public class BlockBreakManager {
    public void livingDestroyBlockEvent(LivingDestroyBlockEvent e) {
        if (e.getState().getBlock() == InitBlocks.ARMORED_BRICKS || e.getState().getBlock() == InitBlocks.REINFORCED_BRICKS) {
            e.setCanceled(true);
        }
    }
}
