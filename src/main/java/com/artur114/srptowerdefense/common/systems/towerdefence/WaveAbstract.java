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
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public abstract class WaveAbstract implements IWave {
    protected Int2ObjectMap<EntityRecord> entityRecords;
    protected AdvancedBlockPos entityMoveTarget;
    protected TowerDefenceManager owner;
    protected IVec2D targetChunk;
    protected WorldServer world;
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
    public void ondChunkLoaded(Chunk chunk) {
        AdvancedBlockPos blockPos = AdvancedBlockPos.obtain();

        for (EntityRecord record : this.entityRecords.values()) {
            if (!record.isLoaded()) {
                blockPos.setPos(chunk.x, chunk.z).add(this.rand.nextInt(16), 0, this.rand.nextInt(16)).setWorldY(this.world);
                record.load(this.world, blockPos);

                if (this.entityMoveTarget == null) {
                    this.entityMoveTarget = new AdvancedBlockPos(chunk.getPos()).add(8, 0, 8).setWorldY(this.world);
                }
            }
        }

        AdvancedBlockPos.release(blockPos);
    }

    @Override
    public void onEntryToLoadedChunk(Chunk chunk) {
        AdvancedBlockPos blockPos = AdvancedBlockPos.obtain();

        for (EntityRecord record : this.entityRecords.values()) {
            if (!record.isLoaded()) {
                blockPos.setPos(chunk.x, chunk.z).add(this.rand.nextInt(16), 0, this.rand.nextInt(16)).setWorldY(this.world);
                record.load(this.world, blockPos);

                if (this.entityMoveTarget == null) {
                    this.entityMoveTarget = new AdvancedBlockPos(chunk.getPos()).add(8, 0, 8).setWorldY(this.world);
                }
            }
        }

        AdvancedBlockPos.release(blockPos);
    }

    @Override
    public void onEntityDied(WaveEntityData entity) {
        if (entity.data.hasKey(EntityRecord.ENTITY_RECORD_NBT_LOCATION)) {
            EntityRecord record = this.entityRecords.remove(entity.data.getInteger(EntityRecord.ENTITY_RECORD_NBT_LOCATION));

            if (record != null) {
                System.out.println("Record(" + record.id + ") is removed");
            }
        }
    }

    @Override
    public void move(IVec2D vec) {
        if (this.entityRecords.values().stream().anyMatch(EntityRecord::isLoaded)) {
            if (!this.isMoveTargetValide()) {
                this.updateMoveTarget();
            }

            this.updateEntitiesMoveSpeed();
            this.rebindMoveTarget();
            this.updateWavePos();
        } else {
            this.entityMoveTarget = null;

            this.pos.add(vec);
        }

        int x = BananaMath.floor(this.pos.x());
        int y = BananaMath.floor(this.pos.y());

        this.box.set(x, y, x, y);

        System.out.println(this.pos);
        System.out.println(this.box);
        System.out.println();
    }

    @Override
    public boolean isAlive() {
        return !this.entityRecords.isEmpty();
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

    private void updateWavePos() {
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

    private void updateMoveTarget() {
        if (this.entityMoveTarget.distanceSq(this.target()) < 64 * 64) {
            this.entityMoveTarget.setPos(this.target()); return;
        }

        AdvancedBlockPos blockPos = AdvancedBlockPos.obtain();
        blockPos.setPos(this.target()).subtract(this.entityMoveTarget).setY(0);
        Vec3d vec = new Vec3d(blockPos).normalize();

        double range = 16.0F;
        int r = 4;

        int x = MathHelper.floor((range * 0.8) * vec.x) + (this.rand.nextInt(r * 2) - r);
        int z = MathHelper.floor((range * 0.8) * vec.z) + (this.rand.nextInt(r * 2) - r);
        blockPos.setPos(this.entityMoveTarget).add(x, 0, z).setWorldY(this.world, state -> state.getMaterial().isReplaceable(), false);
        this.entityMoveTarget.setPos(blockPos);

        AdvancedBlockPos.release(blockPos);
    }

    private void rebindMoveTarget() {
        for (EntityRecord record : this.entityRecords.values()) {
            if (record.isLoaded()) {
                record.entity().setMoveTarget(this.entityMoveTarget);
            }
        }
    }

    private void updateEntitiesMoveSpeed() {
        float maxDistance = -1.0F;

        for (EntityRecord record : this.entityRecords.values()) {
            if (record.isLoaded()) {
                float dist = (float) record.entity().entity.getDistanceSqToCenter(this.entityMoveTarget);
                if (dist > maxDistance) {
                    maxDistance = dist;
                }
            }
        }

        for (EntityRecord record : this.entityRecords.values()) {
            if (record.isLoaded()) {
                record.entity().setMoveSpeed((float) (this.speed * (record.entity().entity.getDistanceSqToCenter(this.entityMoveTarget) / maxDistance)));
            }
        }
    }

    private boolean isMoveTargetValide() {
        if (this.entityMoveTarget.equals(this.target())) {
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
    public void readFromNBT(NBTTagCompound nbt) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return null;
    }

    public static class EntityCreatorRl implements ICanCreateEntity {
        private final ResourceLocation rl;

        public EntityCreatorRl(ResourceLocation rl) {
            this.rl = rl;
        }

        public EntityCreatorRl(String rl) {
            this.rl = new ResourceLocation(rl);
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
        private WaveEntityData entity;

        private EntityRecord(ICanCreateEntity record, WaveAbstract owner, int id) {
            this.record = record;
            this.owner = owner;
            this.id = id;
        }

        public void load(World world, BlockPos pos) {
            EntityLiving entity = this.record.create(world);
            if (entity != null) {
                WaveEntityData data = entity.getCapability(SRPTDCapabilities.WAVE_ENTITY_DATA, null);
                if (data != null) {
                    this.entity = data;

                    data.data.setInteger(ENTITY_RECORD_NBT_LOCATION, this.id);
                    data.bindWave(this.owner);

                    entity.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                    entity.rotationYawHead = entity.rotationYaw;
                    entity.renderYawOffset = entity.rotationYaw;
                    entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                    entity.forceSpawn = true;

                    if (!world.spawnEntity(entity)) {
                        this.entity = null;
                        return;
                    }

                    System.out.println("Record(" + this.id + ")" + " loaded");
                }
            }
        }

        public boolean isLoaded() {
            boolean loaded = this.entity != null && this.entity.entity.isAddedToWorld() && this.entity.entity.addedToChunk && this.entity.entity.world.isAreaLoaded(this.entity.entity.getPosition(), 32);
            if (!loaded && this.entity != null) {
                System.out.println("Record(" + this.id + ")" + " unloaded");
            }
            if (!loaded) this.entity = null;
            return loaded;
        }

        public WaveEntityData entity() {
            return this.entity;
        }
    }
}
