package org.example.tictactoe.socketo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servero{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started. Waiting for opponent...");
        clientSocket = serverSocket.accept();
        System.out.println("Opponent connected.");
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMove(int row, int col) {
        out.println(row + "," + col);
    }

    public int[] receiveMove() throws IOException {
        String move = in.readLine();
        String[] parts = move.split(",");
        return new int[] {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }

    public void sendScore(String score) {
        out.println(score);
    }
    public String receiveMessage() throws IOException {
        return in.readLine(); // Läser nästa rad från motståndaren
    }

    public int[] parseMove(String message) {
        String[] parts = message.split(",");
        return new int[] {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
    }



    public void sendWinSignal() {
        out.println("WIN_SIGNAL"); // Skicka signal om att spelet är slut och uppdatera poängen
    }


    public String receiveScore() throws IOException {
        return in.readLine();
    }


    public void stopServer() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}
