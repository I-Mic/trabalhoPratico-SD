package Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private DataInputStream in;
    private DataOutputStream out;
    private int port;
    private Socket clientSocket;
    private String hostname;
    private BufferedReader systemIn;
    Menu menu = new Menu();
    ClientUI ui = new ClientUI();

    public Client(int port, String ip){
        this.port = port;
        this.hostname = ip;
    }


    private void startMenu(BufferedReader systemIn){
        Client.clearScreen();
        menu.menuWelcomeClient();
        menu.menuInit();
        String userInput;
        String resposta;
        try{ while ((userInput = systemIn.readLine())!=null && !userInput.equals("3")) {
            try{
                int con = Integer.parseInt(userInput);
                switch(con){
                    case 1:{
                        clearScreen();
                        menu.menuRegistrarNome();
                        String nome = systemIn.readLine();
                        clearScreen();
                        menu.menuRegistrarPass();
                        String pass = systemIn.readLine();
                        clearScreen();
                        System.out.println("|REGISTO| nome: "+ nome + "password:"+  pass);
                        clearScreen();
                        resposta = in.readUTF();
                        System.out.println(resposta);
                        if (!resposta.equals("Nome já existe")){
                            menu.menuUser();
                            // falta metodo menuUser semelhante a este
                        }
                        else{
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
                        resposta = in.readUTF();
                        System.out.println(resposta);
                        if(resposta.equals("Login feito com sucesso")){
                            menu.menuUser();
                            // falta metodo menuUser semelhante a este
                        }
                        if((resposta.equals("Password incorreta")) || (resposta.equals("Nome incorreto") || (resposta.equals("Utilizador ja fez login")))) {
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
