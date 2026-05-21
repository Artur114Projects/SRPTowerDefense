package com.artur114.bananalib.math.m2d.matrix;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.*;
import com.artur114.bananalib.math.m2d.vec.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

public class Matrix2D implements IMatrix2D {
    public static final Matrix2D IDENTITY = new Matrix2D();
    private final double m00, m01, m02;
    private final double m10, m11, m12;
    private final double det;

    public Matrix2D() {
        this.m00 = 1.0D; this.m01 = 0.0D; this.m02 = 0.0D;
        this.m10 = 0.0D; this.m11 = 1.0D; this.m12 = 0.0D;
        this.det = 1.0D;
    }

    public Matrix2D(double m00, double m01, double m02, double m10, double m11, double m12) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.det = this.m00 * this.m11 - this.m01 * this.m10;
    }

    public Matrix2D(IMatrix2D m) {
        this.m00 = m.m00(); this.m01 = m.m01(); this.m02 = m.m02();
        this.m10 = m.m10(); this.m11 = m.m11(); this.m12 = m.m12();
        this.det = this.m00 * this.m11 - this.m01 * this.m10;
    }

    public Matrix2D(IMatrix2F m) {
        this.m00 = m.m00(); this.m01 = m.m01(); this.m02 = m.m02();
        this.m10 = m.m10(); this.m11 = m.m11(); this.m12 = m.m12();
        this.det = this.m00 * this.m11 - this.m01 * this.m10;
    }

    @Override
    public double m00() {
        return this.m00;
    }

    @Override
    public double m01() {
        return this.m01;
    }

    @Override
    public double m02() {
        return this.m02;
    }

    @Override
    public double m10() {
        return this.m10;
    }

    @Override
    public double m11() {
        return this.m11;
    }

    @Override
    public double m12() {
        return this.m12;
    }

    @Override
    public double m20() {
        return 0.0D;
    }

    @Override
    public double m21() {
        return 0.0D;
    }

    @Override
    public double m22() {
        return 1.0D;
    }

    @Override
    public double determinant() {
        return this.det;
    }

    @Override
    public boolean isReversible() {
        return Math.abs(this.det) > 1e-12;
    }

    @Override
    public IMatrix2D invert() {
        if (!this.isReversible()) throw new ArithmeticException("Couldn't invert unreversible matrix");
        double detInv = 1.0D / this.det;
        return new Matrix2D(
            this.m11 * detInv, -this.m01 * detInv, (this.m01 * this.m12 - this.m11 * this.m02) * detInv,
            -this.m10 * detInv, this.m00 * detInv, -(this.m00 * this.m12 - this.m10 * this.m02) * detInv
        );
    }

    @Override
    public IMatrix2D mul(IMatrix2D matrix) {
        double m1v00 = matrix.m00(), m1v01 = matrix.m01(), m1v02 = matrix.m02();
        double m1v10 = matrix.m10(), m1v11 = matrix.m11(), m1v12 = matrix.m12();
        double m2v00 = this.m00, m2v01 = this.m01, m2v02 = this.m02;
        double m2v10 = this.m10, m2v11 = this.m11, m2v12 = this.m12;
        return new Matrix2D(
                (m1v00 * m2v00) + (m1v01 * m2v10),
                (m1v00 * m2v01) + (m1v01 * m2v11),
                (m1v00 * m2v02) + (m1v01 * m2v12) + (m1v02),

                (m1v10 * m2v00) + (m1v11 * m2v10),
                (m1v10 * m2v01) + (m1v11 * m2v11),
                (m1v10 * m2v02) + (m1v11 * m2v12) + (m1v12)
        );
    }

    @Override
    public IMatrix2D mul(IMatrix2F matrix) {
        float m1v00 = matrix.m00(), m1v01 = matrix.m01(), m1v02 = matrix.m02();
        float m1v10 = matrix.m10(), m1v11 = matrix.m11(), m1v12 = matrix.m12();
        double m2v00 = this.m00, m2v01 = this.m01, m2v02 = this.m02;
        double m2v10 = this.m10, m2v11 = this.m11, m2v12 = this.m12;
        return new Matrix2D(
                (m1v00 * m2v00) + (m1v01 * m2v10),
                (m1v00 * m2v01) + (m1v01 * m2v11),
                (m1v00 * m2v02) + (m1v01 * m2v12) + (m1v02),

                (m1v10 * m2v00) + (m1v11 * m2v10),
                (m1v10 * m2v01) + (m1v11 * m2v11),
                (m1v10 * m2v02) + (m1v11 * m2v12) + (m1v12)
        );
    }

    @Override
    public IMatrix2D mulPost(IMatrix2D matrix) {
        double m2v00 = matrix.m00(), m2v01 = matrix.m01(), m2v02 = matrix.m02();
        double m2v10 = matrix.m10(), m2v11 = matrix.m11(), m2v12 = matrix.m12();
        double m1v00 = this.m00;
        double m1v01 = this.m01;
        double m1v10 = this.m10;
        double m1v11 = this.m11;
        return new Matrix2D(
                (m1v00 * m2v00) + (m1v01 * m2v10),
                (m1v00 * m2v01) + (m1v01 * m2v11),
                (m1v00 * m2v02) + (m1v01 * m2v12) + (this.m02),

                (m1v10 * m2v00) + (m1v11 * m2v10),
                (m1v10 * m2v01) + (m1v11 * m2v11),
                (m1v10 * m2v02) + (m1v11 * m2v12) + (this.m12)
        );

    }

    @Override
    public IMatrix2D mulPost(IMatrix2F matrix) {
        double m2v00 = matrix.m00(), m2v01 = matrix.m01(), m2v02 = matrix.m02();
        double m2v10 = matrix.m10(), m2v11 = matrix.m11(), m2v12 = matrix.m12();
        double m1v00 = this.m00;
        double m1v01 = this.m01;
        double m1v10 = this.m10;
        double m1v11 = this.m11;
        return new Matrix2D(
                (m1v00 * m2v00) + (m1v01 * m2v10),
                (m1v00 * m2v01) + (m1v01 * m2v11),
                (m1v00 * m2v02) + (m1v01 * m2v12) + (this.m02),

                (m1v10 * m2v00) + (m1v11 * m2v10),
                (m1v10 * m2v01) + (m1v11 * m2v11),
                (m1v10 * m2v02) + (m1v11 * m2v12) + (this.m12)
        );
    }

    @Override
    public IMatrix2D scale(int x, int y) {
        return new Matrix2D(
                x * this.m00, x * this.m01, x * this.m02,
                y * this.m10, y * this.m11, y * this.m12
        );
    }

    @Override
    public IMatrix2D scale(double x, double y) {
        return new Matrix2D(
                x * this.m00, x * this.m01, x * this.m02,
                y * this.m10, y * this.m11, y * this.m12
        );
    }

    @Override
    public IMatrix2D scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IMatrix2D scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IMatrix2D translate(int x, int y) {
        return new Matrix2D(
                this.m00, this.m01, this.m02 + x,
                this.m10, this.m11, this.m12 + y
        );
    }

    @Override
    public IMatrix2D translate(double x, double y) {
        return new Matrix2D(
                this.m00, this.m01, this.m02 + x,
                this.m10, this.m11, this.m12 + y
        );
    }

    @Override
    public IMatrix2D translate(IVec2D vec) {
        return this.translate(vec.x(), vec.y());
    }

    @Override
    public IMatrix2D translate(IVec2I vec) {
        return this.translate(vec.x(), vec.y());
    }

    @Override
    public IMatrix2D rotate(double degrees) {
        double rads = Math.toRadians(degrees);
        double sin = Math.sin(rads);
        double cos = Math.cos(rads);
        return new Matrix2D(
                (cos * this.m00) + (sin * this.m10), (cos * this.m01) + (sin * this.m11), (cos * this.m02) + (sin * this.m12),
                (-sin * this.m00) + (cos * this.m10), (-sin * this.m01) + (cos * this.m11), (-sin * this.m02) + (cos * this.m12)
        );
    }

    @Override
    public IMatrix2D rotateAround(int x, int y, double degrees) {
        double rads = Math.toRadians(degrees);
        double sin = Math.sin(rads);
        double cos = Math.cos(rads);

        double m00p = this.m00, m01p = this.m01, m02p = this.m02 - x;
        double m10p = this.m10, m11p = this.m11, m12p = this.m12 - y;

        double m00r = (cos * m00p) + (sin * m10p), m01r = (cos * m01p) + (sin * m11p), m02r = (cos * m02p) + (sin * m12p);
        double m10r = (-sin * m00p) + (cos * m10p), m11r = (-sin * m01p) + (cos * m11p), m12r = (-sin * m02p) + (cos * m12p);

        return new Matrix2D(
                m00r, m01r, m02r + x,
                m10r, m11r, m12r + y
        );
    }

    @Override
    public IMatrix2D rotateAround(double x, double y, double degrees) {
        double rads = Math.toRadians(degrees);
        double sin = Math.sin(rads);
        double cos = Math.cos(rads);

        double m00p = this.m00, m01p = this.m01, m02p = this.m02 - x;
        double m10p = this.m10, m11p = this.m11, m12p = this.m12 - y;

        double m00r = (cos * m00p) + (sin * m10p), m01r = (cos * m01p) + (sin * m11p), m02r = (cos * m02p) + (sin * m12p);
        double m10r = (-sin * m00p) + (cos * m10p), m11r = (-sin * m01p) + (cos * m11p), m12r = (-sin * m02p) + (cos * m12p);

        return new Matrix2D(
                m00r, m01r, m02r + x,
                m10r, m11r, m12r + y
        );
    }

    @Override
    public IMatrix2D rotateAround(IVec2D point, double degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IMatrix2D rotateAround(IVec2I point, double degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IVec2I transform(int x, int y) {
        return new Vec2I(
            BananaMath.round((this.m00 * x) + (this.m01 * y) + (this.m02)),
            BananaMath.round((this.m10 * x) + (this.m11 * y) + (this.m12))
        );
    }

    @Override
    public IVec2D transform(double x, double y) {
        return new Vec2D(
            (this.m00 * x) + (this.m01 * y) + (this.m02),
            (this.m10 * x) + (this.m11 * y) + (this.m12)
        );
    }

    @Override
    public IVec2I transform(IVec2I vec) {
        return this.transform(vec.x(), vec.y());
    }

    @Override
    public IVec2IM transform(IVec2IM vec) {
        return vec.set(
            BananaMath.round((this.m00 * vec.x()) + (this.m01 * vec.y()) + (this.m02)),
            BananaMath.round((this.m10 * vec.x()) + (this.m11 * vec.y()) + (this.m12))
        );
    }

    @Override
    public IVec2D transform(IVec2D vec) {
        return this.transform(vec.x(), vec.y());
    }

    @Override
    public IVec2DM transform(IVec2DM vec) {
        return vec.set(
            (this.m00 * vec.x()) + (this.m01 * vec.y()) + (this.m02),
            (this.m10 * vec.x()) + (this.m11 * vec.y()) + (this.m12)
        );
    }

    @Override
    public IVec2I[] transform(IVec2I... vec) {
        IVec2I[] ret = new IVec2I[vec.length];
        for (int i = 0; i != vec.length; i++) {
            ret[i] = this.transform(vec[i]);
        }
        return ret;
    }

    @Override
    public IVec2IM[] transform(IVec2IM... vec) {
        IVec2IM[] ret = new IVec2IM[vec.length];
        for (int i = 0; i != vec.length; i++) {
            ret[i] = this.transform(vec[i]);
        }
        return ret;
    }

    @Override
    public IVec2D[] transform(IVec2D... vec) {
        IVec2D[] ret = new IVec2D[vec.length];
        for (int i = 0; i != vec.length; i++) {
            ret[i] = this.transform(vec[i]);
        }
        return ret;
    }

    @Override
    public IVec2DM[] transform(IVec2DM... vec) {
        IVec2DM[] ret = new IVec2DM[vec.length];
        for (int i = 0; i != vec.length; i++) {
            ret[i] = this.transform(vec[i]);
        }
        return ret;
    }

    @Override
    public IBox2I transform(IBox2I box) {
        return new Box2I(
            BananaMath.round((this.m00 * box.minX()) + (this.m01 * box.minY()) + (this.m02)),
            BananaMath.round((this.m10 * box.minX()) + (this.m11 * box.minY()) + (this.m12)),

            BananaMath.round((this.m00 * box.maxX()) + (this.m01 * box.maxY()) + (this.m02)),
            BananaMath.round((this.m10 * box.maxX()) + (this.m11 * box.maxY()) + (this.m12))
        );
    }

    @Override
    public IBox2IM transform(IBox2IM box) {
        return box.set(
            BananaMath.round((this.m00 * box.minX()) + (this.m01 * box.minY()) + (this.m02)),
            BananaMath.round((this.m10 * box.minX()) + (this.m11 * box.minY()) + (this.m12)),

            BananaMath.round((this.m00 * box.maxX()) + (this.m01 * box.maxY()) + (this.m02)),
            BananaMath.round((this.m10 * box.maxX()) + (this.m11 * box.maxY()) + (this.m12))
        );
    }

    @Override
    public IBox2D transform(IBox2D box) {
        return new Box2D(
            (this.m00 * box.minX()) + (this.m01 * box.minY()) + (this.m02),
            (this.m10 * box.minX()) + (this.m11 * box.minY()) + (this.m12),

            (this.m00 * box.maxX()) + (this.m01 * box.maxY()) + (this.m02),
            (this.m10 * box.maxX()) + (this.m11 * box.maxY()) + (this.m12)
        );
    }

    @Override
    public IBox2DM transform(IBox2DM box) {
        return box.set(
            (this.m00 * box.minX()) + (this.m01 * box.minY()) + (this.m02),
            (this.m10 * box.minX()) + (this.m11 * box.minY()) + (this.m12),

            (this.m00 * box.maxX()) + (this.m01 * box.maxY()) + (this.m02),
            (this.m10 * box.maxX()) + (this.m11 * box.maxY()) + (this.m12)
        );
    }

    @Override
    public IBox2I[] transform(IBox2I... box) {
        IBox2I[] ret = new IBox2I[box.length];
        for (int i = 0; i != box.length; i++) {
            ret[i] = this.transform(box[i]);
        }
        return ret;
    }

    @Override
    public IBox2IM[] transform(IBox2IM... box) {
        IBox2IM[] ret = new IBox2IM[box.length];
        for (int i = 0; i != box.length; i++) {
            ret[i] = this.transform(box[i]);
        }
        return ret;
    }

    @Override
    public IBox2D[] transform(IBox2D... box) {
        IBox2D[] ret = new IBox2D[box.length];
        for (int i = 0; i != box.length; i++) {
            ret[i] = this.transform(box[i]);
        }
        return ret;
    }

    @Override
    public IBox2DM[] transform(IBox2DM... box) {
        IBox2DM[] ret = new IBox2DM[box.length];
        for (int i = 0; i != box.length; i++) {
            ret[i] = this.transform(box[i]);
        }
        return ret;
    }

    @Override
    public DoubleBuffer writeToBuffer(DoubleBuffer buf) {
        buf.put(this.m00);
        buf.put(this.m01);
        buf.put(this.m02);
        buf.put(this.m10);
        buf.put(this.m11);
        buf.put(this.m12);
        buf.put(0.0F);
        buf.put(0.0F);
        buf.put(1.0F);
        buf.flip();
        return buf;
    }

    @Override
    public IMatrix2D toImmutable() {
        return this;
    }

    @Override
    public IMatrix2F toFloat() {
        return new Matrix2F(this);
    }
}
