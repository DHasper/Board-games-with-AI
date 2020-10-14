package src.gamemodules.reversigame;

import java.util.ArrayList;
import java.util.Map;

import src.gameframework.GameBoardLogic;

/**
 * Worker thread for our reversi minimax algorithm.
 */
public class ReversiMinimaxWorker implements Runnable{

    // Parameters for minimax algorithm.
    private GameBoardLogic board;
    private int depth;
    private boolean isMax;
    private int move;

    // Map that stores the calculated scores for valid moves.
    private Map<Integer, Integer> result;

    /**
     * Constructor for ReversiMinimaxWorker class.
     * @param board the gameBoard that should be calculated a score for.
     * @param depth the maximum depth of the search tree.
     * @param isMax if the player is maximizing or minimizing.
     * @param move the move that we are calculating a score for.
     * @param result results Map to store results.
     */
    ReversiMinimaxWorker(GameBoardLogic board, int depth, boolean isMax, int move, Map<Integer, Integer> result){
        this.board = board;
        this.depth = depth;
        this.isMax = isMax;
        this.move = move;
        this.result = result;
    }

    /**
     * Run method that calls our minimax algorithm and puts the result in the results map.
     */
    @Override
    public void run() {
        int eval = minimax(board, depth, isMax, -10000, 10000, false);
        result.put(move, eval);
    }

    /**
     * This method iterates the valid moves and it it calculates a score for the gameboard. This method uses our own
     * version of a minimax algorithm with alpha-beta pruning and quiescence search. Because of time and computation
     * constraints our quiescence search functionality only deepens the search tree once if a threatening move is found
     * on a leaf. Ideally it should deepen until the board has no more threatening moves, but we have found this
     * to take to much computation to fit within a 10 second move limit.
     *
     * @param board the gameBoard that should be calculated a score for.
     * @param depth the maximum depth of the search tree.
     * @param isMax if the player is maximizing or minimizing.
     * @param alpha alpha value for alpha-beta pruning.
     * @param beta beta value for alpha-beta pruning.
     * @param deepen should be set to false, quiescence search sets this to true when the search tree needs to be deepened.
     *
     * @return a score for the given gameBoard.
     */
    private int minimax(GameBoardLogic board, int depth, boolean isMax, int alpha, int beta, boolean deepen) {
        int player;
        int bestEval;
        if(isMax){ bestEval = -10000; player = 1; }
        else { bestEval = 10000;player = 2; }

        ReversiGameLogic logic = new ReversiGameLogic();
        logic.setBoard(board);
        ArrayList<Integer> moves = logic.getMoves(player);

        if(depth == 0 || logic.getMoves(player).size() == 0){
            for(int move : moves){
                if(!deepen && isThreat(move, board)){
                    return minimax(board, 1, isMax, alpha, beta, true);
                }
            }
            return evaluate(board);
        }

        for(int move : moves){
            ReversiBoardLogic newBoard = new ReversiBoardLogic();
            newBoard.setBoard(board.getBoard());
            ReversiGameLogic tempGame = new ReversiGameLogic();
            tempGame.setBoard(newBoard);
            tempGame.doMove(move, player);

            int eval = minimax(newBoard, depth-1, !isMax, alpha, beta, deepen);

            if(isMax){
                if(eval > bestEval) bestEval = eval;
                if(eval > alpha) alpha = eval;
            } else {
                if(eval < bestEval) bestEval = eval;
                if(eval < beta) beta = eval;
            }
            if(beta <= alpha) break;
        }

        return bestEval;
    }

    /**
     * This method evaluates the board passed as a parameter. The current state of the board will be scored based on
     * stability, mobility and bias. If this is the end of the game we return the result instead.
     *
     * @param board the board about to be checked.
     * @return a given value which indicates the score of the board.
     */
    private int evaluate(GameBoardLogic board){
        ReversiGameLogic logic = new ReversiGameLogic();
        logic.setBoard(board);

        // If this is the end of the game return the outcome of the game instead.
        int turn = logic.getDiscCount(1) + logic.getDiscCount(2) + 4;
        if(logic.getMoves(1).size() == 0 && logic.getMoves(2).size() == 0 && turn > 50){
            int result = logic.getDiscCount(1) - logic.getDiscCount(2);
            if(result < 0){
                result -= 5000;
            } else if(result > 0){
                result += 5000;
            }
            return result;
        }

        // Calculate the stability and mobility.
        int stability = logic.getStableDiscs(board, 1) - logic.getStableDiscs(board, 2);
        int mobility = logic.getPossibleFlips(board, 1) - logic.getPossibleFlips(board, 2);

        // These numbers have been found through 70000+ tests.
        return (int)((stability * turn) / 12 + mobility * 1.9) + (getBias(board));
    }

//    private int evaluate(GameBoardLogic board) {
//        ReversiGameLogic logic = new ReversiGameLogic();
//        logic.setBoard(board);
//        return logic.getDiscCount(1) - logic.getDiscCount(2);
//    }

    /**
     * This method checks if a move is a threatening move. This is used by our AI to determine if it
     * should search deeper.
     *
     * @param move the move that should be checked.
     * @return whether a move is a threat or not.
     */
    private boolean isThreat(int move, GameBoardLogic board){
        // Move adjacent to corners while the corners hasn't been taken yet. This is dangerous because it can give away
        // a corner.
        if(move == 1 || move == 8 || move == 9){
            if(board.getBoardPos(0) == 0){
                return true;
            }
        }
        if(move == 6 || move == 14 || move == 15){
            if(board.getBoardPos(7) == 0){
                return true;
            }
        }
        if(move == 48 || move == 49 || move == 57){
            if(board.getBoardPos(56) == 0){
                return true;
            }
        }
        if(move == 54 || move == 55 || move == 62){
            if(board.getBoardPos(63) == 0){
                return true;
            }
        }

        // 1 or less moves left. This is dangerous because it can force our AI to give away a strong position.
        ReversiGameLogic logic = new ReversiGameLogic();
        logic.setBoard(board);
        if(logic.gameOver() == 0){
            if(logic.getMoves(1).size() <= 1 || logic.getMoves(0).size() <= 1){
                return true;
            }
        }
        return false;
    }

    /**
     * This method calculates a bias value. The bias value is used to add weight
     * to the evaluation of certain moves. For now we only use a bias for the corner positions.
     * @return the bias value
     */
    private int getBias(GameBoardLogic board){
        int bias = 0;

        if(board.getBoardPos(0) == 1){
            bias += 50;
        } else if(board.getBoardPos(0) == 2){
            bias -= 50;
        }
        if(board.getBoardPos(7) == 1){
            bias += 50;
        } else if(board.getBoardPos(7) == 2){
            bias -= 50;
        }
        if(board.getBoardPos(56) == 1){
            bias += 50;
        } else if(board.getBoardPos(56) == 2){
            bias -= 50;
        }
        if(board.getBoardPos(63) == 1){
            bias += 50;
        } else if(board.getBoardPos(63) == 2){
            bias -= 50;
        }

        return bias;
    }
}
