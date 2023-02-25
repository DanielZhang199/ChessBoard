# Personal Project: Working Chess Board

## Proposed Functionality

- Display a representation of a chess board (obviously).
- Allow the user(s) to manipulate the board state in accordance to the rules of chess.
- Be able to determine when the game is over; this can be from checkmate, stalemate, 3-fold repetition, or 50 move rule.
- Perhaps in the future, add engine analysis or time control functionality.

### Who would use this application?
Such an application can be used by anyone who may want to play chess, but don't have a chessboard available. In other
words, my application would work the same as any other digital board game.

### Why I chose this project:
I found this project idea interesting as I enjoy playing chess occasionally, so I might find it fun to play with.
A chess board application is very simple in nature, but it does meet all the guidelines for our term project. I believe
that this would be a good project for someone who does not have much experience with Java, but still have enough 
challenges to not feel too trivial.


## User Stories
1. As a user, I want to be able to reset the board to the starting state.
   1. ~~This must be possible only though offering a draw **and** resignation.~~ Since this game is played on a single 
   computer, it would make sense that the board can be reset at any point since both players could determine the winner
   in person.
2. As a user, I want to be able to see a representation of the board.
3. As a user, I want to be able to move pieces around on the board.
   1. Only as allowed by the rules of chess. 
   2. This includes removing a piece from the board when moving to the position of an enemy piece.
4. As a user, I want to be able to see previous moves, preferably both graphically and through chess notation. 
*(This requires adding an X to a Y)*
5. As a user, I want the game to automatically detect checkmate or stalemate positions.


## Board Grid
![8x8 grid from 0-63](grid.png "Putting this here for future reference")