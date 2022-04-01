package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.characters.CharacterID;
import it.polimi.ingsw.model.characters.CharacterInfluenceModifier;
import it.polimi.ingsw.model.gameBoard.*;
import it.polimi.ingsw.model.phases.Phase;
import it.polimi.ingsw.model.phases.PhaseFactory;
import it.polimi.ingsw.model.phases.Round;

import java.util.ArrayList;

public class Game {

    static int roundNumber = 1;

    private static Game game = null;
    private NumberOfPlayers numberOfPlayers;
    private GameMode gameMode;
    private GameState gameState;
    private static Round currentRound; //static attribute to be accessed via class
    private PhaseFactory phaseFactory;
    private Phase currentPhase;
    private Player firstPlayer;
    private ArrayList<Player> players;
    private Player currentPlayer;
    private ArrayList<Cloud> clouds;
    private ArrayList<Professor> professors;
    private ArrayList<IslandGroup> islandGroups;
    private ArrayList<Character> drawnCharacters; // TODO this as well
    private ArrayList<Character> activatedCharacters; // TODO check if needed
    private int treasury;

    /**
     * Private constructor
     */
    private Game() {
        players = new ArrayList<>();
        clouds = new ArrayList<>();
        professors = new ArrayList<>();
        for(CreatureColor color : CreatureColor.values()) {
            professors.add(new Professor(color));
        }
        islandGroups = new ArrayList<>();
        for(int i=1; i<=12; i++) {
            Island island = new Island(i);
            IslandGroup islandGroup = new IslandGroup();
            islandGroup.addIsland(island);
            islandGroups.add(islandGroup);
        }
        drawnCharacters = new ArrayList<>();
        phaseFactory = new PhaseFactory();
        gameState = GameState.LOBBY_PHASE;
    }

    /**
     * Game is a Singleton
     * @return singleton instance of the game
     */
    public static Game getGame() {
        if(game == null) {
            game = new Game();
        }
        return game;
    }



    /**
     * @return the number of the current round
     */
    public static int getRoundNumber() {
        return roundNumber;
    }

    /**
     * @return the current phase
     */
    public Phase getCurrentPhase() {
        return currentPhase;
    }


