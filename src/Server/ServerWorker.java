package Server;

import business.*;

import java.net.Socket;

public class ServerWorker implements Runnable {

    private Socket s;
    private ServidorFacade sf;
    private User current_user;

    public ServerWorker(Socket s, ServidorFacade sf) {
        this.s = s;
        this.sf = sf;
        this.current_user = null;
    }

    public void run(){


    }
}
