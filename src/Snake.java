import java.awt.event.KeyEvent;
/*
 * Author: Matthew Goembel, mgoembel2022@gmail.com
 * Course: CSE 1002, Section 7, Fall 2022
 * Project: Snake
 */
public class Snake extends Apple {
   // Canvas Size
   private final int width = 200;
   private final int height = 200;
   // Apple size
   private final int dotSize = 5;
   private final int numSpaces = 400;
   private final int middle = 100;
   private final int space = 90;
   private final int space2 = 80;
   // Controls
   private final int left = KeyEvent.VK_LEFT;
   private final int up = KeyEvent.VK_UP;
   private final int down = KeyEvent.VK_DOWN;
   private final int right = KeyEvent.VK_RIGHT;
   // Snake body count
   private int dots;
   // List and number of apples
   private int appleNum;
   private Apple[] apples;
   private int score = 0;
   // Controls checks
   private boolean dirRight = true;
   private boolean dirLeft = false;
   private boolean dirUp = false;
   private boolean dirDown = false;
   private boolean firstMove = false;
   private boolean inGame = true;
   // List of possible locations
   private final int[] x = new int[numSpaces];
   private final int[] y = new int[numSpaces];

   // Make a Snake
   public Snake() {
      backRound();                             // Clear screen
      StdDraw.setXscale(0, width);          // Set board scale
      StdDraw.setYscale(0, height);
      StdDraw.setPenColor(StdDraw.WHITE);      // Drawing Color
      StdDraw.square(middle, middle, middle);  // Starting Snake
      splashScreen();                          // Start screen
   }

   // Start or Leave the Game
   public void splashScreen () {
      // Clear
      backRound();
      boolean selected = false;
      int option = 0;
      // Determine when user plays or exits
      while (!selected) {
         StdDraw.setPenColor(StdDraw.WHITE);
         StdDraw.text(100, 100, "1. Start");
         StdDraw.text(100, space, "2. Exit");
         StdDraw.show();
         // Menu to start the game or exit the game
         if (StdDraw.isKeyPressed(KeyEvent.VK_1)
                 || StdDraw.isKeyPressed(KeyEvent.VK_S)) {   // If '1' is pressed
            option = 1;
            selected = true;
         } else if (StdDraw.isKeyPressed(KeyEvent.VK_2)
                 || StdDraw.isKeyPressed(KeyEvent.VK_E)) {   // If '2' is pressed
            option = 2;
            selected = true;
         }
      }

      if (option == 1) {
         startMenu();
      } else {
         exitScreen();
      }
   }

   // Clear background to snake checked background
   public void backRound () {
      StdDraw.enableDoubleBuffering();
      // Checker 10x10 tiles
      final int tiles = (numSpaces / 2) + 1;
      for (int i = 0; i < tiles; i++) {
         for (int j = 0; j < tiles; j++) {
            if (((i + j) % 2) != 0) {
               StdDraw.setPenColor(StdDraw.BLUE);
            } else {
               StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            }

            // draw filled square centered at i, j
            StdDraw.filledSquare(i * 10, j * 10, 5);
         }
      }
      StdDraw.show();
   }

   public void startMenu () {
      // Start the game
      backRound();
      gameStart();
   }

   public void exitScreen () {
      // Display Round over
      backRound();
      StdDraw.setPenColor(StdDraw.WHITE);
      StdDraw.text(100, 100, "Game Over");
   }

   public void gameStart () {
      // StdDraw.clear(StdDraw.GREEN);
      backRound();
      dots = 5;
      StdDraw.setPenColor(StdDraw.WHITE);
      StdDraw.text(middle, middle, "Move To Start");
      StdDraw.show();
      // Set the stating point and snake
      int mid = middle + 10;
      for (int i = 0; i < dots; i++) {
         x[i] = middle;
         y[i] = middle;
         mid -= 10;
      }
      // Create a new apple
      appleNum = 1;
      apples = new Apple[appleNum];
      for (int i = 0; i < appleNum; i++) {
         apples[i] = new Apple();
      }
      // While user is playing, move the snake
      while (inGame) {
         if (StdDraw.isKeyPressed(left) || StdDraw.isKeyPressed(right)
                 || StdDraw.isKeyPressed(up) || StdDraw.isKeyPressed(down)) {
            checks();
            firstMove = true;
         }
         // Flicker snake movement
         StdDraw.enableDoubleBuffering();
         while (firstMove) {
            StdDraw.show();
            StdDraw.pause(space);
            backRound();
            checks();
         }
      }
   }

