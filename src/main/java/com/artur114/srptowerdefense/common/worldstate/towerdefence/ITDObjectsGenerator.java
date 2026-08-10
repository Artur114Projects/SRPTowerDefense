package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.mc.nbt.INBTSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface ITDObjectsGenerator extends INBTSerializable {
    void update(WorldServer world, TowerDefenceManager manager);

    Map<ResourceLocation, Class<? extends ITDObjectsGenerator>> NAME_CLASS_REGISTRY = new HashMap<>();
    Map<Class<? extends ITDObjectsGenerator>, ResourceLocation> CLASS_MANE_REGISTRY = new HashMap<>();

    static void registerGenerator(@NotNull ResourceLocation name, @NotNull Class<? extends ITDObjectsGenerator> clazz) {
        NAME_CLASS_REGISTRY.put(name, clazz);
        CLASS_MANE_REGISTRY.put(clazz, name);
    }

    @Nullable
    static Class<? extends ITDObjectsGenerator> classFromName(ResourceLocation name) {
        return NAME_CLASS_REGISTRY.get(name);
    }

    @Nullable
    static ResourceLocation nameFromClass(Class<? extends ITDObjectsGenerator> clazz) {
        return CLASS_MANE_REGISTRY.get(clazz);
    }

    @Nullable
    static ResourceLocation nameFromObj(@NotNull ITDObjectsGenerator obj) {
        return CLASS_MANE_REGISTRY.get(obj.getClass());
    }

    @Nullable
    static ITDObjectsGenerator create(ResourceLocation name) {
        return create(classFromName(name));
    }

    @Nullable
    static ITDObjectsGenerator create(Class<? extends ITDObjectsGenerator> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    static List<Class<? extends ITDObjectsGenerator>> classes() {
        return new ArrayList<>(NAME_CLASS_REGISTRY.values());
    }
}
