package com.artur114.bananalib.math.m2d.vec;

public interface IVec2DM extends IVec2D {
    IVec2DM set(double[] pos);
    IVec2DM set(double x, double y);
    IVec2DM set(int x, int y);
    IVec2DM set(IVec2D vec);
    IVec2DM set(IVec2I vec);
    IVec2DM setX(int x);
    IVec2DM setX(double x);
    IVec2DM setY(int y);
    IVec2DM setY(double y);
    IVec2DM setZero();
    IVec2DM collapseStack();
    IVec2DM resetStack();
    IVec2DM pushPos();
    IVec2DM popPos();
    IVec2DM add(int x, int y);
    IVec2DM add(double x, double y);
    IVec2DM add(IVec2I vec);
    IVec2DM add(IVec2D vec);
    IVec2DM subtract(int x, int y);
    IVec2DM subtract(double x, double y);
    IVec2DM subtract(IVec2I vec);
    IVec2DM subtract(IVec2D vec);
    IVec2DM scale(int val);
    IVec2DM scale(int x, int y);
    IVec2DM scale(double val);
    IVec2DM scale(double x, double y);
    IVec2DM scale(IVec2I vec);
    IVec2DM scale(IVec2D vec);
    IVec2DM rotate(double degrees);
    IVec2DM rotateAround(int x, int y, double degrees);
    IVec2DM rotateAround(double x, double y, double degrees);
    IVec2DM rotateAround(IVec2D point, double degrees);
    IVec2DM rotateAround(IVec2I point, double degrees);
    IVec2DM normalize();
    IVec2IM toInt();
}
