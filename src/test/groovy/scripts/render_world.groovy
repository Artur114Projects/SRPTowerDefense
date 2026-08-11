package scripts

import com.artur114.bananalib.math.m3d.box.Box3D
import com.artur114.bananalib.mc.BananaMC
import com.artur114.bananalib.mc.cap.BananaCaps
import com.artur114.bananalib.mc.math.m2d.vec.PosMc2I
import com.artur114.srptowerdefense.common.init.InitCapabilities
import com.artur114.srptowerdefense.common.pathfinding.PathPointForced
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ITowerDefenceObject
import com.artur114.srptowerdefense.common.worldstate.towerdefence.IWave
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ProtectedZone
import groovy.transform.BaseScript
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.particle.Particle
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.entity.EntityLiving
import net.minecraft.pathfinding.Path
import net.minecraft.pathfinding.PathPoint
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraftforge.common.ForgeChunkManager.Ticket
import net.minecraftforge.fml.common.FMLCommonHandler
import scripts.classes.BaseDevScript

import java.awt.Color

@BaseScript
BaseDevScript script

prepareToDraw(2) {
    BananaCaps.capability(FMLCommonHandler.instance().minecraftServerInstance.getWorld(0), InitCapabilities.TOWER_DEFENCE_SYSTEM).ifPresent {
        it.tdObjects(ITowerDefenceObject).each {
            Color color = Color.BLUE

            if (it instanceof IWave) {
                drawBox(new Box3D(it.pos().toImmutable().xzy(0) * 16, it.pos().toImmutable().xzy(255) * 16), Color.RED)
            } else {
                drawBox(new Box3D((it.pos().toImmutable().xzy(0) * 16).add(8, 0, 8), (it.pos().toImmutable().xzy(255) * 16).add(8, 0, 8)), color)
            }

            drawBox(new Box3D(it.box().minX() * 16, 0, it.box().minY() * 16, it.box().maxX() * 16, 128, it.box().maxY() * 16))
        }
    }
}

prepareToDraw {
    try {
        AxisAlignedBB box = new AxisAlignedBB(player.getPosition().add(-RANGE, -RANGE, -RANGE), player.getPosition().add(RANGE + 1, RANGE + 1, RANGE + 1));
        List<EntityLiving> entities = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getEntitiesWithinAABB(EntityLiving.class, box);
        for (EntityLiving entity : entities) {
            this.renderPath(entity);
        }
    } catch (Exception ignored) {}
}

AxisAlignedBB getBoxToBlocks() {
    return new AxisAlignedBB(BlockPos.ORIGIN).grow(0.002D)
}
AxisAlignedBB getBoxToAir() {
    return new AxisAlignedBB(BlockPos.ORIGIN).grow(-0.25D)
}
int getRANGE() {
    return 32
}


private void renderPath(EntityLiving entity) {
    Path path = entity.navigator.getPath();

    if (path != null && !path.isFinished()) {
        for (int i = 0; i != path.getCurrentPathLength(); i++) {
            this.renderPathPoint(entity, path, path.getPathPointFromIndex(i));
        }
    }

    BananaCaps.capability(entity, InitCapabilities.TD_ENTITY_DATA).ifPresent({defence ->
        BlockPos pos = defence.moveTarget();

        if (pos != null) {
            Color color = Color.WHITE;
            drawBox(boxToAir.offset(pos), color)
            drawBox(boxToBlocks.offset(pos), color)
        }
    });
}

private void renderPathPoint(EntityLiving entity, Path path, PathPoint point) {
    BlockPos pointPos = new BlockPos(point.x, point.y, point.z);
    IBlockState state = entity.world.getBlockState(pointPos);
    AxisAlignedBB box = boxToAir;
    Color color = Color.GREEN;
    float alpha = 0.25F;

    if (path.getFinalPathPoint() == point) {
        color = Color.BLUE;
    }

    if (state.getMaterial() != Material.AIR && !state.getBlock().isPassable(entity.world, pointPos)) {
        box = boxToBlocks;
    }

    try {
        if (path.getPathPointFromIndex(path.getCurrentPathIndex()) == point) {
            alpha = 0.98F;
        }
    } catch (Exception ignored) {}


    boolean flag = true;

    if (point instanceof PathPointForced && ((PathPointForced) point).posToBreak != null) {
        for (BlockPos pos : ((PathPointForced) point).posToBreak) {
            state = entity.world.getBlockState(pos);
            Color color1 = Color.RED;
            box = boxToAir;

            if (state.getMaterial() != Material.AIR && !state.getBlock().isPassable(entity.world, pointPos)) {
                box = boxToBlocks;
            }

            if (pos.equals(pointPos)) {
                flag = false;
            }

            drawBox(box.offset(pos), color1, alpha)
        }
    }

    if (flag) {
        drawBox(box.offset(pointPos), color, alpha)
    }
}