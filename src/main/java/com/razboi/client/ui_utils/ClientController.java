package com.razboi.client.ui_utils;

import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.Response;
import com.razboi.razboi.networking.ResponseType;
import com.razboi.razboi.networking.utils.IObserver;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.game.entity.Player;
import com.razboi.razboi.persistence.user.entity.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ClientController implements IObserver {

    UserDTO userDTO;
    //    private AuthService authService;
    private IServer server;

    private ObservableList<String> loggedInUsers;
    private ObservableList<String> inGameUsers;

    private BooleanProperty gameStarted;

    public void setInGameUsers(ObservableList<String> inGameUsers) {
        this.inGameUsers = inGameUsers;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    private ObservableList<String> cardsList;

    public BooleanProperty isGameStarted() {
        return gameStarted;
    }

    public BooleanProperty gameStartedProperty() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted.set(gameStarted);
    }


    //private ObservableList<String> usersEnteredGame;

    public ClientController(IServer server) throws RemoteException {
        super();
        this.server = server;

        loggedInUsers = FXCollections.observableArrayList();
        inGameUsers = FXCollections.observableArrayList();
        cardsList = FXCollections.observableArrayList();
        gameStarted = new SimpleBooleanProperty();
        gameStarted.setValue(false);
    }

    public ObservableList<String> getInGameUsers() {
        return inGameUsers;
    }

    public void logout() throws ServerException {

        server.logout(userDTO, this);

    }

    /**
     * Getter pentru loggedInUsers
     * @return
     */
    public ObservableList<String> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void setLoggedInUsers(ObservableList<String> loggedInUsers) {
        this.loggedInUsers = loggedInUsers;
    }

    /**
     * Gets all logged in users
     */
    private void getAllLoggedInUsers() {
        try {
            // server e ServerProxy
            loggedInUsers.addAll((List<String>) server.getAllLoggedInUsers().data());
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }

    public void startGame() throws ServerException{
        // ServerObjProxy
        Response startGameResponse = server.startGame(userDTO.getUsername());
        if(startGameResponse.type().equals(ResponseType.OK)){
            List<String> serverInGameUsers = (ArrayList)startGameResponse.data();
            this.inGameUsers.addAll(serverInGameUsers);
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
    public void userLoggedIn(UserDTO user) {
        System.out.println(user.getUsername() + " just logged in.");
        this.loggedInUsers.add(user.getUsername());
    }

//    @Override
//    public void userEnteredGame(UserDTO user) throws ServerException {
//
//    }

    /**
     * Add users to inGameUsers -> asta ajunge in
     * @param inGame
     */
    @Override
    public void gameStarted(List<String> inGame){
        System.out.println("Game strated (CC) ...");
        // adauga useri in ingameuserlist
        inGameUsers.addAll(inGame);
        this.gameStarted.setValue(true);

        // vreau sa ajung in mainview din startview



    }


    public void addPlayer(String username) throws ServerException {
        Player player=new Player();
        player.setUsername(username);
        server.addPlayer(player);
    }

    public IServer getServer() {
        return server;
    }
}
