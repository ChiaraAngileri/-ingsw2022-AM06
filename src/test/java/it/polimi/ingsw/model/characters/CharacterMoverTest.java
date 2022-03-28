package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.Round;
import it.polimi.ingsw.model.gameBoard.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

//the tests work individually but not together (if i try to do: Run CharacterMoverTest)

class CharacterMoverTest {

    ConcreteCharacterFactory cf;
    Character character;
    Bag bag;

    @BeforeEach
    void setUp() {
        cf = new ConcreteCharacterFactory();
        bag = Bag.getBag();
    }

    @AfterEach
    void tearDown() {
        cf = null;
        character = null;
        bag = null;
    }


    @Test
    void initialPreparation() {
        bag.addStudent(new Student(CreatureColor.GREEN));
        bag.addStudent(new Student(CreatureColor.RED));
        bag.addStudent(new Student(CreatureColor.PINK));
        bag.addStudent(new Student(CreatureColor.BLUE));
        bag.addStudent(new Student(CreatureColor.YELLOW));
        bag.addStudent(new Student(CreatureColor.RED));
        bag.addStudent(new Student(CreatureColor.GREEN));
        bag.addStudent(new Student(CreatureColor.RED));
        bag.addStudent(new Student(CreatureColor.PINK));
        bag.addStudent(new Student(CreatureColor.BLUE));
        bag.addStudent(new Student(CreatureColor.YELLOW));
        bag.addStudent(new Student(CreatureColor.RED));

        character = cf.createCharacter(1);
        character.initialPreparation();
        assertEquals(4, character.getStudents().size());
        character = cf.createCharacter(7);
        character.initialPreparation();
        assertEquals(6, character.getStudents().size());

    }


