package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.math.m2d.box.Box2I;
import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;
import com.artur114.bananalib.math.m2d.vec.Vec2D;
import com.artur114.bananalib.math.m2d.vec.Vec2I;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import com.dhanantry.scapeandrunparasites.world.SRPWorldParasiteSpawner;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.jetbrains.annotations.NotNull;

public class WaveDebug extends WaveAbstract {
    public WaveDebug() {}

    public WaveDebug(World world, IVec2I pos, IWaveTarget target) {
        super(pos, target, 1.0F);

        BlockPos blockPos = new BlockPos(this.pos.x() + 8, 0, this.pos.y() + 8);
        int v = 8 + this.rand.nextInt(8);
        int att = 0;

        if (this.rand.nextFloat() < 0.25) {
            v *= 4;
        }

        while (v > 0 && att < 6) {
            Biome.SpawnListEntry entry = SRPWorldParasiteSpawner.getSpawnListEntryForTypeAt((WorldServer) world, blockPos);
            if (entry != null) {
                int count = this.rand.nextInt(entry.maxGroupCount - entry.minGroupCount + 1) + entry.minGroupCount;
                if (v - count < 0) count = v;
                this.addEntity(EntityList.getKey(entry.entityClass), count);
                v -= count;
            }
            att++;
        }
    }

    public static class WaveTargetDebug implements IWaveTarget {
        private static final BlockPos cause = new BlockPos(7, 80, 7);
        private static final BlockPos[] blocks = new BlockPos[] {cause};
        private static final IVec2D chunk = new Vec2D(0, 0);
        private static final IBox2I box = new Box2I(0, 0, 0, 0).grow(4);

        @Override
        public BlockPos[] causalBlocks() {
            return blocks;
        }

        @Override
        public BlockPos causePos() {
            return cause;
        }

        @Override
        public void init(WorldServer world, TowerDefenceManager owner, int id) {}

        @Override
        public void onChunkLoaded(Chunk chunk) {}

        @Override
        public int ticksToUpdate() {return 1;}

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public void onRemove() {}

        @Override
        public void update() {}

        @Override
        public int id() {
            return 0;
        }

        @Override
        public IVec2D pos() {
            return chunk;
        }

        @Override
        public IBox2I box() {
            return box;
        }

        @Override
        public void readFromNBT(@NotNull NBTTagCompound nbt) {}

        @Override
        public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
            return nbt;
        }
    }
}
