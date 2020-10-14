package src.gamemodules.reversigame;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import src.gameframework.GameBoardLogic;
import src.gameframework.aistrategies.MinimaxStrategy;

/**
 * Minimax AI for reversi.
 */
public class ReversiMinimaxStrategy extends MinimaxStrategy {

    // Used for generating random moves.
    private static final Random random = new Random();

    /**
     * This method iterates the valid moves and it it determines which move currently is the best move
     * to make. This method uses our own version of a minimax algorithm with alpha-beta pruning and
     * quiescence search. It also uses multithreading for even better performance.
     *
     * @return the best move.
     */
    @Override
    public synchronized int getBestMove(GameBoardLogic board, int player) {
        // Start the timeout timer.
        long startTime = System.currentTimeMillis();

        // Map that stores the calculated scores for valid moves.
        Map<Integer, Integer> results = new ConcurrentHashMap<>();

        // Board and logic to use later.
        ReversiBoardLogic reversiBoard = (ReversiBoardLogic) board;
        ReversiGameLogic logic = new ReversiGameLogic();
        logic.setBoard(reversiBoard);
        ArrayList<Integer> moves = logic.getMoves(player);

        // Change AI behaviour according to difficulty
        int depth = 0;
        switch(super.getDifficulty()){
            // EASY - just random moves.
            case 0:
                return getRandomValidMove(board, player);
            // MEDIUM - minimax but very low depth.
            case 1:
                depth = 1;
                break;
            // HARD - minimax on highest possible depth.
            case 2:
                float maxTime = getMaxTime();
                // Change depth dynamically.
                if(maxTime > 9.0){
                    if(moves.size() > 6){
                        depth = 4;
                    }else if(moves.size() > 3){
                        depth = 5;
                    }else {
                        depth = 6;
                    }
                } else if(maxTime > 4.0){
                    if(moves.size() > 2){
                        depth = 4;
                    } else {
                        depth = 5;
                    }
                } else{
                    if(moves.size() > 6){
                        depth = 3;
                    } else {
                        depth = 4;
                    }
                }
                break;
            // TESTING
            case 3:
                depth = 5;
        }

        // Choose if player should be maximizing our minimizing.
        int bestEval;
        boolean isMax;
        if(player == 1){
            bestEval = -10000;
            isMax = true;
        } else {
            isMax = false;
            bestEval = 10000;
        }

        int resultCount = 0;
        for(int move : moves){
            // Generate a temp board and do the move.
            ReversiBoardLogic newBoard = new ReversiBoardLogic();
            newBoard.setBoard(reversiBoard.getBoard());
            ReversiGameLogic newLogic = new ReversiGameLogic();
            newLogic.setBoard(newBoard);
            newLogic.doMove(move, player);

            // Give the new board to a minimax worker.
            ReversiMinimaxWorker worker = new ReversiMinimaxWorker(newBoard, depth, !isMax, move, results);
            Thread thread = new Thread(worker);
            thread.start();

            resultCount++;
        }

        // Wait until all results are back.
        boolean timeout = false;
        while(results.size() != resultCount && !timeout){
            try {
                Thread.sleep(10);
                // If it's taking to long we want to stop calculating moves and just work with
                // the results we have gotten so far. This is not the best way of implementing time
                // constraints, but it just acts as a fail-safe. Ideally we want this to never occur.
                if((System.currentTimeMillis() - startTime) / 1000.0 > (super.getMaxTime() - 0.2)){
                    System.err.println("A timeout occurred!");
                    timeout = true;
                }
            } catch (InterruptedException ignored) {}
        }

        // Choose the best result
        int bestMove = -1;
        if(!results.isEmpty()){
            for(Map.Entry<Integer, Integer> result : results.entrySet()){
                int eval = result.getValue();
                int move = result.getKey();
                if(isMax && eval > bestEval || !isMax && eval < bestEval){
                    bestEval = eval;
                    bestMove = move;
                }
            }
        } else {
            if(moves.size() > 0){
                bestMove = moves.get(0);
            }
        }
        results.clear();

        return bestMove;
    }

    /**
     * This method returns a random valid move.
     *
     * @param board the board that is used.
     * @param player the player to find for.
     * @return the random move.
     */
    private int getRandomValidMove(GameBoardLogic board, int player){
        ReversiGameLogic logic = new ReversiGameLogic();
        logic.setBoard(board);
        ArrayList<Integer> moves = logic.getMoves(player);

        if(moves.size() > 0){
            int choice = random.nextInt(moves.size());
            return moves.get(choice);
        } else {
            return -1;
        }
    }
}
