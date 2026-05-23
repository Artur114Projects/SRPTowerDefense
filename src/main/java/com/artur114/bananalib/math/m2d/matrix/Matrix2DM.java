package com.artur114.bananalib.math.m2d.matrix;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.area.*;
import com.artur114.bananalib.math.m2d.vec.*;

import java.nio.DoubleBuffer;
import java.util.Arrays;
import java.util.Objects;

public class Matrix2DM implements IMatrix2DM {
    private static final ThreadLocal<Matrix2DM[]> pool = ThreadLocal.withInitial(() -> new Matrix2DM[16]);
    private static final ThreadLocal<Integer> poolCursor = ThreadLocal.withInitial(() -> -1);

    public static Matrix2DM obtain() {
        Matrix2DM[] pol = pool.get();
        int polCursor = poolCursor.get();

        if (polCursor < 0) {
            return new Matrix2DM();
        }

        Matrix2DM matrix = pol[polCursor];
        pol[polCursor--] = null;
        poolCursor.set(polCursor);

        if (matrix == null) {
            return new Matrix2DM();
        }

        matrix.released = false;
        return matrix;
    }

    public static void release(Matrix2DM matrix) {
        if (matrix == null) {
            return;
        }
        if (matrix.released) {
            throw new IllegalArgumentException("Double release!");
        }

        Matrix2DM[] pol = pool.get();
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

    private double[][] stateStack = null;
    private int stateCursor = 0;
    private double m00, m01, m02;
    private double m10, m11, m12;
    private boolean released;

    public Matrix2DM() {
        this.m00 = 1.0D; this.m01 = 0.0D; this.m02 = 0.0D;
        this.m10 = 0.0D; this.m11 = 1.0D; this.m12 = 0.0D;
    }

    public Matrix2DM(double m00, double m01, double m02, double m10, double m11, double m12) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
    }

    public Matrix2DM(IMatrix2D m) {
        this.m00 = m.m00(); this.m01 = m.m01(); this.m02 = m.m02();
        this.m10 = m.m10(); this.m11 = m.m11(); this.m12 = m.m12();
    }

    public Matrix2DM(IMatrix2F m) {
        this.m00 = m.m00(); this.m01 = m.m01(); this.m02 = m.m02();
        this.m10 = m.m10(); this.m11 = m.m11(); this.m12 = m.m12();
    }

    public Matrix2DM(DoubleBuffer buf) {
        this.m00 = buf.get(); this.m01 = buf.get(); this.m02 = buf.get();
        this.m10 = buf.get(); this.m11 = buf.get(); this.m12 = buf.get();
    }

