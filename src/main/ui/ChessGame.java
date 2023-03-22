package ui;

import model.*;
import model.exceptions.NotValidSquareException;
import persistence.JsonConverter;
import ui.exceptions.InvalidMoveMadeException;
import ui.exceptions.PieceBelongEnemyException;
import ui.exceptions.PieceNoMovesException;
import ui.exceptions.PieceNotExistException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


// Class handling all UI functionality for chess game, instantiating this class runs the application.
public class ChessGame {
    private GameBoard board;
    private MoveList moves;
    private Scanner input;
    private static final int NUM_SAVE_SLOTS = 3;
    private static final String SAVE_FILE_PREFIX = "./data/saveFile";

    // EFFECTS: runs the chess application
    public ChessGame() {
        init();
    }

    // MODIFIES: this
    // EFFECTS: initiates the class, and runs main loop of the game
    private void runGame() {

        boolean notDone = true;
        String action;
        displayInfo();
        System.out.println("Input coordinates of piece or a command (enter 'h' for help):");
        while (notDone) {
            action = input.next().toLowerCase();
            if (action.startsWith("q")) {
                optionalSave();
                notDone = false;
            } else {
                handleAction(action);
            }
            if (board.getStatus() == null) {
                System.out.println("Input coordinates of piece or a command:");
            } else {
                System.out.println("Input a command:");
            }
        }
        System.out.println("\n --Exiting Application--");
    }

    private void handleAction(String action) {
        if (action.startsWith("s")) {
            handleSave();
        } else if (action.startsWith("l")) {
            handleLoad();
        } else if (action.startsWith("r")) {
            resetBoard();
        } else if (action.startsWith("u")) {
            undoMove();
        } else if (action.startsWith("m")) {
            showMoves("");
        } else if (action.startsWith("h")) {
            displayHelp();
        } else if (board.getStatus() == null) {
            handleMove(action);
        } else {
            System.out.println("You cannot move pieces; the game is over!");
        }
    }

    private void displayHelp() {
        System.out.println("Command List:");
        System.out.println("- quit ('q'):");
        System.out.println("- save ('s'):");
        System.out.println("- load ('l'):");
        System.out.println("- reset board ('r'):");
        System.out.println("- undo ('u'):");
        System.out.println("- show moves ('m'):");
        System.out.println("- help ('h'):\n");
        System.out.println("To make a move, type in the coordinates of the piece you wish to move, "
                + "as if you were making a move on a graphical chess board.");
    }

    // MODIFIES: this
    // EFFECTS: Asks user to choose save file, tries to load in new save file by overwriting moves and board successful
    // otherwise, displays relevant error info
    private void handleLoad() {
        displaySaveSlots();
        int choice = input.nextInt();
        if (choice < 0 || choice > NUM_SAVE_SLOTS) {
            System.out.println("Invalid save slot chosen.");
            return;
        }
        try {
            loadGame(choice);
        } catch (IOException e) {
            System.out.println("An error occurred while loading save, or this save file does not exist.");
        }  catch (InvalidMoveMadeException e) {
            System.out.println("There was an issue loading the board; the save file may be corrupted.");
            System.out.println("Returning to original game ...");
        }
    }

    private void loadGame(int slot) throws InvalidMoveMadeException, IOException {
        MoveList tempML = JsonConverter.getMoveList(SAVE_FILE_PREFIX + slot + ".json");
        GameBoard tempBoard = new GameBoard();
        for (Move m : tempML.getMoveList()) {
            if (!tempBoard.movePiece(m.getStart(), m.getEnd())) {
                throw new InvalidMoveMadeException();
            }
        }
        moves = tempML;
        board = tempBoard;
        System.out.println("Game loaded from slot " + slot + ".");
        if (board.checkStatus()) {
            handleGameEnd();
        } else {
            displayInfo();
        }
    }

