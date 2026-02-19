package com.artur114.srptowerdefense.proxy;

import com.artur114.srptowerdefense.common.capabilities.TowerDefenceCapabilities;
import com.artur114.srptowerdefense.main.TowerDefence;
import com.artur114.srptowerdefense.register.Registerer;
import net.minecraftforge.fml.common.event.*;

public class CommonProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        TowerDefenceCapabilities.preInit();
        Registerer.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent e) {

    }

    @Override
    public void serverStopping(FMLServerStoppingEvent e) {

    }
}
