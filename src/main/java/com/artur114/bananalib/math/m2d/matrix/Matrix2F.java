package com.artur114.bananalib.math.m2d.matrix;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.*;
import com.artur114.bananalib.math.m2d.vec.*;

import java.nio.FloatBuffer;
import java.util.Objects;

public class Matrix2F implements IMatrix2F {
    public static final Matrix2F IDENTITY = new Matrix2F();
    private final float m00, m01, m02;
    private final float m10, m11, m12;
    private final float det;

    public Matrix2F() {
        this.m00 = 1.0F; this.m01 = 0.0F; this.m02 = 0.0F;
        this.m10 = 0.0F; this.m11 = 1.0F; this.m12 = 0.0F;
        this.det = 1.0F;
    }

    public Matrix2F(float m00, float m01, float m02, float m10, float m11, float m12) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.det = this.m00 * this.m11 - this.m01 * this.m10;
    }

    public Matrix2F(IMatrix2D m) {
        this.m00 = (float) m.m00(); this.m01 = (float) m.m01(); this.m02 = (float) m.m02();
        this.m10 = (float) m.m10(); this.m11 = (float) m.m11(); this.m12 = (float) m.m12();
        this.det = this.m00 * this.m11 - this.m01 * this.m10;
    }

    public Matrix2F(IMatrix2F m) {
        this.m00 = m.m00(); this.m01 = m.m01(); this.m02 = m.m02();
        this.m10 = m.m10(); this.m11 = m.m11(); this.m12 = m.m12();
        this.det = this.m00 * this.m11 - this.m01 * this.m10;
    }

    public Matrix2F(FloatBuffer buf) {
        this.m00 = buf.get(); this.m01 = buf.get(); this.m02 = buf.get();
        this.m10 = buf.get(); this.m11 = buf.get(); this.m12 = buf.get();
        this.det = this.m00 * this.m11 - this.m01 * this.m10;
    }

    @Override
    public float m00() {
        return this.m00;
    }

    @Override
    public float m01() {
        return this.m01;
    }

    @Override
    public float m02() {
        return this.m02;
    }

    @Override
    public float m10() {
        return this.m10;
    }

    @Override
    public float m11() {
        return this.m11;
    }

    @Override
    public float m12() {
        return this.m12;
    }

    @Override
    public float m20() {
        return 0.0F;
    }

    @Override
    public float m21() {
        return 0.0F;
    }

    @Override
    public float m22() {
        return 1.0F;
    }

    @Override
    public float determinant() {
        return this.det;
    }

    @Override
    public boolean isReversible() {
        return Math.abs(this.det) > BananaMath.FLOAT_EPS;
    }

    @Override
    public IMatrix2F invert() {
        if (!this.isReversible()) throw new ArithmeticException("Couldn't invert unreversible matrix");
        float detInv = 1.0F / this.det;
        return new Matrix2F(
                this.m11 * detInv, -this.m01 * detInv, (this.m01 * this.m12 - this.m11 * this.m02) * detInv,
                -this.m10 * detInv, this.m00 * detInv, -(this.m00 * this.m12 - this.m10 * this.m02) * detInv
        );
    }

    @Override
    public IMatrix2F mul(IMatrix2F matrix) {
        float m1v00 = matrix.m00(), m1v01 = matrix.m01(), m1v02 = matrix.m02();
        float m1v10 = matrix.m10(), m1v11 = matrix.m11(), m1v12 = matrix.m12();
        float m2v00 = this.m00, m2v01 = this.m01, m2v02 = this.m02;
        float m2v10 = this.m10, m2v11 = this.m11, m2v12 = this.m12;
        return new Matrix2F(
                (m1v00 * m2v00) + (m1v01 * m2v10),
                (m1v00 * m2v01) + (m1v01 * m2v11),
                (m1v00 * m2v02) + (m1v01 * m2v12) + (m1v02),

                (m1v10 * m2v00) + (m1v11 * m2v10),
                (m1v10 * m2v01) + (m1v11 * m2v11),
                (m1v10 * m2v02) + (m1v11 * m2v12) + (m1v12)
        );
    }

    @Override
    public IMatrix2F mul(IMatrix2D matrix) {
        float m1v00 = (float) matrix.m00(), m1v01 = (float) matrix.m01(), m1v02 = (float) matrix.m02();
        float m1v10 = (float) matrix.m10(), m1v11 = (float) matrix.m11(), m1v12 = (float) matrix.m12();
        float m2v00 = this.m00, m2v01 = this.m01, m2v02 = this.m02;
        float m2v10 = this.m10, m2v11 = this.m11, m2v12 = this.m12;
        return new Matrix2F(
                (m1v00 * m2v00) + (m1v01 * m2v10),
                (m1v00 * m2v01) + (m1v01 * m2v11),
                (m1v00 * m2v02) + (m1v01 * m2v12) + (m1v02),

                (m1v10 * m2v00) + (m1v11 * m2v10),
                (m1v10 * m2v01) + (m1v11 * m2v11),
                (m1v10 * m2v02) + (m1v11 * m2v12) + (m1v12)
        );
    }

    @Override
    public IMatrix2F mulPost(IMatrix2F matrix) {
        float m2v00 = matrix.m00(), m2v01 = matrix.m01(), m2v02 = matrix.m02();
        float m2v10 = matrix.m10(), m2v11 = matrix.m11(), m2v12 = matrix.m12();
        float m1v00 = this.m00;
        float m1v01 = this.m01;
        float m1v10 = this.m10;
        float m1v11 = this.m11;
        return new Matrix2F(
                (m1v00 * m2v00) + (m1v01 * m2v10),
                (m1v00 * m2v01) + (m1v01 * m2v11),
                (m1v00 * m2v02) + (m1v01 * m2v12) + (this.m02),

                (m1v10 * m2v00) + (m1v11 * m2v10),
                (m1v10 * m2v01) + (m1v11 * m2v11),
                (m1v10 * m2v02) + (m1v11 * m2v12) + (this.m12)
        );
    }

    @Override
    public IMatrix2F mulPost(IMatrix2D matrix) {
        float m2v00 = (float) matrix.m00(), m2v01 = (float) matrix.m01(), m2v02 = (float) matrix.m02();
        float m2v10 = (float) matrix.m10(), m2v11 = (float) matrix.m11(), m2v12 = (float) matrix.m12();
        float m1v00 = this.m00;
        float m1v01 = this.m01;
        float m1v10 = this.m10;
        float m1v11 = this.m11;
        return new Matrix2F(
                (m1v00 * m2v00) + (m1v01 * m2v10),
                (m1v00 * m2v01) + (m1v01 * m2v11),
                (m1v00 * m2v02) + (m1v01 * m2v12) + (this.m02),

                (m1v10 * m2v00) + (m1v11 * m2v10),
                (m1v10 * m2v01) + (m1v11 * m2v11),
                (m1v10 * m2v02) + (m1v11 * m2v12) + (this.m12)
        );
    }

    @Override
    public IMatrix2F scale(int x, int y) {
        return new Matrix2F(
                x * this.m00, x * this.m01, x * this.m02,
                y * this.m10, y * this.m11, y * this.m12
        );
    }

    @Override
    public IMatrix2F scale(float x, float y) {
        return new Matrix2F(
                x * this.m00, x * this.m01, x * this.m02,
                y * this.m10, y * this.m11, y * this.m12
        );
    }

    @Override
    public IMatrix2F scale(double x, double y) {
        return this.scale((float) x, (float) y);
    }

    @Override
    public IMatrix2F scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IMatrix2F scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IMatrix2F translate(int x, int y) {
        return new Matrix2F(
                this.m00, this.m01, this.m02 + x,
                this.m10, this.m11, this.m12 + y
        );
    }

    @Override
    public IMatrix2F translate(float x, float y) {
        return new Matrix2F(
                this.m00, this.m01, this.m02 + x,
                this.m10, this.m11, this.m12 + y
        );
    }

    @Override
    public IMatrix2F translate(double x, double y) {
        return this.translate((float) x, (float) y);
    }

    @Override
    public IMatrix2F translate(IVec2D vec) {
        return this.translate(vec.x(), vec.y());
    }

    @Override
    public IMatrix2F translate(IVec2I vec) {
        return this.translate(vec.x(), vec.y());
    }

    @Override
    public IMatrix2F rotate(float degrees) {
        float rads = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(rads);
        float cos = (float) Math.cos(rads);
        return new Matrix2F(
                (cos * this.m00) + (sin * this.m10), (cos * this.m01) + (sin * this.m11), (cos * this.m02) + (sin * this.m12),
                (-sin * this.m00) + (cos * this.m10), (-sin * this.m01) + (cos * this.m11), (-sin * this.m02) + (cos * this.m12)
        );
    }

    @Override
    public IMatrix2F rotateAround(int x, int y, float degrees) {
        float rads = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(rads);
        float cos = (float) Math.cos(rads);

        float m00p = this.m00, m01p = this.m01, m02p = this.m02 - x;
        float m10p = this.m10, m11p = this.m11, m12p = this.m12 - y;

        float m00r = (cos * m00p) + (sin * m10p), m01r = (cos * m01p) + (sin * m11p), m02r = (cos * m02p) + (sin * m12p);
        float m10r = (-sin * m00p) + (cos * m10p), m11r = (-sin * m01p) + (cos * m11p), m12r = (-sin * m02p) + (cos * m12p);

        return new Matrix2F(
                m00r, m01r, m02r + x,
                m10r, m11r, m12r + y
        );
    }

    @Override
    public IMatrix2F rotateAround(float x, float y, float degrees) {
        float rads = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(rads);
        float cos = (float) Math.cos(rads);

        float m00p = this.m00, m01p = this.m01, m02p = this.m02 - x;
        float m10p = this.m10, m11p = this.m11, m12p = this.m12 - y;

        float m00r = (cos * m00p) + (sin * m10p), m01r = (cos * m01p) + (sin * m11p), m02r = (cos * m02p) + (sin * m12p);
        float m10r = (-sin * m00p) + (cos * m10p), m11r = (-sin * m01p) + (cos * m11p), m12r = (-sin * m02p) + (cos * m12p);

        return new Matrix2F(
                m00r, m01r, m02r + x,
                m10r, m11r, m12r + y
        );
    }

    @Override
    public IMatrix2F rotateAround(double x, double y, float degrees) {
        return this.rotateAround((float) x, (float) y, degrees);
    }

    @Override
    public IMatrix2F rotateAround(IVec2D point, float degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IMatrix2F rotateAround(IVec2I point, float degrees) {
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
    public IVec2D transform(float x, float y) {
        return new Vec2D(
                (this.m00 * x) + (this.m01 * y) + (this.m02),
                (this.m10 * x) + (this.m11 * y) + (this.m12)
        );
    }

    @Override
    public IVec2D transform(double x, double y) {
        return this.transform((float) x, (float) y);
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
    public FloatBuffer writeToBuffer(FloatBuffer buf) {
        buf.put(this.m00);
        buf.put(this.m01);
        buf.put(this.m02);
        buf.put(this.m10);
        buf.put(this.m11);
        buf.put(this.m12);
        buf.put(0.0F);
        buf.put(0.0F);
        buf.put(1.0F);
        return buf;
    }

    @Override
    public IMatrix2FM toMutable() {
        return new Matrix2FM(this);
    }

    @Override
    public IMatrix2F toImmutable() {
        return this;
    }

    @Override
    public IMatrix2D toDouble() {
        return new Matrix2D(this);
    }

    @Override
    public String toString() {
        return "[" + this.m00 + ", " + this.m01 + ", " + this.m02 + "]\n[" + this.m10 + ", " + this.m11 + ", " + this.m12 + "]\n[" + 0.0 + ", " + 0.0 + ", " + 1.0 + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IMatrix2F)) {
            return false;
        }
        IMatrix2F m = ((IMatrix2F) obj);
        return
                m.m00() == this.m00 && m.m01() == this.m01 && m.m02() == this.m02 &&
                m.m10() == this.m10 && m.m11() == this.m11 && m.m12() == this.m12 &&
                m.m20() == 0.0F && m.m21() == 0.0F && m.m22() == 1.0F;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, 0.0F, 0.0F, 1.0F);
    }
}