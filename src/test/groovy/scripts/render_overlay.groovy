package scripts

import com.artur114.bananalib.math.core.m2d.vec.IVec2DC
import com.artur114.bananalib.mc.cap.BananaCaps
import com.artur114.srptowerdefense.common.init.InitCapabilities
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ITowerDefenceObject
import com.artur114.srptowerdefense.common.worldstate.towerdefence.IWave
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ProtectedZone
import com.artur114.srptowerdefense.common.worldstate.towerdefence.WaveAbstract
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase
import com.dhanantry.scapeandrunparasites.world.SRPSaveData
import groovy.transform.BaseScript
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.entity.EntityLiving
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.fml.common.FMLCommonHandler
import scripts.classes.BaseDevScript

@BaseScript
BaseDevScript script

def debug = ["FPS: ${Minecraft.minecraft.debugFPS}"]

MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance()
if (server != null) {
    double tps = time(20)
    TextFormatting c = color(tps)
    double min = min(20)
    TextFormatting c1 = color(min)

    debug << "TPS avg $c${String.format("%.2f", tps)}$TextFormatting.RESET min $c1${String.format("%.2f", min)}$TextFormatting.RESET"

    debug << "SRP Phase $TextFormatting.RED${SRPSaveData.get(server.getWorld(player.dimension), 72).getEvolutionPhase(player.dimension)}"
    debug << "SRP Generation $TextFormatting.RED${SRPSaveData.get(server.getWorld(player.dimension), 72).getGeneration(player.dimension)}"
}


RayTraceResult res = player.rayTrace(8, partialTicksIn)

if (res?.typeOfHit == RayTraceResult.Type.BLOCK) {
    debug << "Look at [${res.blockPos.x}, ${res.blockPos.y}, ${res.blockPos.z}]"
} else {
    debug << "Look at [?, ?, ?]"
}

if (server != null) {
    debug << "Forced chunks $TextFormatting.GREEN${server.getWorld(player.dimension).getPersistentChunks().size()}"


//    AxisAlignedBB box = new AxisAlignedBB(player.getPosition().add(-256, -256, -256), player.getPosition().add(256 + 1, 256 + 1, 256 + 1));
//    List<EntityLiving> entities = server.getWorld(player.dimension).getEntitiesWithinAABB(EntityLiving.class, box);
//    int i = 0;
//    entities.each {
//        if (it instanceof EntityParasiteBase) {
//            i++;
//        }
//    }
//
//    debug << "Parasites $TextFormatting.RED$i"
}

try {
    BananaCaps.capability(FMLCommonHandler.instance().minecraftServerInstance.getWorld(0), InitCapabilities.TOWER_DEFENCE_SYSTEM).ifPresent {
        it.tdObjects(ITowerDefenceObject).each {
            if (it != null && !(it instanceof IWave) && !(it instanceof ProtectedZone)) {
                debug << "${TextFormatting.AQUA}${it.class.name.substring(it.class.name.lastIndexOf('.') + 1)} (${it.id()})${TextFormatting.RESET} ${formatPos(it.pos())}"
            }
        }
        it.tdObjects(ProtectedZone).each {
            if (it != null) {
                debug << "${TextFormatting.AQUA}${it.class.name.substring(it.class.name.lastIndexOf('.') + 1)} (${it.id()})${TextFormatting.RESET} ${formatPos(it.pos())} chunks $TextFormatting.AQUA${it.protectedChunks.size()}$TextFormatting.RESET tickeds $TextFormatting.AQUA${it.tickedLoadCountMap.size()}"
            }
        }
        it.tdObjects(IWave).sort { it.targetChunk().distanceSq(it.pos()) }.each {
            if (it != null) {
                debug << "${TextFormatting.YELLOW}${it.class.name.substring(it.class.name.lastIndexOf('.') + 1)} (${it.id()})${TextFormatting.RESET} ${formatPos(it.pos())} -> ${formatPos(it.target().causePos())} size ${it instanceof WaveAbstract ? "$TextFormatting.BLUE${it.entityRecords.size()}$TextFormatting.RESET" : ""} ${it instanceof WaveAbstract && it.entityRecords.any { it.value.isLoaded() } ? "${TextFormatting.GREEN}loaded${TextFormatting.RESET}" : ""} dist $TextFormatting.AQUA${String.format("%.2f", it.pos().distance(it.target().pos()) * 16)}"
            }
        }
    }
} catch (Exception ignored) {}

this.renderList(debug)

void renderList(List<String> list) {
    FontRenderer font = Minecraft.minecraft.fontRenderer;
    int id = 0

    list.each {
        font.drawStringWithShadow(it, 2, id * (font.FONT_HEIGHT + 1) + 3, 14737632); id++
    }
}

String formatPos(IVec2DC vec) {
    return "[${String.format("%.2f", vec.x())}, ${String.format("%.2f", vec.y())}]"
}

String formatPos(BlockPos vec) {
    return "($vec.x, $vec.y, $vec.z)"
}

TextFormatting color(double tps) {
    if (tps == 20) {
        return TextFormatting.GREEN
    } else if (tps < 20 && tps > 15) {
        return TextFormatting.YELLOW
    } else {
        return TextFormatting.RED
    }
}

double time(int avg) {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    long[] avgArr = new long[avg]
    for (i in 0..<avg) {
        avgArr[i] = server.tickTimeArray[(server.tickCounter - i) % 100]
    }
    return Math.min(20.0, 1000.0 / (MathHelper.average(avgArr) * 1.0E-6D))
}

double min(int range) {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    long[] avgArr = new long[range]
    for (i in 0..<range) {
        avgArr[i] = server.tickTimeArray[(server.tickCounter - i) % 100]
    }
    return Math.min(20.0, 1000.0 / (avgArr.toList().sort()[avgArr.length - 1] * 1.0E-6D))
}