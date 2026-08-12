package com.artur114.srptowerdefense.common.init;

import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

@RegistryContainer
public class InitSounds {
    public static final SoundEvent MIDDLE_PUNCH = create("middle_punch");

    private static SoundEvent create(String name) {
        ResourceLocation rl = SRPTDMain.loc(name);
        return new SoundEvent(rl).setRegistryName(rl);
    }
}
