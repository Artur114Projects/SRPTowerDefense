package com.artur114.srptowerdefense.common.pathfinding;


import com.artur114.srptowerdefense.common.blockdamage.BlockDamageHandler;
import com.artur114.srptowerdefense.common.blockdamage.IDamagedChunk;
import com.artur114.srptowerdefense.common.util.math.AdvancedBlockPos;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class WalkNodeProcessorForced extends WalkNodeProcessor {
    protected final IntHashMap<IBlockState> stateCache = new IntHashMap<>();

    @Override
    public void init(IBlockAccess world, EntityLiving entity) {
        super.init(world, entity);
        this.stateCache.clearMap();
    }

    @Override
    public @NotNull PathPointForced getPathPointToCoords(double x, double y, double z) {
        return this.openPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    @Override
    public @NotNull PathPointForced getStart() {
        AdvancedBlockPos blockPos = AdvancedBlockPos.obtain();
        int i;

        try {
            if (this.getCanSwim() && this.entity.isInWater()) {
                i = (int) this.entity.getEntityBoundingBox().minY;
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));

                for (Block block = this.getBlockState(this.blockaccess, blockpos$mutableblockpos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock()) {
                    ++i;
                    blockpos$mutableblockpos.setPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
                }
            } else if (this.entity.onGround) {
                i = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D);
            } else {
                BlockPos blockpos;

                for (blockpos = new BlockPos(this.entity); (this.getBlockState(this.blockaccess, blockpos).getMaterial() == Material.AIR || this.blockaccess.getBlockState(blockpos).getBlock().isPassable(this.blockaccess, blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down()) {
                    ;
                }

                i = blockpos.up().getY();
            }

            IForcedPathNodeType nodeType = this.pathNodeType(this.entity, blockPos.setPos(this.entity), null);

            if (nodeType.getPriority(this.entity) < 0.0F) {
                Set<BlockPos> set = Sets.newHashSet();
                set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, i, this.entity.getEntityBoundingBox().minZ));
                set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, i, this.entity.getEntityBoundingBox().maxZ));
                set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, i, this.entity.getEntityBoundingBox().minZ));
                set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, i, this.entity.getEntityBoundingBox().maxZ));

                for (BlockPos bp : set) {
                    IForcedPathNodeType offsetNode = this.pathNodeType(this.entity, blockPos.setPos(bp), null);

                    if (offsetNode.getPriority(this.entity) >= 0.0F) {
                        return this.openPoint(bp.getX(), bp.getY(), bp.getZ());
                    }
                }
            }

            return this.openPoint(blockPos.setPos(this.entity).getX(), i, blockPos.getZ());
        } finally {
            AdvancedBlockPos.release(blockPos);
        }
    }

    protected @NotNull PathPointForced openPoint(AdvancedBlockPos pos) {
        return this.openPoint(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected @NotNull PathPointForced openPoint(int x, int y, int z) {
        int i = PathPointForced.makeHash(x, y, z);
        PathPoint pathpoint = this.pointMap.lookup(i);

        if (!(pathpoint instanceof PathPointForced)) {
            pathpoint = new PathPointForced(x, y, z);
            this.pointMap.addKey(i, pathpoint);
        }

        return (PathPointForced) pathpoint;
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
        int i = 0;

        AdvancedBlockPos blockPos = AdvancedBlockPos.obtain();

        for (int y = currentPoint.y > 0 ? -1 : 0; y != 2; y++) {
            BlockPos blockpos = blockPos.setPos(currentPoint.x, currentPoint.y + y, currentPoint.z).down();
            double d0 = (double) currentPoint.y + y - (1.0D - this.getBlockState(this.blockaccess, blockpos).getBoundingBox(this.blockaccess, blockpos).maxY);
            PathPointForced pathPointZP = this.getSafePoint(blockPos.setPos(currentPoint.x, currentPoint.y + y, currentPoint.z + 1), currentPoint, d0);
            PathPointForced pathPointXN = this.getSafePoint(blockPos.setPos(currentPoint.x - 1, currentPoint.y + y, currentPoint.z), currentPoint, d0);
            PathPointForced pathPointXP = this.getSafePoint(blockPos.setPos(currentPoint.x + 1, currentPoint.y + y, currentPoint.z), currentPoint, d0);
            PathPointForced pathPointZN = this.getSafePoint(blockPos.setPos(currentPoint.x, currentPoint.y + y, currentPoint.z - 1), currentPoint, d0);

            if (pathPointZP != null && pathPointZP.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathPointZP;
            }

            if (pathPointXN != null && pathPointXN.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathPointXN;
            }

            if (pathPointXP != null && pathPointXP.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathPointXP;
            }

            if (pathPointZN != null && pathPointZN.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathPointZN;
            }

            boolean flagZN = pathPointZN == null || pathPointZN.nodeType == PathNodeTypeForced.OPEN.toMc() || (pathPointZN.costMalus != 0.0F && pathPointZN.posToBreak == null);
            boolean flagZP = pathPointZP == null || pathPointZP.nodeType == PathNodeTypeForced.OPEN.toMc() || (pathPointZP.costMalus != 0.0F && pathPointZP.posToBreak == null);
            boolean flagXP = pathPointXP == null || pathPointXP.nodeType == PathNodeTypeForced.OPEN.toMc() || (pathPointXP.costMalus != 0.0F && pathPointXP.posToBreak == null);
            boolean flagXN = pathPointXN == null || pathPointXN.nodeType == PathNodeTypeForced.OPEN.toMc() || (pathPointXN.costMalus != 0.0F && pathPointXN.posToBreak == null);

            if (flagZN && flagXN) {
                PathPoint pathPointCorner = this.getSafePoint(blockPos.setPos(currentPoint.x - 1, currentPoint.y + y, currentPoint.z - 1), currentPoint, d0);

                if (pathPointCorner != null && pathPointCorner.distanceTo(targetPoint) < maxDistance) {
                    pathOptions[i++] = pathPointCorner;
                }
            }

            if (flagZN && flagXP) {
                PathPoint pathPointCorner = this.getSafePoint(blockPos.setPos(currentPoint.x + 1, currentPoint.y + y, currentPoint.z - 1), currentPoint, d0);

                if (pathPointCorner != null && pathPointCorner.distanceTo(targetPoint) < maxDistance) {
                    pathOptions[i++] = pathPointCorner;
                }
            }

            if (flagZP && flagXN) {
                PathPoint pathPointCorner = this.getSafePoint(blockPos.setPos(currentPoint.x - 1, currentPoint.y + y, currentPoint.z + 1), currentPoint, d0);

                if (pathPointCorner != null && pathPointCorner.distanceTo(targetPoint) < maxDistance) {
                    pathOptions[i++] = pathPointCorner;
                }
            }

            if (flagZP && flagXP) {
                PathPoint pathPointCorner = this.getSafePoint(blockPos.setPos(currentPoint.x + 1, currentPoint.y + y, currentPoint.z + 1), currentPoint, d0);

                if (pathPointCorner != null && pathPointCorner.distanceTo(targetPoint) < maxDistance) {
                    pathOptions[i++] = pathPointCorner;
                }
            }
        }

        AdvancedBlockPos.release(blockPos);

        return i;
    }

    @Nullable
    private PathPointForced getSafePoint(AdvancedBlockPos pos, PathPoint prevPoint, double blockBoxHeight) {
        PathPointForced pathpoint = null;

        double h = (double) pos.getY() - (1.0D - this.getBlockState(this.blockaccess, pos.down()).getBoundingBox(this.blockaccess, pos).maxY);
        pos.up();

        if (h - blockBoxHeight > 1.125D) {
            return null;
        } else {
            IForcedPathNodeType nodeType = this.pathNodeType(this.entity, pos, prevPoint);
            float f = nodeType.getPriority(this.entity);

            if (f >= 0.0F) {
                pathpoint = this.openPoint(pos);
                pathpoint.setNodeTypeForced(nodeType);
                pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            }

            if (nodeType.toMc() != PathNodeType.WALKABLE) {
                if (nodeType == PathNodeTypeForced.OPEN) {
                    if (this.entity.width >= 1.0F) {
                        IForcedPathNodeType node = this.pathNodeType(this.entity, pos.down(), prevPoint); pos.up();

                        if (node == PathNodeTypeForced.BLOCKED) {
                            pathpoint = this.openPoint(pos);
                            pathpoint.setNodeTypeForced(PathNodeTypeForced.WALKABLE);
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            return pathpoint;
                        }
                    }

                    int i = 0;

                    while (pos.getY() > 0 && nodeType == PathNodeTypeForced.OPEN) {
                        pos.down();

                        if (i++ >= this.entity.getMaxFallHeight()) {
                            return null;
                        }

                        nodeType = this.pathNodeType(this.entity, pos, prevPoint);
                        f = nodeType.getPriority(this.entity);

                        if (nodeType != PathNodeTypeForced.OPEN && f >= 0.0F) {
                            pathpoint = this.openPoint(pos);
                            pathpoint.setNodeTypeForced(nodeType);
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            break;
                        }

                        if (f < 0.0F) {
                            return null;
                        }
                    }
                }
            }

            return pathpoint;
        }
    }

    private IForcedPathNodeType pathNodeType(EntityLiving entity, AdvancedBlockPos pos, PathPoint prevPoint) {
        return this.pathNodeType(this.blockaccess, pos, entity, prevPoint, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }

    public IForcedPathNodeType pathNodeType(IBlockAccess world, AdvancedBlockPos pos, EntityLiving entity, PathPoint prevPoint, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
        IForcedPathNodeType nodeType = PathNodeTypeForced.BLOCKED;
        Set<IForcedPathNodeType> set = new HashSet<>();
        this.currentEntity = entity;
        nodeType = this.pathNodeType(world, pos, prevPoint, xSize, ySize, zSize, canBreakDoorsIn, canEnterDoorsIn, set, nodeType);
        this.currentEntity = null;
        set = set.stream().sorted(Comparator.comparingInt(IForcedPathNodeType::ord)).collect(Collectors.toCollection(LinkedHashSet::new));

        if (set.contains(PathNodeTypeForced.FENCE))
        {
            return PathNodeTypeForced.FENCE;
        }
        else
        {
            IForcedPathNodeType retNode = PathNodeTypeForced.BLOCKED;

            for (IForcedPathNodeType setNode : set) {
                if (setNode.getPriority(entity) < 0.0F)
                {
                    return setNode;
                }

                if (setNode.ord() == -1 && retNode.ord() == -1) {
                    ((PathNodeTypeBreakage) setNode).applyBreakageNode((PathNodeTypeBreakage) retNode);
                }

                if (setNode.getPriority(entity) >= retNode.getPriority(entity))
                {
                    retNode = setNode;
                }
            }

            if (nodeType == PathNodeTypeForced.OPEN && retNode.getPriority(entity) == 0.0F)
            {
                return PathNodeTypeForced.OPEN;
            }
            else
            {
                return retNode;
            }
        }
    }

    public IForcedPathNodeType pathNodeType(IBlockAccess world, AdvancedBlockPos pos, PathPoint prevPoint, int xSize, int ySize, int zSize, boolean canOpenDoorsIn, boolean canEnterDoorsIn, Set<IForcedPathNodeType> nodeSet, IForcedPathNodeType nodeType) {
        if (prevPoint != null) {
            if (prevPoint.y > pos.getY()) {
                ySize++;
            } else if (prevPoint.y < pos.getY()) {
                pos.pushPos();
                pos.setPos(prevPoint.x, prevPoint.y + ySize, prevPoint.z);
                this.pathNodeType(world, pos, null, xSize, 1, zSize, canOpenDoorsIn, canEnterDoorsIn, nodeSet, nodeType);
                pos.popPos();
            }
        }
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    pos.pushPos();
                    pos.add(x, y, z);
                    IForcedPathNodeType pathnodetype = this.pathNodeType(world, pos);

                    if (pathnodetype == PathNodeTypeForced.DOOR_WOOD_CLOSED && canOpenDoorsIn && canEnterDoorsIn)
                    {
                        pathnodetype = PathNodeTypeForced.WALKABLE;
                    }

                    if (pathnodetype == PathNodeTypeForced.DOOR_OPEN && !canEnterDoorsIn)
                    {
                        pathnodetype = PathNodeTypeForced.BLOCKED;
                    }

                    if (x == 0 && y == 0 && z == 0)
                    {
                        nodeType = pathnodetype;
                    }

                    nodeSet.add(pathnodetype);
                    pos.popPos();
                }
            }
        }

        return nodeType;
    }

    public IForcedPathNodeType pathNodeType(IBlockAccess world, AdvancedBlockPos pos) {
        IForcedPathNodeType pathNodeType = this.pathNodeTypeRaw(world, pos);

        if (pathNodeType == PathNodeTypeForced.OPEN && pos.getY() >= 1) {
            Block block = this.getBlockState(world, pos.down()).getBlock();
            IForcedPathNodeType pathNodeTypeDown = this.pathNodeTypeRaw(world, pos); pos.up();
            pathNodeType = pathNodeTypeDown != PathNodeTypeForced.WALKABLE && pathNodeTypeDown != PathNodeTypeForced.OPEN && pathNodeTypeDown != PathNodeTypeForced.WATER && pathNodeTypeDown != PathNodeTypeForced.LAVA ? PathNodeTypeForced.WALKABLE : PathNodeTypeForced.OPEN;

            if (pathNodeTypeDown == PathNodeTypeForced.DAMAGE_FIRE || block == Blocks.MAGMA)
            {
                pathNodeType = PathNodeTypeForced.DAMAGE_FIRE;
            }

            if (pathNodeTypeDown == PathNodeTypeForced.DAMAGE_CACTUS)
            {
                pathNodeType = PathNodeTypeForced.DAMAGE_CACTUS;
            }

            if (pathNodeTypeDown == PathNodeTypeForced.DAMAGE_OTHER)
            {
                pathNodeType = PathNodeTypeForced.DAMAGE_OTHER;
            }
        }

        pathNodeType = this.checkNeighborBlocksF(world, pos, pathNodeType);
        return pathNodeType;
    }

    public IForcedPathNodeType checkNeighborBlocksF(IBlockAccess world, AdvancedBlockPos pos, IForcedPathNodeType pathNodeType) {

        if (pathNodeType == PathNodeTypeForced.WALKABLE) {
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (i != 0 || j != 0) {
                        pos.pushPos();
                        IBlockState state = this.getBlockState(world, pos.add(i, 0, j));
                        Block block = state.getBlock();
                        PathNodeType type = block.getAiPathNodeType(state, world, pos, this.currentEntity);

                        if (block == Blocks.CACTUS || type == PathNodeType.DAMAGE_CACTUS) {
                            pathNodeType = PathNodeTypeForced.DANGER_CACTUS;
                        } else if (block == Blocks.FIRE || type == PathNodeType.DAMAGE_FIRE) {
                            pathNodeType = PathNodeTypeForced.DANGER_FIRE;
                        } else if (type == PathNodeType.DAMAGE_OTHER)  {
                            pathNodeType = PathNodeTypeForced.DANGER_OTHER;
                        }
                        pos.popPos();
                    }
                }
            }
        }

        return pathNodeType;
    }

    protected IForcedPathNodeType pathNodeTypeRaw(IBlockAccess world, AdvancedBlockPos pos) {
        IBlockState iblockstate = this.getBlockState(world, pos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();

        PathNodeType type = block.getAiPathNodeType(iblockstate, world, pos, this.currentEntity);
        if (type != null) return PathNodeTypeForced.fromMc(type);

        if (material == Material.AIR)
        {
            return PathNodeTypeForced.OPEN;
        }
        else if (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY)
        {
            if (block == Blocks.FIRE)
            {
                return PathNodeTypeForced.DAMAGE_FIRE;
            }
            else if (block == Blocks.CACTUS)
            {
                return PathNodeTypeForced.DAMAGE_CACTUS;
            }
            else if (block instanceof BlockDoor && material == Material.WOOD && !iblockstate.getValue(BlockDoor.OPEN))
            {
                return PathNodeTypeForced.DOOR_WOOD_CLOSED;
            }
            else if (block instanceof BlockDoor && material == Material.IRON && !iblockstate.getValue(BlockDoor.OPEN))
            {
                return PathNodeTypeForced.DOOR_IRON_CLOSED;
            }
            else if (block instanceof BlockDoor && iblockstate.getValue(BlockDoor.OPEN))
            {
                return PathNodeTypeForced.DOOR_OPEN;
            }
            else if (block instanceof BlockRailBase)
            {
                return PathNodeTypeForced.RAIL;
            }
            else if (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || iblockstate.getValue(BlockFenceGate.OPEN)))
            {
                if (material == Material.WATER)
                {
                    return PathNodeTypeForced.WATER;
                }
                else if (material == Material.LAVA)
                {
                    return PathNodeTypeForced.LAVA;
                }
                else if (block.isPassable(world, pos))
                {
                    return PathNodeTypeForced.OPEN;
                }
                else if (block.getBlockHardness(iblockstate, this.entity.world, pos) != -1)
                {
                    return new PathNodeTypeBreakage(pos, (block.getBlockHardness(iblockstate, this.entity.world, pos) * 2.0F) * (1.0F - ((float) BlockDamageHandler.getDamage(this.entity.world, pos) / IDamagedChunk.MAX_DAMAGE)));
                }
                else
                {
                    return PathNodeTypeForced.BLOCKED;
                }
            }
            else
            {
                return PathNodeTypeForced.FENCE;
            }
        }
        else
        {
            return PathNodeTypeForced.TRAPDOOR;
        }
    }
    
    private IBlockState getBlockState(IBlockAccess world, BlockPos pos) {
        IBlockState state = this.stateCache.lookup(pos.hashCode());
        
        if (state == null) {
            state = world.getBlockState(pos);
            this.stateCache.addKey(pos.hashCode(), state);
        }
        
        return state;
    }
}
