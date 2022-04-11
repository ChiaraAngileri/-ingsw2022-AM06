package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;


public class EndgamePhase extends Phase {

    private final static int MAX_TOWERS = 8;

    /**
     * Default constructor
     * @param game game played
     */
    public EndgamePhase(Game game) {
        this.game = game;
        this.phaseFactory = new PhaseFactory(game);
    }

    /**
     * Check how ended the game and the winner
     */
    @Override
    public void play() {
        if(game.getGameState().equals(GameState.ENDED_TOWER)) {
            winner = game.getCurrentPlayer();
        } else {
            winner = calculateWinner();
        }
    }

    /**
     * @return the winner
     */
    private Player calculateWinner() {
        int numberOfTowers;
        int minNumberOfTowers = MAX_TOWERS;
        Player playerWinning = null;

        for(Player player : game.getPlayers()) {
            numberOfTowers = player.getBoard().getTowers();
            if(numberOfTowers < minNumberOfTowers) {
                minNumberOfTowers = numberOfTowers;
                playerWinning = player;
            } else if(numberOfTowers == minNumberOfTowers && playerWinning != null) {
                playerWinning = calculateWinner_Professors(player, playerWinning);
            }
        }

        return playerWinning;
    }

    /**
     * Calculates the player who has more professors
     * @param playerA first player
     * @param playerB second player
     * @return the player with more professors
     */
    private Player calculateWinner_Professors(Player playerA, Player playerB) {
        int numberOfProfessorsA = playerA.getBoard().getProfessors().size();
        int numberOfProfessorsB = playerB.getBoard().getProfessors().size();

        if(numberOfProfessorsA > numberOfProfessorsB) {
            return playerA;
        }

        return playerB;
    }

}
