package com.artur114.srptowerdefense.common.items;


import com.artur114.bananalib.mc.base.BItemBase;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.BlockDamageHandler;
import com.artur114.srptowerdefense.common.init.InitItems;
import com.artur114.srptowerdefense.main.SRPTDMain;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
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
public class ItemDebugFish extends BItemBase {

	public ItemDebugFish(String name) {
		super(name);
		this.setMaxStackSize(1);
		this.setContainerItem(this);
		this.setMaxDamage(2);
		this.setCreativeTab(SRPTDMain.CREATIVE_TAB);
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
    public @NotNull Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) Integer.MAX_VALUE * Integer.MAX_VALUE, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.0F, 0));
        }

        return multimap;
    }

    @SubscribeEvent
    public static void blockInteraction(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getItemStack().getItem() == InitItems.DEBUGGING_FISH) {
            BlockDamageHandler.repair(e.getWorld(), e.getPos(), 16);
            e.setCanceled(true);
        }
    }
}
