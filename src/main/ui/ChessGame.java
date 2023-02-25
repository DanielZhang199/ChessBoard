package ui;

import model.*;
import model.exceptions.NotValidSquareException;
import ui.exceptions.PieceBelongEnemyException;
import ui.exceptions.PieceNoMovesException;
import ui.exceptions.PieceNotExistException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

// todo game end checks and undo/move list
public class ChessGame {
    private GameBoard board;
    private MoveList moves;
    private Scanner input;

    // EFFECTS: runs the chess application
    public ChessGame() {
        runGame();
    }

    private void runGame() {
        boolean notDone = true;
        String action;

        init();
        displayInfo();

        while (notDone) {
            System.out.println("Input coordinates of piece to move or a command:");
            action = input.next().toLowerCase();
            if (action.startsWith("q")) {
                notDone = false;
            } else if (action.startsWith("r")) {
                resetBoard();
            } else if (action.startsWith("u")) {
                undoMove();
            } else if (action.startsWith("m")) {
                showMoves("");
            } else if (board.getStatus() == null) {
                handleMove(action);
            } else {
                System.out.println("You cannot move pieces; the game is over!");
            }
        }
        System.out.println("\n --Exiting Application--");
    }


    private void init() {
        board = new GameBoard();
        moves = new MoveList();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    private void displayInfo() {
        int turn = moves.getMoves().size();
        if (turn % 2 == 0) {
            System.out.println("Turn " + (turn / 2 + 1) + ", White to Move:");
        } else {
            System.out.println("Turn " + (turn / 2 + 1) + ", Black to Move:");
        }
        displayBoard();
    }

    private void displayBoard() {
        ArrayList<String> squares = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            if (board.existsPiece(i)) {
                Piece p = board.getPiece(i);
                if (p.getAllegiance().equals("W")) {
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

    private void makeMove(int start, String square) throws NotValidSquareException {
        int end = MoveList.toCoordinate(square);
        if (board.movePiece(start, end)) {
            moves.addMove(board.getLastMove());
            if (board.checkStatus()) {
                handleGameEnd(board.getStatus());
            } else {
                displayInfo();
            }
        } else {
            System.out.println("The selected piece cannot move here!");
            // nothing will be modified, so we go back to main loop
        }
    }

    private void handleGameEnd(String status) {
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


    private int showDestinationsOfSquare(String square)
            throws NotValidSquareException, PieceNoMovesException, PieceNotExistException, PieceBelongEnemyException {

        // using MoveList's toCoordinate static method to convert to and from algebraic notation.
        int coord = MoveList.toCoordinate(square);

        if (!board.existsPiece(coord)) {
            throw new PieceNotExistException();
        }
        Piece p = board.getPiece(coord);
        if (!p.getAllegiance().equals(board.getTurn())) {
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