    @Test
    void effect1_OK() {
        character = cf.createCharacter(1);

        bag.addStudent(new Student(CreatureColor.RED));

        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.GREEN));
        character.students.add(new Student(CreatureColor.BLUE));

        Island island = new Island(1);
        IslandGroup islandGroup = new IslandGroup();
        islandGroup.addIsland(island);
        Game.getGame().addIslandGroup(islandGroup);

        island.addStudent(new Student(CreatureColor.GREEN));
        island.addStudent(new Student(CreatureColor.PINK));

        ((CharacterMover) character).setFromColor(CreatureColor.RED);
        ((CharacterMover) character).setIslandID(1);
        character.effect();
        assertEquals(4, character.getStudents().size());
        assertEquals(CreatureColor.GREEN, island.getStudents().get(0).getColor());
        assertEquals(CreatureColor.PINK, island.getStudents().get(1).getColor());
        assertEquals(CreatureColor.RED, island.getStudents().get(2).getColor());
        assertEquals(0, bag.size());
        assertEquals(4, character.getStudents().size());
    }


    @Test
    void effect1_KO() {
        character = cf.createCharacter(1);

        Bag.getBag().addStudent(new Student(CreatureColor.RED));

        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.GREEN));
        character.students.add(new Student(CreatureColor.BLUE));

        Island island = new Island(1);
        island.addStudent(new Student(CreatureColor.GREEN));
        island.addStudent(new Student(CreatureColor.PINK));

        ((CharacterMover) character).setFromColor(CreatureColor.YELLOW);
        character.effect();

        assertEquals(CreatureColor.RED, character.getStudents().get(0).getColor());
        assertEquals(CreatureColor.RED, character.getStudents().get(1).getColor());
        assertEquals(CreatureColor.GREEN, character.getStudents().get(2).getColor());
        assertEquals(CreatureColor.BLUE, character.getStudents().get(3).getColor());
        assertEquals(4, character.getStudents().size());

        assertEquals(CreatureColor.GREEN, island.getStudents().get(0).getColor());
        assertEquals(CreatureColor.PINK, island.getStudents().get(1).getColor());
        assertEquals(2, island.getStudents().size());

        assertEquals(1, Bag.getBag().size());
    }


    @Test
    void effect7_OK() {
        character = cf.createCharacter(7);

        Player player = new Player("Chiara", PlayerColor.WHITE);
        Round round = new Round();
        Game.getGame().setCurrentRound(round);
        round.setCurrentPlayer(player);

        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.GREEN));
        character.students.add(new Student(CreatureColor.BLUE));
        character.students.add(new Student(CreatureColor.GREEN));
        character.students.add(new Student(CreatureColor.BLUE));


        player.getBoard().addStudentToEntrance(CreatureColor.BLUE);
        player.getBoard().addStudentToEntrance(CreatureColor.PINK);

        ((CharacterMover) character).setFromColor(CreatureColor.RED);
        ((CharacterMover) character).setToColor(CreatureColor.BLUE);

        ArrayList<CreatureColor> expectedStudents = new ArrayList<>(Arrays.asList(CreatureColor.RED,
                CreatureColor.GREEN,CreatureColor.BLUE, CreatureColor.GREEN, CreatureColor.BLUE, CreatureColor.BLUE));

        ArrayList<CreatureColor> expectedEntrance = new ArrayList<>(Arrays.asList(CreatureColor.PINK, CreatureColor.RED));

        character.effect();

        assertEquals(6, character.getStudents().size());
        assertEquals(expectedStudents, character.students.stream().map(Creature::getColor).collect(Collectors.toList()));
        assertEquals(2, player.getBoard().getEntrance().size());
        assertEquals(expectedEntrance, player.getBoard().getEntrance().stream().map(Creature::getColor).collect(Collectors.toList()));
    }


    @Test
    void effect7_KO() {
        character = cf.createCharacter(7);

        Player player = new Player("Chiara", PlayerColor.WHITE);
        Round round = new Round();
        Game.getGame().setCurrentRound(round);
        round.setCurrentPlayer(player);

        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.GREEN));
        character.students.add(new Student(CreatureColor.BLUE));
        character.students.add(new Student(CreatureColor.GREEN));
        character.students.add(new Student(CreatureColor.BLUE));

        player.getBoard().addStudentToEntrance(CreatureColor.BLUE);
        player.getBoard().addStudentToEntrance(CreatureColor.PINK);

        ((CharacterMover) character).setFromColor(CreatureColor.RED);
        ((CharacterMover) character).setToColor(CreatureColor.GREEN);

        ArrayList<CreatureColor> expectedStudents = new ArrayList<>(Arrays.asList(CreatureColor.RED, CreatureColor.RED,
                CreatureColor.GREEN,CreatureColor.BLUE, CreatureColor.GREEN, CreatureColor.BLUE));

        ArrayList<CreatureColor> expectedEntrance = new ArrayList<>(Arrays.asList(CreatureColor.BLUE, CreatureColor.PINK));

        character.effect();

        assertEquals(6, character.getStudents().size());
        assertEquals(expectedStudents, character.getStudents().stream().map(Creature::getColor).collect(Collectors.toList()));
        assertEquals(2, player.getBoard().getEntrance().size());
        assertEquals(expectedEntrance, player.getBoard().getEntrance().stream().map(Creature::getColor).collect(Collectors.toList()));

    }

    @Test
    void effect10_OK() {
        character = cf.createCharacter(10);

        Player player = new Player("Chiara", PlayerColor.WHITE);
        Round round = new Round();
        Game.getGame().setCurrentRound(round);
        round.setCurrentPlayer(player);

        player.getBoard().addStudentToEntrance(CreatureColor.BLUE);
        player.getBoard().addStudentToEntrance(CreatureColor.PINK);

        player.getBoard().addStudentToHall(CreatureColor.PINK);
        player.getBoard().addStudentToHall(CreatureColor.RED);

        ((CharacterMover) character).setFromColor(CreatureColor.PINK);
        ((CharacterMover) character).setToColor(CreatureColor.BLUE);

        ArrayList expectedHall = new ArrayList<>(Arrays.asList(0,1,0,0,1));

        ArrayList<CreatureColor> expectedEntrance = new ArrayList<>(Arrays.asList(CreatureColor.PINK, CreatureColor.PINK));

        character.effect();

        assertEquals(expectedEntrance, player.getBoard().getEntrance().stream().map(Creature::getColor).collect(Collectors.toList()));

        assertEquals(expectedHall, player.getBoard().getHall().stream().map(Table::getLenght).collect(Collectors.toList()));

    }

    @Test
    void effect10_KO() {
        character = cf.createCharacter(10);

        Player player = new Player("Chiara", PlayerColor.WHITE);
        Round round = new Round();
        Game.getGame().setCurrentRound(round);
        round.setCurrentPlayer(player);

        player.getBoard().addStudentToEntrance(CreatureColor.BLUE);
        player.getBoard().addStudentToEntrance(CreatureColor.PINK);

        player.getBoard().addStudentToHall(CreatureColor.PINK);
        player.getBoard().addStudentToHall(CreatureColor.RED);

        ((CharacterMover) character).setFromColor(CreatureColor.GREEN);
        ((CharacterMover) character).setToColor(CreatureColor.RED);

        ArrayList expectedHall = new ArrayList<>(Arrays.asList(0,1,0,1,0));

        ArrayList<CreatureColor> expectedEntrance = new ArrayList<>(Arrays.asList(CreatureColor.BLUE, CreatureColor.PINK));

        character.effect();

        assertEquals(expectedEntrance, player.getBoard().getEntrance().stream().map(Creature::getColor).collect(Collectors.toList()));

        assertEquals(expectedHall, player.getBoard().getHall().stream().map(Table::getLenght).collect(Collectors.toList()));


    }


    @Test
    void effect11_OK() {
        character = cf.createCharacter(11);

        Bag.getBag().addStudent(new Student(CreatureColor.RED));

        Player player = new Player("Chiara", PlayerColor.WHITE);
        Round round = new Round();
        Game.getGame().setCurrentRound(round);
        round.setCurrentPlayer(player);

        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.GREEN));
        character.students.add(new Student(CreatureColor.BLUE));

        player.getBoard().addStudentToHall(CreatureColor.RED);
        player.getBoard().addStudentToHall(CreatureColor.PINK);

        ((CharacterMover)character).setFromColor(CreatureColor.RED);

        ArrayList expectedHall = new ArrayList(Arrays.asList(0, 2, 0, 1, 0));

        character.effect();

        assertEquals(expectedHall, player.getBoard().getHall().stream().map(Table::getLenght).collect(Collectors.toList()));
        assertEquals(4, character.getStudents().size());
        assertEquals(0, Bag.getBag().size());

    }


    @Test
    void effect11_KO() {
        character = cf.createCharacter(11);

        Bag.getBag().addStudent(new Student(CreatureColor.RED));

        Player player = new Player("Chiara", PlayerColor.WHITE);
        Round round = new Round();
        Game.getGame().setCurrentRound(round);
        round.setCurrentPlayer(player);

        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.RED));
        character.students.add(new Student(CreatureColor.GREEN));
        character.students.add(new Student(CreatureColor.BLUE));

        player.getBoard().addStudentToHall(CreatureColor.RED);
        player.getBoard().addStudentToHall(CreatureColor.PINK);

        ((CharacterMover)character).setFromColor(CreatureColor.PINK);

        ArrayList expectedHall = new ArrayList(Arrays.asList(0,1,0,1,0));

        character.effect();

        assertEquals(expectedHall, player.getBoard().getHall().stream().map(Table::getLenght).collect(Collectors.toList()));
        assertEquals(1, Bag.getBag().size());
        assertEquals(4, character.getStudents().size());

    }


    /*
    //need game to be implemented (players methods)
    @Test
    void effect12_OK() {

    }

    //need game to be implemented (players methods)
    @Test
    void effect12_KO() {

    }
    */

}