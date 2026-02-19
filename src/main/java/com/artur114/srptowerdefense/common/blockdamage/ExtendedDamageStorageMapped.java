package com.artur114.srptowerdefense.common.blockdamage;

import com.artur114.srptowerdefense.common.util.math.AdvancedBlockPos;
import com.google.common.collect.AbstractIterator;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ExtendedDamageStorageMapped implements IExtendedDamageStorage {
    private final Int2ByteMap data = new Int2ByteOpenHashMap();

    @Override
    public int getDamage(int x, int y, int z) {
        return this.data.get(this.packPos(x, y, z));
    }

    @Override
    public int getDamage(BlockPos pos) {
        return this.getDamage(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean setDamage(int x, int y, int z, int amount) {
        amount = Math.min(amount, Byte.MAX_VALUE);
        amount = Math.max(amount, 0);

        int index = this.packPos(x, y, z);
        byte data = this.data.get(index);

        boolean flag = data != amount;

        if (amount != 0) {
            this.data.put(index, (byte) amount);
        } else {
            this.data.remove(index);
        }

        return flag;
    }

    @Override
    public boolean setDamage(BlockPos pos, int amount) {
        return this.setDamage(pos.getX(), pos.getY(), pos.getZ(), amount);
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public Iterable<AdvancedBlockPos> allBlocksWithData(ChunkPos chunkPos, int storageIndex) {
        return () -> new AbstractIterator<AdvancedBlockPos>() {
            private final AdvancedBlockPos blockPos = new AdvancedBlockPos();
            private final int[] ints = data.keySet().toArray(new int[0]);
            private final int[] posBuff = new int[3];
            private final int size = ints.length;
            private int cursor = 0;

            @Override
            protected AdvancedBlockPos computeNext() {
                if (this.cursor != this.size) {
                    int next = this.ints[this.cursor];
                    this.cursor++;

                    int[] buf = unpackPos(posBuff, (short) next);

                    this.blockPos.setPos(buf[0] + chunkPos.getXStart(), (storageIndex << 4) + buf[1], buf[2] + chunkPos.getZStart());

                    return this.blockPos;
                }
                return this.endOfData();
            }
        };
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

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.isEmpty()) {
            return nbt;
        }
        NBTTagList damageData = new NBTTagList();
        NBTTagList damagePos = new NBTTagList();
        for (Int2ByteMap.Entry entry : this.data.int2ByteEntrySet()) {
            damageData.appendTag(new NBTTagByte(entry.getByteValue()));
            damagePos.appendTag(new NBTTagShort((short) entry.getIntKey()));
        }
        nbt.setTag("damageData", damageData);
        nbt.setTag("damagePos", damagePos);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("damageData") || !nbt.hasKey("damagePos")) {
            return;
        }

        NBTTagList damageData = nbt.getTagList("damageData", 1);
        NBTTagList damagePos = nbt.getTagList("damagePos", 2);
        int[] posBuf = new int[3];

        if (damageData.tagCount() != damagePos.tagCount()) {
            return;
        }

        for (int i = 0; i != damagePos.tagCount(); i++) {
            int[] pos = this.unpackPos(posBuf, ((NBTTagShort) damagePos.get(i)).getShort());
            byte data = ((NBTTagByte) damageData.get(i)).getByte();

            this.setDamage(pos[0], pos[1], pos[2], data);
        }
    }
}
