package com.razboi.razboi.server;

import com.razboi.razboi.business.service.user.AuthService;
import com.razboi.razboi.business.service.user.UserService;
import com.razboi.razboi.networking.utils.AbstractServer;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ObjectConcurrentServer;
import com.razboi.razboi.networking.utils.ServerException;

public class ServerStart {
    private static int defaultPort = 55555;
    public static void main(String[] args) {

        AuthService authService = new UserService();
        IServer serverImpl = new ServerImpl(authService);
        int serverPort = defaultPort;
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ObjectConcurrentServer(serverPort, serverImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
