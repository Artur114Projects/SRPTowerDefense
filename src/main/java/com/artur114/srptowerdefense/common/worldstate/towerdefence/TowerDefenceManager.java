package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.srptowerdefense.common.init.InitCapabilities;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.*;

public class TowerDefenceManager implements INBTSerializable<NBTTagCompound> {
    private static final Logger log = LogManager.getLogger("SRPTowerDefence");
    private final Map<Class<? extends ITowerDefenceObject>, List<? extends ITowerDefenceObject>> class2ObjectMap = new HashMap<>();
    private final Int2ObjectMap<ITowerDefenceObject> objectsMap = new Int2ObjectOpenHashMap<>();
    private final Map<ResourceLocation, ITDObjectsGenerator> generators = new HashMap<>();
    private final SecureRandom idGen = new SecureRandom();
    private boolean isLoaded = false;
    private final WorldServer world;

    public TowerDefenceManager(WorldServer world) {
        this.world = world;

        for (Class<? extends ITDObjectsGenerator> clazz : ITDObjectsGenerator.classes()) {
            ITDObjectsGenerator generator = ITDObjectsGenerator.create(clazz);

            if (generator != null) {
                this.generators.put(ITDObjectsGenerator.nameFromClass(clazz), generator);
            } else {
                log.warn("Failed to create generator {}", clazz.getName());
            }
        }
    }

    public void load() {
        if (!this.isLoaded) {
            this.objectsMap.forEach((id, obj) -> obj.init(this.world, this, id)); this.isLoaded = true;
        }
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

        for (ITDObjectsGenerator generator : this.generators.values()) {
            generator.update(this.world, this);
        }
    }

    public void entityDead(Entity entity) {
        TowerDefenceEntity data = entity.getCapability(InitCapabilities.TD_ENTITY_DATA, null);
        if (data != null && data.isBindToTDObj()) {
            ITowerDefenceObject object = this.objectsMap.get(data.objectId());
            if (object instanceof ITDEntityManager) ((ITDEntityManager) object).onEntityDied(data);
        }
    }

    public void chunkLoad(Chunk chunk) {
        if (!this.isLoaded) {
            return;
        }
        for (ITowerDefenceObject obj : this.objectsMap.values()) {
            if (obj.box().contains(chunk.x, chunk.z)) {
                obj.onChunkLoaded(chunk);
            }
        }
    }

    public void chunkSave(NBTTagCompound data) {
        this.processChunkData(data);
    }

    public int createSafeId() {
        return this.idGen.nextInt();
    }

    @SuppressWarnings("unchecked")
    public <T extends ITowerDefenceObject> List<T> tdObjects(Class<T> clazz) {
        if (this.class2ObjectMap.containsKey(clazz)) {
            return (List<T>) this.class2ObjectMap.get(clazz);
        }

        List<T> list = new ArrayList<>(this.objectsMap.size());
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
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        this.objectsMap.forEach((id, obj) -> {
            NBTTagCompound n = new NBTTagCompound();
            n.setInteger("objId", id);
            n.setString("objClass", obj.getClass().getName());
            list.appendTag(obj.writeToNBT(n));
        });
        NBTTagList list1 = new NBTTagList();
        this.generators.forEach((id, obj) -> {
            NBTTagCompound n = new NBTTagCompound();
            n.setString("name", id.toString());
            list1.appendTag(obj.writeToNBT(n));
        });
        nbt.setTag("tdGenerators", list1);
        nbt.setTag("tdObjects", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("tdObjects", 10);
        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound obj = list.getCompoundTagAt(i);
            String clazzName = obj.getString("objClass");
            Class<?> clazz;

            try {
                clazz = Class.forName(clazzName);
            } catch (ClassNotFoundException e) {
                log.warn("Cannot find obj class for deserialize {}, skipping...", clazzName); continue;
            }

            try {
                ITowerDefenceObject instance = (ITowerDefenceObject) clazz.newInstance();
                int id = obj.getInteger("objId");
                instance.readFromNBT(obj);
                this.objectsMap.put(id, instance);
            } catch (Exception e) {
                log.warn("Can't instantiate obj {}, skipping...", clazz, e);
            }
        }
        NBTTagList list1 = nbt.getTagList("tdGenerators", 10);
        for (int i = 0; i != list1.tagCount(); i++) {
            NBTTagCompound obj = list1.getCompoundTagAt(i);
            ResourceLocation name = new ResourceLocation(obj.getString("name"));

            ITDObjectsGenerator generator = this.generators.get(name);

            if (generator != null) {
                generator.readFromNBT(obj);
            }
        }
    }

    static {
        ITDObjectsGenerator.registerGenerator(SRPTDMain.loc("spawn_generator"), SpawnWaveGenerator.class);
    }
}
