package com.artur114.srptowerdefense.common.items;


import com.artur114.srptowerdefense.common.blockdamage.BlockDamageHandler;
import com.artur114.srptowerdefense.common.init.InitItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.item.ItemStack.DECIMALFORMAT;

@Mod.EventBusSubscriber
public class ItemDebugCarrot extends BaseItem {

	public ItemDebugCarrot(String name) {
		super(name);
		this.setMaxStackSize(1);
		this.setContainerItem(this);
		this.setMaxDamage(2);
		this.setModCreativeTab();
	}

	@Override
	public @NotNull EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		BlockDamageHandler.damage(worldIn, pos, 32);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return 0.0F;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
	}

	@SubscribeEvent
	public static void blockInteraction(PlayerInteractEvent.LeftClickBlock e) {
		if (e.getItemStack().getItem() == InitItems.DEBUGGING_CARROT) {
			BlockDamageHandler.repair(e.getWorld(), e.getPos(), 16);
			e.setCanceled(true);
		}
	}

	@Override
	public IRarity getForgeRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		target.onKillCommand();
		if (target.getHealth() > 0.0F) {
			target.setHealth(0.0F);
			target.setDead();
		}
		return true;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!player.world.isRemote && !entity.attackEntityFrom(DamageSource.causePlayerDamage(player), 1.0F)) {
			entity.onKillCommand();
			if (!entity.isDead && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0.0F){
				entity.setDead();
				((EntityLivingBase) entity).setHealth(0.0F);
			}
			if (!entity.isDead) {
				player.world.removeEntity(entity);
			}
		}
		entity.hurtResistantTime = 0;
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("");
		tooltip.add(I18n.format("item.modifiers.mainhand"));
		tooltip.add(" " + net.minecraft.util.text.translation.I18n.translateToLocalFormatted("attribute.modifier.equals.0", DECIMALFORMAT.format(3.0D), net.minecraft.util.text.translation.I18n.translateToLocal("attribute.name.generic.attackSpeed")));
		tooltip.add(" " + I18n.format("item.debug_carrot.info.i") + " " + I18n.format("attribute.name.generic.attackDamage"));
	}
}
