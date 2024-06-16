import java.awt.event.KeyEvent;
import java.util.Arrays;

public class AlphaBetaAI {
    private static final int DEPTH = 6;
    public enum Move {UP, DOWN, LEFT, RIGHT};
    private static final Move[] MOVES = Move.values();


    public Move findBestMove(int[][] board) {
        Move bestMove = null;
        double bestScore = -Double.MAX_VALUE;

        for (Move move : MOVES) {
            int[][] newBoard = simulateMove(board, move);
            if (!Arrays.deepEquals(board, newBoard)) {
                double score = alphaBeta(newBoard, DEPTH, -Double.MAX_VALUE, Double.MAX_VALUE, false);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

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


    private double alphaBeta(int[][] board, int depth, double alpha, double beta, boolean isMax) {
        if (depth == 0) {
            return evaluate(board);
        }

        if (isMax) {
            double maxScore = -Double.MAX_VALUE;
            for (Move move : MOVES) {
                int[][] newBoard = simulateMove(board, move);
                if (!Arrays.deepEquals(board, newBoard)) {
                    double score = alphaBeta(newBoard, depth - 1, alpha, beta, false);
                    maxScore = Math.max(maxScore, score);
                    alpha = Math.max(alpha, score);
                    if (beta <= alpha) {
                        break; // Beta cut-off
                    }
                }
            }
            return maxScore;
        } else {
            double minScore = Double.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 0) {
                        int[][] boardWithTwo = copyBoard(board);
                        boardWithTwo[i][j] = 2;
                        minScore = Math.min(minScore, alphaBeta(boardWithTwo, depth - 1, alpha, beta, true));
                        beta = Math.min(beta, minScore);

                        if (beta <= alpha) {
                            break; // Alpha cut-off
                        }

                        int[][] boardWithFour = copyBoard(board);
                        boardWithFour[i][j] = 4;
                        minScore = Math.min(minScore, alphaBeta(boardWithFour, depth - 1, alpha, beta, true));
                        beta = Math.min(beta, minScore);

                        if (beta <= alpha) {
                            break; // Alpha cut-off
                        }
                    }
                }
            }
            return minScore;
        }
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


    private double evaluate(int[][] board) {
        int emptyTiles = 0;
        int maxTile = 0;
        for (int[] row : board) {
            for (int tile : row) {
                if (tile == 0) {
                    emptyTiles++;
                }
                if (tile > maxTile) {
                    maxTile = tile;
                }
            }
        }
        return emptyTiles + Math.log(maxTile) / Math.log(2);
    }

    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }
}
