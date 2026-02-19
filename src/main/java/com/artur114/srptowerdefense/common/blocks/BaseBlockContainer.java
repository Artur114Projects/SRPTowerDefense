package com.artur114.srptowerdefense.common.blocks;

import com.artur114.srptowerdefense.common.init.InitBlocks;
import com.artur114.srptowerdefense.common.init.InitItems;
import com.artur114.srptowerdefense.common.util.interfaces.IHasModel;
import com.artur114.srptowerdefense.common.util.interfaces.IHasTileEntity;
import com.artur114.srptowerdefense.main.TowerDefence;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class BaseBlockContainer<T extends TileEntity> extends BlockContainer implements IHasModel, IHasTileEntity<T> {
    protected boolean isForCreative = false;
    public final Item item;

    protected BaseBlockContainer(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(material);

        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(soundType);

        InitBlocks.BLOCKS_REGISTER_BUSS.add(this);
        this.item = this.createItemBlock();
        InitItems.ITEMS_REGISTER_BUSS.add(this.item);
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState blockState) {
        return true;
    }

    @Override
    public void registerModels() {
        TowerDefence.PROXY.registerItemRenderer(this.item, 0, "inventory");
    }

    protected void getTileAndCall(World world, BlockPos pos, Predicate<T> run) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && this.tileEntityClass().isAssignableFrom(tile.getClass())) {
            run.test(this.tileEntityClass().cast(tile));
        }
    }

    public BaseBlockContainer<T> setModCreativeTab() {
        this.setCreativeTab(TowerDefence.CREATIVE_TAB);
        return this;
    }

    public BaseBlockContainer<T> setForCreative() {
        this.isForCreative = true;
        return this;
    }

    public void addForCreativeOnlyTooltip(List<String> tooltip) {
        tooltip.add(TextFormatting.RED + I18n.format("srptowerdefense.for_creative"));
    }

    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (this.isForCreative) {
            this.addForCreativeOnlyTooltip(tooltip);
        }
        super.addInformation(stack, player, tooltip, advanced);
    }

    protected Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    }
}
