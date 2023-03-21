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
    private static final int SIZE = 800;
    private static final Color WHITE_SQUARE = new Color(217, 169, 137);
    private static final Color BLACK_SQUARE = new Color(115, 69, 23);
    private ArrayList<JButton> squares;
    private GameBoard game;
    private MoveList moveList;
    private Piece selected;

    public static void main(String[] args) {
        new ChessGameGUI();
        //Create a split pane with the two scroll panes in it.
    }

    // EFFECTS: runs the chess application
    public ChessGameGUI() {
        super("Chess: Phase 3");
        setSize(SIZE + 400, SIZE);
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

        game = new GameBoard();
        moveList = new MoveList();
        updatePieces();
    }

    private void updatePieces() {
        for (int i = 0; i < 64; i++) {
            if (this.game.existsPiece(i)) {
                Piece p = game.getPiece(i);
                squares.get(i).setIcon(new ImageIcon("./data/Images/" + p.getAllegiance() + p.getName() + ".png"));
            }
        }
    }

    private void createBoard() {
        squares = new ArrayList<>();
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
        sidePanel.setPreferredSize(new Dimension(400, SIZE));
        JLabel label1 = new JLabel("MOVE LIST");
        label1.setPreferredSize(new Dimension(400, SIZE / 2));
        sidePanel.add(label1);
        for (String s : Arrays.asList("Save", "Load", "Reset", "Undo")) {
            JButton button = new JButton(s);
            button.setActionCommand(s);
            button.setPreferredSize(new Dimension(400, SIZE / 8));
            button.addActionListener(this);
            sidePanel.add(button);
        }
        add(sidePanel, BorderLayout.LINE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int pos = Integer.parseInt(e.getActionCommand());
            if (selected != null && selected.getLegalMoves(game).contains(pos)) {
                makeMove(pos);
            } else {
                selected = game.getPiece(pos);
                if (selected == null) {
                    return;
                }
                if (game.getTurn().equals(selected.getAllegiance())) {
                    displayMoves();
                } else {
                    selected = null;
                }
            }
        } catch (NumberFormatException exception) {
            handleCommand(e.getActionCommand());
        }
    }

    // todo: actually make changes on board
    private void makeMove(int pos) {
        System.out.println("Moved to " + MoveList.fromCoordinate(pos));
    }

    private void handleCommand(String actionCommand) {
        System.out.println("Handle command: " + actionCommand);
    }

    private void displayMoves() {
        System.out.println("Displaying Moves; Currently Selected: " + selected);
    }

}