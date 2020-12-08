package src.gamemodules.connectfourgame;

import java.util.ArrayList;
import java.util.Random;

import src.gameframework.GameBoardLogic;
import src.gameframework.aistrategies.MinimaxStrategy;

/**
 * Really simple AI that always chooses a move that wins and always
 * chooses a move that won't cause the opponent to win in the next turn.
 * If it can't win or prevent a loss next turn, it chooses a random valid move.
 */
public class ConnectFourSimpleStrategy extends MinimaxStrategy {

    Random random = new Random();

    @Override
    public int getBestMove(GameBoardLogic board, int player) {
        ConnectFourGameLogic logic = new ConnectFourGameLogic();
        logic.setBoard(board);
        
        ArrayList<Integer> moves = logic.getMoves(player);
        ArrayList<Integer> results = new ArrayList<>();
        ArrayList<Integer> opResults = new ArrayList<>();

        // Do every move we can and see if we won the game
        for(int move : new int[]{0,1,2,3,4,5,6}){
            if(moves.contains(move)){
                ConnectFourBoardLogic tempBoard = new ConnectFourBoardLogic();
                tempBoard.setBoard(board.getBoard());
                ConnectFourGameLogic tempLogic = new ConnectFourGameLogic();
                tempLogic.setBoard(tempBoard);
                tempLogic.doMove(move, player);
                results.add(tempLogic.gameOver());
            } else {
                results.add(0);
            }
        }

        // Do every move for the opponent and see if we lost the game
        for(int move : new int[]{0,1,2,3,4,5,6}){
            if(moves.contains(move)){
                ConnectFourBoardLogic tempBoard = new ConnectFourBoardLogic();
                tempBoard.setBoard(board.getBoard());
                ConnectFourGameLogic tempLogic = new ConnectFourGameLogic();
                tempLogic.setBoard(tempBoard);
                tempLogic.doMove(move, 3 - player);
                opResults.add(tempLogic.gameOver());
            } else {
                opResults.add(0);
            }
        }

        // If there is any move that wins the game, choose this one instead
        for(int i = 0; i < results.size(); i++){
            if(results.get(i) == player){
                return i;
            }
        }

        // If there is any move that loses us the game if we dont choose it, choose this one
        for(int i = 0; i < opResults.size(); i++){
            if(opResults.get(i) == 3 - player){
                return i;
            }
        }

        // Choose a random move if there is no winning or not losing move.
        return moves.get(random.nextInt(moves.size()));
    }
}