    // MODIFIES: this
    // EFFECTS: Asks user to choose save file, tries to save current data into the save file or displays relevant error
    private void handleSave() {
        displaySaveSlots();
        int choice = input.nextInt();
        if (choice < 0 || choice > NUM_SAVE_SLOTS) {
            System.out.println("Invalid save slot chosen; game was not saved.");
            return;
        }
        try {
            JsonConverter.saveMoveList(moves, SAVE_FILE_PREFIX + choice + ".json");
            System.out.println("Game saved to slot " + choice + ".");
            displayInfo();
        } catch (IOException e) {
            System.out.println("An error occurred while saving.");
        }
    }

    // EFFECTS: displays the save slots available for the user with information about the number of moves and last move
    private void displaySaveSlots() {
        System.out.println("Save Slots: \n");
        for (int i = 1; i <= NUM_SAVE_SLOTS; i++) {
            try {
                List<String> savedMoves = JsonConverter.getNotationList(SAVE_FILE_PREFIX + i + ".json");
                if (savedMoves.isEmpty()) {
                    System.out.println(i + ". EMPTY SLOT");
                }
                System.out.println(i + ". Turn " + (savedMoves.size() / 2 + 1)
                        + " | Last Move: " + savedMoves.get(savedMoves.size() - 1));
            } catch (IOException e) {
                System.out.println(i + ". EMPTY SLOT");
            }
        }
        System.out.println("Select save slot (1 - " + NUM_SAVE_SLOTS + "):");
    }

