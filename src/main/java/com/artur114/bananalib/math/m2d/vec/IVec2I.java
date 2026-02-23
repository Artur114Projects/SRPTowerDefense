package com.artur114.bananalib.math.m2d.vec;

public interface IVec2I {
    float distance(int x, int y);
    float distance(IVec2I vec);
    float distanceSq(int x, int y);
    float distanceSq(IVec2I vec);
    IVec2I add(int x, int y);
    IVec2I add(IVec2I vec);
    IVec2I subtract(int x, int y);
    IVec2I subtract(IVec2I vec);
    IVec2I scale(int x, int y);
    IVec2I scale(int val);
    IVec2I scale(IVec2I vec);
    IVec2I toImmutable();
    IVec2D normalize();
    IVec2D toD();
    float lengthSq();
    float length();
    int x();
    int y();
}
