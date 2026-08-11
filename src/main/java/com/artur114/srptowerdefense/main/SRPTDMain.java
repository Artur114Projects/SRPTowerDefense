package com.artur114.srptowerdefense.main;

import com.artur114.bananalib.mc.registry.BananaRegisterBus;
import com.artur114.bananalib.mc.registry.IRegisterBus;
import com.artur114.srptowerdefense.common.init.InitBlocks;
import com.artur114.srptowerdefense.common.init.InitItems;
import com.artur114.srptowerdefense.common.util.groovy.DevScriptsShell;
import com.artur114.srptowerdefense.proxy.IProxy;
import com.artur114.srptowerdefense.server.event.PublicSStartingEvent;
import com.artur114.srptowerdefense.server.event.PublicSStoppingEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.List;


// TODO: 05.03.2026 Вырезать опцию smooth world в optiFine
@Mod.EventBusSubscriber
@Mod(modid = SRPTDMain.MODID, useMetadata = true)
public class SRPTDMain {
    public static final ForgeChunkManager.LoadingCallback CALLBACK = (tickets, world) -> tickets.forEach(ForgeChunkManager::releaseTicket);
    public static final DevScriptsShell DEV_SHELL = new DevScriptsShell(Paths.get("..", "src/test/groovy/scripts").toAbsolutePath().normalize());
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs("main") {public @NotNull ItemStack getTabIconItem() {return new ItemStack(InitBlocks.AREA_PROTECTOR);}};
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(SRPTDMain.MODID);
    public static final IRegisterBus REGISTER_BUS = new BananaRegisterBus().putNetWrapper(NETWORK);
    public static final EventBus INTERNAL_EVENT_BUS = new EventBus();
    public static final String MODID = "srptowerdefense";

    @Mod.Instance
    public static SRPTDMain INSTANCE;

    @SidedProxy(
        clientSide = "com.artur114.srptowerdefense.proxy.ClientProxy",
        serverSide = "com.artur114.srptowerdefense.proxy.ServerProxy"
    )
    public static IProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(REGISTER_BUS, event);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        proxy.init(REGISTER_BUS, event);
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(REGISTER_BUS, event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent e) {
        INTERNAL_EVENT_BUS.post(new PublicSStartingEvent(e));
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent e) {
        INTERNAL_EVENT_BUS.post(new PublicSStoppingEvent(e));
    }

    public static ResourceLocation loc(String id) {
        return new ResourceLocation(MODID, id);
    }

    static {
        DEV_SHELL.evaluate("register_scripts.groovy");
    }
}