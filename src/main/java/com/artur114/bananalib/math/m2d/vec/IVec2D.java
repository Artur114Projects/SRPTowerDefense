package com.artur114.bananalib.math.m2d.vec;

public interface IVec2D {
    double x();
    double y();
    double length();
    double lengthSq();
    double distance(int x, int y);
    double distance(double x, double y);
    double distance(IVec2I vec);
    double distance(IVec2D vec);
    double distanceSq(int x, int y);
    double distanceSq(double x, double y);
    double distanceSq(IVec2I vec);
    double distanceSq(IVec2D vec);
    IVec2D add(int x, int y);
    IVec2D add(double x, double y);
    IVec2D add(IVec2I vec);
    IVec2D add(IVec2D vec);
    IVec2D subtract(int x, int y);
    IVec2D subtract(double x, double y);
    IVec2D subtract(IVec2I vec);
    IVec2D subtract(IVec2D vec);
    IVec2D scale(int val);
    IVec2D scale(int x, int y);
    IVec2D scale(double val);
    IVec2D scale(double x, double y);
    IVec2D scale(IVec2I vec);
    IVec2D scale(IVec2D vec);
    IVec2D normalize();
    IVec2DM toMutable();
    IVec2D toImmutable();
    IVec2I toInt();
}
