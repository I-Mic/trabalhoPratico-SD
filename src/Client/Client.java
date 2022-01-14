package Client;

import Server.ServerMsgHandler;
import business.Admin;
import business.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private DataInputStream in;
    private DataOutputStream out;
    private int port;
    private Socket clientSocket;
    private String hostname;
    private BufferedReader systemIn;
    Menu menu;
    Admin admin;
    User user;
    ClientUI ui;

    public Client(int port, String ip){
        this.port = port;
        this.hostname = ip;
    }


    private void menuIniUser() throws IOException{
        try{
            String userInput;
            while((userInput = systemIn.readLine()) !=null && !userInput.equals("0")){
                int op = Integer.parseInt(userInput);
                switch (op){
                    case 1:{
                        clearScreen();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                        List <String> percurso = new ArrayList<>();
                        menu.menuInsPerc();
                        System.out.println("Insira o percurso desejado separado por enter, quando quiser acabar insira o numero 5");
                        while(!systemIn.readLine().equals("5")){
                            String cid = systemIn.readLine();
                            percurso.add(cid);
                        }
                        clearScreen();
                        menu.menuInsDatIni();
                        System.out.println("Insira uma data de inicio no formato dd/MM//AAAA");
                        LocalDate ini =  LocalDate.parse(systemIn.readLine(),formatter);
                        clearScreen();
                        menu.menuInsDatFin();
                        System.out.println("Insira uma data de fim no formato dd/MM//AAAA");
                        LocalDate fin =  LocalDate.parse(systemIn.readLine(),formatter);
                        clearScreen();
                        ClientMsgHandler res = new ClientMsgHandler(3,percurso,ini,fin,user.getNome());
                        res.sendMsg(this.clientSocket);
                        //resposta
                        String resposta = in.readUTF();
                        if(resposta.equals("null")){
                            System.out.println("Voo não encontrado.");
                        }
                        else{
                            System.out.println("Código de reserva:" + resposta);}
                        break;
                    }
                    case 2: {
                        clearScreen();
                        menu.menuCancReserva();
                        String cod = systemIn.readLine();
                        ClientMsgHandler canc = new ClientMsgHandler(4,cod);
                        canc.sendMsg(this.clientSocket);
                        int resposta = in.readInt();
                        if (resposta != 0)
                            System.out.println("Reserva removida");
                        else System.out.println("Reserva não encontrada");
                        break;
                    }
                    case 3:{
                        System.out.println("Voos existentes: " + in.readUTF());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void menuIniAdmin() throws IOException{
        try{
            String userInput;
            while((userInput = systemIn.readLine()) !=null && !userInput.equals("0")){
                int op = Integer.parseInt(userInput);
                switch (op){
                    case 1:{
                        clearScreen();
                        menu.menuInsVoo();
                        String ori = systemIn.readLine();
                        clearScreen();
                        menu.menuInsDes();
                        String dest = systemIn.readLine();
                        clearScreen();
                        menu.menuInsCap();
                        int cap = Integer.parseInt(systemIn.readLine());
                        clearScreen();
                        menu.menuCancDia();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                        System.out.println("Insira no formato dd/MM/AAAA");
                        LocalDate dia =  LocalDate.parse(systemIn.readLine(),formatter);
                        clearScreen();
                        ClientMsgHandler voo = new ClientMsgHandler(6,ori,dest,cap,dia);
                        voo.sendMsg(this.clientSocket);
                        //receber
                        System.out.println("Voo criado com sucesso");
                        break;
                    }
                    case 2:{
                        clearScreen();
                        menu.menuCancDia();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                        LocalDate dia =  LocalDate.parse(systemIn.readLine(),formatter);
                        //enviar
                        //receber
                        System.out.println("Dia " + dia.format(formatter) + "cancelado");
                        break;
                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void startMenu(){
        clearScreen();
        menu.menuWelcomeClient();
        menu.menuInit();
        String userInput;
        int resposta;
        try{ while ((userInput = systemIn.readLine())!=null && !userInput.equals("0")) {
            try{
                int con = Integer.parseInt(userInput);
                switch(con){
                    case 1:{
                        clearScreen();
                        int adm = 0;//1 se sim, 0 se não
                        System.out.println("Deseja registar-se como admin?");
                        if(systemIn.readLine().equals("Sim"))
                            adm=1;
                        clearScreen();
                        menu.menuRegistrarNome();
                        String nome = systemIn.readLine();
                        clearScreen();
                        menu.menuRegistrarPass();
                        String pass = systemIn.readLine();
                        clearScreen();
                        ClientMsgHandler log = new ClientMsgHandler(1,nome,pass,adm);
                        log.sendMsg(this.clientSocket);
                        System.out.println("|REGISTO| nome: "+ nome + " password: "+  pass);
                        clearScreen();
                        resposta = in.readInt();
                        System.out.println(resposta);
                        if (resposta==1){
                            System.out.println("Registo admin feito com sucesso");
                            admin = new Admin(nome,pass);
                            menu.menuAdmin();
                            menuIniAdmin();
                        }
                        if(resposta==0){
                            System.out.println("Registo user feito com sucesso");
                            menu.menuUser();
                            user = new User(nome,pass);
                            menuIniUser();
                        }
                        else{
                            System.out.println("Nome já existe.");// Nao foi possivel realizar op
                            menu.menuInit();
                        }
                        break;
                    }
                    case 2:{
                        clearScreen();
                        menu.menuRegistrarNome();
                        String nome = systemIn.readLine();
                        clearScreen();
                        menu.menuRegistrarPass();
                        String pass = systemIn.readLine();
                        clearScreen();
                        System.out.println("|LOGIN| nome: "+ nome + " password: "+  pass);
                        ClientMsgHandler cMsg=new ClientMsgHandler(2,nome,pass);
                        cMsg.sendMsg(clientSocket);
                        //resposta do servidor
                        cMsg.receiveResponse(clientSocket);
                        resposta=cMsg.getRespostaInt();
                        System.out.println(resposta);
                        if(resposta==1){
                            System.out.println("Login feito com sucesso");
                            admin=new Admin(nome,pass);
                            menu.menuAdmin();
                            menuIniAdmin();
                            // falta metodo menuUser semelhante a este
                        }
                        if(resposta==0) {
                            //menu User
                            menu.menuUser();
                            user=new User(nome,pass);
                            menuIniUser();
                        }
                        else{
                            System.out.println("Dados inseridos estão errados");
                            menu.menuInit();
                        }
                        break;
                    }
                    default:
                        System.out.println("A Opção que inseriu não é válida. Por favor tente de novo.");
                        menu.menuInit();
                }
            }
            catch (NumberFormatException e){
                clearScreen();
                System.out.println("Erro");
                menu.menuInit();
            }
            catch (IOException e){
                System.out.println("Erro");
            }

        }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startClient() {

        try {
            System.out.println("------Client------");
            System.out.println("---Ligando ao Servidor---");
            this.clientSocket = new Socket(this.hostname, this.port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            systemIn = new BufferedReader(new InputStreamReader(System.in));
            startMenu();
        }catch(UnknownHostException e){
            System.out.println("[ERROR]: Server doesn't exist!");
        }
        catch(Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
        finally {
            try {
                System.out.println("---Fechando Conexão---");
                this.clientSocket.shutdownInput();
                this.clientSocket.shutdownOutput();
                this.clientSocket.close();
            }
            catch (IOException e){
                System.out.println("[Error]: " + e.getMessage());
            }
        }

    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String args[]){

        Client client = new Client(12345,"127.0.0.1");
        client.startClient();

    }
}
