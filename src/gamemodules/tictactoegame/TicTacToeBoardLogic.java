package src.gamemodules.tictactoegame;

import src.gameframework.GameBoardLogic;

/**
 * This class implements the game board logic for the game Tic Tac Toe.
 */
public class TicTacToeBoardLogic extends GameBoardLogic {

    public TicTacToeBoardLogic(){
        initBoard();
    }

    /**
     * This method initialises the default gameBoard for tic tac toe.
     */
    @Override
    public void initBoard() {
        int[] defaultBoard = new int[9];
        setBoard(defaultBoard);
    }

    /**
     * This method prints a visual representation of the board. This is used to for debugging purposes.
     */
    @Override
    public void printBoard() {
        int[] board = getBoard();
        System.out.println("-------------");
        System.out.println("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
        System.out.println("-------------");
        System.out.println("| " + board[3] + " | " + board[4] + " | " + board[5] + " |");
        System.out.println("-------------");
        System.out.println("| " + board[6] + " | " + board[7] + " | " + board[8] + " |");
        System.out.println("-------------");
    }

    /**
     * This method returns the name of the game.
     *
     * @return the name of the game.
     */
    @Override
    public String getGame() {
        return "Tic Tac Toe";
    }
}