    /**
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    //TEMPORARY PUBLIC JUST FOR DOING CHARACTERMOVERTEST,
    //when the game methods are implemented this method will be private
    public void addIslandGroup(IslandGroup islandGroup){
        islandGroups.add(islandGroup);
    }

    public Island getIslandByID(int islandID) {
        ArrayList<Island> temp;

        for(IslandGroup islandGroup : islandGroups) {
            temp = islandGroup.getIslands();
            for(Island island : temp) {
                if(island.getIslandID() == islandID) {
                    return island;
                }
            }
        }
        return null;
    }

    /**
     * @return the chosen number of players for the game
     */
    public NumberOfPlayers getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        if(numberOfPlayers == NumberOfPlayers.TWO_PLAYERS.getNum()) {
            this.numberOfPlayers = NumberOfPlayers.TWO_PLAYERS;
        } else if(numberOfPlayers == NumberOfPlayers.THREE_PLAYERS.getNum()) {
            this.numberOfPlayers = NumberOfPlayers.THREE_PLAYERS;
        }
    }

    /**
     * @return the chosen mode for the game
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * @return the current state of the game
     */
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * @return a reference to the current round
     */
    public static Round getCurrentRound() {
        return currentRound;
    } //static method to be able to call it through the class

    public static void setCurrentRound(Round currentRound) {
        Game.currentRound = currentRound;
    }

    /**
     * @return the player that played first during the previous round
     */
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * @return a copy of the players' list
     */
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public void addPlayer (Player player){
        //CHECK IF NICKNAME TAKEN....
        players.add(player);
    }

    /**
     * @return a copy of the cloud cards' list
     */
    public ArrayList<Cloud> getClouds() {
        // TODO
        return null;
    }

    /**
     * @return a copy of the professors' set
     */
    public ArrayList<Professor> getProfessors() {
        return new ArrayList<>(professors);
    }

    // TODO check
    public Professor removeProfessor(CreatureColor color) {
        for(Professor professor : professors) {
            if(professor.getColor().equals(color)) {
                professors.remove(professor);
                return professor;
            }
        }
        return null;
    }

    /**
     * @return a copy of the island groups' circular list
     */
    public ArrayList<IslandGroup> getIslandGroups() {
        // TODO
        return null;
    }

    /**
     * @return a copy of the characters' set
     */
    public ArrayList<Character> getCharacters() {
        // TODO
        return null;
    }

    /**
     * @return a copy of the list of characters drawn for the game
     */
    public ArrayList<Character> getDrawnCharacters() {
        // TODO
        return null;
    }

    /**
     * Used to keep track of activated characters whose effect can be
     * delayed within the active player's turn
     * @return a copy of the activated characters' list
     */
    public ArrayList<Character> getActivatedCharacters() {
        // TODO
        return null;
    }

    public void activateCharacter(int characterID) {
        // TODO
    }

    /**
     * @return a copy of the list of coins available on the "board"
     */
    public int getTreasury() {
        // TODO
        return 0;
    }

    /**
     * Prepares the "game board" before the actual game begins
     */
    public void prepareGame() {

    }

    /**
     * Manages the game
     */
    public void startGame() {
        // LobbyPhase
        currentPhase = phaseFactory.createPhase(gameState);
    }

    /**
     * Ends the game based on the ending code defined in gameState
     */
    public void endGame() {

    }

    /**
     * Determines if a certain nickname has already been chosen by another player
     * @param nickname to check
     * @return whether the nickname has alreasy been chosen
     */
    public boolean isNickNameTaken(String nickname) {
        // TODO
        return false;
    }

    /**
     * Returns the player having the inputted nickname
     * @param nickname to check
     * @return the player whose nickname matches the parameter passed
     */
    public Player getPlayerByNickname(String nickname) {
        for(Player player : players) {
            if(player.getNickname().equals(nickname)) {
                return player;
            }
        }

        return null;
    }

    /**
     * Initializes and sets up the island groups on the "board"
     */
    private void initializeIslandGroups() {

    }

    /**
     * Randomly places Mother Nature on one of the initial island groups
     * @return the initial position of Mother Nature
     */
    private int placeMotherNature() {
        // TODO
        return 0;
    }

    /**
     * Place 2 students of each color (10 total) on the islands, except for
     * the one where Mother Nature was placed and the one on the opposite side
     */
    private void placeInitialStudents() {

    }

    public void calculateInfluence(int islandGroupIndex) {

    }

    public void calculateInfluence(int islandGroupIndex, CharacterInfluenceModifier activatedCharacter) {

    }

    /**
     * Updates the owner of each professor
     */
    public void updateProfessors() {
        for(CreatureColor color: CreatureColor.values()) {
            updateProfessor(color);
        }
    }

    /**
     * Updates the owner of a certain professor
     * @param color of the professor whose owner might be in need of an update
     */
    public void updateProfessor(CreatureColor color) {
        int oldOwnerInfluence, maxInfluence = 0, maxInfluenceIndex = -1;
        Player owner = null;

        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).getBoard().containsProfessor(color)) {
                owner = players.get(i);
                oldOwnerInfluence = owner.getBoard().getHall().getTableByColor(color).getLength();
                maxInfluence = oldOwnerInfluence;
                maxInfluenceIndex = i;
                break;
            }
        }

        for(int i = 0; i < players.size(); i++) {
            int tempInfluence = players.get(i).getBoard().getHall().getTableByColor(color).getLength();
            boolean characterEffectForCurrentPlayer = false;
            if(!currentPhase.getActivatedCharacter().equals(CharacterID.CHARACTER_NONE)) {
                characterEffectForCurrentPlayer =
                        players.get(i).equals(currentPlayer) &&
                        currentPhase.getActivatedCharacter().getCharacterID() == CharacterID.CHARACTER_TWO.getID();
            }

            if(tempInfluence > maxInfluence || (characterEffectForCurrentPlayer && tempInfluence >= maxInfluence)) {
                maxInfluence = tempInfluence;
                maxInfluenceIndex = i;
            }
        }

        if(owner != null) {
            if(!players.get(maxInfluenceIndex).equals(owner)) {
                Professor professorToUpdate = owner.getBoard().loseProfessorByColor(color);
                players.get(maxInfluenceIndex).getBoard().winProfessor(professorToUpdate);
            }
        } else if(maxInfluenceIndex != -1) {
            Game.getGame().getProfessors();
            Professor professorToUpdate = Game.getGame().removeProfessor(color);
            players.get(maxInfluenceIndex).getBoard().winProfessor(professorToUpdate);
        }
    }
}
