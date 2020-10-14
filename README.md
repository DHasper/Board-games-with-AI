# Board games + AI

This is a collection of AI for different board games. As of right there are two games:

- tic tac toe
- reversi

I am planning to add more games, different types of AI, and a GUI to visualize games. 

## Tic tac toe

The tic tac toe AI uses a simple minimax strategy to calculate the best move. The AI will never make a mistake so games will always result in a win or draw for the AI. If the AI plays against itself, the games will always result in a draw. The AI also has some lower difficulty levels where it doesn't always win.

## Reversi 

The reversi AI was used in a competition for a university project, where it had to compete against other AI to see which one is the strongest. To be as strong as possible it uses a combination of multiple techniques. It uses a minimax algorithm with alpha-beta pruning and a simple version of quiescence search. The board evaluation is based on the mobility and stability. It's also multi-threaded to use the CPU as much as possible, to be able to explore more possibilities in less time. The time to find a move, and difficulty of the AI is also adjustable.