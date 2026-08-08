package scripts

import com.artur114.bananalib.math.m3d.box.Box3D
import com.artur114.bananalib.mc.cap.BananaCaps
import com.artur114.srptowerdefense.common.init.InitCapabilities
import com.artur114.srptowerdefense.common.worldstate.towerdefence.ITowerDefenceObject
import com.artur114.srptowerdefense.common.worldstate.towerdefence.IWave
import groovy.transform.BaseScript
import net.minecraftforge.fml.common.FMLCommonHandler
import scripts.classes.BaseDevScript

import java.awt.Color

@BaseScript
BaseDevScript script

prepareToDraw(4) {
    BananaCaps.capability(FMLCommonHandler.instance().minecraftServerInstance.getWorld(0), InitCapabilities.TOWER_DEFENCE_SYSTEM).ifPresent {
        it.tdObjects(ITowerDefenceObject).each {
            Color color = Color.BLUE

            if (it instanceof IWave) {
                color = Color.RED
            }

            drawBox(new Box3D(it.pos().toImmutable().xzy(0) * 16, it.pos().toImmutable().xzy(255) * 16), color)
        }
    }
}