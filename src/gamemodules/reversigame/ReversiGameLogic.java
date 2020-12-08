package src.gamemodules.reversigame;

import java.util.ArrayList;

import src.gameframework.GameBoardLogic;
import src.gameframework.GameLogic;

/**
 * This class contains logic for the game Reversi.
 */
public class ReversiGameLogic extends GameLogic {
    /**
     * Constructor for ReversiGameLogic that sets a default board.
     */
    public ReversiGameLogic() {
        ReversiBoardLogic reversiBoardLogic = new ReversiBoardLogic();
        setBoard(reversiBoardLogic);
    }

    /**
     * Method that returns all available valid moves for a given player.
     *
     * @param player integer representing the player that should be checked for.
     * @return ArrayList containing all valid moves.
     */
    @Override
    public ArrayList<Integer> getMoves(int player){
        ArrayList<Integer> result = new ArrayList<>();

        for(int pos = 0; pos < getBoard().getBoard().length; pos++){
            if(getBoard().getBoardPos(pos) == 0){
                for(int dir = 0; dir < 8; dir++){
                    if(checkDir(dir, pos, player)){
                        result.add(pos);
                        break;
                    }
                }
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
        for(int dir=0; dir < 8; dir++){
            if(checkDir(dir, pos, player)){
                flipDir(pos, dir, player);
            }
        }
    }

    /**
     * This method checks if the game is over and then returns the winner.
     *
     * @return int representing the winner of the game, 3 means a draw, 0 means the game is not over.
     */
    @Override
    public int gameOver(){
        if(getMoves(1).size() == 0 && getMoves(2).size() == 0){
            if(getDiscCount(1) > getDiscCount(2)){
                return 1;
            } else if(getDiscCount(1) < getDiscCount(2)){
                return 2;
            } else {
                return 3;
            }
        }
        return 0;
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
        return getMoves(player).contains(move);
    }

    /**
     * This method returns the amount of discs on the board with the specified state.
     *
     * @param state of the discs we want to count.
     * @return the amount of discs with specified state.
     */
    public int getDiscCount(int state){
        int count = 0;
        int[] board = getBoard().getBoard();
        for (int pos : board) {
            if (pos == state) {
                count++;
            }
        }
        return count;
    }

    /**
     * This method finds the position of a target using a direction.
     *
     * @param dir direction to find adjacent position for.
     * @param pos position to search for.
     * @return the position of the target.
     */
    private int getTarget(int dir, int pos){
        // Calculate position of target.
        int target = 0;
        switch(dir){
            case 0:
                target = pos - 9;
                break;
            case 1:
                target = pos - 8;
                break;
            case 2:
                target = pos - 7;
                break;
            case 3:
                target = pos + 1;
                break;
            case 4:
                target = pos + 9;
                break;
            case 5:
                target = pos + 8;
                break;
            case 6:
                target = pos + 7;
                break;
            case 7:
                target = pos - 1;
                break;
        }
        return target;
    }

    /**
     * Recursive method that turns all discs that need to be turned in a given direction.
     *
     * @param pos position to start at.
     * @param dir direction to check for.
     * @param player the player that should be checked for.
     */
    private void flipDir(int pos, int dir, int player){
        int target = getTarget(dir, pos);
        int targetState = getBoard().getBoardPos(target);
        if(targetState == 3 - player){
            getBoard().setBoardPos(target, player);
            flipDir(target, dir, player);
        }
    }

    /**
     * This method is used to call checkDir function without having to pass a boolean.
     * See next method for more.
     */
    private boolean checkDir(int dir, int pos, int color){
        return checkDir(dir, pos, color, false);
    }

    /**
     * This is a recursive method that checks if there are any flippable discs in a specific direction of a position.
     * This is used to determine if a move is valid.
     *
     * @param dir a integer representing a direction that needs to be checked.
     * @param pos integer of the position that needs to be checked.
     * @param player integer representing the color of the player we want to check for.
     * @param flipped boolean indicating if a flippable disc has been found.
     * @return a boolean, true = there is a flippable disc in this direction,
     *                    false = there is no flippable disc in this direction.
     */
    private boolean checkDir(int dir, int pos, int player, boolean flipped){
        // Filtering out vertical edge cases.
        int vEdge = pos % 8;
        switch(vEdge){
            // Left edge
            case 0:
                if(dir == 6 || dir == 7 || dir == 0){
                    return false;
                }
                break;
            // Right edge
            case 7:
                if(dir == 2 || dir == 3 || dir == 4){
                    return false;
                }
                break;
        }

        // Filtering out horizontal edge cases.
        if(pos < 8){
            if(dir == 0 || dir == 1 || dir == 2){
                return false;
            }
        } else if(pos > 55){
            if(dir == 4 || dir == 5 || dir == 6){
                return false;
            }
        }

        // Recursive part that checks if there is a flippable disc in direction.
        int target = getTarget(dir, pos);
        int targetState = getBoard().getBoardPos(target);
        if(targetState == 3 - player){
            if(checkDir(dir, target, player, true)){
                return true;
            }
        } else if(targetState == player && flipped){
            return true;
        }

        return false;
    }

    /**
     * Method that calculates the total amount of opponent discs that can be flipped at this moment.
     *
     * @param board the board that should be checked for.
     * @param player the player that should be checked for.
     * @return the amount of opponent discs that can be flipped.
     */
    public int getPossibleFlips(GameBoardLogic board, int player){
        ReversiGameLogic logic = new ReversiGameLogic();
        logic.setBoard(board);
        ArrayList<Integer> moves = logic.getMoves(player);

        int result = 0;
        for(int move : moves){
            ReversiBoardLogic tempBoard = new ReversiBoardLogic();
            tempBoard.setBoard(board.getBoard());
            ReversiGameLogic tempLogic = new ReversiGameLogic();
            tempLogic.setBoard(tempBoard);
            int oldDiscs = tempLogic.getDiscCount(player);
            tempLogic.doMove(move, player);
            int newDiscs = tempLogic.getDiscCount(player) - 1;
            result += newDiscs - oldDiscs;
        }

        return result;
    }

    /**
     * This method returns the amount of stable discs for a given player.
     * Stable discs are discs that are stuck in position and cannot be changed for the entirety of
     * the game.
     *
     * @param board the board that should be checked for.
     * @param player the player that should be checked for.
     * @return the amount of stable discs.
     */
    public int getStableDiscs(GameBoardLogic board, int player){
        ArrayList<Integer> positions = new ArrayList<>();

        for(int i=0; i<4; i++){
            for(int pos : getStablePositionsFromCorner(i, board, player)){
                if(!positions.contains(pos)){
                    positions.add(pos);
                }
            }
        }
        return positions.size();
    }

    /**
     * Method used by getStableDiscs to find all stable positions for a specific corner.
     *
     * @param corner the corner that should be checked for.
     * @param board the board that should be checked for.
     * @param player the player that should be checked for.
     * @return ArrayList containing stable positions.
     */
    private ArrayList<Integer> getStablePositionsFromCorner(int corner, GameBoardLogic board, int player){
        ArrayList<Integer> positions = new ArrayList<>();

        int xMod = 0;
        int yMod = 0;
        int startPos = 0;
        switch(corner){
            case 0: // top left
                startPos = 0;
                xMod = 1;
                yMod = 1;
                break;
            case 1: // top right
                startPos = 7;
                xMod = -1;
                yMod = 1;
                break;
            case 2: // bottom left
                startPos = 56;
                xMod = 1;
                yMod = -1;
                break;
            case 3: // bottom right
                startPos = 63;
                xMod = -1;
                yMod = -1;
                break;
        }

        int pos = startPos;
        if(board.getBoardPos(0) == player){
            boolean reachedEndX = false;
            boolean reachedEndY = false;
            int row = 0;
            int maxCol = 7;
            while(!reachedEndY){
                int col = 0;
                while(!reachedEndX){
                    if(board.getBoardPos(pos) == player && col < maxCol){
                        if(!positions.contains(pos)){
                            positions.add(pos);
                        }
                        pos += xMod;
                        col++;
                    } else {
                        maxCol = col - 1;
                        if(maxCol == 0) maxCol = 1;
                        reachedEndX = true;
                    }
                }

                row++;
                pos = startPos + (8 * yMod) * row;
                if(row == 8 || board.getBoardPos(pos) != player ){
                    reachedEndY = true;
                }
                reachedEndX = false;
            }
        }

        return positions;
    }
}
