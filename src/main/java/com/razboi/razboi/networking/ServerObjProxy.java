package com.razboi.razboi.networking;

import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.utils.IObserver;
import com.razboi.razboi.networking.utils.IServer;
import com.razboi.razboi.networking.utils.ServerException;
import com.razboi.razboi.persistence.user.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerObjProxy implements IServer {

    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;


    public ServerObjProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }


    @Override
    public Response login(User user, IObserver client) throws ServerException {
        initializeConnection();

        sendRequest(new Request.Builder().data(user).type(RequestType.LOGIN).build());
        Response response = readResponse();

        if (response.type().equals(ResponseType.OK)) {
            this.client = client;
            return response;
        }
        if (response.type().equals(ResponseType.ERROR)) {
            // err=(ErrorResponse)response;
            closeConnection();
            System.out.println("Eroare la login");
            //throw new ServerException(err.getMessage());
        }
        return response;
    }

    @Override
    public Response logout(UserDTO user, IObserver client) throws ServerException {
        initializeConnection();

        sendRequest(new Request.Builder().data(user).type(RequestType.LOGOUT).build());
        Response response = readResponse();

        if (response.type().equals(ResponseType.OK)) {

            return response;
        }
        if (response.type().equals(ResponseType.ERROR)) {
            // err=(ErrorResponse)response;
            closeConnection();
            System.out.println("Eroare la logout");
            //throw new ServerException(err.getMessage());
        }
        return response;
    }


    //proxy methods

    private void sendRequest(Request request) throws ServerException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServerException("Error sending object " + e);
        }


    }

    private Response readResponse() {
        Response response = null;
        try {
            /*synchronized (responses){
                responses.wait();
            }
            response = responses.remove(0);    */
            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }


    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Response received " + response);

                        /*responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses){
                            responses.notify();
                        }*/
                    try {
                        qresponses.put((Response) response);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
