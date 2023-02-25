package ui;

import model.GameBoard;
import model.Move;
import model.MoveList;
import model.Piece;

import java.util.ArrayList;
import java.util.Scanner;

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

        while (notDone) {
            displayInfo();
            action = input.next().toLowerCase();

            if (action.startsWith("q")) {
                notDone = false;
            } else if (action.startsWith("r")) {
                resetBoard();
            } else if (action.startsWith("u")) {
                undoMove();
            } else if (action.startsWith("m")) {
                showMoves();
            } else {
                handleMove(action);
            }
        }
        System.out.println("\n --Exiting Application--");
    }

    private void showMoves() {
    }

    private void undoMove() {

    }

    private void init() {
        board = new GameBoard();
        moves = new MoveList();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    private void displayInfo() {
        int turn = moves.getMoves().size();
        if (turn > 0) {
            if (turn % 2 == 0) {
                System.out.println("Turn " + turn / 2 + ", White to Move:");
            } else {
                System.out.println("Turn " + turn / 2 + ", Black to Move:");
            }
        } else {
            System.out.println("-NEW GAME- \nWhite to Move:");
        }
        displayBoard();
        System.out.println("Type coordinates of piece to move or a command:");
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

    private void handleMove(String action) {

    }

    private void resetBoard() {
        System.out.println("Reset Board? (y/N)");
        if (input.next().equalsIgnoreCase("n")) {
            board = new GameBoard();
            moves = new MoveList();
        }
    }
}
