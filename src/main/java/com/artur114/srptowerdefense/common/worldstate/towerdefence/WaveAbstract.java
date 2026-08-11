package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.core.m3d.vec.IVec3IC;
import com.artur114.bananalib.math.m2d.box.Box2IM;
import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.math.m2d.box.IBox2IM;
import com.artur114.bananalib.math.m2d.vec.*;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.bananalib.mc.nbt.INBTSerializable;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.common.pathfinding.PathNavigateGroundForced;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public abstract class WaveAbstract implements IWave {
    protected Int2ObjectMap<EntityRecord> entityRecords;
    protected PosMc3IM entityMoveTarget;
    protected TowerDefenceManager owner;
    protected IVec2DM targetChunk;
    protected IWaveTarget target;
    protected WorldServer world;
    protected int idleTime;
    protected int targetId;
    protected IVec2DM pos;
    protected IBox2IM box;
    protected Random rand;
    protected float speed;
    protected int id;

    public WaveAbstract() {
        this.rand = new Random();
        this.targetChunk = new Vec2DM();
        this.entityRecords = new Int2ObjectOpenHashMap<>();
    }

    public WaveAbstract(IVec2I wavePos, IWaveTarget target, float speed) {
        this.pos = new Vec2DM(wavePos);
        this.box = new Box2IM(wavePos, wavePos).grow(2);;

        this.speed = speed;
        this.rand = new Random();
        this.targetId = target.id();
        this.targetChunk = new Vec2DM();
        this.entityRecords = new Int2ObjectOpenHashMap<>();
    }

    @Override
    public void update() {
        IVec2DM vec = new Vec2DM();
        IBox2IM box = new Box2IM();

        box.set(this.box());

        if (this.pos().distanceSq(this.targetChunk()) > 1.0D) {
            this.move(vec.set(this.targetChunk()).subtract(this.pos()).normalize().scale(this.speed()));
        }

        IBox2I boxNew = this.box();

        if (!boxNew.equals(box)) {
            this.idleTime = 0;
            for (int x = boxNew.minX(); x <= boxNew.maxX(); x++) {
                for (int y = boxNew.minY(); y <= boxNew.maxY(); y++) {
                    Chunk chunk = this.world.getChunkProvider().id2ChunkMap.get(ChunkPos.asLong(x, y));

                    if (chunk == null || chunk.unloadQueued || !chunk.isLoaded()) {
                        continue;
                    }

                    if (this.world.getPersistentChunks().containsKey(chunk.getPos()) || BananaMC.isChunksLoaded(this.world, box.set(x, y, x, y).grow(2))) {
                        this.onEntryToLoadedChunk(chunk);
                    }
                }
            }
        } else {
            this.idleTime++;
        }

        if (this.world.getPersistentChunks().containsKey(new ChunkPos(BananaMath.floor(this.pos.x()), BananaMath.floor(this.pos.y())))) {
            PosMc3IM blockPos = PosMc3IM.obtain();
            IVec2I pos = this.pos.floor();

            this.entityRecords.values().removeIf((record) -> {
                if (!record.isLoaded()) {
                    blockPos.setChunk(pos).add(this.rand.nextInt(16), 0, this.rand.nextInt(16)).setY(BananaMC.findHighestBlock(this.world, blockPos));
                    record.load(this.world, blockPos);
                    return !record.isLoaded();
                }

                return false;
            });

            PosMc3IM.release(blockPos);
        }
    }

    @Override
    public void init(WorldServer world, TowerDefenceManager owner, int id) {
        this.world = world;
        this.owner = owner;
        this.id = id;

        ITowerDefenceObject obj = owner.tdObjFromId(this.targetId);

        if (obj instanceof IWaveTarget) {
            this.target = (IWaveTarget) obj;
        } else {
            this.target = IWaveTarget.INVALID;
        }
    }

    @Override
    public void onChunkLoaded(Chunk chunk) {
        this.loadRecords(chunk);
    }

    @Override
    public void onEntityDied(TowerDefenceEntity entity) {
        if (entity.data.hasKey(EntityRecord.ENTITY_RECORD_NBT_LOCATION)) {
            this.entityRecords.remove(entity.data.getInteger(EntityRecord.ENTITY_RECORD_NBT_LOCATION));
        }
    }

    @Override
    public void onEntityEvolved(TowerDefenceEntity oldEntity, TowerDefenceEntity newEntity) {
        if (oldEntity == null || newEntity == null) {
            return;
        }

        EntityRecord record = this.entityRecords.get(oldEntity.data.getInteger(EntityRecord.ENTITY_RECORD_NBT_LOCATION));

        if (record != null) {
            record.onEvolved(newEntity);
        }
    }

    @Override
    public NBTTagCompound modifyEntityData(NBTTagCompound entity) {
        return null;
    }

    @Override
    public void onRemove() {
        for (EntityRecord record : this.entityRecords.values()) {
            record.onRemove();
        }
    }

    @Override
    public int ticksToUpdate() {
        return 1;
    }

    @Override
    public boolean isAlive() {
        return !this.entityRecords.isEmpty() && this.target.isAlive() && this.idleTime < 1200;
    }

    @Override
    public float speed() {
        return (this.speed / 16.0F) * (8.0F / 20.0F);
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public IVec2D targetChunk() {
        return this.targetChunk.set(this.target.pos());
    }

    @Override
    public IWaveTarget target() {
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

    protected boolean canAutoMove() {
        return !this.world.getPersistentChunks().containsKey(new ChunkPos(BananaMath.floor(this.pos.x()), BananaMath.floor(this.pos.y())));
    }

    public int addEntity(ResourceLocation entity) {
        int id = this.rand.nextInt();
        this.entityRecords.put(id, new EntityRecord(entity, this, id));
        return id;
    }

    public void addEntity(ResourceLocation entity, int count) {
        for (int i = 0; i != count; i++) {
            int id = this.rand.nextInt();
            this.entityRecords.put(id, new EntityRecord(entity, this, id));
        }
    }

    protected void onEntryToLoadedChunk(Chunk chunk) {
        this.loadRecords(chunk);
    }

    protected void move(IVec2D vec) {
        if (this.entityRecords.values().stream().anyMatch(EntityRecord::isLoaded)) {
            if (!this.isMoveTargetValide()) {
                this.updateMoveTarget();
            }

            this.rebindMoveTarget();
            this.updateWavePos();
            this.gc();
        } else if (this.canAutoMove()) {
            this.entityMoveTarget = null;

            this.pos.add(vec);
        }

        int x = BananaMath.floor(this.pos.x());
        int y = BananaMath.floor(this.pos.y());

        this.box.set(x, y, x, y).grow(2);
    }

    protected void loadRecords(Chunk chunk) {
        PosMc3IM blockPos = PosMc3IM.obtain();
        IVec2I pos = this.pos.floor();

        for (EntityRecord record : this.entityRecords.values()) {
            if (!record.isLoaded()) {
                blockPos.setChunk(pos).add(this.rand.nextInt(16), 0, this.rand.nextInt(16)).setY(BananaMC.findHighestBlock(this.world, blockPos));
                record.load(this.world, blockPos);

                if (this.entityMoveTarget == null) {
                    this.entityMoveTarget = new PosMc3IM().setChunk(pos).add(8, 0, 8).setY(BananaMC.findHighestBlock(this.world, blockPos));
                }
            }
        }

        PosMc3IM.release(blockPos);
    }

    protected void gc() {
        Iterator<EntityRecord> iterator = this.entityRecords.values().iterator();

        while (iterator.hasNext()) {
            EntityRecord record = iterator.next();

            if (record.gc(this.entityMoveTarget)) {
                record.onRemove();
                iterator.remove();
            }
        }
    }

    protected void updateWavePos() {
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
    }

    protected void updateMoveTarget() {
        if (this.target.isForcedChunk(this.pos)) {
            this.entityMoveTarget.set(this.target.causePos()); return;
        }

        PosMc3IM blockPos = PosMc3IM.obtain();
        blockPos.set(this.target.causePos()).subtract((IVec3IC) this.entityMoveTarget).setY(0);
        Vec3d vec = new Vec3d(blockPos).normalize();

        double range = 16.0F;
        int r = 2;

        int x = BananaMath.round(range * vec.x) + (this.rand.nextInt(r * 2) - r);
        int z = BananaMath.round(range * vec.z) + (this.rand.nextInt(r * 2) - r);
        blockPos.set((IVec3IC) this.entityMoveTarget).add(x, 0, z).setY(BananaMC.findHighestBlock(this.world, blockPos, state -> state.getMaterial() == Material.LEAVES)).up();
        this.entityMoveTarget.set((IVec3IC) blockPos);

        PosMc3IM.release(blockPos);
    }

    protected void rebindMoveTarget() {
        for (EntityRecord record : this.entityRecords.values()) {
            if (record.isLoaded()) {
                record.entity().setMoveTarget(this.entityMoveTarget);
                record.entity().setDirectTarget(this.entityMoveTarget.equals(this.target.causePos()));
            }
        }
    }

    protected boolean isMoveTargetValide() {
        if (this.entityMoveTarget.equals(this.target.causePos())) {
            return true;
        }

        float avDistance = -1.0F;
        int entitiesCount = 0;

        for (EntityRecord record : this.entityRecords.values()) {
            if (record.isLoaded()) {
                avDistance += (float) record.entity().entity.getDistanceSq(this.entityMoveTarget.getX() + 0.5, record.entity().entity.posY, this.entityMoveTarget.getZ() + 0.5);
                entitiesCount++;
            }
        }

        avDistance /= entitiesCount;


        return avDistance > 8.0F * 8.0F;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound nbt) {
        this.targetId = nbt.getInteger("targetId");
        this.speed = nbt.getFloat("speed");
        this.pos = new Vec2DM(nbt.getDouble("x"), nbt.getDouble("y"));
        this.box = new Box2IM(this.pos, this.pos).grow(2);
        this.idleTime = nbt.getInteger("idleTime");
        NBTTagList list = nbt.getTagList("entityRecords", 10);
        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound n = list.getCompoundTagAt(i);
            int id = n.getInteger("recordId");
            EntityRecord record = new EntityRecord(this, id);
            record.readFromNBT(n);
            this.entityRecords.put(id, record);
        }
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        nbt.setInteger("targetId", this.targetId);
        nbt.setInteger("idleTime", this.idleTime);
        nbt.setFloat("speed", this.speed);
        nbt.setDouble("x", this.pos.x());
        nbt.setDouble("y", this.pos.y());
        NBTTagList list = new NBTTagList();
        this.entityRecords.forEach((id, record) -> {
            NBTTagCompound n = new NBTTagCompound();
            n.setInteger("recordId", id);
            list.appendTag(record.writeToNBT(n));
        });
        nbt.setTag("entityRecords", list);
        return nbt;
    }

    public static class EntityRecord implements INBTSerializable {
        public static final String ENTITY_RECORD_NBT_LOCATION = "entity_record_id";
        private PosMc3IM prevEntityPos = new PosMc3IM();
        private final IVec2IM prevPos = new Vec2IM();
        private NBTTagCompound entityData = null;
        private TowerDefenceEntity entity;
        private final WaveAbstract owner;
        private int idleTime = 0;
        private String record;
        private final int id;

        private EntityRecord(WaveAbstract owner, int id) {
            this.owner = owner;
            this.id = id;
        }

        private EntityRecord(ResourceLocation record, WaveAbstract owner, int id) {
            this.record = record.toString();
            this.owner = owner;
            this.id = id;
        }

        public void onEvolved(TowerDefenceEntity newEntity) {
            newEntity.setMoveTarget(this.entity.moveTarget());
            newEntity.setMoveSpeed(this.entity.moveSpeed());
            newEntity.data = this.entity.data.copy();
            newEntity.bind(this.owner);
            this.entity = newEntity;

            this.entityData = null;
            this.record = Objects.requireNonNull(EntityList.getKey(newEntity.entity)).toString();
        }

        public void load(World world, BlockPos pos) {
            if (!this.canEntityUpdate(world, pos)) {
                return;
            }
            Entity entityRaw = EntityList.createEntityByIDFromName(new ResourceLocation(this.record), world);
            if (entityRaw instanceof EntityLiving) {
                EntityLiving entity = (EntityLiving) entityRaw;
                TowerDefenceEntity data = entity.getCapability(InitCapabilities.TD_ENTITY_DATA, null);
                if (data != null) {
                    this.entity = data;

                    data.data.setInteger(ENTITY_RECORD_NBT_LOCATION, this.id);
                    data.bind(this.owner);
                    data.setMoveSpeed(this.owner.speed);
                    if (this.entityData != null) {
                        this.entityData.setUniqueId("UUID", UUID.randomUUID());
                        entity.readFromNBT(this.entityData);
                    }
                    if ((!this.prevPos.equals(this.owner.pos.floor()) && this.owner.canAutoMove()) || this.entityData == null) {
                        entity.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                    }
                    entity.rotationYawHead = entity.rotationYaw;
                    entity.renderYawOffset = entity.rotationYaw;
                    entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);

                    if (!world.spawnEntity(entity)) {
                        System.out.println("can't load");
                        this.entity = null;
                    }

                }
            }
        }

        public boolean isLoaded() {
            boolean loaded = this.entity != null && this.entity.entity.isAddedToWorld() && this.canEntityUpdate(this.entity.entity.world, this.entity.entity.getPosition());
            if (!loaded && this.entity != null) {
                this.entityData = this.entity.entity.writeToNBT(new NBTTagCompound());
                this.prevPos.set(this.owner.pos);
                this.entity = this.entity.kill();
            }
            return loaded;
        }

        public boolean gc(PosMc3IM target) {
            if (this.entity == null) {
                return false;
            }

            EntityParasiteBase e = this.entity.entity;

            if (e.getDistanceSq(target.getX() + 0.5, e.posY, target.getZ() + 0.5) > 8 * 8 && this.prevEntityPos.distanceSq(e.posX, e.posY, e.posZ) < 2 * 2) {
                if (!(e.getNavigator() instanceof PathNavigateGroundForced) || ((PathNavigateGroundForced) e.getNavigator()).timeFromLastDamage > 60 * 20) {
                    this.idleTime++;
                }
            } else {
                this.prevEntityPos.set(e);
                this.idleTime = 0;
            }

            return this.idleTime > 600;
        }

        public void onRemove() {
            if (this.entity != null) {
                this.entity.bind(null);
            }
        }

        public TowerDefenceEntity entity() {
            return this.entity;
        }

        private boolean canEntityUpdate(World world, BlockPos pos) {
            int x = pos.getX();
            int z = pos.getZ();
            boolean isForced = world.getPersistentChunks().containsKey(new net.minecraft.util.math.ChunkPos(x >> 4, z >> 4));
            int range = isForced ? 0 : 32;

            PosMc3IM from = PosMc3IM.obtain().set(x - range, 0, z - range);
            PosMc3IM to = PosMc3IM.obtain().set(x + range, 0, z + range);
            boolean flag = world.isAreaLoaded(from, to);
            PosMc3IM.release(from);
            PosMc3IM.release(to);

            return flag;
        }

        private void writeEntity(NBTTagCompound nbt) {
            if (this.entity == null) {
                if (this.entityData != null) {
                    nbt.setTag("entityData", this.entityData);
                }
            } else {
                NBTTagCompound data = new NBTTagCompound();
                this.entity.entity.writeToNBT(data);
                nbt.setTag("entityData", this.entityData = data);
            }
        }

        @Override
        public void readFromNBT(@NotNull NBTTagCompound nbt) {
            this.record = nbt.getString("record");
            this.prevPos.set(nbt.getInteger("prevX"), nbt.getInteger("prevY"));
            this.idleTime = nbt.getInteger("idleTime");
            if (nbt.hasKey("entityData")) {
                this.entityData = nbt.getCompoundTag("entityData");
            }
        }

        @Override
        public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
            if (this.entity != null) {
                this.prevPos.set(this.owner.pos);
            }
            nbt.setInteger("idleTime", this.idleTime);
            nbt.setInteger("prevX", this.prevPos.x());
            nbt.setInteger("prevY", this.prevPos.y());
            nbt.setString("record", this.record);
            this.writeEntity(nbt);
            return nbt;
        }
    }
}
