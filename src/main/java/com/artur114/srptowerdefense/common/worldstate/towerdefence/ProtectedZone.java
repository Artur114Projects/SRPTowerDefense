package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.box.Box2I;
import com.artur114.bananalib.math.m2d.box.Box2IM;
import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.math.m2d.box.IBox2IM;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2DM;
import com.artur114.bananalib.math.m2d.vec.Vec2D;
import com.artur114.bananalib.math.m2d.vec.Vec2DM;
import com.artur114.bananalib.math.m3d.vec.IVec3DM;
import com.artur114.bananalib.math.m3d.vec.Vec3DM;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.math.m2d.vec.IPosMc2I;
import com.artur114.bananalib.mc.math.m2d.vec.PosMc2I;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.common.tileentity.TileEntityAreaProtector;
import com.artur114.srptowerdefense.main.SRPTDMain;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;
import org.jetbrains.annotations.NotNull;

public class ProtectedZone implements IWaveTarget {
    private static final int[] EV_BONUS_PER_32_TICKS = {1, 1, 1, 6, 38, 1000, 4500, 105000, 110000, 170000};
    private final Object2IntMap<ForgeChunkManager.Ticket> tickedLoadCountMap = new Object2IntOpenHashMap<>();
    private final Long2ObjectMap<ForgeChunkManager.Ticket> forcedChunksMap = new Long2ObjectOpenHashMap<>();
    private final LongSet protectedChunks = new LongOpenHashSet();
    private final LongSet forcedChunks = new LongOpenHashSet();
    private final LongSet noSpawnChunks = new LongOpenHashSet();
    private boolean alive = true;
    private WorldServer world;
    private BlockPos tilePos;
    private PosMc2I chunk;
    private IBox2I box;
    private IVec2D pos;
    private int id;

    public ProtectedZone() {}

    public ProtectedZone(BlockPos tilePos) {
        this.tilePos = tilePos;

        this.computePos();
    }

    @Override
    public boolean isForcedChunk(IVec2D pos) {
        return this.forcedChunks.contains(ChunkPos.asLong(BananaMath.floor(pos.x()), BananaMath.floor(pos.y())));
    }

    @Override
    public BlockPos[] causalBlocks() {
        return new BlockPos[] {this.tilePos};
    }

    @Override
    public BlockPos causePos() {
        return this.tilePos;
    }

    @Override
    public void init(WorldServer world, TowerDefenceManager owner, int id) {
        this.world = world;
        this.id = id;

        this.forceChunks();
    }

    @Override
    public void onChunkLoaded(Chunk chunk) {}

    @Override
    public int ticksToUpdate() {
        return 4;
    }

    @Override
    public boolean isAlive() {
        return this.world.getTileEntity(this.tilePos) instanceof TileEntityAreaProtector && this.alive;
    }

    @Override
    public void onRemove() {
        this.unforceChunks();

        SRPSaveData.get(this.world, 43).setTotalKills(this.world.provider.getDimension(), this.noSpawnChunks.size() * 100 * Math.max(1, SRPSaveData.get(this.world, 72).getEvolutionPhase(this.world.provider.getDimension())), true, this.world, true, 1);
    }

