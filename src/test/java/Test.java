import com.artur114.bananalib.math.m2d.matrix.IMatrix2D;
import com.artur114.bananalib.math.m2d.matrix.Matrix2DM;
import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.math.m2d.vec.IVec2I;
import com.artur114.bananalib.math.m2d.vec.Vec2D;
import com.artur114.bananalib.math.m3d.box.IBox3I;
import com.artur114.bananalib.math.m3d.vec.IVec3I;
import com.artur114.bananalib.math.m3d.vec.Vec3I;
import net.minecraft.util.math.ChunkPos;

public class Test {
    public static void main(String[] args) {
//        IVec2D vec = new Vec2D(1, 4);
//        IMatrix2D matrix = new Matrix2DM().rotate(90.0F).translate(2, 40);
//
//        IVec2D vecT = matrix.transform(vec);
//        System.out.println(vecT);
//        System.out.println(matrix.invert().transform(vecT));
//        System.out.println(matrix);

        IVec3I blockPos = new Vec3I(23, 0, 46);

        IVec2I chunkPos = blockPos.xz().divide(16);
        System.out.println(chunkPos);

        IVec3I block = chunkPos.scale(16).add(12, 4).xzy(60);
        System.out.println(block.x());
        System.out.println(block.y());
        System.out.println(block.z());
    }
}
