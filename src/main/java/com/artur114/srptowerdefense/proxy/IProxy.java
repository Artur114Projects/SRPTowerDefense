package com.artur114.srptowerdefense.proxy;

import com.artur114.bananalib.mc.registry.IRegisterBus;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.*;

public interface IProxy {
    void preInit(IRegisterBus bus, FMLPreInitializationEvent e);
    void init(IRegisterBus bus, FMLInitializationEvent e);
    void postInit(IRegisterBus bus, FMLPostInitializationEvent e);
}