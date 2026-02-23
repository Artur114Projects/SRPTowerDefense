package com.artur114.srptowerdefense.common.systems.parasitewaves;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2DM;
import com.artur114.bananalib.math.m2d.vec.Vec2DM;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Iterator;

public class WavesManager implements INBTSerializable<NBTTagCompound> {
    private final Int2ObjectMap<IWave> wavesMap = new Int2ObjectOpenHashMap<>();
    private final World world;

    public WavesManager(World world) {
        this.world = world;
    }

    public void update() {
        if (this.world.getWorldTime() % 20 == 0) {
            Iterator<IWave> iterator = this.wavesMap.values().iterator();
            IVec2DM vec = new Vec2DM();

            while (iterator.hasNext()) {
                IWave wave = iterator.next();

                if (wave.pos().distanceSq(wave.targetChunk()) > 1.0D) {
                    wave.move(vec.set(wave.targetChunk()).subtract(wave.pos()).normalize().scale(wave.speed()));
                }

                if (!wave.isAlive()) {
                    iterator.remove();
                }
            }
        }
    }

    public IWave waveFromId(int id) {
        return this.wavesMap.get(id);
    }

    public void addWave(IWave wave, int id) {
        this.wavesMap.put(id, wave);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