    @Override
    public void update() {
        for (long l : this.noSpawnChunks) {
            Chunk chunk = this.world.getChunkProvider().getLoadedChunk((int) (l), (int) (l >> 32));

            if (chunk == null) {
                continue;
            }

            try {
                for (ClassInheritanceMultiMap<Entity> map : chunk.getEntityLists()) {
                    try {
                        for (Entity entity : map) {
                            TowerDefenceEntity data = entity.getCapability(InitCapabilities.TD_ENTITY_DATA, null);
                            if (data != null) data.tickOnUnnaturalLocation();
                        }
                    } catch (Exception ignored) {}
                }
            } catch (Exception ignored) {}
        }

        int phase = SRPSaveData.get(this.world, 72).getEvolutionPhase(this.world.provider.getDimension());
        if (phase >= 0 && phase < EV_BONUS_PER_32_TICKS.length) {
            int bonus = (int) Math.max(1, EV_BONUS_PER_32_TICKS[phase] * Math.min(this.noSpawnChunks.size() / 100.0F, 2.0F));
            SRPSaveData.get(this.world, 43).setTotalKills(this.world.provider.getDimension(), bonus, true, this.world, true, 1);
        }
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public IVec2D pos() {
        return this.pos;
    }

    public IPosMc2I chunkPos() {
        return this.chunk;
    }

    @Override
    public IBox2I box() {
        return this.box;
    }

    public void explode() {
        this.alive = false;

        this.world.newExplosion(null, this.tilePos.getX(), this.tilePos.getY(), this.tilePos.getZ(), 20, true, true);
        IVec3DM delta = new Vec3DM();
        int radius = 128 / 8;

        for (int y = 0; y != 2; y++) {
            for (int i = 0; i != radius + 1; i++) {
                int count = 8 * i / 2;
                int range = 8 * i;
                for (int c = 0; c != count + 1; c++) {
                    delta.set(range, 0, 0).rotateY(360.0F * ((double) c / count));
                    this.world.newExplosion(null, this.tilePos.getX() + delta.x(), this.tilePos.getY() + y * 4, this.tilePos.getZ() + delta.z(), 10, false, true);
                }
            }
        }
    }

    public boolean canParasiteLocateIn(ChunkPos pos) {
        return this.canParasiteLocateIn(pos.x, pos.z);
    }

    public boolean canParasiteLocateIn(int x, int z) {
        return !this.noSpawnChunks.contains(ChunkPos.asLong(x, z));
    }

    public boolean doProtect(ChunkPos pos, boolean state) {
        if (!this.box.contains(pos.x, pos.z)) {
            return false;
        }

        if (this.chunk.x == pos.x && this.chunk.z == pos.z) {
            return false;
        }

        if (state) {
            this.protectedChunks.add(BananaMC.chunkPosAsLong(pos));
            this.addAllInBox(pos.x, pos.z, 1, this.forcedChunks);
            this.addAllInBox(pos.x, pos.z, 4, this.noSpawnChunks);
        } else {

            this.protectedChunks.remove(BananaMC.chunkPosAsLong(pos));
            this.removeAllInBox(pos.x, pos.z, 1, this.forcedChunks);
            this.removeAllInBox(pos.x, pos.z, 4, this.noSpawnChunks);
        }

        return true;
    }

    public long[] protectedChunks() {
        return this.protectedChunks.toArray(new long[0]);
    }

    private void forceChunks() {
        for (long l : this.forcedChunks) {
            this.force((int) (l), (int) (l >> 32));
        }
    }

    private void unforceChunks() {
        for (long l : this.forcedChunks) {
            this.unforce((int) (l), (int) (l >> 32));
        }
    }

    private void computePos() {
        this.chunk = new PosMc2I(this.tilePos);
        this.pos = new Vec2D(this.chunk);
        this.box = new Box2I(this.pos, this.pos).grow(10);

        this.protectedChunks.add(BananaMC.chunkPosAsLong((ChunkPos) this.chunk));

        for (int xR = this.chunk.x - 1; xR != this.chunk.x + 1 + 1; xR++) {
            for (int zR = this.chunk.z - 1; zR != this.chunk.z + 1 + 1; zR++) {
                this.forcedChunks.add(ChunkPos.asLong(xR, zR));
            }
        }

        for (int xR = this.chunk.x - 4; xR != this.chunk.x + 4 + 1; xR++) {
            for (int zR = this.chunk.z - 4; zR != this.chunk.z + 4 + 1; zR++) {
                this.noSpawnChunks.add(ChunkPos.asLong(xR, zR));
            }
        }
    }

    private void removeAllInBox(int x, int z, int boxRange, LongSet from) {
        for (int xR = x - boxRange; xR != x + boxRange + 1; xR++) {
            for (int zR = z - boxRange; zR != z + boxRange + 1; zR++) {
                if (!this.hasSupport(xR, zR, boxRange)) {
                    if (from == this.forcedChunks) {
                        this.unforce(xR, zR);
                    }

                    from.remove(ChunkPos.asLong(xR, zR));
                }
            }
        }
    }

    private boolean hasSupport(int x, int z, int boxRange) {
        for (int xR = x - boxRange; xR != x + boxRange + 1; xR++) {
            for (int zR = z - boxRange; zR != z + boxRange + 1; zR++) {
                if (this.protectedChunks.contains(ChunkPos.asLong(xR, zR))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addAllInBox(int x, int z, int boxRange, LongSet out) {
        for (int xR = x - boxRange; xR != x + boxRange + 1; xR++) {
            for (int zR = z - boxRange; zR != z + boxRange + 1; zR++) {
                if (out == this.forcedChunks) {
                    this.force(xR, zR);
                }

                out.add(ChunkPos.asLong(xR, zR));
            }
        }
    }

    private void force(int x, int z) {
        long id = ChunkPos.asLong(x, z);

        if (this.forcedChunksMap.containsKey(id)) {
            return;
        }

        ForgeChunkManager.Ticket ticket = this.findFreeTicked();
        ForgeChunkManager.forceChunk(ticket, new ChunkPos(x, z));
        this.forcedChunksMap.put(id, ticket);
        this.tickedLoadCountMap.put(ticket, this.tickedLoadCountMap.get(ticket) + 1);
    }

    private void unforce(int x, int z) {
        long id = ChunkPos.asLong(x, z);
        ForgeChunkManager.Ticket ticket = this.forcedChunksMap.get(id);

        if (ticket == null) {
            return;
        }

        ForgeChunkManager.unforceChunk(ticket, new ChunkPos(x, z));
        this.forcedChunksMap.remove(id);
        int count = this.tickedLoadCountMap.get(ticket);

        if (count == 1) {
            this.tickedLoadCountMap.remove(ticket);
            ForgeChunkManager.releaseTicket(ticket);
        } else {
            this.tickedLoadCountMap.put(ticket, count - 1);
        }
    }

    private ForgeChunkManager.Ticket findFreeTicked() {
        int val = ForgeChunkManager.getMaxChunkDepthFor(SRPTDMain.MODID);
        for (ForgeChunkManager.Ticket ticket : this.tickedLoadCountMap.keySet()) {
            if (this.tickedLoadCountMap.get(ticket) < val) {
                return ticket;
            }
        }

        return this.createTicked();
    }

    private ForgeChunkManager.Ticket createTicked() {
        ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestTicket(SRPTDMain.INSTANCE, this.world, ForgeChunkManager.Type.NORMAL);
        this.tickedLoadCountMap.put(ticket, 0);
        return ticket;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound nbt) {
        this.tilePos = BlockPos.fromLong(nbt.getLong("tilePos"));
        this.alive = nbt.getBoolean("alive");
        this.readLongSet(nbt.getTagList("protectedChunks", 4), this.protectedChunks);
        this.readLongSet(nbt.getTagList("noSpawnChunks", 4), this.noSpawnChunks);
        this.readLongSet(nbt.getTagList("forcedChunks", 4), this.forcedChunks);
        this.computePos();
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        nbt.setLong("tilePos", this.tilePos.toLong());
        nbt.setBoolean("alive", this.alive);
        nbt.setTag("protectedChunks", this.writeLongSet(this.protectedChunks));
        nbt.setTag("noSpawnChunks", this.writeLongSet(this.noSpawnChunks));
        nbt.setTag("forcedChunks", this.writeLongSet(this.forcedChunks));
        return nbt;
    }

    private NBTTagList writeLongSet(LongSet set) {
        NBTTagList list = new NBTTagList();
        for (long l : set) list.appendTag(new NBTTagLong(l));
        return list;
    }

    private void readLongSet(NBTTagList from, LongSet set) {
        for (int i = 0; i != from.tagCount(); i++) {
            set.add(((NBTTagLong) from.get(i)).getLong());
        }
    }
}
