package com.artur114.srptowerdefense.common.blocks;

import com.artur114.bananalib.mc.base.BBlockBase;
import com.artur114.bananalib.mc.base.BItemBlockBase;
import com.artur114.bananalib.mc.base.MaterialArray;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.registry.BlockMetaRegistry;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockWall extends BBlockBase {

    public BlockWall(String name, MaterialArray mat, int resistance) {
        this(name, mat, resistance, -1, false);
    }

    public BlockWall(String name, MaterialArray mat, int resistance, int regen, boolean organic) {
        super(name, mat);
        this.setCreativeTab(SRPTDMain.CREATIVE_TAB);
        BlockMetaRegistry.bindResistance(this, resistance);

        if (regen != -1) {
            BlockMetaRegistry.bindRegenPower(this, regen);
        }

        if (organic) {
            BlockMetaRegistry.bindResistanceMul(this, BlockMetaRegistry.ResMulCause.SEMI_ORGANIC_BLOCK, 0.5F);
        }


        if (regen != -1 && this.item != null) {
            ((BItemBlockBase) this.item).setRarity(EnumRarity.UNCOMMON);
        }
    }
}
