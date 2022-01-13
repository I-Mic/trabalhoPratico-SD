package Server;

import data.ReservasDAO;
import data.UtilizadoresDAO;
import data.VoosDao;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String [] args) throws Exception {

        ServerSocket ss = new ServerSocket(12345);
        ServidorFacade sf = new ServidorFacade();

        while(true){
            Socket s = ss.accept();
            ServerWorker sw=new ServerWorker(s,sf);
            new Thread(sw).start();
        }
        //ver forma de dar save ao sistema quando nao existe nenhum cliente no sistema uma vez que o sistema entra no ciclo e nao sai
        //UtilizadoresDAO.saveInstanceObj(sf.getUtilizadores(),"/input_files/Utilizadores");
        //ReservasDAO.saveInstanceObj(sf.getReservas(),"/input_files/Reservas");
        //VoosDao.saveInstanceObj(sf.getVoos(),"/input_files/Voos");
    }
}
