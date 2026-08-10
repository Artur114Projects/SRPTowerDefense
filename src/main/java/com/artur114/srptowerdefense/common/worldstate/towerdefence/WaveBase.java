package com.artur114.srptowerdefense.common.worldstate.towerdefence;

import com.artur114.bananalib.math.m2d.vec.IVec2I;

public class WaveBase extends WaveAbstract {
    public WaveBase() {}

    public WaveBase(IVec2I pos, IWaveTarget target) {
        super(pos, target, 1.0F);
    }
}
