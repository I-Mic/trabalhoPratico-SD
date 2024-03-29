package Server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ServerMsgHandler {

    private final int id;
    private String nome;
    private String password;
    private int isAdmin;
    private List<String> percurso;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String codReserva;
    private String origem;
    private String destino;
    private int capacidade;
    private LocalDate data;



    public ServerMsgHandler() {
        this.id = 0;
    }

    //So o id
    public ServerMsgHandler(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }


    public List<String> getPercurso() {
        return percurso;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public String getCodReserva() {
        return codReserva;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public LocalDate getData() {
        return data;
    }


    // converte uma mensagem  num array de bytes
    public int receiveMsg(DataInputStream dis) throws IOException {
        int i = dis.readInt();

        switch(i){
            case 1:
                this.nome = dis.readUTF();
                this.password = dis.readUTF();
                this.isAdmin = dis.read();
                break;

            case 2:
                this.nome = dis.readUTF();
                this.password = dis.readUTF();
                break;

            case 3:
                String percusoString = dis.readUTF();
                this.percurso = Arrays.asList(percusoString.substring(1,percusoString.length()-1).split(", "));
                this.dataInicio = LocalDate.parse(dis.readUTF());
                this.dataFim = LocalDate.parse(dis.readUTF());
                this.nome = dis.readUTF();
                break;

            case 4:
                this.codReserva = dis.readUTF();
                this.nome = dis.readUTF();
                break;

            case 6:
                this.origem = dis.readUTF();
                this.destino = dis.readUTF();
                this.capacidade= dis.read();
                this.data = LocalDate.parse(dis.readUTF());
                break;

        }

        return i;
    }

    public void sendResponseInt(DataOutputStream dos,int resposta) throws IOException {
        dos.writeInt(1);
        dos.writeInt(resposta);
    }

    public void sendResponseString(DataOutputStream dos,String resposta) throws IOException {
        dos.writeInt(2);
        dos.writeUTF(resposta);
    }
}
