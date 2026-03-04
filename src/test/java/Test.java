import com.artur114.bananalib.math.BananaMath;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Random rand = new Random();
        for (int i = 0; i != 100; i++) {
            double d = rand.nextDouble() * 10.0D;
            System.out.println(d);
            System.out.println(BananaMath.floor(d));
            System.out.println(Math.round(d));
            System.out.println();
        }
    }
}
