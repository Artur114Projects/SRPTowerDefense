package com.artur114.srptowerdefense.common.worldstate.blockdamage.registry;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityDamageRegistry {
    private static final Map<ResourceLocation, Integer> REGISTRY = new ConcurrentHashMap<>();

    public static void registerEntity(ResourceLocation location, int damage, float contactArea) {
        REGISTRY.put(location, (int) (damage / contactArea));
    }

    public static void registerEntity(String location, int damage, float contactArea) {
        registerEntity(new ResourceLocation(location), damage, contactArea);
    }

    public static int damageOf(EntityLiving entity) {
        if (entity instanceof EntityParasiteBase) {
            return damageOf((EntityParasiteBase) entity);
        }
        return damageFromRegistry(entity);
    }

    public static int damageOf(EntityParasiteBase parasite) {
        int generation = SRPSaveData.get(parasite.world, 72).getGeneration(parasite.dimension);
        return (int) (damageFromRegistry(parasite) * Math.max((generation / 5.0F) * 3.0F, 1.0F));
    }

    private static int damageFromRegistry(EntityLiving entity) {
        return REGISTRY.computeIfAbsent(EntityList.getKey(entity), (key) -> computeDamage(entity));
    }

    private static int computeDamage(EntityLiving entity) {
        return (int) (6000 * ((entity.width + entity.height) / 3.0F));
    }
}
