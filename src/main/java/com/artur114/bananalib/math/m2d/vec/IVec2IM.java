package com.artur114.bananalib.math.m2d.vec;

public interface IVec2IM extends IVec2I {
    IVec2IM set(int[] pos);
    IVec2IM set(double x, double y);
    IVec2IM set(int x, int y);
    IVec2IM set(IVec2D vec);
    IVec2IM set(IVec2I vec);
    IVec2IM setZero();
    IVec2IM collapseStack();
    IVec2IM resetStack();
    IVec2IM pushPos();
    IVec2IM popPos();
    IVec2IM add(int x, int y);
    IVec2IM add(double x, double y);
    IVec2IM add(IVec2I vec);
    IVec2IM add(IVec2D vec);
    IVec2IM subtract(int x, int y);
    IVec2IM subtract(double x, double y);
    IVec2IM subtract(IVec2I vec);
    IVec2IM subtract(IVec2D vec);
    IVec2IM scale(int val);
    IVec2IM scale(int x, int y);
    IVec2IM scale(double val);
    IVec2IM scale(double x, double y);
    IVec2IM scale(IVec2I vec);
    IVec2IM scale(IVec2D vec);
    IVec2DM normalize();
    IVec2DM toDouble();
}
