package com.artur114.srptowerdefense.common.worldstate.blockdamage;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.google.common.collect.AbstractIterator;
import it.unimi.dsi.fastutil.shorts.Short2ShortMap;
import it.unimi.dsi.fastutil.shorts.Short2ShortOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class ExtendedDamageStorageMapped implements IExtendedDamageStorage {
    private final Short2ShortMap data = new Short2ShortOpenHashMap();
    private static final int MAX_DATA = 65535;

    @Override
    public int getDamage(int x, int y, int z) {
        return this.data.get((short) (((x & 15) << 8) | ((y & 15) << 4) | (z & 15))) & 0xFFFF;
    }

    @Override
    public int getDamage(BlockPos pos) {
        return this.getDamage(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean setDamage(int x, int y, int z, int amount) {
        amount = Math.min(amount, MAX_DATA);
        amount = Math.max(amount, 0);

        short index = (short) (((x & 15) << 8) | ((y & 15) << 4) | (z & 15));
        int data = this.data.get(index) & 0xFFFF;

        boolean flag = data != amount;

        if (amount != 0) {
            this.data.put(index, (short) amount);
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
    public Iterator<Short2ShortMap.Entry> iterator() {
        return this.data.short2ShortEntrySet().iterator();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public Iterable<PosMc3IM> allBlocksWithData(ChunkPos chunkPos, int storageIndex) {
        return () -> new AbstractIterator<PosMc3IM>() {
            private final PosMc3IM blockPos = new PosMc3IM();
            private final short[] shorts = data.keySet().toArray(new short[0]);
            private final int[] posBuff = new int[3];
            private final int size = shorts.length;
            private int cursor = 0;

            @Override
            protected PosMc3IM computeNext() {
                if (this.cursor != this.size) {
                    short next = this.shorts[this.cursor];
                    this.cursor++;

                    int[] buf = unpackPos(posBuff, next);

                    this.blockPos.set(buf[0] + chunkPos.getXStart(), (storageIndex << 4) + buf[1], buf[2] + chunkPos.getZStart());

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
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        if (this.isEmpty()) {
            return nbt;
        }
        NBTTagList damageData = new NBTTagList();
        NBTTagList damagePos = new NBTTagList();
        for (Short2ShortMap.Entry entry : this.data.short2ShortEntrySet()) {
            damageData.appendTag(new NBTTagShort(entry.getShortValue()));
            damagePos.appendTag(new NBTTagShort(entry.getShortKey()));
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

        NBTTagList damageData = nbt.getTagList("damageData", 2);
        NBTTagList damagePos = nbt.getTagList("damagePos", 2);
        int[] posBuf = new int[3];

        if (damageData.tagCount() != damagePos.tagCount()) {
            return;
        }

        for (int i = 0; i != damagePos.tagCount(); i++) {
            int[] pos = this.unpackPos(posBuf, ((NBTTagShort) damagePos.get(i)).getShort());
            int data = ((NBTTagShort) damageData.get(i)).getShort() & 0xFFFF;

            this.setDamage(pos[0], pos[1], pos[2], data);
        }
    }
}
