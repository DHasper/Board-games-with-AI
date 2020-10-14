package src;

import java.util.ArrayList;
import java.util.Random;

import src.gamemodules.tictactoegame.TicTacToeBoardLogic;
import src.gamemodules.tictactoegame.TicTacToeGameLogic;
import src.gamemodules.tictactoegame.TicTacToeMinimaxStrategy;

public class TicTacToeExample {

    private TicTacToeBoardLogic board;
    private TicTacToeGameLogic logic;
    private TicTacToeMinimaxStrategy ai;
    private Random random;

    public static void main(String[] args){
        TicTacToeExample test = new TicTacToeExample();
        test.play();
    }

    public TicTacToeExample(){
        this.random = new Random();
        this.board = new TicTacToeBoardLogic();
        this.logic = new TicTacToeGameLogic();
        logic.setBoard(board);
        this.ai = new TicTacToeMinimaxStrategy();
    }

    private void play(){
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println("Now automatically playing a game of tic tac toe!");
        System.out.println("Player 1: random valid moves");
        System.out.println("Player 2: best move found by the AI");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        this.board.printBoard();

        int turn = 2;
        while(logic.gameOver() == 0){
            ArrayList<Integer> moves = logic.getMoves(turn);

            int move = -1;
            if(!moves.isEmpty()){
                move = turn == 2
                    ? ai.getBestMove(board, turn)
                    : moves.get(random.nextInt(moves.size()));
            }

            if(move != -1){
                logic.doMove(move, turn);
            }

            System.out.println("Player " + turn + " did move " + move);
            board.printBoard();

            turn = 3 - turn;
        }

        System.out.println("Game over!");
        switch(logic.gameOver()){
            case 1:
                System.out.println("The AI lost the game!");
                break;
            case 2:
                System.out.println("The AI won the game!");
                break;
            case 3:
                System.out.println("The game resulted in a draw!");
                break;
        }
    }
}
