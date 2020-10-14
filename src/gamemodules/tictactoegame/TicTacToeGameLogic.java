package src.gamemodules.tictactoegame;

import java.util.ArrayList;

import src.gameframework.GameLogic;

/**
 * This class contains logic for the game Tic Tac Toe.
 */
public class TicTacToeGameLogic extends GameLogic {
    /**
     * Constructor for ReversiGameLogic that sets a default board.
     */
    public TicTacToeGameLogic() {
        TicTacToeBoardLogic ticTacToeBoardLogic = new TicTacToeBoardLogic();
        setBoard(ticTacToeBoardLogic);
    }

    /**
     * Method that returns all available valid moves for a given player.
     *
     * @param player integer representing the player that should be checked for.
     * @return ArrayList containing all valid moves.
     */
    @Override
    public ArrayList<Integer> getMoves(int player) {
        ArrayList<Integer> result = new ArrayList<>();

        for(int pos = 0; pos<getBoard().getBoard().length; pos++){
            if(getBoard().getBoardPos(pos) == 0){
                result.add(pos);
            }
        }

        return result;
    }

    /**
     * This method does a move on the gameboard and changes the board accordingly.
     * The method assumes a move is correct.
     *
     * @param pos position of the move.
     * @param player player that is making the move.
     */
    @Override
    public void doMove(int pos, int player) {
        getBoard().setBoardPos(pos, player);
    }

    /**
     * This method checks if the game is over and then returns the winner.
     *
     * @return int representing the winner of the game, 3 means a draw, 0 means the game is not over.
     */
    @Override
    public int gameOver() {
        TicTacToeBoardLogic b = (TicTacToeBoardLogic) getBoard();
        int[] board = b.getBoard();

        // search for a winner
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0:
                    if(board[0] == board[1] && board[1] == board[2] && board[0] != 0) {
                        String s = String.valueOf(board[0]);
                        return Integer.parseInt(s);
                    }
                    break;
                case 1:
                    if(board[3] == board[4] && board[4] == board[5] && board[4] != 0) {
                        String s = String.valueOf(board[4]);
                        return Integer.parseInt(s);
                    }
                    break;
                case 2:
                    if(board[6] == board[7] && board[7] == board[8] && board[6] != 0) {
                        String s = String.valueOf(board[6]);
                        return Integer.parseInt(s);
                    }
                    break;
                case 3:
                    if(board[0] == board[3] && board[3] == board[6] && board[0] != 0) {
                        String s = String.valueOf(board[0]);
                        return Integer.parseInt(s);
                    }
                    break;
                case 4:
                    if(board[1] == board[4] && board[4] == board[7] && board[4] != 0) {
                        String s = String.valueOf(board[4]);
                        return Integer.parseInt(s);
                    }
                    break;
                case 5:
                    if(board[2] == board[5] && board[5] == board[8] && board[2] != 0) {
                        String s = String.valueOf(board[2]);
                        return Integer.parseInt(s);
                    }
                    break;
                case 6:
                    if(board[0] == board[4] && board[4] == board[8] && board[0] != 0) {
                        String s = String.valueOf(board[0]);
                        return Integer.parseInt(s);
                    }
                    break;
                case 7:
                    if(board[2] == board[4] && board[4] == board[6] && board[2] != 0) {
                        String s = String.valueOf(board[2]);
                        return Integer.parseInt(s);
                    }
                    break;
            }
        }

        // check if game is not over yet
        for(int value : board) {
            if(value == 0) {
                return 0;
            }
        }

        // when the game ended in a draw
        return 3;
    }

    /**
     * This method checks whether a move is valid or not.
     *
     * @param move the move that should be checked for.
     * @param player the player that should be checked for.
     * @return false if move is invalid, true if move is valid.
     */
    @Override
    public boolean isValid(int move, int player) {
        return getBoard().getBoardPos(move) == 0;
    }
}
