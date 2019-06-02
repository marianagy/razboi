package com.razboi.razboi.server;

import com.razboi.razboi.business.service.user.AuthService;
import com.razboi.razboi.business.service.user.ServiceException;
import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.Response;
import com.razboi.razboi.networking.ResponseType;
import com.razboi.razboi.networking.utils.IObserver;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.user.entity.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerImpl implements IServer {

    AuthService authService;
    private Map<String, IObserver> loggedClients = new ConcurrentHashMap<>();


    public ServerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Response login(User user, IObserver client) throws ServerException {
        UserDTO userDTO = null;
        try {
            userDTO = authService.login(user.getUsername(), user.getPassword());
        } catch (ServiceException e) {
            throw new ServerException("Authentication failed.");
        }
        if (userDTO != null) {
            if (loggedClients.get(userDTO.getUsername()) != null) {
                throw new ServerException("User already logged in.");
            }
            loggedClients.put(userDTO.getUsername(), client);
            return new Response.Builder().data(userDTO).type(ResponseType.OK).build();

        } else {
            throw new ServerException("Authentication failed.");
        }
    }

    @Override
    public Response logout(UserDTO user, IObserver client) throws ServerException {
        IObserver localClient = loggedClients.remove(user.getUsername());
        if (localClient == null) {
            throw new ServerException("User " + user.getUsername() + " is not logged in");
        }
        return new Response.Builder().type(ResponseType.OK).build();

    }
}
