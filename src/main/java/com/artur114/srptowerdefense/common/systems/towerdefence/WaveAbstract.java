package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.Box2IM;
import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.area.IBox2IM;
import com.artur114.bananalib.math.m2d.vec.*;
import com.artur114.bananalib.math.m3d.vec.AdvancedBlockPos;
import com.artur114.srptowerdefense.common.capabilities.SRPTDCapabilities;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public abstract class WaveAbstract implements IWave {
    protected Int2ObjectMap<EntityRecord> entityRecords;
    protected TowerDefenceManager owner;
    protected WorldServer world;
    protected IVec2D targetChunk;
    protected BlockPos target;
    protected IVec2DM pos;
    protected IBox2IM box;
    protected Random rand;
    protected float speed;
    protected int id;

    public WaveAbstract() {}

    public WaveAbstract(IVec2I pos, BlockPos target, float speed) {
        this.pos = new Vec2DM(pos);
        this.box = new Box2IM(pos, pos);

        this.speed = speed;
        this.target = target;
        this.rand = new Random();
        this.entityRecords = new Int2ObjectOpenHashMap<>();
        this.targetChunk = new Vec2D((target.getX() >> 4) + 0.5F, (target.getZ() >> 4) + 0.5F);
    }

    @Override
    public void init(WorldServer world, TowerDefenceManager owner, int id) {
        this.world = world;
        this.owner = owner;
        this.id = id;
    }

    @Override
    public void onChunkLoaded(Chunk chunk) {
        AdvancedBlockPos blockPos = AdvancedBlockPos.obtain();

        for (EntityRecord record : this.entityRecords.values()) {
            if (!record.isLoaded()) {
                blockPos.setPos(chunk.x, chunk.z).add(this.rand.nextInt(16), 0, this.rand.nextInt(16)).setWorldY(this.world);
                record.load(this.world, blockPos);
            }
        }

        AdvancedBlockPos.release(blockPos);
    }

    @Override
    public void onEntityUnload(WaveEntityData entity) {
        if (entity.data.hasKey(EntityRecord.ENTITY_RECORD_NBT_LOCATION)) {
            EntityRecord record = this.entityRecords.get(entity.data.getInteger(EntityRecord.ENTITY_RECORD_NBT_LOCATION));
            if (record != null) record.unload(entity);
        }
    }

    @Override
    public void onEntityDied(WaveEntityData entity) {
        if (entity.data.hasKey(EntityRecord.ENTITY_RECORD_NBT_LOCATION)) {
            this.entityRecords.remove(entity.data.getInteger(EntityRecord.ENTITY_RECORD_NBT_LOCATION));
        }
    }

    @Override
    public void move(IVec2D vec) {
        if (this.entityRecords.values().stream().anyMatch(EntityRecord::isLoaded)) {
            IVec2DM vec2D = new Vec2DM();
            int count = 0;

            for (EntityRecord record : this.entityRecords.values()) {
                if (record.isLoaded()) {
                    double x = record.entity().entity.posX / 16.0D;
                    double y = record.entity().entity.posZ / 16.0D;
                    vec2D.add(x, y);
                    count++;
                }
            }

            this.pos.set(vec2D.scale(1.0D / count));
        } else {
            this.pos.add(vec);
        }

        int x = BananaMath.floor(this.pos.x());
        int y = BananaMath.floor(this.pos.y());

        this.box.set(x, y, x, y);

        System.out.println(this.pos);
    }

    @Override
    public boolean isAlive() {
        return !this.entityRecords.isEmpty();
    }

    @Override
    public float speed() {
        return this.speed;
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public IVec2D targetChunk() {
        return this.targetChunk;
    }

    @Override
    public BlockPos target() {
        return this.target;
    }

    @Override
    public IVec2D pos() {
        return this.pos;
    }

    @Override
    public IBox2I box() {
        return this.box;
    }

    protected int addEntity(ICanCreateEntity creator) {
        int id = this.rand.nextInt();
        this.entityRecords.put(id, new EntityRecord(creator, this, id));
        return id;
    }

    protected void addEntities(ICanCreateEntity creator, int count) {
        for (int i = 0; i != count; i++) {
            int id = this.rand.nextInt();
            this.entityRecords.put(id, new EntityRecord(creator, this, id));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return null;
    }

    public static class EntityCounted {
        private final ICanCreateEntity creator;
        private final int count;

        public EntityCounted(Class<? extends EntityLiving> clazz, int count) {
            this.creator = new EntityCreatorClass(clazz);
            this.count = count;
        }

        public EntityCounted(ResourceLocation rl, int count) {
            this.creator = new EntityCreatorRl(rl);
            this.count = count;
        }

        private EntityRecord[] records(Random keysCreator, WaveAbstract parent) {
            EntityRecord[] records = new EntityRecord[this.count];

            for (int i = 0; i != this.count; i++) {
                records[i] = new EntityRecord(this.creator, parent, keysCreator.nextInt());
            }

            return records;
        }
    }

    public static class EntityCreatorRl implements ICanCreateEntity {
        private final ResourceLocation rl;

        public EntityCreatorRl(ResourceLocation rl) {
            this.rl = rl;
        }

        @Override
        public EntityLiving create(World world) {
            return (EntityLiving) EntityList.createEntityByIDFromName(this.rl, world);
        }
    }

    public static class EntityCreatorClass implements ICanCreateEntity {
        private final Class<? extends EntityLiving> clazz;

        public EntityCreatorClass(Class<? extends EntityLiving> clazz) {
            this.clazz = clazz;
        }

        @Override
        public EntityLiving create(World world) {
            return (EntityLiving) EntityList.newEntity(this.clazz, world);
        }
    }

    public interface ICanCreateEntity {
        EntityLiving create(World world);
    }

    public static class EntityRecord {
        public static final String ENTITY_RECORD_NBT_LOCATION = "entity_record_id";
        private final ICanCreateEntity record;
        private final WaveAbstract owner;
        private final int id;
        private boolean loaded = false;
        private WaveEntityData entity;

        private EntityRecord(ICanCreateEntity record, WaveAbstract owner, int id) {
            this.record = record;
            this.owner = owner;
            this.id = id;
        }

        public void load(World world, BlockPos pos) {
            if (this.loaded) {
                return;
            }
            EntityLiving entity = this.record.create(world);
            if (entity != null) {
                WaveEntityData data = entity.getCapability(SRPTDCapabilities.WAVE_ENTITY_DATA, null);
                if (data != null) {
                    this.entity = data;

                    data.data.setInteger(ENTITY_RECORD_NBT_LOCATION, this.id);
                    data.bindWave(this.owner);

                    entity.setPositionAndRotation(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                    world.spawnEntity(entity);

                    this.loaded = true;

                    System.out.println("Record(" + this.id + ")" + " loaded");
                }
            }
        }

        public void unload(WaveEntityData entity) {
            entity.kill();

            this.entity = null;
            this.loaded = false;

            System.out.println("Record(" + this.id + ")" + " unloaded");
        }

        public boolean isLoaded() {
            return this.loaded;
        }

        public WaveEntityData entity() {
            return this.entity;
        }
    }
}
