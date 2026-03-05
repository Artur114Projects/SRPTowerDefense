package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.vec.Vec2I;
import com.artur114.srptowerdefense.common.capabilities.SRPTDCapabilities;
import com.artur114.srptowerdefense.main.SRPTDMain;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

public class TowerDefenceManager implements INBTSerializable<NBTTagCompound> {
    private final Map<Class<? extends ITowerDefenceObject>, List<? extends ITowerDefenceObject>> class2ObjectMap = new HashMap<>();
    private final Int2ObjectMap<ITowerDefenceObject> objectsMap = new Int2ObjectOpenHashMap<>();
    private final WorldServer world;

    public TowerDefenceManager(WorldServer world) {
        this.world = world;

        this.addObject(new WaveDebug(new Vec2I(20, 0)), 0);
    }

    public void update() {
        Iterator<ITowerDefenceObject> iterator = this.objectsMap.values().iterator();

        while (iterator.hasNext()) {
            ITowerDefenceObject obj = iterator.next();

            if (this.world.getTotalWorldTime() % (obj.ticksToUpdate() * 8L) == 0) {
                obj.update();
            }

            if (!obj.isAlive()) {
                iterator.remove();
                this.onObjRemoved(obj);
            }
        }
    }

    public void entityDead(Entity entity) {
        TowerDefenceEntity data = entity.getCapability(SRPTDCapabilities.TD_ENTITY_DATA, null);
        if (data != null && data.isBindToTDObj()) {
            ITowerDefenceObject object = this.objectsMap.get(data.objectId());
            if (object instanceof ITDEntityManager) ((ITDEntityManager) object).onEntityDied(data);
        }
    }

    public void chunkLoad(Chunk chunk) {
        for (ITowerDefenceObject obj : this.objectsMap.values()) {
            if (obj.box().contains(chunk.x, chunk.z)) {
                obj.onChunkLoaded(chunk);
            }
        }
    }

    public void chunkSave(NBTTagCompound data) {
        this.processChunkData(data);
    }

    @SuppressWarnings("unchecked")
    public <T extends ITowerDefenceObject> List<T> tdObjects(Class<T> clazz) {
        if (this.class2ObjectMap.containsKey(clazz)) {
            return (List<T>) this.class2ObjectMap.get(clazz);
        }

        List<T> list = new ArrayList<>(this.objectsMap.values().size());
        if (clazz.equals(ITowerDefenceObject.class)) {
            list.addAll((Collection<? extends T>) this.objectsMap.values());
        } else {
            for (ITowerDefenceObject obj : this.objectsMap.values()) {
                if (clazz.isInstance(obj)) {
                    list.add(clazz.cast(obj));
                }
            }
        }

        this.class2ObjectMap.put(clazz, list);

        return list;
    }

    public ITowerDefenceObject tdObjFromId(int id) {
        return this.objectsMap.get(id);
    }

    public void addObject(ITowerDefenceObject obj, int id) {
        this.objectsMap.put(id, obj);
        this.onObjAdded(obj, id);
    }

    public void removeObject(int id) {
        this.onObjRemoved(this.objectsMap.remove(id));
    }

    private void onObjAdded(ITowerDefenceObject obj, int id) {
        if (obj == null) {
            return;
        }

        this.class2ObjectMap.clear();
        obj.init(this.world, this, id);
    }

    private void onObjRemoved(ITowerDefenceObject obj) {
        if (obj == null) {
            return;
        }

        this.class2ObjectMap.clear();
        obj.onRemove();
    }

    private void processChunkData(NBTTagCompound data) {
        String waveDataName = new ResourceLocation(SRPTDMain.MODID, "tower_defence_entity").toString();
        if (data.hasKey("Level")) {
            NBTTagCompound level = data.getCompoundTag("Level");
            if (level.hasKey("Entities")) {
                NBTTagList entities = level.getTagList("Entities", 10);
                NBTTagList entitiesRebuild = new NBTTagList();
                for (int i = 0; i != entities.tagCount(); i++) {
                    NBTTagCompound entity = entities.getCompoundTagAt(i);


                    if (entity.hasKey("ForgeCaps")) {
                        NBTTagCompound forgeCaps = entity.getCompoundTag("ForgeCaps");
                        if (forgeCaps.hasKey(waveDataName)) {
                            NBTTagCompound tdData = forgeCaps.getCompoundTag(waveDataName);
                            if (tdData.hasKey("bindToObject")) {
                                ITowerDefenceObject obj = this.tdObjFromId(tdData.getInteger("bindToObject"));

                                if (obj instanceof ITDEntityManager) {
                                    entity = ((ITDEntityManager) obj).modifyEntityData(entity);
                                }
                            }
                        }
                    }

                    if (entity != null) {
                        entitiesRebuild.appendTag(entity);
                    }
                }
                level.setTag("Entities", entitiesRebuild);
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
