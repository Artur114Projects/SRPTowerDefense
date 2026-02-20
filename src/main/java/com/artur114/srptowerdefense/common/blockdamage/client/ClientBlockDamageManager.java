package com.artur114.srptowerdefense.common.blockdamage.client;

import com.artur114.srptowerdefense.common.blockdamage.BlockDamageHandler;
import com.artur114.srptowerdefense.common.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.capabilities.GenericCapProviderNS;
import com.artur114.srptowerdefense.common.capabilities.GenericCapProviderS;
import com.artur114.srptowerdefense.common.capabilities.TowerDefenceCapabilities;
import com.artur114.srptowerdefense.main.TowerDefence;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ClientBlockDamageManager {
    public final Set<IClientDamagedChunk> loadedChunks = new HashSet<>();

    public void chunkEventUnload(ChunkEvent.Unload e) {
        Chunk chunk = e.getChunk();

        if (chunk.isEmpty()) {
            return;
        }

        IDamagedChunk damagedChunk = chunk.getCapability(TowerDefenceCapabilities.BLOCK_DAMAGE, null);

        if (damagedChunk != null && damagedChunk.isRemote()) {
            ((IClientDamagedChunk) damagedChunk).unloadChunk();

            this.loadedChunks.remove((IClientDamagedChunk) damagedChunk);
        }
    }

    public void chunkEventLoad(ChunkEvent.Load e) {
        Chunk chunk = e.getChunk();

        if (chunk.isEmpty()) {
            return;
        }

        IDamagedChunk damagedChunk = chunk.getCapability(TowerDefenceCapabilities.BLOCK_DAMAGE, null);

        if (damagedChunk != null && damagedChunk.isRemote()) {
            this.loadedChunks.add((IClientDamagedChunk) damagedChunk);
        }
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            for (IClientDamagedChunk chunk : this.loadedChunks) {
                chunk.update();
            }
        }
    }

    public void attachCapabilitiesEventChunk(AttachCapabilitiesEvent<Chunk> e) {
        e.addCapability(new ResourceLocation(TowerDefence.MODID, "blocks_damage"), new GenericCapProviderNS<>(new ClientDamagedChunk(e.getObject().getPos(), e.getObject().getWorld().provider.getDimension()), TowerDefenceCapabilities.BLOCK_DAMAGE));
    }
}
