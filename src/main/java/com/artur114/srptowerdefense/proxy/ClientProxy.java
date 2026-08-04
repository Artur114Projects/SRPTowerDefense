package com.artur114.srptowerdefense.proxy;

import com.artur114.bananalib.mc.registry.IRegisterBus;
import com.artur114.srptowerdefense.client.render.dev.DevScriptedTickAndRender;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(IRegisterBus bus, FMLPreInitializationEvent e) {
        super.preInit(bus, e);
    }

    @Override
    public void init(IRegisterBus bus, FMLInitializationEvent e) {
        super.init(bus, e);
    }

    @Override
    public void postInit(IRegisterBus bus, FMLPostInitializationEvent e) {
        super.postInit(bus, e);
    }

    @Override
    public List<Class<?>> classesToRegister() {
        return Collections.singletonList(DevScriptedTickAndRender.class);
    }
}
