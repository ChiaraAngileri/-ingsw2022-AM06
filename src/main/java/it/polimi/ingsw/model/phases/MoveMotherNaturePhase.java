package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.exceptions.ExceededStepsException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameMode;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.gameBoard.IslandGroup;

public class MoveMotherNaturePhase extends ActionPhase {

    private final int maxNumberOfSteps;

    /**
     * Default constructor
     * @param game game played
     * @param currentPlayer player who is playing
     */
    public MoveMotherNaturePhase(Game game, Player currentPlayer) {
        this.game = game;
        this.currentPlayer = currentPlayer;
        this.phaseFactory = new PhaseFactory(game);
        if(activatedCharacter != null) {
            this.maxNumberOfSteps = game.getPlayerPriority().get(currentPlayer).getMaxSteps(activatedCharacter);
        } else {
            this.maxNumberOfSteps = game.getPlayerPriority().get(currentPlayer).getMaxSteps();
        }
    }


    /**
     * Checks if the number of steps chosen is allowed
     * by comparing them to the number of steps indicated by the assistant played
     * @param numberOfSteps number of steps chosen
     * @return whether the steps chosen are allowed
     */
    private boolean checkNumberOfSteps(int numberOfSteps) {
        return numberOfSteps <= maxNumberOfSteps;
    }

    /**
     * Moves mother nature in the steps indicated
     * Calculates the influence on the island where mother nature arrives
     * and replaces the towers if necessary
     * Check whether adjacent islands need to be merged
     * @throws ExceededStepsException if it's tried to perform more steps than allowed
     */
    @Override
    public void play() throws ExceededStepsException {
        IslandGroup currentIslandGroup = game.getMotherNature().getCurrentIslandGroup();
        int numberOfIslandGroups = game.getIslandGroups().size();
        int currentIslandGroupIndex = game.getIndexOfIslandGroup(currentIslandGroup);
        IslandGroup nextIslandGroup;

        if (currentIslandGroupIndex >= 0) {
            int nextIndex;
            if (checkNumberOfSteps(numberOfSteps)) {
                nextIndex = (currentIslandGroupIndex + numberOfSteps) % numberOfIslandGroups;
            } else {
                throw new ExceededStepsException();
            }
            nextIslandGroup = game.getIslandGroupByIndex(nextIndex);
            game.getMotherNature().setCurrentIslandGroup(nextIslandGroup);

            if (game.getGameMode().equals(GameMode.EXPERT)) {
                game.calculateInfluence(nextIndex, activatedCharacter);
            } else {
                game.calculateInfluence(nextIndex);
            }
        }

        calculateNextPhase();
        game.setCurrentPhase(phaseFactory.createPhase(game.getGameState()));
    }

    /**
     * Sets the next phase of the play
     * checking the end of the game
     * due to the end of a player's towers or
     * reaching the maximum number of island groups
     */
    private void calculateNextPhase() {
        boolean endedTower = currentPlayer.getBoard().isThereNoTowers();
        boolean endedIsland = game.checkEndDueToIslandGroup();
        boolean skipPickCloudPhase = game.getSkipPickCloudPhase();

        int numberOfPlayers = game.getNumberOfPlayers().getNum();
        Player lastPlayer = game.getPlayingOrder().get(numberOfPlayers - 1);
        boolean isCurrentPlayerTheLastOne = game.getCurrentPlayer().equals(lastPlayer);

        if(endedTower) {
            game.setGameState(GameState.ENDED_TOWER);
            return;
        }

        if(endedIsland) {
            game.setGameState(GameState.ENDED_ISLAND);
            return;
        }

        if(skipPickCloudPhase) {
            if(isCurrentPlayerTheLastOne) {
                game.setGameState(GameState.ENDED_STUDENTS);
            } else {
                game.setGameState(GameState.MOVE_STUDENT_PHASE);
                game.setCurrentPlayer(game.getNextPlayer());
            }
            return;
        }

        game.setGameState(GameState.PICK_CLOUD_PHASE);
    }
}
