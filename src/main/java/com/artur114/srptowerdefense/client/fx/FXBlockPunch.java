package com.artur114.srptowerdefense.client.fx;

import com.artur114.bananalib.math.m3d.matrix.IMatrix3FM;
import com.artur114.bananalib.math.m3d.matrix.Matrix3FM;
import com.artur114.bananalib.math.m3d.vec.IVec3D;
import com.artur114.bananalib.math.m3d.vec.IVec3DM;
import com.artur114.bananalib.math.m3d.vec.Vec3D;
import com.artur114.bananalib.math.m3d.vec.Vec3DM;
import com.artur114.bananalib.mc.math.m3d.vec.VecMc3D;
import com.artur114.srptowerdefense.main.SRPTDMain;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class FXBlockPunch {
    public static void draw(BlockPos pos, NBTTagCompound nbt) {
        EnumFacing facing = EnumFacing.values()[nbt.getByte("facing")];
        IVec3D vec = new VecMc3D(nbt.getDouble("vecX"), nbt.getDouble("vecY"), nbt.getDouble("vecZ"));
        draw(pos, facing, vec, nbt.getInteger("power"));
    }

    private static void draw(BlockPos pos, EnumFacing facing, IVec3D vec, int pow) {
        Minecraft mc = Minecraft.getMinecraft();
        Random rand = new Random();
        World world = mc.world;
        float power = pow / 10.0F * 2.0F;
        int circles = (int) ((power * 10.0F) / 2.0F) + 1;

        double x = vec.x() + facing.getFrontOffsetX() / 32.0D;
        double y = vec.y() + facing.getFrontOffsetY() / 32.0D;
        double z = vec.z() + facing.getFrontOffsetZ() / 32.0D;
        double xSpeed = facing.getFrontOffsetX() / 32.0D;
        double ySpeed = facing.getFrontOffsetY() / 32.0D;
        double zSpeed = facing.getFrontOffsetZ() / 32.0D;

        IMatrix3FM matrix = new Matrix3FM();
        IVec3DM vecM = new Vec3DM();

        for (int i = 0; i != circles + 1; i++) {
            float radius = power * ((float) i / circles);
            float offset = 360.0F * rand.nextFloat();
            for (int r = 0; r != ((int) (3 * (radius * 10))); r++) {
                float angle = 360.0F * (r / 8.0F) + offset * (16 * rand.nextFloat());
                float range = radius + ((0.2F * rand.nextFloat()) - 0.1F);
                IVec3D vecRot;
                if (facing.getFrontOffsetX() == 1) {
                    vecRot = vecM.set(0, 0, range);
                } else if (facing.getFrontOffsetY() == 1) {
                    vecRot = vecM.set(range, 0, 0);
                } else {
                    vecRot = vecM.set(0, range, 0);
                }
                IVec3D rot = matrix.setIdentity().rotate(angle, facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ()).transform(vecRot);
                world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + rot.x(), y + rot.y(), z + rot.z(), xSpeed + rot.x() * 4, ySpeed + rot.y() * 4, zSpeed + rot.z() * 4, Block.getStateId(world.getBlockState(pos)));
            }
        }
    }
}
