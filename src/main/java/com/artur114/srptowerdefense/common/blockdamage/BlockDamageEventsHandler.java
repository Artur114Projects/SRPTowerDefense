package com.artur114.srptowerdefense.common.blockdamage;

import com.artur114.srptowerdefense.common.blockdamage.client.ClientBlockDamageManager;
import com.artur114.srptowerdefense.common.blockdamage.server.ServerBlockDamageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber
public class BlockDamageEventsHandler {
    public static final ClientBlockDamageManager CLIENT_MANAGER = new ClientBlockDamageManager();
    public static final ServerBlockDamageManager SERVER_MANAGER = new ServerBlockDamageManager();

    @SubscribeEvent
    public static void attachCapabilitiesChunk(AttachCapabilitiesEvent<Chunk> e) {
        if (e.getObject() != null && !e.getObject().isEmpty() && e.getObject().getWorld() != null &&  e.getObject().getWorld().isRemote) CLIENT_MANAGER.attachCapabilitiesEventChunk(e);
        if (e.getObject() != null && !e.getObject().isEmpty() && e.getObject().getWorld() != null && !e.getObject().getWorld().isRemote) SERVER_MANAGER.attachCapabilitiesEventChunk(e);
    }

    @SubscribeEvent
    public static void blockBreakEvent(BlockEvent.BreakEvent e) {
        if (!e.getWorld().isRemote) SERVER_MANAGER.blockEventBreakEvent(e);
    }

    @SubscribeEvent
    public static void tickEventServerTickEvent(TickEvent.ServerTickEvent e) {
        if (e.side == Side.SERVER) SERVER_MANAGER.tickEventServerTickEvent(e);
    }

    @SubscribeEvent
    public static void chunkWatchEventWatch(ChunkWatchEvent.Watch e) {
        if (!Objects.requireNonNull(e.getChunkInstance()).getWorld().isRemote) SERVER_MANAGER.chunkWatchEventWatch(e);
    }

    @SubscribeEvent
    public static void chunkUnloadEvent(ChunkEvent.Unload e) {
        if (e.getWorld().isRemote) CLIENT_MANAGER.chunkEventUnload(e);
    }

    @SubscribeEvent
    public static void chunkLoadEvent(ChunkEvent.Load e) {
        if (e.getWorld().isRemote) CLIENT_MANAGER.chunkEventLoad(e);
    }

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent e) {
        if (e.side == Side.CLIENT) CLIENT_MANAGER.tickEventClientTickEvent(e);
    }
}
