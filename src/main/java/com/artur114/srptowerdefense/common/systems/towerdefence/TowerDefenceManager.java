package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2DM;
import com.artur114.bananalib.math.m2d.vec.Vec2DM;
import com.artur114.bananalib.math.m2d.vec.Vec2I;
import com.artur114.srptowerdefense.common.capabilities.SRPTDCapabilities;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TowerDefenceManager implements INBTSerializable<NBTTagCompound> {
    private final Int2ObjectMap<IWave> wavesMap = new Int2ObjectOpenHashMap<>();
    private final WorldServer world;

    public TowerDefenceManager(WorldServer world) {
        this.world = world;

        Random rand = new Random();
        this.addWave(new WaveTest(new Vec2I(rand.nextInt(80) - 40, rand.nextInt(80) - 40)), 0);
    }

    public void update() {
        Iterator<IWave> iterator = this.wavesMap.values().iterator();
        IVec2DM vec = new Vec2DM();

        while (iterator.hasNext()) {
            IWave wave = iterator.next();

            if (wave.pos().distanceSq(wave.targetChunk()) > 1.0D) {
                wave.move(vec.set(wave.targetChunk()).subtract(wave.pos()).normalize().scale(wave.speed()));
            }

            IBox2I box = wave.box();

            for (int x = box.minX(); x <= box.maxX(); x++) {
                for (int y = box.minY(); y <= box.maxY(); y++) {
                    Chunk chunk = this.world.getChunkProvider().id2ChunkMap.get(ChunkPos.asLong(x, y));

                    if (chunk == null || chunk.unloadQueued || !chunk.isLoaded()) {
                        continue;
                    }

                    wave.onChunkLoaded(chunk);
                }
            }

            if (!wave.isAlive()) {
                iterator.remove();
            }
        }
    }

    public void entityDead(Entity entity) {
        WaveEntityData data = entity.getCapability(SRPTDCapabilities.WAVE_ENTITY_DATA, null);
        if (data != null && data.isBindToWave()) {
            IWave wave = this.wavesMap.get(data.waveId());
            if (wave != null) wave.onEntityDied(data);
        }
    }

    public void chunkUnload(Chunk chunk) {
        List<Entity> toRemove = new ArrayList<>();
        for (ClassInheritanceMultiMap<Entity> entities : chunk.getEntityLists()) {
            for (Entity entity : entities) {
                if (this.processUnloadEntity(entity)) {
                    toRemove.add(entity);
                }
            }
        }
        for (Entity entity : toRemove) {
            ClassInheritanceMultiMap<Entity>[] entityLists = chunk.getEntityLists();
            int index = entity.chunkCoordY;

            if (index < 0) {
                index = 0;
            }

            if (index >= entityLists.length) {
                index = entityLists.length - 1;
            }

            entityLists[index].remove(entity);
        }
    }

    public void unload() {
        for (Entity entity : this.world.loadedEntityList) {
            if (this.processUnloadEntity(entity)) {
                Chunk chunk = this.world.getChunkProvider().getLoadedChunk(entity.chunkCoordX, entity.chunkCoordZ);
                if (chunk != null) {
                    chunk.removeEntity(entity);
                }
            }
        }
    }

    public IWave waveFromId(int id) {
        return this.wavesMap.get(id);
    }

    public void addWave(IWave wave, int id) {
        wave.init(this.world, this, id);
        this.wavesMap.put(id, wave);
    }

    private boolean processUnloadEntity(Entity entity) {
        WaveEntityData data = entity.getCapability(SRPTDCapabilities.WAVE_ENTITY_DATA, null);
        if (data != null && data.isBindToWave()) {
            IWave wave = this.wavesMap.get(data.waveId());
            if (wave != null) {
                return wave.onEntityUnload(data);
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
