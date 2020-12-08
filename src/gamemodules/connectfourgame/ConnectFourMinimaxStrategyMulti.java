package src.gamemodules.connectfourgame;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import src.gameframework.GameBoardLogic;
import src.gameframework.aistrategies.MinimaxStrategy;

/**
 * Really simple AI that always chooses a move that wins
 */
public class ConnectFourMinimaxStrategyMulti extends MinimaxStrategy {

    Random random = new Random();

    private static final int DEPTH = 9;
    private static final int[] MOVE_ORDER = new int[] {3, 2, 4, 1, 5, 0, 6};

    @Override
    public synchronized int getBestMove(GameBoardLogic board, int player) {

        ConnectFourGameLogic logic = new ConnectFourGameLogic();
        logic.setBoard(board);
        ArrayList<Integer> moves = logic.getMoves(player);

        if (logic.midEmpty()) {
            return 3;
        }

        // Map that stores the calculated scores for valid moves.
        Map<Integer, Double> results = new ConcurrentHashMap<>();

        // Whether the AI is maximizing or not
        boolean isMax = player == 1;

        for (int move : moves) {
            ConnectFourBoardLogic tempBoard = new ConnectFourBoardLogic();
            tempBoard.setBoard(board.getBoard());
            ConnectFourGameLogic tempLogic = new ConnectFourGameLogic();
            tempLogic.setBoard(tempBoard);
            tempLogic.doMove(move, player);

            MinimaxWorker worker = new MinimaxWorker(tempBoard, ConnectFourMinimaxStrategyMulti.DEPTH, !isMax, move, results, true);
            Thread thread = new Thread(worker);
            thread.start();
        }

        // Wait untill all results are back
        while (results.size() != moves.size()) {
            try {
                wait(10);
            } catch (InterruptedException ignored) { }
        }

        // Find best eval
        double bestMoveEval = player == 1 ? -1000 : 1000;
        for(double eval : results.values()){
            if(isMax && eval > bestMoveEval || !isMax && eval < bestMoveEval){
                bestMoveEval = eval;
            }
        }

        // Find moves that have the best eval
        ArrayList<Integer> bestMoves = new ArrayList<>();
        for(Map.Entry<Integer, Double> result : results.entrySet()){
            if(result.getValue() == bestMoveEval){
                bestMoves.add(result.getKey());
            }
        }

        // If there are multiple best moves choose one according to move order
        for(int move : MOVE_ORDER){
            if(bestMoves.contains(move)){
                return move;
            }
        }

        // int bestMove = -1;
        // for(Map.Entry<Integer, Double> result : results.entrySet()){
        //     double eval = result.getValue();
        //     int move = result.getKey();
        //     if(isMax && eval > bestMoveEval || !isMax && eval < bestMoveEval){
        //         bestMoveEval = eval;
        //         bestMove = move;
        //     }
        // }

        // System.out.println("Best eval : " + bestMoveEval + " best move : " + bestMove);
        return -1;
        // return bestMove;
    }

    private class MinimaxWorker implements Runnable {

        // Parameters for minimax algorithm.
        private GameBoardLogic board;
        private int depth;
        private boolean isMax;
        private int evalMove;
        private boolean root;

        // Map that stores the calculated scores for valid moves.
        private Map<Integer, Double> finalResult;

        MinimaxWorker(GameBoardLogic board, int depth, boolean isMax, int move, Map<Integer, Double> result, boolean root){
            this.board = board;
            this.depth = depth;
            this.isMax = isMax;
            this.evalMove = move;
            this.finalResult = result;
            this.root = root;
        }

