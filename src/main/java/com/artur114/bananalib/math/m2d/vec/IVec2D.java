package com.artur114.bananalib.math.m2d.vec;

public interface IVec2D {
    double distance(double x, double y);
    double distance(IVec2D vec);
    double distanceSq(double x, double y);
    double distanceSq(IVec2D vec);
    IVec2D add(double x, double y);
    IVec2D add(IVec2D vec);
    IVec2D subtract(double x, double y);
    IVec2D subtract(IVec2D vec);
    IVec2D scale(double x, double y);
    IVec2D scale(double val);
    IVec2D scale(IVec2D vec);
    IVec2D toImmutable();
    IVec2D normalize();
    IVec2I toI();
    double lengthSq();
    double length();
    double x();
    double y();
}
