package com.razboi.client.ui_utils;

import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.Response;
import com.razboi.razboi.networking.ResponseType;
import com.razboi.razboi.networking.utils.IObserver;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.game.entity.Game;
import com.razboi.razboi.persistence.game.entity.Player;
import com.razboi.razboi.persistence.user.entity.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class ClientController implements IObserver {

    UserDTO userDTO;
    //    private AuthService authService;
    private IServer server;

    private Game game;
    private Player opponent;

    public Player getOpponent() {
        return opponent;
    }

    private BooleanProperty currentTurn = new SimpleBooleanProperty();


    public BooleanProperty isCurrentTurn() {
        return currentTurn;
    }

    public BooleanProperty currentTurnProperty() {
        return currentTurn;
    }

    public Game getGame() {
        return game;
    }

    private ObservableList<Player> loggedInUsers;

    private StringProperty opponentName = new SimpleStringProperty();

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public StringProperty getOpponentName() {
        return opponentName;
    }

    public StringProperty opponentNameProperty() {
        return opponentName;
    }

    public Player currentPlayer;

    public ClientController(IServer server) throws RemoteException {
        super();
        this.server = server;

        loggedInUsers = FXCollections.observableArrayList();
        getAllLoggedInUsers();
    }

    public void logout() throws ServerException {

        server.logout(userDTO, this);

    }

    public ObservableList<Player> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void setLoggedInUsers(ObservableList<Player> loggedInUsers) {
        this.loggedInUsers = loggedInUsers;
    }

    private void getAllLoggedInUsers() {
//        try {
//            loggedInUsers.addAll((List<String>) server.getAllLoggedInUsers().data());
//        } catch (ServerException e) {
//            e.printStackTrace();
//        }
    }

    public void gameStart() {
        Player player = new Player();
        player.setUsername(userDTO.getUsername());
        //player.setPosition(position);
        try {
            Response response = this.server.startGame(player, this);
            if (response.type().equals(ResponseType.ERROR)) {
                Exception e = (Exception) response.data();
                throw new ServerException("Game failed to start");
            } else if(response.type().equals(ResponseType.START_GAME)) {


                this.game = (Game) response.data();
                loggedInUsers.removeAll();
                loggedInUsers.addAll(game.getParticipants());
                List<Player> filtered = loggedInUsers.stream()
                        .filter(playerL -> playerL.getUsername().equals(userDTO.getUsername()))
                        .collect(Collectors.toList());
                this.currentPlayer = filtered.get(0);


            }

        } catch (ServerException e) {
            e.printStackTrace();
        }
    }


    public void login(String username, String password) throws ServerException {
        // server -> server proxy
        User loginDTO = new User();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);
        Response loginResp = server.login(loginDTO, this);
        if (loginResp.type().equals(ResponseType.ERROR)) {
            Exception e = (Exception) loginResp.data();
            throw new ServerException("User sau parola gresita");
        } else {
            this.userDTO = (UserDTO) loginResp.data();
        }

    }

    public void submitWord(String word) throws ServerException {

        int playerIndex = loggedInUsers.indexOf(currentPlayer);
        loggedInUsers.remove(currentPlayer);
        currentPlayer.setChosenWord(word);

        StringBuilder transfWordBuilder = new StringBuilder(word);
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                transfWordBuilder.setCharAt(i, 'v');
            } else {
                transfWordBuilder.setCharAt(i, 'c');
            }

        }

        currentPlayer.setChosenWordTransformed(transfWordBuilder.toString());
        loggedInUsers.add(playerIndex, currentPlayer);

        this.server.submitWord(currentPlayer);
    }

//    @Override
//    public void statsChanged(List<Score> scores) {
//
//    }



    @Override
    public void gameStarted(Game game) throws RemoteException, ServerException {
        System.out.println("game started notification"+ game.toString());
        loggedInUsers.removeAll();
        loggedInUsers.addAll(game.getParticipants());
        List<Player> filtered = loggedInUsers.stream()
                .filter(playerL -> playerL.getUsername().equals(userDTO.getUsername()))
                .collect(Collectors.toList());
        this.currentPlayer = filtered.get(0);
        this.game = game;
        //this.currentTurn.setValue(true);
    }

    @Override
    public void userLoggedIn(UserDTO user) {
        System.out.println(user.getUsername() + " just logged in.");
        //this.loggedInUsers.add(user.getUsername());
    }


    @Override
    public void wordChosen(Player player) throws RemoteException, ServerException {
        int playerIndex = loggedInUsers.indexOf(player);
        if (playerIndex != -1) {

            loggedInUsers.remove(player);
            loggedInUsers.add(playerIndex, player);
        }
    }

    public IServer getServer() {
        return server;
    }
}
