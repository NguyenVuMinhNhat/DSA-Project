import java.awt.event.KeyEvent;
import java.util.Arrays;

public class HeuristicAI {
    private static final Move[] MOVES = Move.values();
    

    public enum Move {UP, DOWN, LEFT, RIGHT}

    public int moveKey(int[][] board){
        Move bestMove = findBestMove(board);
        if (bestMove == Move.UP) {
            return KeyEvent.VK_W;
        }
        else if (bestMove == Move.DOWN){
            return KeyEvent.VK_S;
        }
        else if (bestMove == Move.LEFT){
            return KeyEvent.VK_A;
        }else {
            return KeyEvent.VK_D;
        }
    }

    public Move findBestMove(int[][] board) {
        Move bestMove = null;
        double bestScore = -Double.MAX_VALUE;

        for (Move move : MOVES) {
            int[][] newBoard = simulateMove(board, move);
            if (!Arrays.deepEquals(board, newBoard)) {
                double score = evaluateBoard(newBoard);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    private int[][] simulateMove(int[][] board, Move move) {
        int[][] newBoard = copyBoard(board);
        switch (move) {
            case UP:
                for (int col = 0; col < newBoard.length; col++) {
                    int[] column = getColumn(newBoard, col);
                    int[] newColumn = moveAndMerge(column);
                    setColumn(newBoard, col, newColumn);
                }
                break;
            case DOWN:
                for (int col = 0; col < newBoard.length; col++) {
                    int[] column = getColumn(newBoard, col);
                    int[] newColumn = moveAndMerge(reverseArray(column));
                    setColumn(newBoard, col, reverseArray(newColumn));
                }
                break;
            case LEFT:
                for (int row = 0; row < newBoard.length; row++) {
                    newBoard[row] = moveAndMerge(newBoard[row]);
                }
                break;
            case RIGHT:
                for (int row = 0; row < newBoard.length; row++) {
                    newBoard[row] = reverseArray(moveAndMerge(reverseArray(newBoard[row])));
                }
                break;
        }
        return newBoard;
    }

    private int[] moveAndMerge(int[] array) {
        int[] newArray = new int[array.length];
        int insertPosition = 0;
        boolean mergedLast = false;

        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                if (!mergedLast && insertPosition > 0 && newArray[insertPosition - 1] == array[i]) {
                    newArray[insertPosition - 1] *= 2;
                    mergedLast = true;
                } else {
                    newArray[insertPosition] = array[i];
                    insertPosition++;
                    mergedLast = false;
                }
            }
        }
        return newArray;
    }

    private int[] getColumn(int[][] board, int col) {
        int[] column = new int[board.length];
        for (int row = 0; row < board.length; row++) {
            column[row] = board[row][col];
        }
        return column;
    }

    private void setColumn(int[][] board, int col, int[] column) {
        for (int row = 0; row < board.length; row++) {
            board[row][col] = column[row];
        }
    }

    private int[] reverseArray(int[] array) {
        int[] reversed = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            reversed[i] = array[array.length - 1 - i];
        }
        return reversed;
    }

    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(board[row], 0, newBoard[row], 0, board[row].length);
        }
        return newBoard;
    }

    

    public double evaluateBoard(int[][] board) {
        int emptyTiles = countEmptyTiles(board);
        int maxTile = getMaxTile(board);
        double monotonicity = calculateMonotonicity(board);
        double smoothness = calculateSmoothness(board);
        double mergingPotential = calculateMergingPotential(board);

        double score = 0.0;
        score += emptyTiles * 3;         
        score += Math.log(maxTile) * 2.5;  
        score += monotonicity * 1;       
        score += smoothness * 0.1;         
        score += mergingPotential * 0.7;   

        return score;
    }

    private int countEmptyTiles(int[][] board) {
        int count = 0;
        for (int[] row : board) {
            for (int tile : row) {
                if (tile == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private int getMaxTile(int[][] board) {
        int max = 0;
        for (int[] row : board) {
            for (int tile : row) {
                if (tile > max) {
                    max = tile;
                }
            }
        }
        return max;
    }

    private double calculateMonotonicity(int[][] board) {
        double[] totals = {0.0, 0.0, 0.0, 0.0};

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length - 1; col++) {
                if (board[row][col] > board[row][col + 1]) {
                    totals[0] += Math.log(board[row][col]) - Math.log(board[row][col + 1]);
                } else if (board[row][col] < board[row][col + 1]) {
                    totals[1] += Math.log(board[row][col + 1]) - Math.log(board[row][col]);
                }
            }
        }

        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board.length - 1; row++) {
                if (board[row][col] > board[row + 1][col]) {
                    totals[2] += Math.log(board[row][col]) - Math.log(board[row + 1][col]);
                } else if (board[row][col] < board[row + 1][col]) {
                    totals[3] += Math.log(board[row + 1][col]) - Math.log(board[row][col]);
                }
            }
        }

        return Math.min(totals[0], totals[1]) + Math.min(totals[2], totals[3]);
    }

    private double calculateSmoothness(int[][] board) {
        double smoothness = 0.0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length - 1; col++) {
                if (board[row][col] != 0 && board[row][col + 1] != 0) {
                    smoothness -= Math.abs(Math.log(board[row][col]) - Math.log(board[row][col + 1]));
                }
            }
        }

        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board.length - 1; row++) {
                if (board[row][col] != 0 && board[row + 1][col] != 0) {
                    smoothness -= Math.abs(Math.log(board[row][col]) - Math.log(board[row + 1][col]));
                }
            }
        }

        return smoothness;
    }

    private double calculateMergingPotential(int[][] board) {
        double mergingPotential = 0.0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length - 1; col++) {
                if (board[row][col] != 0 && board[row][col] == board[row][col + 1]) {
                    mergingPotential += Math.log(board[row][col]) / Math.log(2);
                }
            }
        }

        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board.length - 1; row++) {
                if (board[row][col] != 0 && board[row][col] == board[row + 1][col]) {
                    mergingPotential += Math.log(board[row][col]) / Math.log(2);
                }
            }
        }

        return mergingPotential;
    }
}