    // EFFECTS: Gives user option to save the game
    private void optionalSave() {
        System.out.println("Would you like to save the game? (y/N)");
        String reply = input.next().toLowerCase();
        if (reply.startsWith("y")) {
            handleSave();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes fields
    private void init() {
        System.out.println("Load Graphic User Interface? (Y/n)");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        if (input.next().toLowerCase().startsWith("n")) {
            board = new GameBoard();
            moves = new MoveList();
            runGame();
        } else {
            new ChessGameGUI();
        }
    }

    // EFFECTS: displays the turn and a console representation of the board to the console
    private void displayInfo() {
        int turn = moves.getSize();
        if (turn % 2 == 0) {
            System.out.println("Turn " + (turn / 2 + 1) + ", White to Move:");
        } else {
            System.out.println("Turn " + (turn / 2 + 1) + ", Black to Move:");
        }
        displayBoard();
    }

    // EFFECTS: prints out the board to console
    private void displayBoard() {
        ArrayList<String> squares = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            if (board.existsPiece(i)) {
                Piece p = board.getPiece(i);
                if (p.getSide().equals("W")) {
                    squares.add(p.getName());
                } else {
                    squares.add(p.getName().toLowerCase());
                }
            } else {
                squares.add(" ");
            }
        }

        System.out.println("   a   b   c   d   e   f   g   h  ");
        System.out.println(" ---------------------------------");
        for (int i = 8; i >= 1; i--) {
            for (int j = 64 - i * 8; j < 72 - i * 8; j++) {
                System.out.print(" | " + squares.get(j));
            }
            System.out.print(" | " + i + "\n");
            System.out.println(" ---------------------------------");
        }
    }

    // MODIFIES: this
    // EFFECTS: shows destinations that piece on inputted square can move to, and tries to move piece to square that
    // user inputs to console. Handles exceptions that may result from bad inputs.
    private void handleMove(String square) {
        try {
            int start = showDestinationsOfSquare(square);
            makeMove(start, input.next());
        } catch (NotValidSquareException e) {
            System.out.println("Not a valid board coordinate.");
        } catch (PieceNoMovesException e) {
            System.out.println("This piece cannot move!");
        } catch (PieceNotExistException e) {
            System.out.println("There does not exist a piece here!");
        } catch (PieceBelongEnemyException e) {
            System.out.println("This is not your piece!");
        }
    }

    // REQUIRES: board.getPiece(start) != null
    // MODIFIES: this
    // EFFECTS: tries to move piece on start square (in coordinates) to specified square (in notation); if inputted
    // square is valid but not a possible destination square, piece will not be moved and message will be printed.
    // if the inputted square is not a valid board coordinate, exception is thrown.
    private void makeMove(int start, String square) throws NotValidSquareException {
        int end = MoveList.toCoordinate(square);
        if (board.movePiece(start, end)) {
            moves.addMove(board.getLastMove());
            if (board.checkStatus()) {
                handleGameEnd();
            } else {
                displayInfo();
            }
        } else {
            System.out.println("The selected piece cannot move here!");
            // nothing will be modified, so we go back to main loop
        }
    }

    // REQUIRES: board.getStatus() is not null
    // MODIFIES: nothing
    // EFFECTS: Prints out the end result of game and the moves to arrive at that position, then displays the position
    private void handleGameEnd() {
        String status = board.getStatus();
        System.out.println("Game Over: " + status);
        String result;
        if (status.startsWith("W")) {
            moves.wasCheckmate();
            result = "1-0";
        } else if (status.startsWith("B")) {
            moves.wasCheckmate();
            result = "0-1";
        } else {
            result = "1/2-1/2";
        }
        showMoves(result);
        System.out.println("Position:");
        displayBoard();
    }

    // EFFECTS: prints out the move list in algebraic notation
    private void showMoves(String result) {
        System.out.println("Moves Played:");
        ArrayList<String> toPrint = moves.getNotationList();
        for (int i = 0; i < toPrint.size(); i++) {
            if (i % 2 == 0) {  // print white's move
                System.out.print((i / 2 + 1) + ".  " + toPrint.get(i) + "  ");
            } else {
                System.out.print(toPrint.get(i) + "\n");
            }
        }
        if (toPrint.size() % 2 == 1) {  // if white moved last, print a new line at end
            // result may be blank, or the score if game is over
            System.out.print(result + "\n");
        } else {
            System.out.print(result);
        }
    }

    // MODIFIES: this
    // EFFECTS: as long as there is a previous move saved in board, takes back that move and reverts the game to the
    // same state as before the move was made, then displays the position.
    // Otherwise, outputs that there are no moves to undo.
    private void undoMove() {
        if (moves.getSize() == 0) {
            System.out.println("Cannot undo any more moves!");
            return;
        }
        moves.undo();
        if (moves.getSize() > 0) {
            Move last = moves.getPreviousMove();
            board.undo(last);
        } else {
            board.undo();
        }
        displayInfo();
    }

    // EFFECTS: translates notation coordinate to integer coordinate and prints out notation coordinates of squares that
    // piece on that square can move to, returns the translated integer coordinate.
    private int showDestinationsOfSquare(String square)
            throws NotValidSquareException, PieceNoMovesException, PieceNotExistException, PieceBelongEnemyException {

        // this method isn't super elegant since it was once part of a larger method
        int coord = MoveList.toCoordinate(square);

        if (!board.existsPiece(coord)) {
            throw new PieceNotExistException();
        }
        Piece p = board.getPiece(coord);
        if (!p.getSide().equals(board.getTurn())) {
            throw new PieceBelongEnemyException();
        }
        Set<Integer> possibleMoves = p.getLegalMoves(board);
        if (possibleMoves.size() == 0) {
            throw new PieceNoMovesException();
        }
        System.out.println("Possible destination squares (in no particular order):");
        for (int i : possibleMoves) {
            // ideally this should display on board, but that would be a lot of work for just 2 phases
            System.out.print(MoveList.fromCoordinate(i) + "  ");
        }
        System.out.print("\nPlease enter a move from the list:");
        return coord;
    }


    // MODIFIES: this
    // EFFECTS: prompts the user to confirm whether they wish to reset the board, if they type something
    // that starts with y, reset the board, otherwise keeps board as is.
    private void resetBoard() {
        System.out.println("Reset Board? (y/N)");
        String next = input.next().toLowerCase();
        if (next.startsWith("y")) {
            System.out.println("\nRESETTING BOARD\n");
            board = new GameBoard();
            moves = new MoveList();
            displayInfo();
        } else {
            System.out.println("Reset Cancelled");
        }
    }
}
