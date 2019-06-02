package com.razboi.razboi.networking;

import com.razboi.razboi.utils.IObserver;
import com.razboi.razboi.utils.IServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
        return null;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }
}
