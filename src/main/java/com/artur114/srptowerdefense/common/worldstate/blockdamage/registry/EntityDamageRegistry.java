package com.artur114.srptowerdefense.common.worldstate.blockdamage.registry;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;

public class EntityDamageRegistry {
    public static int damageOf(EntityLiving entity) {
        return (int) (4000 / 0.25F);
    }
}
