package src.gamemodules.connectfourgame;

import src.gameframework.GameBoardLogic;

/**
 * This class implements the game board logic for the game Connect Four.
 */
public class ConnectFourBoardLogic extends GameBoardLogic {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * This method initialises the default gameBoard for connect four.
     */
    @Override
    public void initBoard() {
        int[] defaultBoard = new int[42];
        setBoard(defaultBoard);
    }

    /**
     * This method prints a visual representation of the board. This is used to for debugging purposes.
     */
    @Override
    public void printBoard() {
        int pos = 0;
        for(int row=0; row < 6; row++){
            if(row == 0){
                for(int i=0; i < 7; i++){
                    System.out.print(i + "  ");
                }
                System.out.println(" 1 = " + ANSI_RED + "red" + ANSI_RESET + ", 2 = " + ANSI_YELLOW + "yellow");
            }
            for(int column=0; column < 7; column++){
                switch(getBoardPos(pos)){
                    case 0:
                        System.out.print(ANSI_WHITE_BACKGROUND + "  ");
                        break;
                    case 1:
                        System.out.print(ANSI_RED_BACKGROUND + "  ");
                        break;
                    case 2:
                        System.out.print(ANSI_YELLOW_BACKGROUND + "  ");
                        break;
                }
                System.out.print(ANSI_BLUE_BACKGROUND + " " + ANSI_RESET);
                pos++;
            }
            System.out.println(' ');
        }
    }

    /**
     * This method returns the name of the game.
     *
     * @return the name of the game.
     */
    @Override
    public String getGame() {
        return "Connect Four";
    }

}