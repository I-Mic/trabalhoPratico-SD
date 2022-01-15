package Server;

import data.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String [] args) throws Exception {

        ServerSocket ss = new ServerSocket(12345);
        ServidorFacade sf = new ServidorFacade();

        sf.setUtilizadores(DAO.load("input_files/Utilizadores"));
        sf.setReservas(DAO.load("input_files/Reservas"));
        sf.setVoos(DAO.load("input_files/Voos"));
        sf.setcodVoo(DAO.load("input_files/codVoo"));
        sf.setCodigoREserva(DAO.load("input_files/codRes"));


        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Prepare to exit");
                try {
                    DAO.store(sf.getUtilizadores(),"input_files/Utilizadores");
                    DAO.store(sf.getReservas(),"input_files/Reservas");
                    DAO.store(sf.getVoos(),"input_files/Voos");
                    DAO.store(sf.getCodigoREserva(),"input_files/codRes");
                    DAO.store(sf.getCodVoo(),"input_files/codVoo");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



        while(true){
            Socket s = ss.accept();

            ServerWorker sw=new ServerWorker(s,sf);
            new Thread(sw).start();
        }

    }
}