    @Override
    public IMatrix2DM set(double m00, double m01, double m02, double m10, double m11, double m12) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        return this;
    }

    @Override
    public IMatrix2DM set(double[] ms) {
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
    public IMatrix2DM setM00(double m00) {
        this.m00 = m00;
        return this;
    }

    @Override
    public IMatrix2DM setM01(double m01) {
        this.m01 = m01;
        return this;
    }

    @Override
    public IMatrix2DM setM02(double m02) {
        this.m02 = m02;
        return this;
    }

    @Override
    public IMatrix2DM setM10(double m10) {
        this.m10 = m10;
        return this;
    }

    @Override
    public IMatrix2DM setM11(double m11) {
        this.m11 = m11;
        return this;
    }

    @Override
    public IMatrix2DM setM12(double m12) {
        this.m12 = m12;
        return this;
    }

    @Override
    public IMatrix2DM setIdentity() {
        this.m00 = 1.0D; this.m01 = 0.0D; this.m02 = 0.0D;
        this.m10 = 0.0D; this.m11 = 1.0D; this.m12 = 0.0D;
        return this;
    }

    @Override
    public IMatrix2DM collapseStack() {
        this.stateStack = null;
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IMatrix2DM resetStack() {
        this.stateCursor = 0;
        return this;
    }

    @Override
    public IMatrix2DM pushMatrix() {
        if (this.stateStack == null) {
            this.stateStack = new double[1][];
        }
        if (this.stateCursor >= this.stateStack.length) {
            this.stateStack = Arrays.copyOf(this.stateStack, this.stateCursor + 1);
        }
        double[] arr = this.stateStack[this.stateCursor++];

        if (arr == null) {
            arr = new double[6];
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
    public IMatrix2DM popMatrix() {
        if (this.stateCursor - 1 < 0) {
            throw new IllegalStateException();
        }
        return this.set(this.stateStack[--this.stateCursor]);
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
        return this.m00 * this.m11 - this.m01 * this.m10;
    }

    @Override
    public boolean isReversible() {
        return Math.abs(this.determinant()) > BananaMath.DOUBLE_EPS;
    }

    @Override
    public IMatrix2DM invert() {
        double det = this.determinant();
        if (Math.abs(det) < BananaMath.DOUBLE_EPS) throw new ArithmeticException("Couldn't invert unreversible matrix");
        double detInv = 1.0D / det;
        return this.set(
            this.m11 * detInv, -this.m01 * detInv, (this.m01 * this.m12 - this.m11 * this.m02) * detInv,
            -this.m10 * detInv, this.m00 * detInv, -(this.m00 * this.m12 - this.m10 * this.m02) * detInv
        );
    }

    @Override
    public IMatrix2DM mul(IMatrix2D matrix) {
        double m1v00 = matrix.m00(), m1v01 = matrix.m01(), m1v02 = matrix.m02();
        double m1v10 = matrix.m10(), m1v11 = matrix.m11(), m1v12 = matrix.m12();
        double m2v00 = this.m00, m2v01 = this.m01, m2v02 = this.m02;
        double m2v10 = this.m10, m2v11 = this.m11, m2v12 = this.m12;
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
    public IMatrix2DM mul(IMatrix2F matrix) {
        float m1v00 = matrix.m00(), m1v01 = matrix.m01(), m1v02 = matrix.m02();
        float m1v10 = matrix.m10(), m1v11 = matrix.m11(), m1v12 = matrix.m12();
        double m2v00 = this.m00, m2v01 = this.m01, m2v02 = this.m02;
        double m2v10 = this.m10, m2v11 = this.m11, m2v12 = this.m12;
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
    public IMatrix2DM mulPost(IMatrix2D matrix) {
        double m2v00 = matrix.m00(), m2v01 = matrix.m01(), m2v02 = matrix.m02();
        double m2v10 = matrix.m10(), m2v11 = matrix.m11(), m2v12 = matrix.m12();
        double m1v00 = this.m00;
        double m1v01 = this.m01;
        double m1v10 = this.m10;
        double m1v11 = this.m11;
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
    public IMatrix2DM mulPost(IMatrix2F matrix) {
        float m2v00 = matrix.m00(), m2v01 = matrix.m01(), m2v02 = matrix.m02();
        float m2v10 = matrix.m10(), m2v11 = matrix.m11(), m2v12 = matrix.m12();
        double m1v00 = this.m00;
        double m1v01 = this.m01;
        double m1v10 = this.m10;
        double m1v11 = this.m11;
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
    public IMatrix2DM scale(int x, int y) {
        return this.set(
            x * this.m00, x * this.m01, x * this.m02,
            y * this.m10, y * this.m11, y * this.m12
        );
    }

    @Override
    public IMatrix2DM scale(double x, double y) {
        return this.set(
            x * this.m00, x * this.m01, x * this.m02,
            y * this.m10, y * this.m11, y * this.m12
        );
    }

    @Override
    public IMatrix2DM scale(IVec2D vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IMatrix2DM scale(IVec2I vec) {
        return this.scale(vec.x(), vec.y());
    }

    @Override
    public IMatrix2DM translate(int x, int y) {
        this.m02 += x;
        this.m12 += y;
        return this;
    }

    @Override
    public IMatrix2DM translate(double x, double y) {
        this.m02 += x;
        this.m12 += y;
        return this;
    }

    @Override
    public IMatrix2DM translate(IVec2D vec) {
        return this.translate(vec.x(), vec.y());
    }

    @Override
    public IMatrix2DM translate(IVec2I vec) {
        return this.translate(vec.x(), vec.y());
    }

    @Override
    public IMatrix2DM rotate(double degrees) {
        double rads = Math.toRadians(degrees);
        double sin = Math.sin(rads);
        double cos = Math.cos(rads);
        return this.set(
            (cos * this.m00) + (sin * this.m10), (cos * this.m01) + (sin * this.m11), (cos * this.m02) + (sin * this.m12),
            (-sin * this.m00) + (cos * this.m10), (-sin * this.m01) + (cos * this.m11), (-sin * this.m02) + (cos * this.m12)
        );
    }

    @Override
    public IMatrix2DM rotateAround(int x, int y, double degrees) {
        double rads = Math.toRadians(degrees);
        double sin = Math.sin(rads);
        double cos = Math.cos(rads);

        double m00p = this.m00, m01p = this.m01, m02p = this.m02 - x;
        double m10p = this.m10, m11p = this.m11, m12p = this.m12 - y;

        double m00r = (cos * m00p) + (sin * m10p), m01r = (cos * m01p) + (sin * m11p), m02r = (cos * m02p) + (sin * m12p);
        double m10r = (-sin * m00p) + (cos * m10p), m11r = (-sin * m01p) + (cos * m11p), m12r = (-sin * m02p) + (cos * m12p);

        return this.set(m00r, m01r, m02r + x, m10r, m11r, m12r + y);
    }

    @Override
    public IMatrix2DM rotateAround(double x, double y, double degrees) {
        double rads = Math.toRadians(degrees);
        double sin = Math.sin(rads);
        double cos = Math.cos(rads);

        double m00p = this.m00, m01p = this.m01, m02p = this.m02 - x;
        double m10p = this.m10, m11p = this.m11, m12p = this.m12 - y;

        double m00r = (cos * m00p) + (sin * m10p), m01r = (cos * m01p) + (sin * m11p), m02r = (cos * m02p) + (sin * m12p);
        double m10r = (-sin * m00p) + (cos * m10p), m11r = (-sin * m01p) + (cos * m11p), m12r = (-sin * m02p) + (cos * m12p);

        return this.set(m00r, m01r, m02r + x, m10r, m11r, m12r + y);
    }

    @Override
    public IMatrix2DM rotateAround(IVec2D point, double degrees) {
        return this.rotateAround(point.x(), point.y(), degrees);
    }

    @Override
    public IMatrix2DM rotateAround(IVec2I point, double degrees) {
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
        buf.put(0.0D);
        buf.put(0.0D);
        buf.put(1.0D);
        return buf;
    }

    @Override
    public IMatrix2D toMutable() {
        return this;
    }

    @Override
    public IMatrix2D toImmutable() {
        return new Matrix2D(this);
    }

    @Override
    public IMatrix2FM toFloat() {
        return new Matrix2FM(this);
    }

    @Override
    public String toString() {
        return "[" + ((float) this.m00) + ", " + ((float) this.m01) + ", " + ((float) this.m02) + "]\n[" + ((float) this.m10) + ", " + ((float) this.m11) + ", " + ((float) this.m12) + "]\n[" + 0.0 + ", " + 0.0 + ", " + 1.0 + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IMatrix2D)) {
            return false;
        }
        IMatrix2D m = ((IMatrix2D) obj);
        return
                m.m00() == this.m00 && m.m01() == this.m01 && m.m02() == this.m02 &&
                m.m10() == this.m10 && m.m11() == this.m11 && m.m12() == this.m12 &&
                m.m20() == 0.0D && m.m21() == 0.0D && m.m22() == 1.0D;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, 0.0D, 0.0D, 1.0D);
    }
}
