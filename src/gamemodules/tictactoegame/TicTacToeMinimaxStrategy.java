/**
 * Inspiration for this code: https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
 *
 * this class does all the calculations for the game.
 * It is based on the MiniMax algorithm.
 *
 * @author Joost Blaauwwiekel, Dylan Hasperhoven
 * @version 1.0
 *
 */

package src.gamemodules.tictactoegame;

import java.util.ArrayList;
import java.util.Random;

import src.gameframework.GameBoardLogic;
import src.gameframework.aistrategies.MinimaxStrategy;

/**
 * Minimax AI for Tic Tac Toe.
 */
public class TicTacToeMinimaxStrategy extends MinimaxStrategy {

    // Used for generating random moves.
    private static Random random = new Random();

    /**
     * This method evaluates the the gameboard. A win situation returns
     * 1, a lose situation returns -1. A draw returns 0.
     *
     * @param b         the board of the game
     * @return          the amount of points after the evaluation
     */
    private int evaluate(GameBoardLogic b){
        int[] board = b.getBoard();

        // Check rows for a win position.
        int rowCounter = 0;
        for(int row = 0; row < 3; row++) {
            if(board[rowCounter] == board[rowCounter + 1] && board[rowCounter + 1] == board[rowCounter + 2]) {
                if(board[rowCounter] == 1) {
                    return 1;
                } else if(board[rowCounter] == 2) {
                    return -1;
                }
            }
            rowCounter = rowCounter + 3;
        }

        // Check columns for a win position.
        int colCounter = 0;
        for(int col = 0; col < 3; col++) {
            if(board[colCounter] == board[colCounter + 3] && board[colCounter + 3] == board[colCounter + 6]) {
                if(board[colCounter] == 1) {
                    return 1;
                } else if(board[colCounter] == 2) {
                    return -1;
                }
            }
            colCounter = colCounter + 1;
        }

        // Check if there is a win position for diagonals
        if(board[0] == board[4] && board[4] == board[8]) {
            if (board[0] == 1) {
                return 1;
            } else if(board[0] == 2) {
                return -1;
            }
        }
        if(board[2] == board[4] && board[4] == board[6]) {
            if (board[2] == 1) {
                return 1;
            } else if(board[2] == 2) {
                return -1;
            }
        }

        // If it got here there is no winner yet, return 0.
        return 0;
    }

    /**
     * This method returns the best possible move based on
     * the minimax algorithm adapted to the difficulty
     *
     * @param b         The gameBoard, used to get the int[] board layout
     * @param p         The player, either 1 or 2
     * @return          int with the best possible position
     */
    @Override
    public int getBestMove(GameBoardLogic b, int p) {
        TicTacToeGameLogic logic = new TicTacToeGameLogic();
        logic.setBoard(b);

        switch (super.getDifficulty()) {
            case 0:
                return getRandomValidMove(b, p);
            case 1:
                return calculateBestMove(p, 1, logic, b);
            case 2:
                return calculateBestMove(p, 2, logic, b);
            case 3:
                return calculateBestMove(p, 5, logic, b);
            default:
                return -1;
        }
    }

    /**
     * Method that finds all valid moves, and calls the minimax algorithm on them to find
     * out which of the moves is the best choice.
     *
     * @param player    int player, either 1 or 2.
     * @param depth     The depth for the minimax algorithm.
     * @param logic     GameLogic used for getting the remaining possible moves.
     * @param board     The board used for getting the int[] layout.
     * @return          Returns the best possible move.
     */
    private int calculateBestMove(int player, int depth, TicTacToeGameLogic logic, GameBoardLogic board) {
        boolean isMax;
        int bestMoveValue;
        int bestMove = -1;

        if(player == 1){
            bestMoveValue = -10;
            isMax = true;
        } else {
            isMax = false;
            bestMoveValue = 10;
        }

        for(int move : logic.getMoves(player)) {
            TicTacToeBoardLogic tempBoard = new TicTacToeBoardLogic();
            tempBoard.setBoard(board.getBoard());
            TicTacToeGameLogic tempLogic = new TicTacToeGameLogic();
            tempLogic.setBoard(tempBoard);
            tempLogic.doMove(move, player);
            int moveValue = miniMax(tempBoard, depth, !isMax);
            if(isMax && moveValue > bestMoveValue || !isMax && moveValue < bestMoveValue){
                bestMoveValue = moveValue;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * This method is a simple implementation of a minimax algorithm that recursively generates a search tree
     * with evaluations of the scoreboard, and generates a score for this board.
     *
     * @param board     The gameBoard, used to get the int[] board layout.
     * @param depth     the depth of the search tree (number of moves to be calculated).
     * @param isMax     true when the current player is the maximizer, false when the current player is the minimizer.
     * @return          Returns the score for this board.
     */
    private int miniMax(GameBoardLogic board, int depth, boolean isMax) {
        int player;
        int bestEval;
        if(isMax){
            bestEval = -10;
            player = 1;
        } else {
            bestEval = 10;
            player = 2;
        }

        TicTacToeGameLogic logic = new TicTacToeGameLogic();
        logic.setBoard(board);

        int score = evaluate(board);
        if(depth == 0 || logic.gameOver() != 0) {
            return score;
        }

        for(int move : logic.getMoves(player)){
            TicTacToeBoardLogic newBoard = new TicTacToeBoardLogic();
            newBoard.setBoard(board.getBoard());
            TicTacToeGameLogic tempGame = new TicTacToeGameLogic();
            tempGame.setBoard(newBoard);
            tempGame.doMove(move, player);
            if(isMax){
                int eval = miniMax(newBoard, depth-1, false);
                if(eval > bestEval){
                    bestEval = eval;
                }
            } else {
                int eval = miniMax(newBoard, depth-1, true);
                if(eval < bestEval){
                    bestEval = eval;
                }
            }
        }
        return bestEval;
    }

    /**
     * This method returns a random valid move. This is used for the lowest difficulty AI.
     *
     * @param board the board that is used.
     * @param player the player to find for.
     * @return the random move.
     */
    private int getRandomValidMove(GameBoardLogic board, int player){
        TicTacToeGameLogic logic = new TicTacToeGameLogic();
        logic.setBoard(board);
        ArrayList<Integer> moves = logic.getMoves(player);

        if(!moves.isEmpty()){
            int choice = random.nextInt(moves.size());
            return moves.get(choice);
        } else {
            return -1;
        }
    }
}

