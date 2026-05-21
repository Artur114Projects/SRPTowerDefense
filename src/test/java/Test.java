import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m2d.matrix.IMatrix2D;
import com.artur114.bananalib.math.m2d.matrix.Matrix2D;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.Vec2D;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        IVec2D vec = new Vec2D(1, 4);
        IMatrix2D matrix = Matrix2D.IDENTITY.rotate(90.0D).translate(2, 40);

        IVec2D vecT = matrix.transform(vec);
        System.out.println(vecT);
        System.out.println(matrix.invert().transform(vecT));
    }
}
