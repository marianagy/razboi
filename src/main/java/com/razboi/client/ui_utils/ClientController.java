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

    private ObservableList<String> loggedInUsers;

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

    public ClientController(IServer server) throws RemoteException {
        super();
        this.server = server;

        loggedInUsers = FXCollections.observableArrayList();
        getAllLoggedInUsers();
    }

    public void logout() throws ServerException {

        server.logout(userDTO, this);

    }

    public ObservableList<String> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void setLoggedInUsers(ObservableList<String> loggedInUsers) {
        this.loggedInUsers = loggedInUsers;
    }

    private void getAllLoggedInUsers() {
        try {
            loggedInUsers.addAll((List<String>) server.getAllLoggedInUsers().data());
        } catch (ServerException e) {
            e.printStackTrace();
        }
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
                ;



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

//    @Override
//    public void statsChanged(List<Score> scores) {
//
//    }


    @Override
    public void gameStarted(Game game) throws RemoteException, ServerException {
        System.out.println("game started notification"+ game.toString());

        this.game = game;
        //this.currentTurn.setValue(true);
    }

    @Override
    public void userLoggedIn(UserDTO user) {
        System.out.println(user.getUsername() + " just logged in.");
        this.loggedInUsers.add(user.getUsername());
    }

    public IServer getServer() {
        return server;
    }
}
