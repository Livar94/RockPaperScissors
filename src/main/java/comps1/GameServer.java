package comps1;

import java.io.*;
import java.net.*;

public class GameServer {
    private ServerSocket serverSocket;
    private Socket player1Socket;
    private Socket player2Socket;
    private PrintWriter player1Writer;
    private PrintWriter player2Writer;

    public GameServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForPlayers() {
        try {
            System.out.println("Väntar på spelare 1...");
            player1Socket = serverSocket.accept();
            System.out.println("Spelare 1 har anslutit.");

            System.out.println("Väntar på spelare 2...");
            player2Socket = serverSocket.accept();
            System.out.println("Spelare 2 har anslutit.");

            player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);
            player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);

            player1Writer.println("TOKEN_PLAYER1");
            player2Writer.println("TOKEN_PLAYER2");

            GameHandler gameHandler = new GameHandler(player1Socket, player2Socket);
            gameHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer(1234); // Ange önskat portnummer
        gameServer.waitForPlayers();
    }
}

class GameHandler extends Thread {
    private Socket player1Socket;
    private Socket player2Socket;
    private BufferedReader player1Reader;
    private BufferedReader player2Reader;
    private PrintWriter player1Writer;
    private PrintWriter player2Writer;

    public GameHandler(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
    }

    public void run() {
        try {
            player1Reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            player2Reader = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);
            player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);

            String player1Choice, player2Choice;

            while (true) {
                player1Choice = player1Reader.readLine();
                player2Choice = player2Reader.readLine();

                String winner = determineWinner(player1Choice, player2Choice);

                player1Writer.println(winner);
                player2Writer.println(winner);

                if (!winner.equals("Tie")) {
                    break;
                }
            }

            player1Socket.close();
            player2Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String determineWinner(String player1Choice, String player2Choice) {

        if (player1Choice.equals(player2Choice)) {
            return "Tie";
        } else if ((player1Choice.equals("sten") && player2Choice.equals("sax")) ||
                (player1Choice.equals("sax") && player2Choice.equals("påse")) ||
                (player1Choice.equals("påse") && player2Choice.equals("sten"))) {
            return "Spelare 1 vinner!";
        } else {
            return "Spelare 2 vinner!";
        }
    }
}
