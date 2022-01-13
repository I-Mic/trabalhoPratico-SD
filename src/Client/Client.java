package Client;

import business.Admin;
import business.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    Menu menu = new Menu();
    Admin admin;
    User user;
    ClientUI ui = new ClientUI();

    public Client(int port, String ip){
        this.port = port;
        this.hostname = ip;
    }


    private void menuIniUser(User user) throws IOException{
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
    private void menuIniAdmin(Admin admin) throws IOException{
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
                        System.out.println("Voo criado com sucesso");
                        break;
                    }
                    case 2:{
                        clearScreen();
                        menu.menuCancDia();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                        LocalDate dia =  LocalDate.parse(systemIn.readLine(),formatter);
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
                        System.out.println("|REGISTO| nome: "+ nome + "password:"+  pass);
                        clearScreen();
                        resposta = in.readInt();
                        System.out.println(resposta);
                        if (resposta==1){
                            System.out.println("Registo admin feito com sucesso");
                            admin = new Admin(nome,pass);
                            menu.menuAdmin();
                            menuIniAdmin(admin);
                        }
                        if(resposta==0){
                            System.out.println("Registo user feito com sucesso");
                            menu.menuUser();
                            user = new User(nome,pass);
                            menuIniUser(user);
                        }
                        else{
                            System.out.println("Nome já existe.");
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
                        System.out.println("|LOGIN| nome: "+ nome + "password:"+  pass);
                        resposta = in.readInt();
                        System.out.println(resposta);
                        if(resposta==1){
                            System.out.println("Login feito com sucesso");
                            menu.menuUser();
                            // falta metodo menuUser semelhante a este
                        }
                        if(resposta==0) {
                            System.out.println("Dados inseridos estão errados");
                            menu.menuInit();
                        }
                        break;}
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

    private void startClient() throws Exception {

        try {
            System.out.println("------Client------");
            System.out.println("---Ligando ao Servidor---");
            this.clientSocket = new Socket(this.hostname, this.port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            systemIn = new BufferedReader(new InputStreamReader(System.in));

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

    public static void main(String args[]) throws IOException, Exception{

        Client client = new Client(12345,"127.0.0.1");
        client.startClient();

    }
}