        @Override
        public synchronized void run() {
            double eval = miniMax(board, isMax, depth, -10000, 10000);
            finalResult.put(evalMove, eval);
            // if(!root){
            //     double eval = miniMax(board, isMax, depth, -10000, 10000);
            //     finalResult.put(evalMove, eval);
            // } else {
            //     // Get game logic
            //     ConnectFourGameLogic logic = new ConnectFourGameLogic();
            //     logic.setBoard(board);

            //     // Get all valid moves
            //     int player = isMax ? 1 : 2;
            //     ArrayList<Integer> moves = logic.getMoves(player);

            //     Map<Integer, Double> nodeResult = new ConcurrentHashMap<>();

            //     for(int move : moves){
            //         ConnectFourBoardLogic tempBoard = new ConnectFourBoardLogic();
            //         tempBoard.setBoard(board.getBoard());
            //         ConnectFourGameLogic tempLogic = new ConnectFourGameLogic();
            //         tempLogic.setBoard(tempBoard);
            //         tempLogic.doMove(move, player);

            //         MinimaxWorker worker = new MinimaxWorker(tempBoard, depth-1, !isMax, move, nodeResult, false);
            //         Thread thread = new Thread(worker);
            //         thread.start();
            //     }

            //     // Wait untill all results are back
            //     while (nodeResult.size() != moves.size()) {
            //         try {
            //             wait(10);
            //         } catch (InterruptedException ignored) { }
            //     }

            //     int bestMove = -1;
            //     double bestMoveEval = player == 1 ? -1000 : 1000;
            //     for(Map.Entry<Integer, Double> result : nodeResult.entrySet()){
            //         double eval = result.getValue();
            //         int move = result.getKey();
            //         if(isMax && eval > bestMoveEval || !isMax && eval < bestMoveEval){
            //             bestMoveEval = eval;
            //             bestMove = move;
            //         }
            //     }

            //     finalResult.put(evalMove, bestMoveEval);
            // }

            // System.out.println("Eval for move " + evalMove + " : " + eval);
            // System.out.println(result);
        }

        private double miniMax(GameBoardLogic board, boolean isMax, int depth, double alpha, double beta){
            // Get game logic
            ConnectFourGameLogic logic = new ConnectFourGameLogic();
            logic.setBoard(board);
    
            if(depth == 0 || logic.gameOver() != 0){
                return evaluate(board, depth);
            }
    
            double bestEval = isMax ? -1000 : 1000;
            int player = isMax ? 1 : 2;
            // Get all valid moves
            // ArrayList<Integer> moves = logic.getMoves(player);

            for(int move : ConnectFourMinimaxStrategyMulti.MOVE_ORDER){
            // for(int move : moves){
                if(logic.isValid(move)){
                    ConnectFourBoardLogic tempBoard = new ConnectFourBoardLogic();
                    tempBoard.setBoard(board.getBoard());
                    ConnectFourGameLogic tempLogic = new ConnectFourGameLogic();
                    tempLogic.setBoard(tempBoard);
                    tempLogic.doMove(move, player);

                    double eval = miniMax(tempBoard, !isMax, depth-1, alpha, beta);

                    if(isMax){
                        if(eval > bestEval) bestEval = eval;
                        if(eval > alpha) alpha = eval;
                    } else {
                        if(eval < bestEval) bestEval = eval;
                        if(eval < beta) beta = eval;
                    }

                    if(beta <= alpha) break;
                }
            }
            return bestEval;
        }
    
        private double evaluate(GameBoardLogic board, int depth){
            ConnectFourGameLogic logic = new ConnectFourGameLogic();
            logic.setBoard(board);
    
            ArrayList<Integer> discs = new ArrayList<>();
            for(int pos = 0; pos < board.getBoard().length; pos++){
                if(board.getBoardPos(pos) != 0){
                    discs.add(pos);
                }
            }
    
            // Check for every non empty pos if there is 3 in a row
            double combo = 0;
            for(int discPos : discs){
                if(logic.isCombination(discPos, 4, true)){
                    combo = board.getBoardPos(discPos) == 1 ? combo + 1 : combo - 1;
                }
            }
            combo = combo * 0.5;
    
            switch(logic.gameOver()){
                case 1:
                    // return (this.depth - depth + 1) + combo;
                    return (depth + 1) + combo;
                case 2:
                    return (-depth - 1) + combo;
                default:
                    return 0 + combo;
            }
        }

    }
}
