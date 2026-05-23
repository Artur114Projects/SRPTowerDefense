package com.artur114.bananalib.math.m2d.vec;

public interface IVec2I {
    int x();
    int y();
    float length();
    float lengthSq();
    float distance(int x, int y);
    float distance(double x, double y);
    float distance(IVec2I vec);
    float distance(IVec2D vec);
    float distanceSq(int x, int y);
    float distanceSq(double x, double y);
    float distanceSq(IVec2I vec);
    float distanceSq(IVec2D vec);
    IVec2I add(int x, int y);
    IVec2I add(double x, double y);
    IVec2I add(IVec2I vec);
    IVec2I add(IVec2D vec);
    IVec2I subtract(int x, int y);
    IVec2I subtract(double x, double y);
    IVec2I subtract(IVec2I vec);
    IVec2I subtract(IVec2D vec);
    IVec2I scale(int val);
    IVec2I scale(int x, int y);
    IVec2I scale(double val);
    IVec2I scale(double x, double y);
    IVec2I scale(IVec2I vec);
    IVec2I scale(IVec2D vec);
    IVec2D normalize();
    IVec2IM toMutable();
    IVec2I toImmutable();
    IVec2D toDouble();
}
