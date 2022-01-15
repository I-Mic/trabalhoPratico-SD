package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket s;
    private ServidorFacade sf;

    public ServerWorker(Socket s, ServidorFacade sf) throws IOException {
        this.dis = new DataInputStream(s.getInputStream());
        this.dos = new DataOutputStream(s.getOutputStream());
        this.s = s;
        this.sf = sf;
    }

    public void run(){
        try{
            ServerMsgHandler mesenger = new ServerMsgHandler();
            int i;
            int respostaInt;
            String respostaString;
            while(!s.isClosed()) {
                i = mesenger.receiveMsg(this.dis);
                switch (i) {
                    case 1:

                        respostaInt = sf.registarUtilizador(mesenger.getNome(), mesenger.getPassword(), mesenger.getIsAdmin());
                        mesenger.sendResponseInt(this.dos, respostaInt);
                        break;

                    case 2:
                        respostaInt = sf.login(mesenger.getNome(), mesenger.getPassword());
                        mesenger.sendResponseInt(this.dos, respostaInt);
                        break;

                    case 3:
                        respostaString = sf.addReserva(mesenger.getPercurso(), mesenger.getDataInicio(), mesenger.getDataFim(), mesenger.getNome());
                        mesenger.sendResponseString(this.dos, respostaString);
                        break;

                    case 4:
                        respostaInt = sf.removeReserva(mesenger.getCodReserva(), mesenger.getNome());
                        mesenger.sendResponseInt(this.dos, respostaInt);
                        break;

                    case 5:
                        respostaString = sf.listaVoosExistentes();
                        mesenger.sendResponseString(this.dos, respostaString);
                        break;

                    case 6:
                        sf.addVoo(mesenger.getOrigem(), mesenger.getDestino(), mesenger.getCapacidade(), mesenger.getData());
                        break;

                    case 7:
                        respostaInt = sf.closeServer();
                        mesenger.sendResponseInt(this.dos, respostaInt);
                        break;

                    case 8:
                        respostaInt = sf.openServer();
                        mesenger.sendResponseInt(this.dos, respostaInt);
                        break;

                }
            }



        } catch (IOException e) {

        }

    }
}
