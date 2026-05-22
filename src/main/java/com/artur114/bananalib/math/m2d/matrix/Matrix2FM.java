package com.artur114.bananalib.math.m2d.matrix;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.*;
import com.artur114.bananalib.math.m2d.vec.*;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Objects;

public class Matrix2FM implements IMatrix2FM {
    private static final ThreadLocal<Matrix2FM[]> pool = ThreadLocal.withInitial(() -> new Matrix2FM[16]);
    private static final ThreadLocal<Integer> poolCursor = ThreadLocal.withInitial(() -> -1);

    public static Matrix2FM obtain() {
        Matrix2FM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor < 0) {
            return new Matrix2FM();
        }

        Matrix2FM matrix = pol[polCursor];
        pol[polCursor--] = null;
        poolCursor.set(polCursor);

        if (matrix == null) {
            return new Matrix2FM();
        }

        matrix.released = false;
        return matrix;
    }

    public static void release(Matrix2FM matrix) {
        if (matrix == null) {
            return;
        }
        if (matrix.released) {
            throw new IllegalArgumentException("Double release!");
        }

        Matrix2FM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor + 1 >= pol.length) {
            pol = Arrays.copyOf(pol, pol.length * 2);
            pool.set(pol);
        }

        matrix.released = true;
        matrix.resetStack().setIdentity();
        pol[++polCursor] = matrix;
        poolCursor.set(polCursor);
    }

    private static final float EPS = 1e-6F;
    private float[][] stateStack = null;
    private int stateCursor = 0;
    private float m00, m01, m02;
    private float m10, m11, m12;
    private boolean released;

    public Matrix2FM() {
        this.m00 = 1.0F; this.m01 = 0.0F; this.m02 = 0.0F;
        this.m10 = 0.0F; this.m11 = 1.0F; this.m12 = 0.0F;
    }

    public Matrix2FM(double m00, double m01, double m02, double m10, double m11, double m12) {
        this.m00 = (float) m00; this.m01 = (float) m01; this.m02 = (float) m02;
        this.m10 = (float) m10; this.m11 = (float) m11; this.m12 = (float) m12;
    }

    public Matrix2FM(float m00, float m01, float m02, float m10, float m11, float m12) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
    }

    public Matrix2FM(IMatrix2D m) {
        this.m00 = (float) m.m00(); this.m01 = (float) m.m01(); this.m02 = (float) m.m02();
        this.m10 = (float) m.m10(); this.m11 = (float) m.m11(); this.m12 = (float) m.m12();
    }

    public Matrix2FM(IMatrix2F m) {
        this.m00 = m.m00(); this.m01 = m.m01(); this.m02 = m.m02();
        this.m10 = m.m10(); this.m11 = m.m11(); this.m12 = m.m12();
    }

    public Matrix2FM(FloatBuffer buf) {
        this.m00 = buf.get(); this.m01 = buf.get(); this.m02 = buf.get();
        this.m10 = buf.get(); this.m11 = buf.get(); this.m12 = buf.get();
    }

    @Override
    public IMatrix2FM set(float m00, float m01, float m02, float m10, float m11, float m12) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        return this;
    }

    @Override
    public IMatrix2FM set(float[] ms) {
        if (ms.length < 6) {
            throw new IllegalArgumentException();
        }
        this.m00 = ms[0];
        this.m01 = ms[1];
        this.m02 = ms[2];
        this.m10 = ms[3];
        this.m11 = ms[4];
        this.m12 = ms[5];
        return this;
    }

    @Override
    public IMatrix2FM setM00(float m00) {
        this.m00 = m00;
        return this;
    }

    @Override
    public IMatrix2FM setM01(float m01) {
        this.m01 = m01;
        return this;
    }

    @Override
    public IMatrix2FM setM02(float m02) {
        this.m02 = m02;
        return this;
    }

    @Override
    public IMatrix2FM setM10(float m10) {
        this.m10 = m10;
        return this;
    }

    @Override
    public IMatrix2FM setM11(float m11) {
        this.m11 = m11;
        return this;
    }

    @Override
    public IMatrix2FM setM12(float m12) {
        this.m12 = m12;
        return this;
    }

    @Override
    public IMatrix2FM setIdentity() {
        this.m00 = 1.0F; this.m01 = 0.0F; this.m02 = 0.0F;
        this.m10 = 0.0F; this.m11 = 1.0F; this.m12 = 0.0F;
        return this;
    }

    @Override
    public IMatrix2FM collapseStack() {
        this.stateStack = null;
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IMatrix2FM resetStack() {
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IMatrix2FM pushMatrix() {
        if (this.stateStack == null) {
            this.stateStack = new float[1][];
        }
        if (this.stateCursor >= this.stateStack.length) {
            this.stateStack = Arrays.copyOf(this.stateStack, this.stateCursor + 1);
        }
        float[] arr = this.stateStack[this.stateCursor++];

        if (arr == null) {
            arr = new float[6];
        }

        arr[0] = this.m00;
        arr[1] = this.m01;
        arr[2] = this.m02;
        arr[3] = this.m10;
        arr[4] = this.m11;
        arr[5] = this.m12;

        this.stateStack[this.stateCursor - 1] = arr;

        return this;
    }

    @Override
    public IMatrix2FM popMatrix() {
        if (this.stateCursor - 1 < 0) {
            throw new IllegalStateException();
        }
        return this.set(this.stateStack[--this.stateCursor]);
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
        return this.m00 * this.m11 - this.m01 * this.m10;
    }

    @Override
    public boolean isReversible() {
        return Math.abs(this.determinant()) > EPS;
    }

    @Override
    public IMatrix2FM invert() {
        float det = this.determinant();
        if (Math.abs(det) < EPS) throw new ArithmeticException("Couldn't invert unreversible matrix");
        float detInv = 1.0F / det;
        return this.set(
            this.m11 * detInv, -this.m01 * detInv, (this.m01 * this.m12 - this.m11 * this.m02) * detInv,
            -this.m10 * detInv, this.m00 * detInv, -(this.m00 * this.m12 - this.m10 * this.m02) * detInv
        );
    }

    @Override
    public IMatrix2FM mul(IMatrix2D matrix) {
        float m1v00 = (float) matrix.m00(), m1v01 = (float) matrix.m01(), m1v02 = (float) matrix.m02();
        float m1v10 = (float) matrix.m10(), m1v11 = (float) matrix.m11(), m1v12 = (float) matrix.m12();
        float m2v00 = this.m00, m2v01 = this.m01, m2v02 = this.m02;
        float m2v10 = this.m10, m2v11 = this.m11, m2v12 = this.m12;
        return this.set(
            (m1v00 * m2v00) + (m1v01 * m2v10),
            (m1v00 * m2v01) + (m1v01 * m2v11),
            (m1v00 * m2v02) + (m1v01 * m2v12) + (m1v02),

            (m1v10 * m2v00) + (m1v11 * m2v10),
            (m1v10 * m2v01) + (m1v11 * m2v11),
            (m1v10 * m2v02) + (m1v11 * m2v12) + (m1v12)
        );
    }

    @Override
    public IMatrix2FM mul(IMatrix2F matrix) {
        float m1v00 = matrix.m00(), m1v01 = matrix.m01(), m1v02 = matrix.m02();
        float m1v10 = matrix.m10(), m1v11 = matrix.m11(), m1v12 = matrix.m12();
        float m2v00 = this.m00, m2v01 = this.m01, m2v02 = this.m02;
        float m2v10 = this.m10, m2v11 = this.m11, m2v12 = this.m12;
        return this.set(
            (m1v00 * m2v00) + (m1v01 * m2v10),
            (m1v00 * m2v01) + (m1v01 * m2v11),
            (m1v00 * m2v02) + (m1v01 * m2v12) + (m1v02),

            (m1v10 * m2v00) + (m1v11 * m2v10),
            (m1v10 * m2v01) + (m1v11 * m2v11),
            (m1v10 * m2v02) + (m1v11 * m2v12) + (m1v12)
        );
    }

    @Override
    public IMatrix2FM mulPost(IMatrix2D matrix) {
        float m2v00 = (float) matrix.m00(), m2v01 = (float) matrix.m01(), m2v02 = (float) matrix.m02();
        float m2v10 = (float) matrix.m10(), m2v11 = (float) matrix.m11(), m2v12 = (float) matrix.m12();
        float m1v00 = this.m00;
        float m1v01 = this.m01;
        float m1v10 = this.m10;
        float m1v11 = this.m11;
        return this.set(
            (m1v00 * m2v00) + (m1v01 * m2v10),
            (m1v00 * m2v01) + (m1v01 * m2v11),
            (m1v00 * m2v02) + (m1v01 * m2v12) + (this.m02),

            (m1v10 * m2v00) + (m1v11 * m2v10),
            (m1v10 * m2v01) + (m1v11 * m2v11),
            (m1v10 * m2v02) + (m1v11 * m2v12) + (this.m12)
        );
    }

    @Override
    public IMatrix2FM mulPost(IMatrix2F matrix) {
        float m2v00 = matrix.m00(), m2v01 = matrix.m01(), m2v02 = matrix.m02();
        float m2v10 = matrix.m10(), m2v11 = matrix.m11(), m2v12 = matrix.m12();
        float m1v00 = this.m00;
        float m1v01 = this.m01;
        float m1v10 = this.m10;
        float m1v11 = this.m11;
        return this.set(
            (m1v00 * m2v00) + (m1v01 * m2v10),
            (m1v00 * m2v01) + (m1v01 * m2v11),
            (m1v00 * m2v02) + (m1v01 * m2v12) + (this.m02),

            (m1v10 * m2v00) + (m1v11 * m2v10),
            (m1v10 * m2v01) + (m1v11 * m2v11),
            (m1v10 * m2v02) + (m1v11 * m2v12) + (this.m12)
        );
    }

    @Override
    public IMatrix2FM scale(int x, int y) {
        return this.set(
            x * this.m00, x * this.m01, x * this.m02,
            y * this.m10, y * this.m11, y * this.m12
        );
    }

    @Override
    public IMatrix2FM scale(float x, float y) {
        return this.set(
            x * this.m00, x * this.m01, x * this.m02,
            y * this.m10, y * this.m11, y * this.m12
        );
    }

    @Override
    public IMatrix2FM scale(double x, double y) {
        return this.scale((float) x, (float) y);
    }

    @Override
    public IMatrix2FM scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IMatrix2FM scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IMatrix2FM translate(int x, int y) {
        this.m02 += x;
        this.m12 += y;
        return this;
    }

    @Override
    public IMatrix2FM translate(float x, float y) {
        this.m02 += x;
        this.m12 += y;
        return this;
    }

    @Override
    public IMatrix2FM translate(double x, double y) {
        return this.translate((float) x, (float) y);
    }

    @Override
    public IMatrix2FM translate(IVec2D vec) {
        return this.translate(vec.x(), vec.y());
    }

    @Override
    public IMatrix2FM translate(IVec2I vec) {
        return this.translate(vec.x(), vec.y());
    }

    @Override
    public IMatrix2FM rotate(float degrees) {
        float rads = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(rads);
        float cos = (float) Math.cos(rads);
        return this.set(
            (cos * this.m00) + (sin * this.m10), (cos * this.m01) + (sin * this.m11), (cos * this.m02) + (sin * this.m12),
            (-sin * this.m00) + (cos * this.m10), (-sin * this.m01) + (cos * this.m11), (-sin * this.m02) + (cos * this.m12)
        );
    }

    @Override
    public IMatrix2FM rotateAround(int x, int y, float degrees) {
        float rads = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(rads);
        float cos = (float) Math.cos(rads);

        float m00p = this.m00, m01p = this.m01, m02p = this.m02 - x;
        float m10p = this.m10, m11p = this.m11, m12p = this.m12 - y;

        float m00r = (cos * m00p) + (sin * m10p), m01r = (cos * m01p) + (sin * m11p), m02r = (cos * m02p) + (sin * m12p);
        float m10r = (-sin * m00p) + (cos * m10p), m11r = (-sin * m01p) + (cos * m11p), m12r = (-sin * m02p) + (cos * m12p);

        return this.set(m00r, m01r, m02r + x, m10r, m11r, m12r + y);
    }

    @Override
    public IMatrix2FM rotateAround(float x, float y, float degrees) {
        float rads = (float) Math.toRadians(degrees);
        float sin = (float) Math.sin(rads);
        float cos = (float) Math.cos(rads);

        float m00p = this.m00, m01p = this.m01, m02p = this.m02 - x;
        float m10p = this.m10, m11p = this.m11, m12p = this.m12 - y;

        float m00r = (cos * m00p) + (sin * m10p), m01r = (cos * m01p) + (sin * m11p), m02r = (cos * m02p) + (sin * m12p);
        float m10r = (-sin * m00p) + (cos * m10p), m11r = (-sin * m01p) + (cos * m11p), m12r = (-sin * m02p) + (cos * m12p);

        return this.set(m00r, m01r, m02r + x, m10r, m11r, m12r + y);
    }

    @Override
    public IMatrix2FM rotateAround(double x, double y, float degrees) {
        return this.rotateAround((float) x, (float) y, degrees);
    }

    @Override
    public IMatrix2FM rotateAround(IVec2D point, float degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IMatrix2FM rotateAround(IVec2I point, float degrees) {
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
        return this;
    }

    @Override
    public IMatrix2F toImmutable() {
        return new Matrix2F(this);
    }

    @Override
    public IMatrix2DM toDouble() {
        return new Matrix2DM(this);
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