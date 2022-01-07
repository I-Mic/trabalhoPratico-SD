package Client;

import java.io.IOException;

public class Client {

    private void startClient() throws Exception {

        try {


        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }

    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String args[]) throws IOException, Exception{

        Client client = new Client();
        client.startClient();

    }
}
