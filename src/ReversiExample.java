package src;

import java.util.ArrayList;
import java.util.Random;

import src.gamemodules.reversigame.ReversiBoardLogic;
import src.gamemodules.reversigame.ReversiGameLogic;
import src.gamemodules.reversigame.ReversiMinimaxStrategy;

public class ReversiExample {

    private ReversiBoardLogic board;
    private ReversiGameLogic logic;
    private ReversiMinimaxStrategy ai;
    private Random random;

    public static void main(String[] args){
        ReversiExample test = new ReversiExample();
        test.play();
    }

    public ReversiExample(){
        this.random = new Random();
        this.board = new ReversiBoardLogic();
        this.logic = new ReversiGameLogic();
        logic.setBoard(board);
        this.ai = new ReversiMinimaxStrategy();
    }

    private void play(){
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println("Now automatically playing a game of reversi!");
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
        System.out.println("The AI ended the game with " + logic.getDiscCount(2) + " stones.");
    }
}
