package com.artur114.srptowerdefense.main;

import com.artur114.srptowerdefense.common.init.InitItems;
import com.artur114.srptowerdefense.proxy.IProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
@Mod(modid = TowerDefence.MODID, useMetadata = true)
public class TowerDefence {
    public static final String MODID = "srptowerdefense";
    public static final String CLIENT_PROXY = "com.artur114.srptowerdefense.proxy.ClientProxy";
    public static final String SERVER_PROXY = "com.artur114.srptowerdefense.proxy.ServerProxy";

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs("main") {public @NotNull ItemStack getTabIconItem() {return new ItemStack(InitItems.DEBUGGING_CARROT);}};
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(TowerDefence.MODID);
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static IProxy PROXY;


    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        PROXY.preInit(event);
    }

    @Mod.EventHandler
    public static void Init(FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent e) {
        PROXY.serverStarting(e);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent e) {
        PROXY.serverStopping(e);
    }
}