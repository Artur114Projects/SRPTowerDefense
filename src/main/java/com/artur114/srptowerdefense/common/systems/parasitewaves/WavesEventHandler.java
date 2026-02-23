package com.artur114.srptowerdefense.common.systems.parasitewaves;

import com.artur114.srptowerdefense.common.capabilities.GenericCapProviderS;
import com.artur114.srptowerdefense.common.capabilities.TowerDefenceCapabilities;
import com.artur114.srptowerdefense.main.TowerDefence;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//@Mod.EventBusSubscriber
public class WavesEventHandler {

    @SubscribeEvent
    public static void attachCapabilitiesWorld(AttachCapabilitiesEvent<World> e) {
        if (e.getObject() != null && !e.getObject().isRemote) e.addCapability(new ResourceLocation(TowerDefence.MODID, "waves_system"), new GenericCapProviderS<>(new WavesManager(e.getObject()), TowerDefenceCapabilities.WAVES_SYSTEM));
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.START && !e.world.isRemote) {
            WavesManager manager = e.world.getCapability(TowerDefenceCapabilities.WAVES_SYSTEM, null);

            if (manager != null) {
                manager.update();
            }
        }
    }
}
