package com.artur114.srptowerdefense.client.events.managers;

import com.artur114.srptowerdefense.common.pathfinding.PathPointForced;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.awt.*;
import java.util.List;

public class EntityPathDrawManager {
    public static final AxisAlignedBB boxToBlocks = new AxisAlignedBB(BlockPos.ORIGIN).grow(0.002D);
    public static final AxisAlignedBB boxToAir = new AxisAlignedBB(BlockPos.ORIGIN).grow(-0.25D);
    public static final int RANGE = 32;

    public void renderWorldLastEvent(RenderWorldLastEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        World world = mc.world;

        if (player == null) {
            return;
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        try {
            AxisAlignedBB box = new AxisAlignedBB(player.getPosition().add(-RANGE, -RANGE, -RANGE), player.getPosition().add(RANGE + 1, RANGE + 1, RANGE + 1));
            List<EntityLiving> entities = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getEntitiesWithinAABB(EntityLiving.class, box);
            for (EntityLiving entity : entities) {
                this.renderPath(entity);
            }
        } catch (Exception ignored) {}

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private void renderPath(EntityLiving entity) {
        Path path = entity.navigator.getPath();

        if (path != null && !path.isFinished()) {
            for (int i = 0; i != path.getCurrentPathLength(); i++) {
                this.renderPathPoint(entity, path, path.getPathPointFromIndex(i));
            }
        }
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

        RenderGlobal.drawSelectionBoundingBox(box.offset(pointPos).offset(-Particle.interpPosX, -Particle.interpPosY, -Particle.interpPosZ), color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha);

        if (point instanceof PathPointForced && ((PathPointForced) point).posToBreak != null) {
            for (BlockPos pos : ((PathPointForced) point).posToBreak) {
                state = entity.world.getBlockState(pos);
                box = boxToAir;

                if (state.getMaterial() != Material.AIR && !state.getBlock().isPassable(entity.world, pointPos)) {
                    box = boxToBlocks;
                }

                RenderGlobal.drawSelectionBoundingBox(box.offset(pos).offset(-Particle.interpPosX, -Particle.interpPosY, -Particle.interpPosZ), Color.RED.getRed() / 255.0F, Color.RED.getGreen() / 255.0F, Color.RED.getBlue() / 255.0F, alpha);
            }
        }
    }
}
