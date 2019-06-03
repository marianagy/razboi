package com.razboi.razboi.server;

import com.razboi.razboi.RazboiApplication;
import com.razboi.razboi.business.service.PlayerService;
import com.razboi.razboi.business.service.user.AuthService;
import com.razboi.razboi.business.service.user.UserService;
import com.razboi.razboi.networking.utils.AbstractServer;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ObjectConcurrentServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.game.PlayerDAO;
import com.razboi.razboi.persistence.user.dao.UserDAO;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;

public class ServerStart {
    private static int defaultPort = 55555;
    public static void main(String[] args) {

        SpringApplication.run(RazboiApplication.class, args);

        SpringApplicationContext applicationContext =

        UserDAO userDAO = new UserDAO();
        AuthService authService = new UserService();
        UserService userService = new UserService();
        PlayerDAO playerDAO;
        PlayerService playerService;
        userService.setDao(userDAO);
        IServer serverImpl = new ServerImpl();
        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        factory.autowireBean( serverImpl );
        factory.initializeBean( serverImpl, "bean" );
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
