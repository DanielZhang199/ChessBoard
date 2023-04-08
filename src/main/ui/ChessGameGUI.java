package ui;

import model.*;
import model.Event;
import persistence.JsonConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;


// Class handling all GUI functionality for chess game
public class ChessGameGUI extends JFrame implements ActionListener {
    private static final int SIZE_BOARD = 550;
    private static final int SIZE_SIDE = 120;
    private static final Color WHITE_SQUARE = new Color(217, 169, 137);
    private static final Color BLACK_SQUARE = new Color(115, 69, 23);
    private static final String TITLE = "Chess: ";

    private final DefaultListModel<String> moves = new DefaultListModel<>();
    private final ArrayList<JButton> squares = new ArrayList<>();
    private GameBoard gameBoard = new GameBoard();
    private MoveList moveList = new MoveList();
    private Piece selected;
    private boolean confirmReset;

    // EFFECTS: runs the chess application
    public ChessGameGUI() {
        super(TITLE + "New Game");
        setSize(SIZE_BOARD + SIZE_SIDE, SIZE_BOARD);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        init();
        setVisible(true);

        WindowListener listener = new WindowAdapter() {
            @Override
            // EFFECTS: prints each event of the event log to the console
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString());
                }
            }
        };
        addWindowListener(listener);
    }

    // REQUIRES: can only be called once
    // MODIFIES: this
    // EFFECTS: sets up all elements of window, and GameBoard and MoveList classes,
    // initializes fields to starting values
    private void init() {
        createBoard();
        addSidePanel();
        displayPieces();
        confirmReset = false;
        selected = null;
    }

    // MODIFIES: this
    // EFFECTS: Displays all pieces at their locations on the board, removing any move dots
    private void displayPieces() {
        for (int i = 0; i < 64; i++) {
            if (this.gameBoard.existsPiece(i)) {
                Piece p = gameBoard.getPiece(i);
                squares.get(i).setIcon(new ImageIcon("./data/Images/" + p.getAllegiance() + p.getName() + ".png"));
            } else {
                squares.get(i).setIcon(null);
            }
        }
    }

    // REQUIRES: selected != null (message will be printed to console instead of crashing)
    // MODIFIES: this
    // EFFECTS: Displays dots on squares that the selected piece can move to, replaces pieces with the dot if they can
    // be captured. This may be changed later on to have a dot overlay the piece for clarity
    private void displayMoves() {
        if (selected == null) {
            System.out.println("Something went wrong as no piece is selected!");
            // should also not ever run, but better than program crashing
            return;
        }
        for (int i : selected.getLegalMoves(gameBoard)) {
            squares.get(i).setIcon(new ImageIcon("./data/Images/dot.png"));
        }
    }

    // REQUIRES: selected != null
    // (assign selected = null after calling this method; it's my private method, so I can do what I want)
    // MODIFIES: this
    // EFFECTS: Undoes the effects of displayMoves()
    private void undisplayMoves() {
        if (selected == null) {
            return;
        }
        for (int i : selected.getLegalMoves(gameBoard)) {
            if (gameBoard.existsPiece(i)) {
                Piece p = gameBoard.getPiece(i);
                squares.get(i).setIcon(new ImageIcon("./data/Images/" + p.getAllegiance() + p.getName() + ".png"));
            } else {
                squares.get(i).setIcon(null);
            }
        }
    }

    // REQUIRES: this method can only be called once
    // MODIFIES: this
    // EFFECTS: adds an interactive chess board JPanel to this
    private void createBoard() {
        JPanel board = new JPanel(new GridLayout(8, 8));
        Insets buttonMargin = new Insets(0,0,0, 0);

        for (int i = 0; i < 64; i++) {
            JButton b = new JButton();
            b.setActionCommand(Integer.toString(i));
            b.setMargin(buttonMargin);
            ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
            b.setIcon(icon);
            if (i % 2  == 0 ^ i % 16 >= 8) {
                b.setBackground(WHITE_SQUARE);
            } else {
                b.setBackground(BLACK_SQUARE);
            }
            b.setBorder(null);
            b.addActionListener(this);
            board.add(b);
            squares.add(b);
        }
        add(board, BorderLayout.CENTER);
    }

    // REQUIRES: this method can only be called once
    // MODIFIES: this
    // EFFECTS: adds a side panel using JPanel to this
    private void addSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(5, 1));
        sidePanel.setPreferredSize(new Dimension(SIZE_SIDE, SIZE_BOARD));
        JList<String> list = new JList<>(moves);
        list.setPreferredSize(new Dimension(80, SIZE_BOARD / 5));
        list.setLayoutOrientation(JList.VERTICAL);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        sidePanel.add(scrollPane);
        for (String s : Arrays.asList("Save", "Load", "Reset", "Undo")) {
            JButton button = new JButton(s);
            button.setActionCommand(s);
            button.addActionListener(this);
            sidePanel.add(button);
        }
        add(sidePanel, BorderLayout.LINE_END);
    }

    // REQUIRES: ActionEvent is one of the action commands that were set in initialization
    // either [0, 63] or one of "Save", "Load", "Reset", and "Undo"
    // MODIFIES: this
    // EFFECTS:
    // IF the user clicked on a chess square:
    //  IF no piece is selected, and the square has a piece of the same colour as whoever is moving,
    //      select that piece and display its moves
    //  OTHERWISE, IF no piece is selected, do nothing.
    //  OTHERWISE, IF there is a piece selected, and they can move to that square, move the piece to that square
    //  OTHERWISE, the selected piece cannot move to the square, so deselect the current piece
    // IF the user clicked on the button, perform the appropriate action for that button, either save to file, load from
    // file, undo move, or reset board (reset only after it is clicked twice with not other calls to this method)
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int pos = Integer.parseInt(e.getActionCommand());
            confirmReset = false;
            if (selected != null && selected.getLegalMoves(gameBoard).contains(pos)) {
                makeMove(pos);
            } else if (gameBoard.getStatus() == null) {
                undisplayMoves();
                selected = gameBoard.getPiece(pos);
                if (selected == null) {
                    return;
                }
                if (gameBoard.getTurn().equals(selected.getAllegiance())) {
                    displayMoves();
                } else {
                    selected = null;
                }
            }
        } catch (NumberFormatException exception) {
            handleCommand(e.getActionCommand());
        }
    }

    // REQUIRES: selected != null, and 0 <= pos < 64
    // MODIFIES: this
    // EFFECTS: makes a move and updates the graphic interface
    private void makeMove(int pos) {
        if (gameBoard.movePiece(selected.getPosition(), pos)) {
            moveList.addMove(gameBoard.getLastMove());
            displayPieces();
            if (gameBoard.checkStatus()) {
                handleGameEnd();
            } else {
                updateMoves();
            }
        } else {
            System.out.println("Something went wrong trying to move!");
            // this section should never run, but is here just in case
        }
        selected = null;
    }

    // MODIFIES: this
    // EFFECTS: updates the graphic interface
    private void updateMoves() {
        // store as local variable to reduce calls to this method
        int size = moveList.getSize();
        if (gameBoard.getTurn().equals("B")) {
            moves.addElement((size / 2 + 1) + ". "
                    + moveList.getNotationList().get(size - 1) + "  ");
            setTitle(TITLE + "Turn " + (size / 2 + 1) + ", Black to move");
        } else {
            String s = moves.getElementAt(size / 2 - 1).concat(
                    moveList.getNotationList().get(size - 1));
            moves.removeElementAt(size / 2 - 1);
            moves.addElement(s);
            setTitle(TITLE + "Turn " + (size / 2 + 1) + ", White to move");
        }
    }

    // REQUIRES: gameBoard.checkStatus() is true
    // MODIFIES: this
    // EFFECTS: changes window to display result of the game
    private void handleGameEnd() {
        String status = gameBoard.getStatus();
        if (status.startsWith("W") || status.startsWith("B")) {
            moveList.wasCheckmate();
        }
        updateMoves();
        setTitle(TITLE + status);
    }

    // REQUIRES: command is one of "Save", "Load", "Undo", or "Reset"
    // MODIFIES: this
    // EFFECTS: either saves the game, loads from the save, undoes the last move, or resets the board when confirmReset
    // is true, otherwise sets confirmReset to true.
    private void handleCommand(String command) {
        undisplayMoves();
        selected = null;
        if (command.equals("Save")) {
            try {
                JsonConverter.saveMoveList(moveList, "./data/saveFile1.json");
            } catch (IOException e) {
                System.out.println("Something went wrong saving the file!");
            }
        } else if (command.equals("Load")) {
            handleLoad();
        } else if (command.equals("Undo")) {
            handleUndo();
        } else if (command.equals("Reset") && confirmReset) {
            moves.clear();
            this.moveList = new MoveList();
            this.gameBoard = new GameBoard();
            displayPieces();
            setTitle(TITLE + "New Game");
        }
        confirmReset = command.equals("Reset");
    }

    // MODIFIES: this
    // EFFECTS: undoes the last move made
    private void handleUndo() {
        if (moveList.getSize() == 0) {
            return;
        }
        moveList.undo();
        if (moveList.getSize() > 0) {
            gameBoard.undo(moveList.getPreviousMove());
            moves.removeElementAt(moves.getSize() - 1);
            if (gameBoard.getTurn().equals("B")) {
                updateMoves();
            } else {
                setTitle(TITLE + "Turn " + (moveList.getSize() / 2 + 1) + ", White to move");
            }
        } else {
            gameBoard.undo();
            moves.clear();
            setTitle(TITLE + "Turn 1, White to move");
        }
        displayPieces();
    }

    // MODIFIES: this
    // EFFECTS: loads a game from saved file, overwriting the current game
    private void handleLoad() {
        moves.clear();
        try {
            MoveList temp = JsonConverter.getMoveList("./data/saveFile1.json");
            this.moveList = new MoveList();
            this.gameBoard = new GameBoard();
            for (Move m : temp.getMoveList()) {
                selected = gameBoard.getPiece(m.getStart());
                makeMove(m.getEnd());
            }
        } catch (IOException e) {
            System.out.println("Something went wrong loading from the file!");
        } catch (NullPointerException e) {
            System.out.println("Something went wrong making moves from the file!");
            this.moveList = new MoveList();
            this.gameBoard = new GameBoard();
            displayPieces();
        }
    }

}