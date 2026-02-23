package com.artur114.bananalib.math.m2d.vec;

public interface IVec2IM extends IVec2I {
    IVec2IM set(int x, int y);
    IVec2IM set(IVec2I vec);
    IVec2IM add(int x, int y);
    IVec2IM add(IVec2I vec);
    IVec2IM subtract(int x, int y);
    IVec2IM subtract(IVec2I vec);
    IVec2IM scale(int x, int y);
    IVec2IM scale(IVec2I vec);
    IVec2DM normalize();
    IVec2DM toD();
}
