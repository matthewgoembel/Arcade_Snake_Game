import java.util.Random;
/*
 * Author: Matthew Goembel, mgoembel2022@gmail.com
 * Course: CSE 1002, Section 7, Fall 2022
 * Project: Snake
 */
public class Apple {
   private int y;
   private int x;
   // Length and width of board are the same
   private final int width = 200;
   private final int limit = width - 20;
   // Random number
   private static final Random RNG = new Random (Long.getLong (
           "seed", System.nanoTime()));

   public Apple() {
      // Random Coordinates
      x = (RNG.nextInt(limit) / 10) * 10;
      y = (RNG.nextInt(limit) / 10) * 10;
      // Make sure the value does not fall outside of the canvas
      final int plotLimit = 20;
      final int correct = 30;

      // Make sure apple is withing boundaries
      if (x > width - plotLimit) {
         x -= correct;
      } else if (x < plotLimit) {
         x += correct;
      }
      if (y > width - plotLimit) {
         y -= correct;
      } else if (y < plotLimit) {
         y += correct;
      }
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.square(x, y, 5);
   }

   /* Getter Methods */
   public int getX () {
      return x;
   }

   public int getY () {
      return y;
   }
}
