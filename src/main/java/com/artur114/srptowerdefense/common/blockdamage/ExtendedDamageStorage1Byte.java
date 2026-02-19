package com.artur114.srptowerdefense.common.blockdamage;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.math.BlockPos;

public class ExtendedDamageStorage1Byte implements IExtendedDamageStorage {
    private final byte[] data = new byte[16 * 16 * 16];
    private int damagedBlocks = 0;

    @Override
    public int getDamage(int x, int y, int z) {
        return this.data[this.indexOf(x, y, z)];
    }

    @Override
    public int getDamage(BlockPos pos) {
        return this.getDamage(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean setDamage(int x, int y, int z, int amount) {
        amount = Math.min(amount, Byte.MAX_VALUE);
        amount = Math.max(amount, 0);

        int index = this.indexOf(x, y, z);
        byte data = this.data[index];

        boolean flag = data != amount;

        if (data > 0 && amount == 0) {
            this.damagedBlocks--;
        } else if (data == 0 && amount > 0) {
            this.damagedBlocks++;
        }

        this.data[index] = (byte) amount;

        return flag;
    }

    @Override
    public boolean setDamage(BlockPos pos, int amount) {
        return this.setDamage(pos.getX(), pos.getY(), pos.getZ(), amount);
    }

    @Override
    public boolean isEmpty() {
        return this.damagedBlocks == 0;
    }

    private int indexOf(int x, int y, int z) {
        return (x & 15) + (y & 15) * 16 + (z & 15) * (16 * 16);
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
        for (int x = 0; x != 16; x++) {
            for (int y = 0; y != 16; y++) {
                for (int z = 0; z != 16; z++) {
                    byte damage = this.data[this.indexOf(x, y, z)];

                    if (damage != 0) {
                        damageData.appendTag(new NBTTagByte(damage));
                        damagePos.appendTag(new NBTTagShort(this.packPos(x, y, z)));
                    }
                }
            }
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
