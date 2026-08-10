package com.artur114.srptowerdefense.register;

import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.data.PacketRegData;
import com.artur114.bananalib.mc.registry.data.PacketRegDataList;
import com.artur114.bananalib.mc.registry.interf.IHasNetworkPacket;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePre;
import com.artur114.srptowerdefense.common.commands.CommandTeleportToWave;
import com.artur114.srptowerdefense.common.network.client.CPacketAreaProtector;
import com.artur114.srptowerdefense.common.network.client.CPacketSyncBlocksDamage;
import com.artur114.srptowerdefense.common.network.server.SPacketAreaProtector;
import com.artur114.srptowerdefense.main.SRPTDMain;
import com.artur114.srptowerdefense.server.event.PublicSStartingEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@AutoInstantiate
public class RegisterHandler implements IHasNetworkPacket, ILoadStagePre {

    @SubscribeEvent
    public void serverStarting(PublicSStartingEvent e) {
        e.fml().registerServerCommand(new CommandTeleportToWave());
    }

    @Override
    public List<PacketRegData> registerPacketsData() {
        PacketRegDataList list = new PacketRegDataList();
        list.apply(CPacketSyncBlocksDamage.HandlerSPC.class, CPacketSyncBlocksDamage.class, Side.CLIENT);
        list.apply(CPacketAreaProtector.HandlerCAP.class, CPacketAreaProtector.class, Side.CLIENT);
        list.apply(SPacketAreaProtector.HandlerSAP.class, SPacketAreaProtector.class, Side.SERVER);
        return list.list();
    }

    @Override
    public void onPreInit() {
        ForgeChunkManager.setForcedChunkLoadingCallback(SRPTDMain.INSTANCE, SRPTDMain.CALLBACK);
        SRPTDMain.INTERNAL_EVENT_BUS.register(this);
    }
}
