package Server;

import data.ReservasDAO;
import data.UtilizadoresDAO;
import data.VoosDao;
import data.codReservaDAO;
import data.codVooDAO;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String [] args) throws Exception {

        ServerSocket ss = new ServerSocket(12345);
        ServidorFacade sf = new ServidorFacade();

        sf.setUtilizadores(UtilizadoresDAO.getInstanceObj("input_files/Utilizadores"));
        sf.setReservas(ReservasDAO.getInstanceObj("input_files/Reservas"));
        sf.setVoos(VoosDao.getInstanceObj("input_files/Voos"));
        sf.setcodVoo(codVooDAO.getInstanceObj("input_files/codVoo"));
        sf.setCodigoREserva(codReservaDAO.getInstanceObj("input_files/codRes"));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Prepare to exit");
                UtilizadoresDAO.saveInstanceObj(sf.getUtilizadores(),"input_files/Utilizadores");
                ReservasDAO.saveInstanceObj(sf.getReservas(),"input_files/Reservas");
                VoosDao.saveInstanceObj(sf.getVoos(),"input_files/Voos");
                codReservaDAO.saveInstanceObj(sf.getCodigoREserva(),"input_files/codRes");
                codVooDAO.saveInstanceObj(sf.getCodVoo(),"input_files/codVoo");
            }
        });



        while(true){
            Socket s = ss.accept();

            ServerWorker sw=new ServerWorker(s,sf);
            new Thread(sw).start();
        }
      
    }
}
