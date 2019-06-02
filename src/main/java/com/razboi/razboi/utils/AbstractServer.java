package com.razboi.razboi.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public abstract class AbstractServer {
    private int port;
    private ServerSocket server=null;
    public AbstractServer( int port){
              this.port=port;
    }

    public void start() throws ServerException {
        try{
            server=new ServerSocket(port);
            while(true){
                System.out.println("Waiting for clients ...");
                Socket client=server.accept();
                System.out.println("Client connected ...");
                // processRequest e implementata si pornita in AsbConcurrentServer
                // Thread tw=createWorker(client);
                //        tw.start();
                // si createWorker e implementata in ObjectConcurrentServer
                processRequest(client);
            }
        } catch (Exception e ) {
            throw new ServerException("Starting server errror ",e);
        }finally {
            try{
                server.close();
            } catch (IOException e) {
                throw new ServerException("Closing server error ", e);
            } catch (Exception e){
                throw new ServerException("Some other error, maybe port", e);
            }
        }
    }

    protected abstract  void processRequest(Socket client);
    public void stop() throws ServerException {
        try {
            server.close();
        } catch (IOException e) {
            throw new ServerException("Closing server error ", e);
        }
    }
}
