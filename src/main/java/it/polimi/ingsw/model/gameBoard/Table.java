package it.polimi.ingsw.model.gameBoard;

public class Table {

    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = 10;

    private final CreatureColor color;
    private int index;

    public Table(CreatureColor color) {
        this.color = color;
        this.index = MIN_INDEX;
    }

    public CreatureColor getColor() {
        return color;
    }

    public boolean addStudent() {
        if(index < MAX_INDEX) {
            index = index + 1;
            return true;
        }
        return false;
    }

    public boolean removeStudent() {
        if(index > MIN_INDEX) {
            index = index - 1;
            return true;
        }
        return false;
    }

    public int getLength(){
        return index;
    }
}
