package comps1;

import java.io.*;
import java.net.*;

public class GameClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);


            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String token = reader.readLine();
            System.out.println("Ditt spelartoken: " + token);

            System.out.println("Skapar ett nytt spel och inväntar en annan spelare...");
            String response = reader.readLine();
            System.out.println(response);

            System.out.println("Inväntar anslutning från en annan spelare...");
            response = reader.readLine();
            System.out.println(response);

            System.out.println("Spelet har börjat. Välj din figur (sten, sax, påse):");
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String choice = consoleReader.readLine();

            writer.println(choice);

            String result = reader.readLine();
            System.out.println(result);


            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

