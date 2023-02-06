package model;

import java.util.ArrayList;

public interface Piece {

    ArrayList<Integer> getMoves();

    int getPosition();

    void setPosition(int position);

    String getID();

    String getName();
}
