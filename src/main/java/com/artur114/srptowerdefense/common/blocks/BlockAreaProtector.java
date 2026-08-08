package com.artur114.srptowerdefense.common.blocks;

import com.artur114.bananalib.mc.base.BBlockBase;
import com.artur114.srptowerdefense.client.gui.GuiAreaProtector;
import com.artur114.srptowerdefense.main.SRPTDCore;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAreaProtector extends BBlockBase {
    public BlockAreaProtector(String name) {
        super(name, Material.ROCK, 25, 4000, SoundType.METAL);
        this.setCreativeTab(SRPTDMain.CREATIVE_TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            SRPTDMain.DEV_SHELL.evaluate("display_screen.groovy", "pos", pos);
        }

        return true;
    }
}
