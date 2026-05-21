package com.artur114.bananalib.math.m2d.matrix;

import com.artur114.bananalib.math.m2d.area.IBox2D;
import com.artur114.bananalib.math.m2d.area.IBox2DM;
import com.artur114.bananalib.math.m2d.area.IBox2I;
import com.artur114.bananalib.math.m2d.area.IBox2IM;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2DM;
import com.artur114.bananalib.math.m2d.vec.IVec2I;
import com.artur114.bananalib.math.m2d.vec.IVec2IM;

import java.nio.DoubleBuffer;


public interface IMatrix2D {
    double m00();
    double m01();
    double m02();
    double m10();
    double m11();
    double m12();
    double m20();
    double m21();
    double m22();
    IMatrix2D invert();
    double determinant();
    boolean isReversible();
    IMatrix2D mul(IMatrix2D matrix);
    IMatrix2D mul(IMatrix2F matrix);
    IMatrix2D mulPost(IMatrix2D matrix);
    IMatrix2D mulPost(IMatrix2F matrix);
    IMatrix2D scale(int x, int y);
    IMatrix2D scale(double x, double y);
    IMatrix2D scale(IVec2D vec);
    IMatrix2D scale(IVec2I vec);
    IMatrix2D translate(int x, int y);
    IMatrix2D translate(double x, double y);
    IMatrix2D translate(IVec2D vec);
    IMatrix2D translate(IVec2I vec);
    IMatrix2D rotate(double degrees);
    IMatrix2D rotateAround(int x, int y, double degrees);
    IMatrix2D rotateAround(double x, double y, double degrees);
    IMatrix2D rotateAround(IVec2D point, double degrees);
    IMatrix2D rotateAround(IVec2I point, double degrees);
    IVec2I transform(int x, int y);
    IVec2D transform(double x, double y);
    IVec2I transform(IVec2I vec);
    IVec2IM transform(IVec2IM vec);
    IVec2D transform(IVec2D vec);
    IVec2DM transform(IVec2DM vec);
    IVec2I[] transform(IVec2I... vec);
    IVec2IM[] transform(IVec2IM... vec);
    IVec2D[] transform(IVec2D... vec);
    IVec2DM[] transform(IVec2DM... vec);
    IBox2I transform(IBox2I box);
    IBox2IM transform(IBox2IM box);
    IBox2D transform(IBox2D box);
    IBox2DM transform(IBox2DM box);
    IBox2I[] transform(IBox2I... box);
    IBox2IM[] transform(IBox2IM... box);
    IBox2D[] transform(IBox2D... box);
    IBox2DM[] transform(IBox2DM... box);
    DoubleBuffer writeToBuffer(DoubleBuffer buf);
    IMatrix2D toImmutable();
    IMatrix2F toFloat();
}
