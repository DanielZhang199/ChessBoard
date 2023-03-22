package ui;

import model.GameBoard;
import model.Move;
import model.MoveList;
import model.Piece;
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

    public static void main(String[] args) {
        new ChessGameGUI();
        //Create a split pane with the two scroll panes in it.
    }

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

    }

    // MODIFIES: this
    // EFFECTS: sets up all elements of window, and GameBoard and MoveList classes
    private void init() {
        createBoard();
        addSidePanel();
        displayPieces();
        confirmReset = false;
        selected = null;
    }

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

    private void handleGameEnd() {
        String status = gameBoard.getStatus();
        if (status.startsWith("W") || status.startsWith("B")) {
            moveList.wasCheckmate();
        }
        updateMoves();
        setTitle(TITLE + status);
    }

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