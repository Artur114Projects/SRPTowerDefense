package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.area.Box2IM;
import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.area.IBox2IM;
import com.artur114.bananalib.math.m2d.vec.IVec2DM;
import com.artur114.bananalib.math.m2d.vec.Vec2DM;
import com.artur114.bananalib.math.m2d.vec.Vec2I;
import com.artur114.bananalib.math.m3d.vec.AdvancedBlockPos;
import com.artur114.srptowerdefense.common.capabilities.SRPTDCapabilities;
import com.artur114.srptowerdefense.main.SRPTDMain;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Iterator;
import java.util.Random;

public class TowerDefenceManager implements INBTSerializable<NBTTagCompound> {
    private final Int2ObjectMap<IWave> wavesMap = new Int2ObjectOpenHashMap<>();
    private final WorldServer world;

    public TowerDefenceManager(WorldServer world) {
        this.world = world;

        Random rand = new Random();
        this.addWave(new WaveTest(new Vec2I(rand.nextInt(40) - 20, rand.nextInt(40) - 20)), 0);
    }

    public void update() {
        Iterator<IWave> iterator = this.wavesMap.values().iterator();
        IVec2DM vec = new Vec2DM();
        IBox2IM box = new Box2IM();

        while (iterator.hasNext()) {
            IWave wave = iterator.next();

            box.set(wave.box());

            if (wave.pos().distanceSq(wave.targetChunk()) > 1.0D) {
                wave.move(vec.set(wave.targetChunk()).subtract(wave.pos()).normalize().scale(wave.speed()));
            }

            IBox2I boxNew = wave.box();

            if (!boxNew.equals(box)) {
                for (int x = boxNew.minX(); x <= boxNew.maxX(); x++) {
                    for (int y = boxNew.minY(); y <= boxNew.maxY(); y++) {
                        Chunk chunk = this.world.getChunkProvider().id2ChunkMap.get(ChunkPos.asLong(x, y));

                        if (chunk == null || chunk.unloadQueued || !chunk.isLoaded()) {
                            continue;
                        }

                        wave.onChunkLoaded(chunk);
                    }
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

    public void chunkSave(NBTTagCompound data) {
        String waveDataName = new ResourceLocation(SRPTDMain.MODID, "wave_data").toString();
        if (data.hasKey("Level")) {
            NBTTagCompound level = data.getCompoundTag("Level");
            if (level.hasKey("Entities")) {
                NBTTagList entities = level.getTagList("Entities", 10);
                NBTTagList entitiesRebuild = new NBTTagList();
                for (int i = 0; i != entities.tagCount(); i++) {
                    NBTTagCompound entity = entities.getCompoundTagAt(i);

                    boolean flag = true;

                    if (entity.hasKey("ForgeCaps")) {
                        NBTTagCompound forgeCaps = entity.getCompoundTag("ForgeCaps");
                        if (forgeCaps.hasKey(waveDataName)) {
                            NBTTagCompound waveData = forgeCaps.getCompoundTag(waveDataName);
                            if (waveData.getBoolean("bindToWave")) {
                                flag = false;
                            }
                        }
                    }

                    if (flag) {
                        entitiesRebuild.appendTag(entity);
                    }
                }
                level.setTag("Entities", entitiesRebuild);
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

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
