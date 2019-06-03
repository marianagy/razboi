package com.razboi.razboi;

import com.razboi.razboi.networking.utils.AbstractServer;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ObjectConcurrentServer;
import com.razboi.razboi.networking.utils.ServerException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com")
@SpringBootApplication
public class ServerStart {
    private static int defaultPort = 55555;

    public static void main(String[] args) {

        SpringApplication.run(RazboiApplication.class, args);


    }

    @Bean
    public CommandLineRunner demo(IServer serverImpl) {
        return (args) -> {
            int serverPort = defaultPort;
            System.out.println("Starting server on port: " + serverPort);
            AbstractServer server = new ObjectConcurrentServer(serverPort, serverImpl);
            try {
                server.start();
            } catch (ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());
            }
        };
    }

}
