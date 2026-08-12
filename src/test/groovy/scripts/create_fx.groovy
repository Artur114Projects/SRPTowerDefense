package scripts

import com.artur114.bananalib.math.m3d.matrix.IMatrix3FM
import com.artur114.bananalib.math.m3d.matrix.Matrix3FM
import com.artur114.bananalib.math.m3d.vec.IVec3D
import com.artur114.bananalib.math.m3d.vec.Vec3D
import com.artur114.srptowerdefense.common.init.InitSounds
import com.artur114.srptowerdefense.common.items.ItemMallet
import com.artur114.srptowerdefense.common.worldstate.blockdamage.registry.BlockMetaRegistry
import groovy.transform.BaseScript
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import scripts.classes.BaseDevScript

@BaseScript
BaseDevScript script

Random rand = new Random();
BlockPos pos = posIn

