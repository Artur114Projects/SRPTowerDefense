package com.artur114.srptowerdefense.common.blockdamage.server;


import com.artur114.srptowerdefense.common.blockdamage.BlockDamageHandler;
import com.artur114.srptowerdefense.common.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.capabilities.GenericCapProviderS;
import com.artur114.srptowerdefense.common.capabilities.TowerDefenceCapabilities;
import com.artur114.srptowerdefense.main.TowerDefence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ServerBlockDamageManager {
    private static final Set<IServerDamagedChunk> syncQueue = new HashSet<>();

    /*--------------------------------------EVENTS--------------------------------------*/

    public void attachCapabilitiesEventChunk(AttachCapabilitiesEvent<Chunk> e) {
        e.addCapability(new ResourceLocation(TowerDefence.MODID, "blocks_damage"), new GenericCapProviderS<>(new ServerDamagedChunk(e.getObject().getWorld(), e.getObject().getPos()), TowerDefenceCapabilities.BLOCK_DAMAGE));
    }

    public void blockEventBreakEvent(BlockEvent.BreakEvent e) {
        Chunk chunk = e.getWorld().getChunkFromBlockCoords(e.getPos());

        if (chunk.isEmpty()) {
            return;
        }

        IDamagedChunk damagedChunk = chunk.getCapability(TowerDefenceCapabilities.BLOCK_DAMAGE, null);

        if (damagedChunk != null && !damagedChunk.isRemote()) {
            ((IServerDamagedChunk) damagedChunk).onBlockBreak(e.getPos());
        }
    }

    public void tickEventServerTickEvent(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            return;
        }

        this.processSyncQueue();
    }

    public void chunkWatchEventWatch(ChunkWatchEvent.Watch e) {
        Chunk chunk = e.getChunkInstance();
        if (chunk != null && !chunk.isEmpty()) {
            IServerDamagedChunk protectedChunk = (IServerDamagedChunk) chunk.getCapability(TowerDefenceCapabilities.BLOCK_DAMAGE, null);
            if (protectedChunk != null && !protectedChunk.isEmpty()) {
                protectedChunk.sendInitialDataToClient(e.getPlayer());
            }
        }
    }

    /*--------------------------------------UTILS--------------------------------------*/

    private void processSyncQueue() {
        if (syncQueue.isEmpty()) return;

        Iterator<IServerDamagedChunk> iterator = syncQueue.iterator();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        while (iterator.hasNext()) {
            IServerDamagedChunk chunk = iterator.next();
            World world = server.getWorld(chunk.getDimension());
            for (EntityPlayer player : world.playerEntities) {
                if (player != null && isPlayerWatch((EntityPlayerMP) player, world, chunk.getPos()) ) {
                    chunk.syncToClient((EntityPlayerMP) player);
                }
            }
            chunk.syncToClient(null);
            iterator.remove();
        }
    }

    private boolean isPlayerWatch(EntityPlayerMP player, World world, ChunkPos pos) {
        return ((WorldServer) world).getPlayerChunkMap().isPlayerWatchingChunk(player, pos.x, pos.z);
    }

    /*--------------------------------------PUBLIC METHODS--------------------------------------*/

    public void addToSyncQueue(IServerDamagedChunk chunk) {
        syncQueue.add(chunk);
    }
}
