package com.artur114.srptowerdefense.common.blockdamage.server;

import com.artur114.srptowerdefense.common.blockdamage.*;
import com.artur114.srptowerdefense.common.network.client.CPacketSyncBlocksDamage;
import com.artur114.srptowerdefense.common.util.math.AdvancedBlockPos;
import com.artur114.srptowerdefense.main.TowerDefence;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class ServerDamagedChunk extends DamagedChunk implements IServerDamagedChunk {
    private final SyncManager syncManager;
    private final World world;

    public ServerDamagedChunk(World world, ChunkPos pos) {
        super(pos, world != null ? world.provider.getDimension() : 0);

        this.world = world;
        this.syncManager = new SyncManager(this);
    }

    @Override
    public void damage(BlockPos pos, int amount) {
        this.damage(pos.getX(), pos.getY(), pos.getZ(), amount);
    }

    @Override
    public void repair(BlockPos pos, int amount) {
        this.repair(pos.getX(), pos.getY(), pos.getZ(), amount);
    }

    @Override
    public void damage(int x, int y, int z, int amount) {
        if (y >> 4 >= 16 || y >> 4 < 0) {
            return;
        }

        IExtendedDamageStorage storage = this.storages[y >> 4];
        AdvancedBlockPos pos = AdvancedBlockPos.obtain().setPos((x & 15) + this.pos.getXStart(), y, (z & 15) + this.pos.getZStart());

        if (storage == null) {
            storage = new ExtendedDamageStorage1Byte();
            this.storages[y >> 4] = storage;
            this.initStoragesCount++;
        }

        int newDamage = (int) (storage.getDamage(x, y, z) + (amount * this.damageMultiplierFor(pos)));

        if (newDamage > MAX_DAMAGE) {
            this.world.destroyBlock(pos, true);
            newDamage = 0;
        }

        boolean flag = storage.setDamage(x, y, z, newDamage);

        if (flag) {
            this.syncManager.notifyDamageChange(x, y, z, newDamage);
        }

        AdvancedBlockPos.release(pos);
    }

    @Override
    public void repair(int x, int y, int z, int amount) {
        if (y >> 4 >= 16 || y >> 4 < 0) {
            return;
        }

        IExtendedDamageStorage storage = this.storages[y >> 4];
        if (storage == null) {
            return;
        }

        AdvancedBlockPos pos = AdvancedBlockPos.obtain().setPos((x & 15) + this.pos.getXStart(), y, (z & 15) + this.pos.getZStart());
        int newDamage = (int) (storage.getDamage(x, y, z) - (amount * this.repairMultiplierFor(pos)));
        boolean flag = storage.setDamage(x, y, z, newDamage);

        if (storage.isEmpty()) {
            this.storages[y >> 4] = null;
            this.initStoragesCount--;
            newDamage = 0;
        }

        if (flag) {
            this.syncManager.notifyDamageChange(x, y, z, newDamage);
        }

        AdvancedBlockPos.release(pos);
    }

    @Override
    public float damageMultiplierFor(BlockPos pos) {
        IBlockState state = this.world.getBlockState(pos);
        Material material = state.getMaterial();
        Block block = state.getBlock();
        float hardness = block.getBlockHardness(state, this.world, pos);
        if (material.isLiquid() || block.isAir(state, this.world, pos) || hardness < 0.0F) {
            return 0.0F;
        } else {
            return 1.0F / (hardness * 4.0F);
        }
    }

    @Override
    public float repairMultiplierFor(BlockPos pos) {
        IBlockState state = this.world.getBlockState(pos);
        Material material = state.getMaterial();
        Block block = state.getBlock();
        float hardness = block.getBlockHardness(state, this.world, pos);
        if (material.isLiquid() || block.isAir(state, this.world, pos) || hardness < 0.0F) {
            return 128.0F;
        } else {
            return 1.0F / hardness;
        }
    }

    @Override
    public void onBlockBreak(BlockPos pos) {
        if (pos.getY() >> 4 >= 16 || pos.getY() >> 4 < 0) {
            return;
        }

        IExtendedDamageStorage storage = this.storages[pos.getY() >> 4];

        if (storage == null) {
            return;
        }

        boolean flag = storage.setDamage(pos, 0);

        if (storage.isEmpty()) {
            this.storages[pos.getY() >> 4] = null;
            this.initStoragesCount--;
        }

        if (flag) {
            this.syncManager.notifyDamageChange(pos.getX(), pos.getY(), pos.getZ(), 0);
        }
    }

    @Override
    public void syncToClient(EntityPlayerMP clientIn) {
        if (clientIn != null) {
            TowerDefence.NETWORK.sendTo(new CPacketSyncBlocksDamage(this.dimension, this.pos, this.syncManager.data()), clientIn);
        } else {
            this.syncManager.finish();
        }
    }

    @Override
    public void sendInitialDataToClient(EntityPlayerMP clientIn) {
        if (!this.isEmpty() && clientIn != null) {
            TowerDefence.NETWORK.sendTo(new CPacketSyncBlocksDamage(this.dimension, this.pos, this.serializeNBT()), clientIn);
        }
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public ChunkPos getPos() {
        return pos;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        if (this.isEmpty()) {
            return new NBTTagCompound();
        }

        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for (int y = 0; y != 16; y++) {
            IExtendedDamageStorage storage = storages[y];

            if (storage != null) {
                NBTTagCompound data = new NBTTagCompound();
                data.setInteger("storageIndex", y);
                list.appendTag(storage.writeToNBT(data));
            }
        }

        nbt.setTag("storages", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("storages")) {
            return;
        }

        NBTTagList list = nbt.getTagList("storages", 10);

        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            IExtendedDamageStorage storage = new ExtendedDamageStorage1Byte();
            storage.readFromNBT(data);
            this.storages[data.getInteger("storageIndex")] = storage;
            this.initStoragesCount++;
        }
    }

    private static class SyncManager {
        private NBTTagList rawDataList = new NBTTagList();
        private final ServerDamagedChunk chunk;
        private NBTTagCompound data = null;

        private SyncManager(ServerDamagedChunk chunk) {
            this.chunk = chunk;
        }

        protected void notifyDamageChange(int x, int y, int z, int newDamage) {
            NBTTagCompound data = new NBTTagCompound();

            data.setByte("storage", (byte) (y >> 4));
            data.setShort("packedPos", this.packPos(x, y, z));
            data.setByte("damage", (byte) Math.max(0, Math.min(newDamage, MAX_DAMAGE)));

            this.rawDataList.appendTag(data);

            BlockDamageEventsHandler.SERVER_MANAGER.addToSyncQueue(this.chunk);
        }

        protected NBTTagCompound data() {
            if (this.data == null) {
                NBTTagCompound data = new NBTTagCompound();
                data.setTag("dataChange", this.rawDataList);
                this.data = data;
            }
            return this.data;
        }

        protected void finish() {
            this.rawDataList = new NBTTagList();
            this.data = null;
        }

        private short packPos(int x, int y, int z) {
            return (short) (((x & 15) << 8) | ((y & 15) << 4) | (z & 15));
        }

        private int[] unpackPos(int[] posBuf, short packedPos) {
            posBuf[0] = (packedPos >> 8) & 15;
            posBuf[1] = (packedPos >> 4) & 15;
            posBuf[2] = packedPos & 15;
            return posBuf;
        }
    }
}
