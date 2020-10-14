package src.gameframework.aistrategies;

import src.gameframework.GameAI;

public abstract class MinimaxStrategy implements GameAI {

    // Maximum time per move.
    private float maxTime = 10;

    // Difficulty for our AI.
    private int difficulty = 3;

    /**
     * Method to get the difficulty of our AI.
     * This can be 0 = EASY, 1 = MEDIUM or 2 = HARD.
     *
     * @return the difficulty.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Method to change the difficulty of our AI.
     * This can be 0 = EASY, 1 = MEDIUM or 2 = HARD.
     *
     * @param difficulty the difficulty.
     */
    public void setDifficulty(int difficulty) {
        if(difficulty > 2 || difficulty < 0){
            System.err.println("Not a valid difficulty : " + difficulty);
        } else {
            this.difficulty = difficulty;
            System.out.println("Difficulty is now set to : " + difficulty);
        }
    }

    /**
     * Method to get the maximum time to calculate a move.
     *
     * @return maximum time.
     */
    public float getMaxTime() {
        return maxTime;
    }

    /**
     * Method to change the maximum time to calculate a move.
     *
     * @param maxTime maximum time.
     */
    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

}
