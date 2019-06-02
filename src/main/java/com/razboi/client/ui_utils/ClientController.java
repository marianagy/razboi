package com.razboi.client.ui_utils;

import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.Response;
import com.razboi.razboi.networking.ResponseType;
import com.razboi.razboi.networking.utils.IObserver;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.user.entity.User;

public class ClientController implements IObserver {

    UserDTO userDTO;
    //    private AuthService authService;
    private IServer server;

    public ClientController(IServer server) {
        this.server = server;
    }

    public void logout() throws ServerException {

        server.logout(userDTO, this);

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

    public IServer getServer() {
        return server;
    }
}
