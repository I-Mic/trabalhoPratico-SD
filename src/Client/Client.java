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

    ClientUI ui;


    public Client(int port, String ip){
        this.port = port;
        this.hostname = ip;
        this.menu = new Menu();
    }


    private void menuIniUser(User user) throws IOException{
        try{
            menu.menuUser();
            String userInput;
            while((userInput = systemIn.readLine()) !=null && !userInput.equals("0")){
                int op = Integer.parseInt(userInput);
                switch (op){
                    case 1:{
                        //Fazer Reserva
                        clearScreen();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        List <String> percurso = new ArrayList<>();

                        //Inserir Percurso
                        menu.menuInsPerc();
                        System.out.println("Insira o percurso desejado separado por enter, quando quiser acabar insira o numero 5");

                        while(true){
                            String lineRead = systemIn.readLine();
                            if(lineRead.equals("5")) break;
                            percurso.add(lineRead);
                        }
                        clearScreen();
                        System.out.println(percurso);

                        //Data do inicio
                        menu.menuInsDatIni();
                        System.out.println("Insira uma data de inicio no formato dd/MM//AAAA");
                        LocalDate ini =  LocalDate.parse(systemIn.readLine(),formatter);
                        clearScreen();
                        //Data do fim
                        menu.menuInsDatFin();
                        System.out.println("Insira uma data de fim no formato dd/MM//AAAA");
                        LocalDate fin =  LocalDate.parse(systemIn.readLine(),formatter);
                        clearScreen();

                        //Enviar
                        ClientMsgHandler res = new ClientMsgHandler(3,percurso,ini,fin,user.getNome());
                        res.sendMsg(out);
                        //resposta
                        res.receiveResponse(in);
                        String resposta = res.getRespostaString();

                        if(resposta.equals("n/a")){
                            System.out.println("Voo não encontrado.");
                        }
                        else if(resposta.equals("closed")){
                            System.out.println("Servidor encontra-se fechado");
                        }
                        else System.out.println("Código de reserva:" + resposta);

                        break;
                    }
                    case 2: {
                        //Cancelar uma reserva
                        clearScreen();
                        menu.menuCancReserva();
                        String cod = systemIn.readLine();
                        System.out.println(cod);
                        //enviar
                        ClientMsgHandler canc = new ClientMsgHandler(cod,user.getNome());
                        canc.sendMsg(out);
                        System.out.println("enviado");
                        //receber
                        canc.receiveResponse(in);
                        int resposta = canc.getRespostaInt();

                        if (resposta == 1) System.out.println("Reserva removida");
                        else if (resposta == 0) System.out.println("Reserva não encontrada");
                        else System.out.println("Servidor encontra-se fechado");

                        break;
                    }
                    case 3:{
                        //Lista de Voos
                        //enviar
                        ClientMsgHandler listVoo = new ClientMsgHandler(5);
                        listVoo.sendMsg(out);
                        //receber
                        listVoo.receiveResponse(in);
                        System.out.println("Voos existentes: " + listVoo.getRespostaString());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void menuIniAdmin(Admin admin) throws IOException{
        try{
            menu.menuAdmin();
            String userInput;
            while((userInput = systemIn.readLine()) !=null && !userInput.equals("0")){
                int op = Integer.parseInt(userInput);
                switch (op){
                    case 1:{
                        //Inserir Novo Voo
                        clearScreen();
                        //Inserir Origem do Voo
                        menu.menuInsVoo();
                        String ori = systemIn.readLine();
                        clearScreen();
                        //Inserir Destino do Voo
                        menu.menuInsDes();
                        String dest = systemIn.readLine();
                        clearScreen();
                        //Inserir Capacidade do Voo
                        menu.menuInsCap();
                        int cap = Integer.parseInt(systemIn.readLine());
                        clearScreen();
                        //Inserir Data do Voo
                        menu.menuCancDia();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        System.out.println("Insira no formato dd/MM/AAAA");
                        LocalDate dia =  LocalDate.parse(systemIn.readLine(),formatter);
                        clearScreen();

                        //Enviar
                        ClientMsgHandler voo = new ClientMsgHandler(6,ori,dest,cap,dia);
                        voo.sendMsg(out);
                        System.out.println("Voo criado com sucesso");
                        break;
                    }
                    case 2: {
                        //Cancelar Dia
                        clearScreen();
                        ClientMsgHandler cancelarDia = new ClientMsgHandler(7);
                        //enviar
                        cancelarDia.sendMsg(out);
                        //resposta
                        cancelarDia.receiveResponse(in);
                        int resposta = cancelarDia.getRespostaInt();
                        if (resposta == 1) System.out.println("Servidor fechado por hoje");
                        else System.out.println("Servidor ja se encontra fechado por hoje");
                        break;
                    }
                    case 3:{
                        //Reabrir Dia
                        clearScreen();
                        ClientMsgHandler abrirDia = new ClientMsgHandler(8);
                        //Enviar
                        abrirDia.sendMsg(out);
                        //resposta
                        abrirDia.receiveResponse(in);
                        int response = abrirDia.getRespostaInt();
                        if(response == 1) System.out.println("Servidor reaberto");
                        else System.out.println("Servidor ja se encontra aberto");
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

        try{ while ((userInput = systemIn.readLine())!=null && !userInput.equals("0")) {
            try{
                int con = Integer.parseInt(userInput);
                switch(con){
                    case 1:{
                        //registar Conta
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

                        //Enviar
                        ClientMsgHandler log = new ClientMsgHandler(1,nome,pass,adm);
                        log.sendMsg(out);
                        System.out.println("|REGISTO| nome: "+ nome + " password: "+  pass);
                        clearScreen();

                        //receber
                        log.receiveResponse(in);
                        int resposta = log.getRespostaInt();
                        if (resposta==1){
                            System.out.println("Registo feito com sucesso");
                            if(adm == 1) {
                                Admin admin = new Admin(nome, pass);
                                menuIniAdmin(admin);
                            }
                            else{
                                User user = new User(nome,pass);
                                menuIniUser(user);
                            }
                        }
                        else{
                            System.out.println("Nome ja existe");
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
                        cMsg.sendMsg(out);

                        //resposta do servidor
                        cMsg.receiveResponse(in);
                        int resposta = cMsg.getRespostaInt();
                        System.out.println(resposta);

                        if(resposta==1){
                            //Menu Admin
                            System.out.println("Login feito com sucesso");
                            Admin admin=new Admin(nome,pass);
                            menuIniAdmin(admin);
                        }
                        if(resposta==0) {
                            //menu User
                            System.out.println("Login feito com sucesso");
                            User user=new User(nome,pass);
                            menuIniUser(user);
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
