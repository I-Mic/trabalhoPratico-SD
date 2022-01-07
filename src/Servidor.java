import business.ServidorFacade;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String [] args) throws Exception {

        ServerSocket ss = new ServerSocket(12345);
        ServidorFacade sf = new ServidorFacade();

        while(true){
            Socket s = ss.accept();
            new Thread(new ServerWorker(s,sf)).start();
        }
    }
}
