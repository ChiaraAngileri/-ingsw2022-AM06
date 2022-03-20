package it.polimi.ingsw.characters;

public class CharacterOneDecorator extends CharacterDecorator{

    protected final int characterID = 1;
    protected final String name = "Personaggio1";
    protected final int cost = 1;

    private ArrayList<Student> students;

    /**
     * Draws students to place on the card.
     */
    private void addStudent(){

    }

    /**
     * Remove a student from the card
     * @param student that has been moved to an island
     */
    private void removeStudent(Student student){

    }

    @Override
    public void initialPreparation() {

    }

    /**
     * Return the students on the card.
     * @return ArrayList<Student> students on the card
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * Move a student from this card to an island
     * @param student to move
     * @param island to move the student to
     */
    private void moveStudentToIsland(Student student, Island island){

    }

    @Override
    public void effect() {

    }


}