package scripts

import com.artur114.bananalib.math.m3d.box.Box3D
import com.artur114.bananalib.mc.BananaMC
import com.artur114.bananalib.mc.cap.BananaCaps
import com.artur114.bananalib.mc.math.m2d.vec.PosMc2I
import com.artur114.srptowerdefense.common.init.InitCapabilities
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ITowerDefenceObject
import com.artur114.srptowerdefense.common.worldstate.towerdefence.IWave
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ProtectedZone
import groovy.transform.BaseScript
import net.minecraft.server.MinecraftServer
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

//            if (it instanceof ProtectedZone) {
//                try {
//                    it.noSpawnChunks.each {
//                        PosMc2I pos = BananaMC.chunkPosBFromLong(it)
//                        drawBox(new Box3D((pos.xzy(0) * 16).add(8, 0, 8), (pos.xzy(255) * 16).add(8, 0, 8)), Color.PINK.darker().darker())
//                    }
//
//                    it.forcedChunks.each {
//                        PosMc2I pos = BananaMC.chunkPosBFromLong(it)
//                        drawBox(new Box3D((pos.xzy(0) * 16).add(8, 0, 8), (pos.xzy(255) * 16).add(8, 0, 8)), Color.PINK.darker())
//                    }
//                    it.protectedChunks.each {
//                        PosMc2I pos = BananaMC.chunkPosBFromLong(it)
//                        drawBox(new Box3D((pos.xzy(0) * 16).add(8, 0, 8), (pos.xzy(255) * 16).add(8, 0, 8)), Color.PINK)
//                    }
//                } catch (Exception ignored) {}
//            }
        }
    }
//    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance()
//    if (server != null) {
//        server.getWorld(player.dimension).getPersistentChunks().forEach {ChunkPos chunkPos, Ticket ticket ->
//            PosMc2I pos = new PosMc2I(chunkPos.x, chunkPos.z)
//            drawBox(new Box3D((pos.xzy(0) * 16).add(8, 0, 8), (pos.xzy(255) * 16).add(8, 0, 8)), Color.BLACK)
//        }
//    }
}