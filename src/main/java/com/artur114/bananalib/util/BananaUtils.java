package com.artur114.bananalib.util;

import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2I;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BananaUtils {

    public static boolean isChunkLoaded(World world, IVec2I pos) {
        return isChunkLoaded(world, pos.x(), pos.y());
    }

    public static boolean isChunkLoaded(World world, ChunkPos pos) {
        return isChunkLoaded(world, pos.x, pos.z);
    }

    public static boolean isChunkLoaded(World world, int x, int z) {
        if (world.isRemote) {
            return ((WorldClient) world).getChunkProvider().getLoadedChunk(x, z) != null;
        } else {
            return ((WorldServer) world).getChunkProvider().id2ChunkMap.containsKey(ChunkPos.asLong(x, z));
        }
    }

    public static boolean isChunksLoaded(World world, IBox2I box2I) {
        return isChunksLoaded(world, box2I.minX(), box2I.minY(), box2I.maxX(), box2I.maxY());
    }

    public static boolean isChunksLoaded(World world, IVec2I from, IVec2I to) {
        return isChunksLoaded(world, from.x(), from.y(), to.x(), to.y());
    }

    public static boolean isChunksLoaded(World world, ChunkPos from, ChunkPos to) {
        return isChunksLoaded(world, from.x, from.z, to.x, to.z);
    }

    public static boolean isChunksLoaded(World world, int startX, int startZ, int endX, int endZ) {
        int minX = Math.min(startX, endX);
        int minZ = Math.min(startZ, endZ);
        int maxX = Math.max(startX, endX);
        int maxZ = Math.max(startZ, endZ);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (!isChunkLoaded(world, x, z)) {
                    return false;
                }
            }
        }

        return true;
    }
}
