package com.artur114.bananalib.math.m2d.matrix;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IMatrix2FM extends IMatrix2F {
    IMatrix2FM set(float m00, float m01, float m02, float m10, float m11, float m12);
    IMatrix2FM set(float[] ms);
    IMatrix2FM setM00(float m00);
    IMatrix2FM setM01(float m01);
    IMatrix2FM setM02(float m02);
    IMatrix2FM setM10(float m10);
    IMatrix2FM setM11(float m11);
    IMatrix2FM setM12(float m12);
    IMatrix2FM setIdentity();
    IMatrix2FM collapseStack();
    IMatrix2FM resetStack();
    IMatrix2FM pushMatrix();
    IMatrix2FM popMatrix();
    IMatrix2FM invert();
    IMatrix2FM mul(IMatrix2F matrix);
    IMatrix2FM mul(IMatrix2D matrix);
    IMatrix2FM mulPost(IMatrix2F matrix);
    IMatrix2FM mulPost(IMatrix2D matrix);
    IMatrix2FM scale(int x, int y);
    IMatrix2FM scale(float x, float y);
    IMatrix2FM scale(double x, double y);
    IMatrix2FM scale(IVec2D vec);
    IMatrix2FM scale(IVec2I vec);
    IMatrix2FM translate(int x, int y);
    IMatrix2FM translate(float x, float y);
    IMatrix2FM translate(double x, double y);
    IMatrix2FM translate(IVec2D vec);
    IMatrix2FM translate(IVec2I vec);
    IMatrix2FM rotate(float degrees);
    IMatrix2FM rotateAround(int x, int y, float degrees);
    IMatrix2FM rotateAround(float x, float y, float degrees);
    IMatrix2FM rotateAround(double x, double y, float degrees);
    IMatrix2FM rotateAround(IVec2D point, float degrees);
    IMatrix2FM rotateAround(IVec2I point, float degrees);
    IMatrix2DM toDouble();
}
