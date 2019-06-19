package com.razboi.razboi.server;

import com.razboi.razboi.business.service.user.AuthService;
import com.razboi.razboi.business.service.user.ServiceException;
import com.razboi.razboi.business.service.user.UserService;
import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.Response;
import com.razboi.razboi.networking.ResponseType;
import com.razboi.razboi.networking.utils.IObserver;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.game.entity.Game;
import com.razboi.razboi.persistence.game.entity.Player;
import com.razboi.razboi.persistence.user.dao.UserDAO;
import com.razboi.razboi.persistence.user.entity.User;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ServerImpl implements IServer {

    AuthService authService;
    UserService userService;
    private Map<String, IObserver> loggedClients;

    private Map<Player,IObserver> waitingList;

    private boolean gameIsGoingOn;

    public ServerImpl() {
        this.authService = new UserService();
        this.userService = new UserService();
        UserDAO userDAO = new UserDAO();
        this.userService.setDao(userDAO);
        loggedClients = new ConcurrentHashMap<>();
        waitingList = new ConcurrentHashMap<>();
        this.gameIsGoingOn = false;
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
            Player playerInstance = new Player();
            playerInstance.setUsername(userDTO.getUsername());
            waitingList.put(playerInstance, client);
            notifyUserLoggedIn(userDTO);
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

    @Override
    public Response getAllLoggedInUsers() {
        return new Response.Builder().data(new ArrayList(loggedClients.keySet())).type(ResponseType.OK).build();
    }

    //TODO: putin mai optim (notify score added, notify score removed) de schimbat cu logged clients
    private void notifyUserLoggedIn(UserDTO loggedInUser) {
        //List<User> users = userService.readAll();


        ExecutorService executor = Executors.newFixedThreadPool(5);
        System.out.println("BeforeL: ");
        System.out.println(loggedClients);
        Iterator it = loggedClients.entrySet().iterator();
        System.out.println("After");
        System.out.println(loggedClients);

        System.out.println();

        while (it.hasNext()) {
            //IObserver notificationClient=loggedClients.get(us.getUsername());
            Map.Entry pair = (Map.Entry) it.next();
            IObserver notificationClient = (IObserver) pair.getValue();
            String username = (String) pair.getKey();
            System.out.println(notificationClient);
            System.out.println(loggedClients);
            if (notificationClient != null && !username.equals(loggedInUser.getUsername()))
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + username + "] that " + loggedInUser.getUsername() + " logged in.");
                        notificationClient.userLoggedIn(loggedInUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Ouch happened when notifying " + username);
                    }
                });
            //it.remove(); // avoids a ConcurrentModificationException

        }
        System.out.println("After after");
        System.out.println(loggedClients);
        executor.shutdown();
    }

    // un notifier de end game

    // o metoda de a verifica daca un joc se deruleaza


    @Override
    public Response startGame(Player player, IObserver client) throws ServerException {
        //check if user is in logged clients
        if (loggedClients.get(player.getUsername()) == null) {
            throw new ServerException("User is not logged in.");
        }
        if (gameIsGoingOn) {
            throw new ServerException("Game is already in progress.");
        }
        // check if waiting list has users
        while (waitingList.isEmpty()) {
            // if waiting list empty
            // asteapta o secunda dupa care checkuie din nou
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("we are effed");
            }

            //return new Response.Builder().type(ResponseType.PLAYER_WAITING).build();
        }

        {
            // if waiting list not empty -> porneste jocul
            // instantiaza  joc
            Game game = new Game();
            game.setParticipants(new ArrayList<>());
            //game.getParticipants().add(player);



            Set<Player> players = waitingList.keySet();
            game.getParticipants().addAll(players);

            for (Map.Entry<Player, IObserver> entry : waitingList.entrySet()) {
                System.out.println(entry.getKey() + "/" + entry.getValue());
                if (!player.getUsername().equals(entry.getKey().getUsername())) {
                    IObserver notificationClient = entry.getValue();
                    System.out.println("Notifying: " + player.getUsername());
                    try {
                        notificationClient.gameStarted(game);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                        System.out.println("could not notify " + player.getUsername() + " that game has started");
                        return new Response.Builder().data(e).type(ResponseType.ERROR).build();
                    }
                }
            }
            waitingList.clear();
            return new Response.Builder().data(game).type(ResponseType.START_GAME).build();









        }
    }
}
