package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.Vec2D;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.dhanantry.scapeandrunparasites.world.SRPWorldParasiteSpawner;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpawnWaveGenerator implements ITDObjectsGenerator {
    private final Set<ResourceLocation> blackList = new HashSet<>();
    private final Random rand = new Random();

    public SpawnWaveGenerator() {
        this.blackList.addAll(Stream.of(
            "srparasites:fer_enderman", "srparasites:lice",
            "srparasites:hostii", "srparasites:airscrew",
            "srparasites:host", "srparasites:carrier_colony",
            "srparasites:architect", "srparasites:kirin",
            "srparasites:mar_enderman", "srparasites:sim_enderman",
            "srparasites:haunter"
        ).map(ResourceLocation::new).collect(Collectors.toList()));
    }

    @Override
    public void update(WorldServer world, TowerDefenceManager manager) {
        if (world.getTotalWorldTime() % (12 * 60 * 20) == 0) {
            int phase = SRPSaveData.get(world, 72).getEvolutionPhase(world.provider.getDimension());

            if (phase < 8 && world.getTotalWorldTime() % (24 * 60 * 20) != 0) {
                return;
            }

            for (ProtectedZone zone : manager.tdObjects(ProtectedZone.class)) {
                int count = 1;

                if (phase < 4) {
                    count = 0;
                }

                if (phase > 4) {
                    count += this.randInt(0, phase - 4);
                }

                for (int i = 0; i != count; i++) {
                    manager.addObject(this.createWave(world, zone, new Vec2D(this.randInt(16, 64), 0).rotate(360.0D * this.rand.nextDouble()).add(zone.pos()), phase), manager.createSafeId());
                }
            }
        }
    }

    private WaveBase createWave(WorldServer world, ProtectedZone zone, IVec2D pos, int phase) {
        WaveBase wave = new WaveBase(pos.floor(), zone);
        BlockPos blockPos = new BlockPos((pos.x() * 16) + 8, 0, (pos.y() * 16) + 8);
        int v = 8 + this.rand.nextInt(8);
        int att = 0;

        if (this.rand.nextFloat() < 0.25) {
            v *= 4;
        }

        if (phase > 4) {
            v *= (phase - 4);
        }

        while (v > 0 && att < 16) {
            Biome.SpawnListEntry entry = SRPWorldParasiteSpawner.getSpawnListEntryForTypeAt(world, blockPos);
            if (entry != null && this.canSpawn(entry.entityClass)) {
                int count = this.rand.nextInt(entry.maxGroupCount - entry.minGroupCount + 1) + entry.minGroupCount;
                if (v - count < 0) count = v;
                wave.addEntity(EntityList.getKey(entry.entityClass), count);
                v -= count;
            }
            att++;
        }

        return wave;
    }

    private int randInt(int min, int max) {
        return this.rand.nextInt(max - min + 1) + min;
    }

    private boolean canSpawn(Class<? extends EntityLiving> clazz) {
        return !this.blackList.contains(EntityList.getKey(clazz));
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound nbt) {}

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        return nbt;
    }
}
