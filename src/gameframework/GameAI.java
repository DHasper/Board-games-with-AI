package src.gameframework;

public interface GameAI {

    /**
     * This method should iterate the whole game board and it should determine which move currently is the best move
     * to make. This method should use the computeAlgorithm() method. Which is an implementation of the algorithm or AI
     * used. (E.g. Minimax, Neural Network, Tabular Q learning, etc).
     *
     * @return the best move.
     */
    public int getBestMove(GameBoardLogic board, int player);

    /**
     * This method should return the difficulty of our AI.
     * This can be 0 = EASY, 1 = MEDIUM or 2 = HARD.
     *
     * @return the difficulty.
     */
    public int getDifficulty();

    /**
     * This method should be able to change the difficulty of our AI.
     * This can be 0 = EASY, 1 = MEDIUM or 2 = HARD.
     *
     * @param difficulty the difficulty.
     */
    public void setDifficulty(int difficulty);

    /**
     * This method should return to get the maximum time to calculate a move.
     *
     * @return maximum time.
     */
    public float getMaxTime();

    /**
     * This method should be able to change the maximum time to calculate a move.
     *
     * @param maxTime maximum time.
     */
    public void setMaxTime(float maxTime);

}
