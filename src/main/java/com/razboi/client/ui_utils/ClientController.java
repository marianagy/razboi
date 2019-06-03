package com.razboi.client.ui_utils;

import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.Response;
import com.razboi.razboi.networking.ResponseType;
import com.razboi.razboi.networking.utils.IObserver;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.user.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.util.List;

public class ClientController implements IObserver {

    UserDTO userDTO;
    //    private AuthService authService;
    private IServer server;

    private ObservableList<String> loggedInUsers;

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

    public IServer getServer() {
        return server;
    }
}
