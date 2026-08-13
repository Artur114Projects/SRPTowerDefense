package com.artur114.srptowerdefense.common.items;

import com.artur114.bananalib.mc.base.BItemBase;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.srptowerdefense.common.init.InitSounds;
import com.artur114.srptowerdefense.common.util.CIMetricsUtils;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.BlockDamageHandler;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.registry.BlockMetaRegistry;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber
public class ItemMallet extends BItemBase {
    private final int repairArea;
    private final int repairPower;
    private final int punchPower;

    public ItemMallet(String name, int durability, int repairPower, int punchPower, int repairArea) {
        super(name);
        this.setMaxDamage(durability);
        this.setMaxStackSize(1);
        this.setCreativeTab(SRPTDMain.CREATIVE_TAB);
        this.repairPower = repairPower;
        this.punchPower = punchPower;
        this.repairArea = repairArea;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return 0.0F;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.mallet.all.info.0"));
        tooltip.add(I18n.format("item.mallet.all.info.1") + " " + TextFormatting.YELLOW + CIMetricsUtils.formatJoules(this.repairPower));
        if (this.repairArea > 1) {
            tooltip.add(I18n.format("item.mallet.all.info.2") + " " + TextFormatting.AQUA + CIMetricsUtils.formatMeters2(this.repairArea * this.repairArea));
        }
    }

    public int punchPower() {
        return this.punchPower;
    }

    @SideOnly(Side.CLIENT)
    public void onClicked(EnumHand hand) {
        Minecraft mc = Minecraft.getMinecraft();

        mc.player.rotationYaw += mc.world.rand.nextFloat() * 2 - 1;
        mc.player.rotationPitch += mc.world.rand.nextFloat() * 2 - 1;
    }

    public void onBlockClick(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, ItemStack stack) {
        PosMc3IM posM = PosMc3IM.obtain();
        if (this.repairArea == 1) {
            BlockDamageHandler.repair(world, pos, this.repairPower);
        } else if (facing.getFrontOffsetX() != 0) {
            int semiArea = this.repairArea / 2;
            for (int y = pos.getY() - semiArea; y != pos.getY() + semiArea + 1; y++) {
                for (int z = pos.getZ() - semiArea; z != pos.getZ() + semiArea + 1; z++) {
                    BlockDamageHandler.repair(world, posM.set(pos.getX(), y, z), this.repairPower);
                }
            }
        } else if (facing.getFrontOffsetZ() != 0) {
            int semiArea = this.repairArea / 2;
            for (int y = pos.getY() - semiArea; y != pos.getY() + semiArea + 1; y++) {
                for (int x = pos.getX() - semiArea; x != pos.getX() + semiArea + 1; x++) {
                    BlockDamageHandler.repair(world, posM.set(x, y, pos.getZ()), this.repairPower);
                }
            }
        } else {
            int semiArea = this.repairArea / 2;
            for (int x = pos.getX() - semiArea; x != pos.getX() + semiArea + 1; x++) {
                for (int z = pos.getZ() - semiArea; z != pos.getZ() + semiArea + 1; z++) {
                    BlockDamageHandler.repair(world, posM.set(x, pos.getY(), z), this.repairPower);
                }
            }
        }
        world.playSound(null, pos, InitSounds.MIDDLE_PUNCH, SoundCategory.PLAYERS, (float) (0.35F * ((ItemMallet) stack.getItem()).punchPower()), Math.min((BlockMetaRegistry.resistanceOf(world, pos) / 500000.0F) + (world.rand.nextFloat() * 0.05F), 2.0F));
        PosMc3IM.release(posM);
    }
}
