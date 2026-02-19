package com.artur114.srptowerdefense.common.blockdamage.client;

import com.artur114.srptowerdefense.common.blockdamage.DamagedChunk;
import com.artur114.srptowerdefense.common.blockdamage.ExtendedDamageStorage1Byte;
import com.artur114.srptowerdefense.common.blockdamage.ExtendedDamageStorageMapped;
import com.artur114.srptowerdefense.common.blockdamage.IExtendedDamageStorage;
import com.artur114.srptowerdefense.common.util.math.AdvancedBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;

public class ClientDamagedChunk extends DamagedChunk implements IClientDamagedChunk {
    private int tick = 0;

    public ClientDamagedChunk(ChunkPos pos, int dimension) {
        super(pos, dimension);
    }

    @Override
    public void processSyncData(NBTTagCompound dataIn) {
        if (dataIn.hasKey("dataChange")) {
            this.processDataChange(dataIn.getTagList("dataChange", 10));
        } else if (dataIn.hasKey("storages")) {
            this.processInitialData(dataIn.getTagList("storages", 10));
        } else {
            this.crearChunkData();
        }
    }

    @Override
    public void unloadChunk() {
        this.sendNullBlocksProgress();
    }

    @Override
    public void update() {
        if (this.tick % 80 == 0) {
            for (int i = 0; i != this.storages.length; i++) {
                IExtendedDamageStorage storage = this.storages[i];
                if (storage != null) {
                    this.sendBlocksProgress((ExtendedDamageStorageMapped) storage, i);
                }
            }
        }

        this.tick++;
    }

    private void processDataChange(NBTTagList list) {
        int[] posBuff = new int[3];
        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            int storageIndex = data.getByte("storage");
            int packedPos = data.getShort("packedPos");
            int newDamage = data.getByte("damage");

            IExtendedDamageStorage storage = this.storages[storageIndex];

            if (storage == null) {
                storage = new ExtendedDamageStorageMapped();
                this.storages[storageIndex] = storage;
                this.initStoragesCount++;
            }

            int[] buff = this.unpackPos(posBuff, (short) packedPos);

            storage.setDamage(buff[0], buff[1], buff[2], newDamage);
            this.sendBlockProgress(new BlockPos(buff[0] + this.pos.getXStart(), buff[1] + (storageIndex << 4), buff[2] + this.pos.getZStart()), newDamage);

            if (storage.isEmpty()) {
                this.storages[storageIndex] = null;
                this.initStoragesCount--;
            }
        }
    }

    private int[] unpackPos(int[] posBuf, short packedPos) {
        posBuf[0] = (packedPos >> 8) & 15;
        posBuf[1] = (packedPos >> 4) & 15;
        posBuf[2] = packedPos & 15;
        return posBuf;
    }

    private void processInitialData(NBTTagList list) {
        this.crearChunkData();
        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            ExtendedDamageStorageMapped storage = new ExtendedDamageStorageMapped();
            storage.readFromNBT(data);
            int index = data.getInteger("storageIndex");
            this.sendBlocksProgress(storage, index);
            this.storages[index] = storage;
            this.initStoragesCount++;
        }
    }

    private void crearChunkData() {
        this.sendNullBlocksProgress();
        Arrays.fill(this.storages, null);
        this.initStoragesCount = 0;
    }

    private void sendNullBlocksProgress() {
        RenderGlobal renderGlobal = Minecraft.getMinecraft().renderGlobal;
        for (int i = 0; i != this.storages.length; i++) {
            IExtendedDamageStorage storage = this.storages[i];
            if (storage != null) {
                for (AdvancedBlockPos pos : ((ExtendedDamageStorageMapped) storage).allBlocksWithData(this.pos, i)) {
                    renderGlobal.sendBlockBreakProgress(pos.hashCode(), pos, -1);
                }
            }
        }
    }

    private void sendBlocksProgress(ExtendedDamageStorageMapped storage, int index) {
        if (storage != null) {

            for (AdvancedBlockPos pos : storage.allBlocksWithData(this.pos, index)) {
                this.sendBlockProgress(pos, storage.getDamage(pos));
            }
        }
    }

    private void sendBlockProgress(BlockPos pos, int damage) {
        RenderGlobal renderGlobal = Minecraft.getMinecraft().renderGlobal;
        int progress;

        if (damage == 0) {
            progress = -1;
        } else {
            progress = (int) (10 * ((float) damage / MAX_DAMAGE));
        }

        BlockPos blockPos;

        if (progress != -1) {
            blockPos = pos.toImmutable();
        } else {
            blockPos = pos;
        }

        renderGlobal.sendBlockBreakProgress(blockPos.hashCode(), blockPos, progress);
    }

    @Override
    public String toString() {
        return this.pos.toString() + " isEmpty:" + this.isEmpty();
    }
}
