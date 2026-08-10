package com.artur114.srptowerdefense.common.blocks;

import com.artur114.bananalib.mc.base.BBlockTileBase;
import com.artur114.srptowerdefense.common.tileentity.TileEntityAreaProtector;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAreaProtector extends BBlockTileBase<TileEntityAreaProtector> {
    public BlockAreaProtector(String name) {
        super(name, Material.ROCK, 25, 4000, SoundType.METAL);
        this.setCreativeTab(SRPTDMain.CREATIVE_TAB);
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
