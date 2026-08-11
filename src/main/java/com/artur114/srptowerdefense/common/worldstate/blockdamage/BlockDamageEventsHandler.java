package com.artur114.srptowerdefense.common.worldstate.blockdamage;

import com.artur114.srptowerdefense.common.worldstate.blockdamage.client.ClientBlockDamageManager;
import com.artur114.srptowerdefense.common.worldstate.blockdamage.server.ServerBlockDamageManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public static void worldTick(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote) SERVER_MANAGER.tickEventWorldTickEvent(e);
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void renderWorldLast(RenderWorldLastEvent e) {
        CLIENT_MANAGER.renderWorldLastEvent(e);
    }
}
