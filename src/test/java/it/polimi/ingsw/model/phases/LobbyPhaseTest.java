package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.exceptions.MaxPlayersReachedException;
import it.polimi.ingsw.exceptions.NicknameTakenException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyPhaseTest {

    private Game game;
    private Phase phase;
    private String nickname;

    @BeforeEach
    void setUp() {
        game = new Game();
        nickname = "";
    }

    @Test
    void play_2P() {
        game.setNumberOfPlayers(2);

        phase = game.getCurrentPhase();

        assertEquals(GameState.LOBBY_PHASE, game.getGameState());

        // Add first player
        nickname = "Stefano";
        phase.setPlayerNickname(nickname);
        try {
            phase.play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(GameState.LOBBY_PHASE, game.getGameState());

        // Try nickname of first player
        nickname = "Stefano";
        phase.setPlayerNickname(nickname);
        assertThrows(NicknameTakenException.class, () -> phase.play());

        assertEquals(GameState.LOBBY_PHASE, game.getGameState());

        // Add second player
        nickname = "Chiara";
        phase.setPlayerNickname(nickname);
        try {
            phase.play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(GameState.PREPARE_PHASE, game.getGameState());

        // Try nickname of first player again
        nickname = "Stefano";
        phase.setPlayerNickname(nickname);

        assertThrows(NicknameTakenException.class, () -> phase.play());
        assertEquals(GameState.PREPARE_PHASE, game.getGameState());

        // Try adding third player
        nickname = "Nick";
        phase.setPlayerNickname(nickname);

        assertThrows(MaxPlayersReachedException.class, () -> phase.play());
        assertEquals(GameState.PREPARE_PHASE, game.getGameState());
        assertInstanceOf(PreparePhase.class, game.getCurrentPhase());

        // Try nickname of second player
        nickname = "Chiara";
        phase.setPlayerNickname(nickname);

        assertThrows(NicknameTakenException.class, () -> phase.play());
        assertEquals(GameState.PREPARE_PHASE, game.getGameState());

        // Try nickname of third player again
        nickname = "Nick";
        phase.setPlayerNickname(nickname);

        assertThrows(MaxPlayersReachedException.class, () -> phase.play());
        assertEquals(GameState.PREPARE_PHASE, game.getGameState());

        assertEquals("Stefano", game.getPlayers().get(0).getNickname());
        assertEquals("Chiara", game.getPlayers().get(1).getNickname());
        assertEquals(PlayerColor.BLACK, game.getPlayers().get(0).getColor());
        assertEquals(PlayerColor.WHITE, game.getPlayers().get(1).getColor());
    }

    @Test
    void play_3P() {
        game.setNumberOfPlayers(3);

        phase = game.getCurrentPhase();

        assertEquals(GameState.LOBBY_PHASE, game.getGameState());

        // Add first player
        nickname = "Stefano";
        phase.setPlayerNickname(nickname);
        try {
            phase.play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(GameState.LOBBY_PHASE, game.getGameState());

        // Try nickname of first player
        nickname = "Stefano";
        phase.setPlayerNickname(nickname);

        assertThrows(NicknameTakenException.class, () -> phase.play());
        assertEquals(GameState.LOBBY_PHASE, game.getGameState());

        // Add second player
        nickname = "Chiara";
        phase.setPlayerNickname(nickname);
        try {
            phase.play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(GameState.LOBBY_PHASE, game.getGameState());

        // Try nickname of first player again
        nickname = "Stefano";
        phase.setPlayerNickname(nickname);

        assertThrows(NicknameTakenException.class, () -> phase.play());
        assertEquals(GameState.LOBBY_PHASE, game.getGameState());

        // Add third player
        nickname = "Nick";
        phase.setPlayerNickname(nickname);
        try {
            phase.play();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(GameState.PREPARE_PHASE, game.getGameState());

        // Try nickname of second player
        nickname = "Chiara";
        phase.setPlayerNickname(nickname);

        assertThrows(NicknameTakenException.class, () -> phase.play());
        assertEquals(GameState.PREPARE_PHASE, game.getGameState());

        // Try nickname of third player
        nickname = "Nick";
        phase.setPlayerNickname(nickname);

        assertThrows(NicknameTakenException.class, () -> phase.play());
        assertEquals(GameState.PREPARE_PHASE, game.getGameState());

        assertEquals("Stefano", game.getPlayers().get(0).getNickname());
        assertEquals("Chiara", game.getPlayers().get(1).getNickname());
        assertEquals("Nick", game.getPlayers().get(2).getNickname());
        assertEquals(PlayerColor.BLACK, game.getPlayers().get(0).getColor());
        assertEquals(PlayerColor.WHITE, game.getPlayers().get(1).getColor());
        assertEquals(PlayerColor.GRAY, game.getPlayers().get(2).getColor());
    }
}