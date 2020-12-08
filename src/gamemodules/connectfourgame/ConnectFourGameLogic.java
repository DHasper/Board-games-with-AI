package src.gamemodules.connectfourgame;

import java.util.ArrayList;

import src.gameframework.GameLogic;

public class ConnectFourGameLogic extends GameLogic {

    /**
     * Constructor for ConnectFourGameLogic that sets a default board.
     */
    public ConnectFourGameLogic() {
        ConnectFourBoardLogic connectFourBoardLogic = new ConnectFourBoardLogic();
        setBoard(connectFourBoardLogic);
    }

	@Override
	public ArrayList<Integer> getMoves(int player) {
        ArrayList<Integer> result = new ArrayList<>();
        
        for(int col = 0; col < 7; col++){
            if(getBoard().getBoardPos(col) == 0){
                result.add(col);
            }
        }

        return result;
	}

	@Override
	public void doMove(int pos, int player) {
        int move = -1;
        int oldPos = pos;

        while(move == -1){
            int newPos = oldPos + 7;
            if(getBoard().getBoardPos(newPos) != 0){
                move = oldPos;
            } else if(newPos == (pos + 35)){
                move = newPos;
            }
            oldPos = newPos;
        }

        getBoard().setBoardPos(move, player);
	}

	@Override
	public int gameOver() {
        // Find all none empty positions
        ArrayList<Integer> discs = new ArrayList<>();
        for(int pos = 0; pos < getBoard().getBoard().length; pos++){
            if(getBoard().getBoardPos(pos) != 0){
                discs.add(pos);
            }
        }

        // Check for every non empty pos if there is 4 in a row
        for(int discPos : discs){
            if(isCombination(discPos, 4, false)){
                return getBoard().getBoardPos(discPos);
            }
        }

        // If no one has won yet and there are 42 stones on the board, there is a draw
        if(discs.size() == 42){
            return 3;
        }

        // If no one has won and there is no draw, the game is not over yet
		return 0;
    }

    public boolean isValid(int col){
        return getBoard().getBoardPos(col) == 0;
    }

    public boolean isCombination(int pos, int len, boolean vul){
        //     0    1    2    3    4    5    6
        //     7    8    9    10   11   12   13
        //     14   15   16   17   18   19   20
        //     21   22   23   24   25   26   27
        //     28   29   30   31   32   33   34
        //     35   36   37   38   39   40   41
        // links rechts: 6:1 5:2 4:3 3:4 2:5
        // onder boven: 6:5 5:4 4:3 3:2 2:1
        // onder naar rechts boven: 2:5 3:4 4:3 5:2 6:1
        // onder naar links boven:  2:1 3:2 4:3 5:4 6:5
        // haal alle gebruikte tiles op 
        // voor elke gebruikte tile:
        // check van links naar rechts wanneer col <= 3
        // check van onder naar boven wanneer row >= 3
        // check diagonaal naar rechts wanneer col <= 3 en row >= 3
        // check diagonaal naar links wanneer col >= 3 en row >= 3
        int col = getCol(pos);
        int row = getRow(pos);

        // check left to right
        if(col <= 3 + (4-len) && checkDir(0, pos, len, vul)){
            return true;
        }

        if(row >= 3 - (4-len)){
            // check bottom to top
            if(checkDir(1, pos, len, vul)){
                return true;
            }

            // check diagonal up right
            if(col <= 3 + (4-len) && checkDir(2, pos, len, vul)){
                return true;
            }

            // check diagonal up left
            if(col >= 3 - (4-len) && checkDir(3, pos, len, vul)){
                return true;
            }
        }

        // only check these directions when we are looking for vulnerable combinations
        // check right to left
        if(vul && col >= 3 - (4-len) && checkDir(4, pos, len, vul)){
            return true;
        }

        if(vul && row <= 2 + (4-len)){
            // check diagonal down right
            if(col <= 3 + (4-len) && checkDir(5, pos, len, vul)){
                return true;
            }

            // check diagonal down left
            if(col >= 3 - (4-len) && checkDir(6, pos, len, vul)){
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if there is a combination of a certain amount of stones in a row.
     * @param dir direction to check in
     * @param pos position to start from
     * @param len combination length to check 4, i.e. 4 to check if a player has 4 in a row
     * @return boolean that indicates if there is a certain amount of stones in a row.
     */
    private boolean checkDir(int dir, int pos, int len, boolean vul){
        int player = getBoard().getBoardPos(pos);
        int newPos = pos;
        for(int i = 0; i < len-1; i++){
            newPos = getTarget(dir, newPos);
            if(vul && i == len-2 && getBoard().getBoardPos(newPos) == 0){
                return true;
            }
            if(getBoard().getBoardPos(newPos) != player){
                return false;
            }
        }
        return true;
    }
    
    private int getTarget(int dir, int pos){
        switch(dir){
            // left to right
            case 0:
                return pos + 1;
            // bottom to top
            case 1:
                return pos - 7;
            // diagonal up right
            case 2:
                return pos - 6;
            // diagonal up left
            case 3:
                return pos - 8;
            // right to left
            case 4:
                return pos - 1;
            // diagonal down right
            case 5:
                return pos + 8;
            // diagonal down left
            case 6:
                return pos + 6;
            default:
                return -1;
        }
    }

	@Override
	public boolean isValid(int move, int player) {
		return getMoves(player).contains(move);
    }
    
    public boolean midEmpty(){
        return this.getBoard().getBoardPos(31) == 0;
    }
    
    private int getCol(int pos){
        return pos % 7;
    }

    private int getRow(int pos){
        return pos / 7;
    }
}