   // Draw apples and add to end of the snake
   public void paint () {
      for (int i = 0; i < appleNum; i++) {
         StdDraw.setPenColor(StdDraw.RED);
         StdDraw.filledSquare(apples[i].getX(), apples[i].getY(), 5);
         StdDraw.show();
      }
      // Snake eats an apple, add to snake
      for (int i = 0; i < dots; i++) {
         StdDraw.setPenColor(StdDraw.GREEN);
         StdDraw.filledSquare(x[i], y[i], 5);
      }
      // Display score after snake dies or wins
      StdDraw.setPenColor(StdDraw.WHITE);
      final int scoreX = 305;
      final int scoreY = 15;
      StdDraw.text(scoreY, scoreX, "Score: " + score);
      StdDraw.square(middle, middle, middle);
   }

   // Make sure the snake passes all checks while in game
   public void checks () {
      checkKey();    // Is Snake moving direction of key press
      move();        // Move snake
      paint();       // Display Instructions
      checkApple();  // Is apple eaten
      isDead();      // Is snake alive
   }

   public void checkKey () {
      // Moves snake down till other direction is pressed
      if (StdDraw.isKeyPressed(down) && (!dirUp)) {
         dirDown = true;
         dirRight = false;
         dirLeft = false;
      }
      // Moves snake up till other direction is pressed
      if (StdDraw.isKeyPressed(up) && (!dirDown)) {
         dirUp = true;
         dirRight = false;
         dirLeft = false;
      }
      // Moves snake right till other direction is pressed
      if (StdDraw.isKeyPressed(right) && (!dirLeft)) {
         dirRight = true;
         dirDown = false;
         dirUp = false;
      }
      // Moves snake left till other direction is pressed
      if (StdDraw.isKeyPressed(left) && (!dirRight)) {
         dirLeft = true;
         dirDown = false;
         dirUp = false;
      }
   }

   public void move () {
      // Move from the head down to tail
      for (int i = dots; i > 0; i--) {
         x[i] = x[(i - 1)];  // Previous position
         y[i] = y[(i - 1)];  // Previous position
      }
      /* Based on direction, decrease or increase position by tile size to move */
      if (dirDown && !dirUp) {
         y[0] -= dotSize;
      } else if (dirUp && !dirDown) {
         y[0] += dotSize;
      } else if (dirRight && !dirLeft) {
         x[0] += dotSize;
      } else if (dirLeft && !dirRight) {
         x[0] -= dotSize;
      }
   }

   public void isDead () {
      for (int i = dots; i > 0; i--) {
         if ((i > 1) && (x[0] == x[i]) && (y[0] == y[i])) {
            inGame = false;
            gameOver();
         }
      }

      if (y[0] >= height) {
         inGame = false;
         gameOver();
      }

      if (y[0] <= 0) {
         inGame = false;
         gameOver();
      }

      if (x[0] <= 0) {
         inGame = false;
         gameOver();
      }

      if (x[0] >= width) {
         inGame = false;
         gameOver();
      }
   }
	
   // Is the apple been eaten?
   public void checkApple () {
      for (int i = 0; i < appleNum; i++) {
         // If the snake eats the apple, spawn new apple
         if ((x[0] == apples[i].getX()) && (y[0] == apples[i].getY())) {
            dots++;                    // Add snake body to end
            score++;                   // Increase Score
            apples[i] = new Apple();   // Spawn New apple
            paint();
         }
      }
   }

   public void gameOver () {
      backRound();
      StdDraw.setPenColor(StdDraw.WHITE);
	// Does the snake eat apples to fill up the whole board?
      if (score >= numSpaces) {
         StdDraw.text(100, 100, "YOU WIN!");
         StdDraw.text(100, space, "Score: " + score);
         StdDraw.text(100, space2, "Play Again? Y/N ");
         StdDraw.show();
	   // Probably not ^. So show score when snake dies
      } else {
         StdDraw.text(100, 100, "GAME OVER");
         StdDraw.text(100, space, "SCORE: " + score);
         StdDraw.text(100, space2, "Play Again? Press Y");
         StdDraw.show();
	   // User can play again if they want. Or not
         while (!inGame) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_Y)) {
               score = 0;
               inGame = true;
               backRound();
               startMenu();

            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_N)) {
               backRound();
               exitScreen();
            }
         }
      }
   }
	
   // Main Method
   public static void main (final String[] args) {
      new Snake();
   }
}
