package com.artur114.srptowerdefense.register;

import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.data.PacketRegData;
import com.artur114.bananalib.mc.registry.data.PacketRegDataList;
import com.artur114.bananalib.mc.registry.interf.IHasCraftRecipe;
import com.artur114.bananalib.mc.registry.interf.IHasNetworkPacket;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePost;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePre;
import com.artur114.srptowerdefense.common.commands.CommandTeleportToWave;
import com.artur114.srptowerdefense.common.network.client.CPacketAreaProtector;
import com.artur114.srptowerdefense.common.network.client.CPacketCreateFX;
import com.artur114.srptowerdefense.common.network.client.CPacketSyncBlocksDamage;
import com.artur114.srptowerdefense.common.network.server.SPacketAreaProtector;
import com.artur114.srptowerdefense.common.network.server.SPacketMalletClick;
import com.artur114.srptowerdefense.main.SRPTDMain;
import com.artur114.srptowerdefense.server.event.PublicSStartingEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AutoInstantiate
public class RegisterHandler implements IHasNetworkPacket, IHasCraftRecipe, ILoadStagePre {

    @SubscribeEvent
    public void serverStarting(PublicSStartingEvent e) {
        e.fml().registerServerCommand(new CommandTeleportToWave());
    }

    @Override
    public List<PacketRegData> registerPacketsData() {
        PacketRegDataList list = new PacketRegDataList();
        list.apply(CPacketSyncBlocksDamage.HandlerSPC.class, CPacketSyncBlocksDamage.class, Side.CLIENT);
        list.apply(CPacketAreaProtector.HandlerCAP.class, CPacketAreaProtector.class, Side.CLIENT);
        list.apply(CPacketCreateFX.HandlerCFX.class, CPacketCreateFX.class, Side.CLIENT);
        list.apply(SPacketAreaProtector.HandlerSAP.class, SPacketAreaProtector.class, Side.SERVER);
        list.apply(SPacketMalletClick.HandlerPMC.class, SPacketMalletClick.class, Side.SERVER);
        return list.list();
    }

    @Override
    public void onPreInit() {
        ForgeChunkManager.setForcedChunkLoadingCallback(SRPTDMain.INSTANCE, SRPTDMain.CALLBACK);
        SRPTDMain.INTERNAL_EVENT_BUS.register(this);
    }

    @Override
    public List<ResourceLocation> registerCraftRecipesName() {
        return Arrays.asList(SRPTDMain.loc("wooden_mallet"), SRPTDMain.loc("stone_mallet.json"), SRPTDMain.loc("iron_mallet.json"));
    }
}
