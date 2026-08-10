package scripts

import groovy.transform.BaseScript
import net.minecraft.util.math.BlockPos
import scripts.classes.BaseDevScript

@BaseScript
BaseDevScript script
BlockPos pos = posIn

world.newExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 20, true, true);

int radius = (int) (128 / 8)

for (y in -1..1) {
    for (i in 0..<radius) {
        this.expRing(pos.up(y), 8 * i, 8 * i / 2 as int)
    }
}

void expRing(BlockPos pos, int range, int count = 8) {
    for (i in 0..<count) {
        def delta = vec3d(range, 0, 0).rotateY(360 * (i / count))

        world.newExplosion(null, pos.getX() + delta.x, pos.getY(), pos.getZ() + delta.z, 10, false, true);
    }
}