package ui;

import model.GameBoard;
import model.MoveList;
import model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;


// Class handling all GUI functionality for chess game
public class ChessGameGUI extends JFrame implements ActionListener {
    private static final int SIZE_BOARD = 550;
    private static final int SIZE_SIDE = 80;
    private static final Color WHITE_SQUARE = new Color(217, 169, 137);
    private static final Color BLACK_SQUARE = new Color(115, 69, 23);

    private final DefaultListModel<String> moves = new DefaultListModel<>();
    private final ArrayList<JButton> squares = new ArrayList<>();
    private final GameBoard gameBoard = new GameBoard();
    private final MoveList moveList = new MoveList();
    private Piece selected;

    public static void main(String[] args) {
        new ChessGameGUI();
        //Create a split pane with the two scroll panes in it.
    }

    // EFFECTS: runs the chess application
    public ChessGameGUI() {
        super("Chess: Phase 3");
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

    // todo: make this actually look good
    private void addSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(5, 1));
        sidePanel.setPreferredSize(new Dimension(SIZE_SIDE, SIZE_BOARD));
        JList<String> list = new JList<>(moves);
        list.setPreferredSize(new Dimension(SIZE_SIDE, SIZE_BOARD / 2));
        sidePanel.add(list);
        for (String s : Arrays.asList("Save", "Load", "Reset", "Undo")) {
            JButton button = new JButton(s);
            button.setActionCommand(s);
            button.setPreferredSize(new Dimension(SIZE_SIDE, SIZE_BOARD / 8));
            button.addActionListener(this);
            sidePanel.add(button);
        }
        add(sidePanel, BorderLayout.LINE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int pos = Integer.parseInt(e.getActionCommand());
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
            if (gameBoard.checkStatus()) {
                handleGameEnd();
            }
            displayPieces();
            updateMoves();
        } else {
            System.out.println("Something went wrong trying to move!");
            // this section should never run, but is here just in case
        }
        selected = null;
    }

    private void updateMoves() {
        if (gameBoard.getTurn().equals("B")) {
            moves.addElement((moveList.getSize() / 2) + 1 + ". "
                    + moveList.getNotationList().get(moveList.getSize() - 1) + "  ");
        } else {
            String s = moves.getElementAt(moves.getSize() - 1).concat(moveList.toNotation(gameBoard.getLastMove()));
            moves.removeElementAt(moves.getSize() - 1);
            moves.addElement(s);
        }
    }

    // todo
    private void handleGameEnd() {
        System.out.println("Game Ended!");
    }

    private void handleCommand(String actionCommand) {
        System.out.println("Handle command: " + actionCommand);
    }

}