# Personal Project: Chess Board With GUI

## Functionality
- Displays a representation of a chess board (obviously).
- Allow the user(s) to manipulate the board state in accordance to the rules of chess.
- Able to determine when the game is over; excludes 3-fold repetition or 50 move rule due to design/time limitations.
- In theory, should work like any other chess board, except for the above limitations, as well as not being able to
choose the piece being promoted into (always a queen).

### Why I chose this project:
I found this project idea interesting as I enjoy playing chess occasionally, so I might find it fun to play with.
A chess board application is very simple in nature, but requires extensive planning and design considerations. I believe
that this would be a great first project, as I do not have any prior experience with this language, while still having  
enough challenges to not feel too trivial.

### TODO:
1. Fix undo castling bug
2. Refactor `toNotation(Move)` to be a method of `Move` objects, rather than a static method under `MoveList`.
3. Reimplement multiple save files (?)

## UML diagram
![UML Diagram](UML_Design_Diagram.png)

### Reflection
From the diagram above, it initially looks like my code is structured and somewhat organized. 
However, there is quite a bit of coupling between classes, and certain functions in classes that can be further subdivided.
Most notably, the Piece class and GameBoard class are *very* strongly coupled together. Moving a piece also requires updating the position of the piece on the board;
pieces stored in GameBoard are organized by their position in a HashMap. Finding the moves of a piece also requires the knowledge of every other piece.
Knowing this, it would be better to separate the GameBoard class into 2 classes, one that handles the game of Chess, and one that handles 
the location of every piece on the board. This second class could be a field of each piece, so functions in the Piece class would not rely on the entirety of GameBoard and would work more independently.

Of course, the prior proposal would require the rewriting of nearly all model package, and would be highly impractical.
A more likely and useful refactoring would be the separation of the GUI with the actual chess game. This would allow me to keep both 
the command line interface (which was recently deleted) along with the current graphic interface, and would make it substantially easier to work on UI improvements.

The last refactoring proposal would be to change how the data is saved and loaded. Right now, data can be saved
by saving the entirety of MoveList, which requires saving every Move and thus every Piece that made the move. 
This is technically unnecessary, as the only data that is needed to make moves from the starting position of the board to the saved position
are starting and ending squares. This is because it can be assumed that all moves made are legal, and the data of which piece made which move is only
relevant when undoing moves. 

I will likely not be implementing these proposed changes as I am not planning on releasing this code for any reason
other than to build my portfolio at this point in time. There are many better chess applications available on the
internet and this project was only intended to be for education/practice rather than a useful application.

## Board Coordinate System
![8x8 grid from 0-63](grid.png)