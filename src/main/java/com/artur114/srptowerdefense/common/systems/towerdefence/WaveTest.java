package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.vec.IVec2I;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import net.minecraft.util.math.BlockPos;

public class WaveTest extends WaveAbstract {
    public WaveTest(IVec2I pos) {
        super(pos, new BlockPos(8, 10, 8), 1.0F);

        this.addEntities(new EntityCreatorClass(EntityInfHuman.class), 16);
    }
}
