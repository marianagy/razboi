package com.razboi.razboi.networking;

import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.utils.IObserver;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.game.entity.Game;
import com.razboi.razboi.persistence.game.entity.Player;
import com.razboi.razboi.persistence.user.entity.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientObjectWorker implements Runnable, IObserver {

    private IServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();



    public ClientObjectWorker(IServer server, Socket connection){
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);

                if (response!=null){
                    sendResponse(response);
                }
//                else{
//                    sendResponse(new ErrorResponse("Response is null"));
//                }
            }catch (EOFException e) {

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e){

                System.out.println("Error: "+e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Error "+e);
        }

    }

    private Response handleRequest(Request request){
        Response response = null;
        System.out.println();

        //handle login
        if (request.type().equals(RequestType.LOGIN)) {
            System.out.println("Login request ...");
            User loginDTO = (User) request.data();
            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();
            try {
                // server din triathlon server
                return server.login(loginDTO, this);

            } catch (ServerException e) {
                connected = false;
                return new Response.Builder().data(e).type(ResponseType.ERROR).build();
            } catch (Exception e) {
                connected = false;
                e.printStackTrace();
                return new Response.Builder().data(e).type(ResponseType.ERROR).build();
            }
        } else if (request.type().equals(RequestType.LOGOUT)) {
            System.out.println("Login request ...");
            UserDTO logoutDTO = (UserDTO) request.data();
            String username = logoutDTO.getUsername();
            try {
                return server.logout(logoutDTO, this);
            } catch (ServerException e) {
                connected = false;
                return new Response.Builder().data(e).type(ResponseType.ERROR).build();
            } catch (Exception e) {
                connected = false;
                e.printStackTrace();
                return new Response.Builder().data(e).type(ResponseType.ERROR).build();
            }
        } else if (request.type().equals(RequestType.GET_LOGGED_IN_USERS)) {
            System.out.println("get logged in users request ...");
            try {
                return server.getAllLoggedInUsers();
            } catch (ServerException e) {
                e.printStackTrace();
                return new Response.Builder().data(e).type(ResponseType.ERROR).build();
            }
        } else if(request.type().equals(RequestType.START_GAME)){
            System.out.println("Start game request...");
            // start game request in server
            Player player = (Player)request.data();
            try {
                return server.startGame(player, this);
            } catch (ServerException e) {
                e.printStackTrace();

                return new Response.Builder().data(e).type(ResponseType.ERROR).build();
            }
        } else if (request.type().equals(RequestType.SUBMIT_WORD)) {
            System.out.println("Submit word request...");

            Player player = (Player) request.data();
            try {
                return server.submitWord(player);
            } catch (Exception e) {
                e.printStackTrace();

                return new Response.Builder().data(e).type(ResponseType.ERROR).build();
            }
        }
        return new Response.Builder().data("Unknown Request").type(ResponseType.ERROR).build();
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);


        output.writeObject(response);
        output.flush();
    }

    @Override
    public void userLoggedIn(UserDTO user) throws ServerException {
        System.out.println("User Logged in (COW)");
        Response response = new Response.Builder().type(ResponseType.USER_LOGGED_IN).data(user).build();
        try {
            sendResponse(response);
        } catch (Exception e) {
            throw new ServerException("Error notifying");
        }
    }

    @Override
    public void gameStarted(Game game) throws RemoteException, ServerException {
        System.out.println("Game Started (COW)");

        Response response = new Response.Builder().type(ResponseType.GAME_STARTED).data(game).build();
        try {
            Thread.sleep(500);
            sendResponse(response);
        } catch (Exception e) {
            throw new ServerException("Error notifying");
        }
    }

    @Override
    public void wordChosen(Player player) throws RemoteException, ServerException {
        System.out.println("Word chosen (COW)");
        Response response = new Response.Builder().type(ResponseType.WORD_SUBMITTED).data(player).build();
        try {
            Thread.sleep(500);
            sendResponse(response);
        } catch (Exception e) {
            throw new ServerException("Error notifying");
        }

    }
}
