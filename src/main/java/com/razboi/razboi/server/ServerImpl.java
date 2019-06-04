package com.razboi.razboi.server;

import com.razboi.razboi.business.service.GameService;
import com.razboi.razboi.business.service.PlayerService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class ServerImpl implements IServer {

    AuthService authService;
    UserService userService;

    private GameService gameService;

    private PlayerService playerService;
    private Map<String, IObserver> loggedClients;
    private Map<String, IObserver> inGameClients;

    public PlayerService getPlayerService() {
        return playerService;
    }

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public ServerImpl() {
        this.authService = new UserService();
        this.userService = new UserService();
        UserDAO userDAO = new UserDAO();
        this.userService.setDao(userDAO);
        loggedClients = new ConcurrentHashMap<>();
        inGameClients = new ConcurrentHashMap<>();
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

    /**
     * Gets logged in users from loggedClients list
     * @return
     */
    @Override
    public Response getAllLoggedInUsers() {
        return new Response.Builder().data(new ArrayList(loggedClients.keySet())).type(ResponseType.OK).build();
    }

    @Override
    public Response startGame(String username) throws ServerException {
        // lista useri logati - ii bagam in joc
        // toti userii in copiem in lista de ingameusers
        Game game=new Game();
        gameService.save(game);
        loggedClients.forEach(inGameClients::putIfAbsent);
        List<String> inGameUsernames = new ArrayList<>();
        // salvam fiecare user ca player
        inGameClients.forEach((key,value)->{
            Player player=new Player();
            player.setUsername(key);
            player.setCards(getCards());
            try {
                player.setParticipatedInGame(gameService.findById(game.getID()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            inGameUsernames.add(key);
            playerService.save(player);


        });

        // anuntam userii ca a inceput jocul
        inGameClients.forEach((key,value)->{
            try {
                if(!key.equals(username)) {
                    value.gameStarted(inGameUsernames);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ServerException e) {
                e.printStackTrace();
            }
        });


        // returnez ingameusers in response
        return new Response.Builder().data(inGameUsernames).type(ResponseType.OK).build();
    }

    @Override
    public Response addPlayer(Player player) throws ServerException {
        player.setCards(getCards());
        playerService.save(player);
        return new Response.Builder().type(ResponseType.OK).build();
    }

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

    /**
     * Method generates initial card list
     * @return
     */
    public String getCards() {
        String cartiInitiale = "";
        Random rand = new Random();
        List<String> givenList = new ArrayList<>();
        givenList.add("6");
        givenList.add("7");
        givenList.add("8");
        givenList.add("9");
        givenList.add("J");
        givenList.add("Q");
        givenList.add("K");
        givenList.add("A");

        int numberOfElements = 4;

        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(givenList.size());
            String randomElement = givenList.get(randomIndex);
            cartiInitiale = cartiInitiale + randomElement;
            givenList.remove(randomIndex);
        }
        return cartiInitiale;
    }
}
