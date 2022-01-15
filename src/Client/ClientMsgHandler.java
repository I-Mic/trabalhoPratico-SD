package Client;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;


public class ClientMsgHandler {

    private final int id;
    private  String nome;
    private  String password;
    private int isAdmin;
    private  List<String> percurso;
    private  LocalDate dataInicio;
    private  LocalDate dataFim;
    private  String codReserva;
    private String origem;
    private String destino;
    private int capacidade;
    private LocalDate data;
    private int respostaInt;
    private String respostaString;


    public ClientMsgHandler() {
        this.id = 0;
    }

    //So o id
    public ClientMsgHandler(int id){
        this.id = id;
    }

    //Caso Registar Conta  id = 1
    public ClientMsgHandler(int id, String nome, String pass, int isAdmin){
        this.id = id;
        this.nome = nome;
        this.password = pass;
        this.isAdmin = isAdmin;
    }


    //Caso LogIn id = 2
    public ClientMsgHandler(int id, String nome, String pass){
        this.id = id;
        this.nome = nome;
        this.password = pass;
    }

    //Caso fazer reserva id = 3
    public ClientMsgHandler(int id, List<String> percurso, LocalDate dataInicio, LocalDate daataFim, String nome){
        this.id = id;
        this.percurso = percurso;
        this.dataInicio = dataInicio;
        this.dataFim = daataFim;
        this.nome = nome;
    }

    //Caso Cancela reserva id = 4
    public ClientMsgHandler(String codReserva, String nome){
        this.id = 4;
        this.codReserva = codReserva;
        this.nome = nome;
    }

    //Caso AddVoo id = 6
    public ClientMsgHandler(int id, String origem, String destino, int capacidade, LocalDate data){
        this.origem = origem;
        this.destino = destino;
        this.capacidade=capacidade;
        this.data = data;
        this.id = id;
    }

    //Caso Recebe resposta com int id = 7
    public ClientMsgHandler(int id, int resposta){
        this.id = id;
        this.respostaInt = resposta;
    }

    //Caso Recebe resposta com String id = 8
    public ClientMsgHandler(int id, String resposta){
        this.id = id;
        this.respostaString = resposta;
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

    public int getRespostaInt() {
        return respostaInt;
    }

    public String getRespostaString() {
        return respostaString;
    }

    public String fromByteToString(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (i<a.length && a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }

    // converte uma mensagem  num array de bytes
    public void sendMsg(DataOutputStream dos) throws IOException {

        dos.writeInt(this.id);

        switch(this.id){
            case 1:
                dos.writeUTF(this.nome);
                dos.writeUTF(this.password);
                dos.write(this.isAdmin);
                break;

            case 2:
                dos.writeUTF(this.nome);
                dos.writeUTF(this.password);
                break;

            case 3:
                dos.writeUTF(String.valueOf(this.percurso));
                dos.writeUTF(String.valueOf(this.dataInicio));
                dos.writeUTF(String.valueOf(this.dataFim));
                dos.writeUTF(this.nome);
                break;

            case 4:
                dos.writeUTF(this.codReserva);
                dos.writeUTF(this.nome);
                break;

            case 6:
                dos.writeUTF(this.origem);
                dos.writeUTF(this.destino);
                dos.write(this.capacidade);
                dos.writeUTF(String.valueOf(this.data));
                break;

        }
    }

    public void receiveResponse(DataInputStream dis) throws IOException {
          int id = dis.readInt();
          if(id == 1) this.respostaInt = dis.readInt();
          else this.respostaString = dis.readUTF();



    }

}
