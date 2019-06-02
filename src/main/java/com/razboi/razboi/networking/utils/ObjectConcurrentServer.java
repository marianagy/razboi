package com.razboi.razboi.networking.utils;



import com.razboi.razboi.networking.ClientObjectWorker;

import java.net.Socket;


public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IServer server;
    public ObjectConcurrentServer(int port, IServer server) {
        super(port);
        this.server = server;
        System.out.println("Chat- ChatObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // ClientObjectWorker e clasa (proxy client) in care se trimit requesturile mai departe catre TriatlonServer
        ClientObjectWorker worker=new ClientObjectWorker(server, client);
        Thread tw=new Thread(worker);
        return tw;
    }

}
