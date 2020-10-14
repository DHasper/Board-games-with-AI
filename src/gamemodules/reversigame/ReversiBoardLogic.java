package src.gamemodules.reversigame;

import src.gameframework.GameBoardLogic;

/**
 * This class implements the game board logic for the game Reversi.
 */
public class ReversiBoardLogic extends GameBoardLogic {

    /**
     * Constructor for the reversi board that call initBoard.
     */
    public ReversiBoardLogic(){
        initBoard();
    }

    /**
     * This method initialises the default gameBoard for reversi.
     */
    @Override
    public void initBoard() {
        // 0 = unoccupied 1 = white 2 = black
        int[] defaultBoard = new int[64];
        defaultBoard[27] = 1;
        defaultBoard[28] = 2;
        defaultBoard[35] = 2;
        defaultBoard[36] = 1;
        setBoard(defaultBoard);
    }

    /**
     * This method prints a visual representation of the board. This is used to for debugging purposes.
     */
    @Override
    public void printBoard() {
        int i = 0;
        for(int row=0; row < 8; row++){
            // Print the position of the first spot in the gameboard, so it's easier to identify the positions
            // when debugging. Also print a extra " " for the first 2 numbers so they line up with the others.
            if(row <= 1){
                System.out.print(" ");
            }
            System.out.print(row * 8 + " - ");

            for(int col=0; col < 8; col++){
                System.out.print(getBoardPos(i) + " ");
                i++;
            }
            System.out.println();
        }
        System.out.println("-=-=-=-=-=-=-=-=-=-=-");
    }

    /**
     * This method returns the name of the game.
     *
     * @return the name of the game.
     */
    @Override
    public String getGame() {
        return "Reversi";
    }
}
