import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    public static List<GameObject> objects;
    public static boolean moving = false, hasMoved = true, somethingIsMoving = false;
    public static int dir = 0;
    public static int score = 0; // Add score variable
    private int[][] board = new int[4][4];
    private AlphaBetaAI AlphaBetaAI = new AlphaBetaAI();
    private HeuristicAI heuristicAI = new HeuristicAI();
    private RandomAI randomAI = new RandomAI();

    private Random rand = new Random();

    public Game() {
        init();
    }

    public int[][] getBoard() {
        return board;
    }

    public void init() {
        objects = new ArrayList<GameObject>();
        moving = false;
        hasMoved = true;
        somethingIsMoving = false;
        score = 0; // Reset score

        spawn();
    }

    public void update() {
        if (Keyboard.keyUp(KeyEvent.VK_R)) {
            init();
        }

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update();
        }

        checkForValueIncrease();
        movingLogic();

    }

    public void updateAI() {

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update();
        }

        checkForValueIncrease();
        movingAILogic();

    }

    public void updateRandom() {
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update();
        }

        checkForValueIncrease();
        movingRandom();
    }

    private void checkForValueIncrease() {

        for (int i = 0; i < objects.size(); i++) {
            for (int j = 0; j < objects.size(); j++) {
                if (i == j)
                    continue;
                if (objects.get(i).x == objects.get(j).x && objects.get(i).y == objects.get(j).y
                        && !objects.get(i).remove && !objects.get(j).remove) {
                    // j (removed) object

                    objects.get(j).remove = true;

                    // i (doubled) object
                    objects.get(i).value *= 2;

                    score += objects.get(i).value; // Update score

                    objects.get(i).createSprite();

                    // update the board

                    updateBoard();
                }
            }
        }
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).remove)
                objects.remove(i);
            updateBoard();
        }
    }

    private void spawn() {
        if (objects.size() == 16)
            return;

        boolean available = false;
        int x = 0, y = 0;
        while (!available) {
            x = rand.nextInt(4);
            y = rand.nextInt(4);
            boolean isAvailable = true;
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i).x / 100 == x && objects.get(i).y / 100 == y) {
                    isAvailable = false;
                }
            }
            if (isAvailable)
                available = true;
        }

        objects.add(new GameObject(x * 100, y * 100));
        updateBoard();
    }

    private void movingAILogic() {
        somethingIsMoving = false;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).moving) {
                somethingIsMoving = true;
            }
        }

        if (!somethingIsMoving) {
            moving = false;
            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).hasMoved = false;
            }
        }

        if (!moving && hasMoved) {
            spawn();
            hasMoved = false;
        }

        int moveValue = heuristicAI.moveKey(board);

        if (!moving && !hasMoved) {
            if (moveValue == 65 ) {
                hasMoved = true;
                moving = true;
                dir = 0;
            } else if (moveValue == 68) {
                hasMoved = true;
                moving = true;
                dir = 1;
            } else if (moveValue == 87) {
                hasMoved = true;
                moving = true;
                dir = 2;
            } else if (moveValue == 83) {
                hasMoved = true;
                moving = true;
                dir = 3;
            }
        }
    }

    private void movingRandom() {
        somethingIsMoving = false;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).moving) {
                somethingIsMoving = true;
            }
        }

        if (!somethingIsMoving) {
            moving = false;
            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).hasMoved = false;
            }
        }

        if (!moving && hasMoved) {
            spawn();
            hasMoved = false;
        }

        int moveValue = randomAI.getNextMove();

        if (!moving && !hasMoved) {
            if (moveValue == KeyEvent.VK_A ) {
                hasMoved = true;
                moving = true;
                dir = 0;
            } else if (moveValue == KeyEvent.VK_D) {
                hasMoved = true;
                moving = true;
                dir = 1;
            } else if (moveValue == KeyEvent.VK_W) {
                hasMoved = true;
                moving = true;
                dir = 2;
            } else if (moveValue == KeyEvent.VK_S) {
                hasMoved = true;
                moving = true;
                dir = 3;
            }
        }
    }

    private void movingLogic() {
        somethingIsMoving = false;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).moving) {
                somethingIsMoving = true;
            }
        }

        if (!somethingIsMoving) {
            moving = false;
            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).hasMoved = false;
            }
        }

        if (!moving && hasMoved) {
            spawn();
            hasMoved = false;
        }

        if (!moving && !hasMoved) {
            if (Keyboard.keyDown(KeyEvent.VK_A) || Keyboard.keyDown((KeyEvent.VK_LEFT))) {
                hasMoved = true;
                moving = true;
                dir = 0;
            } else if (Keyboard.keyDown(KeyEvent.VK_D) || Keyboard.keyDown((KeyEvent.VK_RIGHT))) {
                hasMoved = true;
                moving = true;
                dir = 1;
            } else if (Keyboard.keyDown(KeyEvent.VK_W) || Keyboard.keyDown((KeyEvent.VK_UP))) {
                hasMoved = true;
                moving = true;
                dir = 2;
            } else if (Keyboard.keyDown(KeyEvent.VK_S) || Keyboard.keyDown((KeyEvent.VK_DOWN))) {
                hasMoved = true;
                moving = true;
                dir = 3;
            }
        }
    }

    public void render() {
        Renderer.renderBackground();

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).render();
        }

        for (int i = 0; i < Main.pixels.length; i++) {
            Main.pixels[i] = Renderer.pixels[i];
        }
    }

    public void renderText(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(new Font("Verdana", 0, 100));
        g.setColor(Color.BLACK);

        for (int i = 0; i < objects.size(); i++) {
            GameObject o = objects.get(i);
            String s = o.value + "";
            int sw = (int) (g.getFontMetrics().stringWidth(s) / 2 / Main.scale);
            g.drawString(s, (int) (o.x + o.width / 2 - sw) * Main.scale, (int) (o.y + o.height / 2 + 18) * Main.scale);
        }

        // Render the score
        g.setFont(new Font("Verdana", 0, 30));
        g.setColor(Color.RED);
        g.drawString("Score: " + score, 0, 30);
    }

    public void updateBoard() {
        int x, y, value;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
            }
        }
        for (int i = 0; i < objects.size(); i++) {
            x = (int) objects.get(i).x / 100;
            y = (int) objects.get(i).y / 100;
            value = objects.get(i).value;
            board[y][x] = value;
        }
    }

    public void printBoard() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
            System.out.println();
        }
        System.out.println();
    }
}
