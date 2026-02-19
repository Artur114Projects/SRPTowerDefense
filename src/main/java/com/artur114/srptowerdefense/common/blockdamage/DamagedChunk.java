package com.artur114.srptowerdefense.common.blockdamage;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public abstract class DamagedChunk implements IDamagedChunk {
    protected final IExtendedDamageStorage[] storages = new IExtendedDamageStorage[16];
    protected int initStoragesCount = 0;
    protected final int dimension;
    protected final ChunkPos pos;

    public DamagedChunk(ChunkPos pos, int dimension) {
        this.dimension = dimension;
        this.pos = pos;
    }

    @Override
    public int getDamage(int x, int y, int z) {
        if (this.isEmpty()) return 0;
        IExtendedDamageStorage storage = this.storages[y >> 4];
        if (storage == null) return 0;

        return storage.getDamage(x, y, z);
    }

    @Override
    public int getDamage(BlockPos pos) {
        return this.getDamage(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean isEmpty() {
        return this.initStoragesCount == 0;
    }
}
