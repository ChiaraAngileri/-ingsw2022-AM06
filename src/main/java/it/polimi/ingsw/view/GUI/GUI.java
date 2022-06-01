package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.enumerations.CreatureColor;
import it.polimi.ingsw.model.gameBoard.Hall;
import it.polimi.ingsw.model.gameBoard.Professor;
import it.polimi.ingsw.network.client.MessageParser;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.message.serverToclient.*;
import it.polimi.ingsw.util.Constants;
import it.polimi.ingsw.view.GUI.controllers.GUIController;
import it.polimi.ingsw.view.GUI.controllers.PlayAssistantController;
import it.polimi.ingsw.view.GUI.controllers.PlayController;
import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GUI extends Application implements View {

    private SocketClient socketClient;
    private final PropertyChangeSupport listener = new PropertyChangeSupport(this);
    private ModelView modelView;
    private MessageParser messageParser;

    private final HashMap<String, Scene> nameToScene = new HashMap<>();
    private final HashMap<String, GUIController> nameToController = new HashMap<>();
    private Scene currentScene;
    private Stage stage;

    private CreatureColor firstColor;
    private CreatureColor secondColor;
    private int destinationIsland;

    @Override
    public void start(Stage stage) {
        setup();
        this.stage = stage;
        stage.setScene(currentScene);

        this.stage.setOnCloseRequest(e -> {
            e.consume();
            int exitStatus = ConfirmationBox.display(1, this.stage, "Are you sure you want to quit?");
            if(exitStatus == 1) {
                System.exit(0);
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void setup() {
        this.modelView = new ModelView();
        List<String> fxmlList = Constants.SCENES;
        try {
            for (String path : fxmlList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
                Scene scene = new Scene(loader.load());
                nameToScene.put(path, scene);
                if(path.equals(Constants.MENU)) {
                    this.currentScene = scene;
                }
                GUIController controller = loader.getController();
                if(controller != null) {
                    controller.setGUI(this);
                    nameToController.put(path, controller);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HashMap<String, GUIController> getNameToController() {
        return nameToController;
    }

    public Scene getSceneByName(String name) {
        return nameToScene.get(name);
    }

    public Scene getSceneByController(GUIController controller) {
        for(String scene : nameToController.keySet()) {
            if(nameToController.get(scene).equals(controller)) {
                return nameToScene.get(scene);
            }
        }
        return null;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void changeStage(String scene) {
        Platform.runLater(() -> {
            currentScene = nameToScene.get(scene);
            stage.setScene(currentScene);
            stage.show();
            GUIController controller = nameToController.get(scene);
            if(controller != null) {
                nameToController.get(scene).init();
            }
        });
    }

    public ModelView getModelView() {
        return modelView;
    }

    public MessageParser getMessageParser() {
        return messageParser;
    }

    public void setMessageParser(SocketClient socketClient) {
        this.messageParser = new MessageParser(socketClient);
    }

    /**
     * Adds a listener to the GUI.
     * @param propertyName name of the observed property of the GUI.
     * @param listener listener added to the CLI.
     */
    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {
        this.listener.addPropertyChangeListener(propertyName, listener);
    }//todo needed?

    /**
     * @return the socket attribute of this client.
     */
    @Override
    public SocketClient getSocketClient() {
        return this.socketClient;
    }

    /**
     * Sets the socket attribute of this client to the specified one.
     * @param socketClient socket to set.
     */
    @Override
    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
        socketClient.readMessage();
        socketClient.enablePinger(true); //todo check
    }

    public CreatureColor getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(CreatureColor firstColor) {
        this.firstColor = firstColor;
    }

    public CreatureColor getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(CreatureColor secondColor) {
        this.secondColor = secondColor;
    }

    public int getDestinationIsland() {
        return destinationIsland;
    }

    public void setDestinationIsland(int destinationIsland) {
        this.destinationIsland = destinationIsland;
    }

    /**
     * Handles the LoginReplyMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void loginReplyHandler(LoginReplyMessage msg) {
        modelView.setNickname(msg.getNickname());
        modelView.setNumberOfPlayers(msg.getNumberOfPlayers().getNum());
        modelView.setGameMode(msg.getGameMode());

        changeStage(Constants.LOBBY);
    }

    /**
     * Handles the GameStartedMessage sent by the server.
     */
    @Override
    public void gameStartingHandler() {
        changeStage(Constants.WIZARD);
    }

    /**
     * Handles the GameReadyMessage sent by the server.
     */
    @Override
    public void gameReadyHandler() {
        changeStage(Constants.BOARD_AND_ISLANDS);
    }

    /**
     * Handles the WizardsAvailableMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void wizardsHandler(WizardsAvailableMessage msg) {
        // TODO modify wizards scene
    }

    /**
     * Handles the AssistantsMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void assistantsHandler(AssistantsMessage msg) {
        //TODO check
        PlayAssistantController controller = (PlayAssistantController) nameToController.get(Constants.ASSISTANTS);
        if(msg.getAssistants().size() > 1) {
            controller.updateAvailable(msg.getAssistants());
        }
        //oppure si salvano gli assistenti disponibili nella model view
    }

    /**
     * Handles the ActivePlayersMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void activePlayersHandler(ActivePlayersMessage msg) {
        modelView.setPlayers(msg.getActivePlayers());
        //...
    }

    /**
     * Handles the BoardMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void boardHandler(BoardMessage msg) {
        modelView.setBoard(msg);
        ((PlayController)(nameToController.get(Constants.BOARD_AND_ISLANDS))).updateBoard(msg);
        //...
    }

    /**
     * Handles the IslandGroupsMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void islandGroupsHandler(IslandGroupsMessage msg) {
        modelView.setIslandGroups(msg.getIslandGroups(), msg.getMotherNatureIndex());
        //...
    }

    /**
     * Handles the IslandMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void islandHandler(IslandMessage msg) {
        modelView.setIsland(msg.getIsland());
        //...
    }

    /**
     * Handles the CloudsAvailableMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void cloudsAvailableHandler(CloudsAvailableMessage msg) {
        modelView.setClouds(msg.getClouds());
        //...
    }

    /**
     * Handles the CloudChosenMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void cloudChosenHandler(CloudChosenMessage msg) {

    }

    /**
     * Handles the CoinMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void coinsHandler(CoinMessage msg) {
        modelView.setCoins(msg);
        ((PlayController)(nameToController.get(Constants.BOARD_AND_ISLANDS))).updateCoins(msg.getCoins());
    }

    /**
     * Handles the CurrentPlayerMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void currentPlayerHandler(CurrentPlayerMessage msg) {
        String currentPlayer = msg.getCurrentPlayer();
        modelView.setCurrentPlayer(currentPlayer);
        ((PlayController)(nameToController.get(Constants.BOARD_AND_ISLANDS))).updateCurrentPlayer(currentPlayer);
    }

    /**
     * Handles the CurrentPhaseMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void currentPhaseHandler(CurrentPhaseMessage msg) {
        String currentPhase = msg.getCurrentPhase();
        modelView.setCurrentPhase(currentPhase);
        ((PlayController)(nameToController.get(Constants.BOARD_AND_ISLANDS))).updateCurrentPhase(currentPhase);
    }

    /**
     * Handles the CharacterDrawnMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void charactersDrawnHandler(CharacterDrawnMessage msg) {
        modelView.setDrawnCharacter(msg);
        //...
    }

    /**
     * Handles the CharacterInfoMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void characterInfoHandler(CharacterInfoMessage msg) {

    }

    /**
     * Handles the CharacterPlayedMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void characterPlayedHandler(CharacterPlayedMessage msg) {
        modelView.setPlayedCharacter(msg);
        //...
    }

    /**
     * Displays the match's current state.
     */
    @Override
    public void matchInfoHandler() {
        // TODO remove
    }

    /**
     * Handles the WinnerMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void winnerHandler(WinnerMessage msg) {
        modelView.setWinner(msg.getWinnerNickname());
        //...
    }

    /**
     * Handles the LoserMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void loserHandler(LoserMessage msg) {
        modelView.setWinner(msg.getWinnerNickname());
        //...
    }

    /**
     * Handles the end of the game
     */
    @Override
    public void gameEndedHandler() {

    }

    /**
     * Handles the GenericMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void genericMessageHandler(GenericMessage msg) {
        //Platform.runLater(() -> AlertBox.display("Message", msg.getMessage()));

        String[] message = msg.getMessage().split("_");

        if(message.length == 3) {
            sendGeneric(message[0], message[2]);
        } else {
            sendGeneric(message[0], null);
        }
    }

    private void sendGeneric(String message, String option) {
        switch (message) {
            case "PLAYER":
                ((PlayController)(nameToController.get(Constants.BOARD_AND_ISLANDS))).updateCurrentPlayer(getModelView().getNickname());
                break;
            case "PHASE":
                System.out.println(message + "  " + option); //todo: error at the beginning prepare -> planning
                ((PlayController)(nameToController.get(Constants.BOARD_AND_ISLANDS))).updateCurrentPhase(option);
                System.out.println(option);
                break;
            case "ASSISTANTPLAYED":
                //todo check
                Platform.runLater(() -> AlertBox.display("Message", modelView.getCurrentPlayer() + " played: " + option));
                break;
            case "MAXSTEPS":
                // maxSteps = option;
                //todo..
                break;
            case "DISCONNECT":
                Platform.runLater(() -> AlertBox.display("Message", option + " has disconnect, the game will end soon."));
                break;
            case "JOIN":
                Platform.runLater(() -> AlertBox.display("Message", option + " has join the game."));
                break;
        }
    }


    /**
     * Handles the ErrorMessage sent by the server.
     *
     * @param msg the message to handle.
     */
    @Override
    public void errorMessageHandler(ErrorMessage msg) {
        Platform.runLater(() -> AlertBox.display("Error", msg.getError()));
    }
}
