package com.artur114.srptowerdefense.common.blocks;

import com.artur114.bananalib.mc.base.BBlockTileBase;
import com.artur114.bananalib.mc.base.BItemBlockBase;
import com.artur114.srptowerdefense.common.tileentity.TileEntityAreaProtector;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BlockAreaProtector extends BBlockTileBase<TileEntityAreaProtector> {
    public BlockAreaProtector(String name) {
        super(name, Material.ROCK, 25, 4000, SoundType.METAL);
        this.setCreativeTab(SRPTDMain.CREATIVE_TAB);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.addAll(Arrays.asList(I18n.format("tile.area_protector.info").split("<br>")));
        tooltip.add("");
        for (String s : I18n.format("tile.area_protector.info.warn").split("<br>")) {
            tooltip.add(TextFormatting.RED + s);
        }
    }

    @Override
    protected @Nullable Item createItemBlock() {
        return new BItemBlockBase(this).setRarity(EnumRarity.EPIC).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    }

    @Override
    public @NotNull Class<TileEntityAreaProtector> tileClass() {
        return TileEntityAreaProtector.class;
    }

    @Override
    public @Nullable TileEntityAreaProtector createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAreaProtector();
    }
}
