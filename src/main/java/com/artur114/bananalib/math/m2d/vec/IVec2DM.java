package com.artur114.bananalib.math.m2d.vec;

public interface IVec2DM extends IVec2D {
    IVec2DM set(double x, double y);
    IVec2DM set(IVec2D vec);
    IVec2DM add(double x, double y);
    IVec2DM add(IVec2D vec);
    IVec2DM subtract(double x, double y);
    IVec2DM subtract(IVec2D vec);
    IVec2DM scale(double x, double y);
    IVec2DM scale(IVec2D vec);
    IVec2DM normalize();
    IVec2IM toI();
}
