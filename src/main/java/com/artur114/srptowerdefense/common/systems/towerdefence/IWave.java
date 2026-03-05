package com.artur114.srptowerdefense.common.systems.towerdefence;

import com.artur114.bananalib.math.m2d.vec.IVec2D;

public interface IWave extends ITowerDefenceObject, ITDEntityManager {
    float speed();
    IVec2D targetChunk();
    IWaveTarget target();
}
