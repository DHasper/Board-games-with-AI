package src.gameframework;

import java.util.ArrayList;

/**
 * Abstract class that should be used for game logic.
 */
public abstract class GameLogic {

    private GameBoardLogic board;

    /**
     * This method sets the gameboard that is used to play the game.
     * @param board the gameboard that will be uses.
     */
    public void setBoard(GameBoardLogic board){
        this.board = board;
    }

    /**
     * This method is used to get to the gameboard.
     * @return GameBoardLogic with current GameBoardLogic.
     */
    public GameBoardLogic getBoard(){
        return board;
    }

    /**
     * This method should check for all valid moves.
     * @param player integer representing the player that should be checked for.
     * @return ArrayList with integers containing all positions of valid moves.
     */
    public abstract ArrayList<Integer> getMoves(int player);

    /**
     * This method shold be used to make a move on the gameboard.
     * @param pos position of the move.
     * @param player player that is making the move.
     */
    public abstract void doMove(int pos, int player);

    /**
     * This method should check if the game is over, and who won the game.
     * @return integer representing the player that won the game.
     */
    public abstract int gameOver();

    /**
     * This method should check whether a move is valid or not.
     * @param move the move that should be checked.
     * @param move the player that should be checked for.
     * @return true if valid, false if not.
     */
    public abstract boolean isValid(int move, int player);
}
