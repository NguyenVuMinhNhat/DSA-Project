import java.util.Random;
import java.awt.event.KeyEvent;
public class RandomAI {
    private char[] MOVES = {'w', 'a', 's','d'};  
    private Random random;
    
    public RandomAI(){
        random = new Random();  
    }

    private char randomMoves(){
        int index = random.nextInt(MOVES.length);
        return MOVES[index];
    }

    public int getNextMove(){
        if (randomMoves() == 'w') {
            return KeyEvent.VK_W;
        }
        else if (randomMoves() == 'a'){
            return KeyEvent.VK_A;
        }
        else if (randomMoves() == 's'){
            return KeyEvent.VK_S;
        }
        else {
            return KeyEvent.VK_D;
        }
    }
    
}
