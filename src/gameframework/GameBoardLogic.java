package src.gameframework;

/**
 * Abstract class for logic for gameboards.
 */
public abstract class GameBoardLogic {

    // This variable stores all states within the gameBoard
    private int[] board;

    /**
     * This method should initialise the gameBoard.
     */
    public abstract void initBoard();

    /**
     * This method should print the board. This is used to simplify debugging.
     */
    public abstract void printBoard();

    /**
     * This method is used to set the state of a position within the board
     * @param pos the position within the board that will be changed.
     * @param state the state it should be changed to.
     */
    public void setBoardPos(int pos, int state){
        board[pos] = state;
    }

    /**
     * This method is used to return the state of a position within the board.
     * @param pos the position in the board that needs to be returned.
     * @return int that represents the state of a position.
     */
    public int getBoardPos(int pos){
        return board[pos];
    }

    /**
     * This method is used to return the board.
     * @return int array that represents our game board.
     */
    public int[] getBoard(){
        return board;
    }

    /**
     * This method is used to set the board. This should always be used on board initialisation.
     * @param newBoard a int array that represents the new game board.
     */
    public void setBoard(int[] newBoard){
        board = new int[newBoard.length];
        for(int i=0; i<newBoard.length; i++){
            board[i] = newBoard[i];
        }
    }

    /**
     * This method resets the board back to its begin state.
     */
    public void resetBoard() {
        initBoard();
    }

    public abstract String getGame();
}
