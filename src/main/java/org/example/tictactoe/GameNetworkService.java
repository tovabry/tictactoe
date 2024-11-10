package org.example.tictactoe;

import org.example.tictactoe.socketo.Cliento;
import org.example.tictactoe.socketo.Servero;

import java.io.IOException;

import static java.lang.System.out;

public class GameNetworkService {
    private Servero server;
    private Cliento client;
    private boolean isServer;
    private boolean isClient;

    public GameNetworkService(Servero server) {
        this.server = server;
        this.isServer = true;
    }

    public GameNetworkService(Cliento client) {
        this.client = client;
        this.isServer = false;
    }

    public void sendMove(int row, int col) {
        if (isServer && server != null) {
            server.sendMove(row, col);
        } else if (!isServer && client != null) {
            client.sendMove(row, col);
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    public String receiveMessage() throws IOException {
        if (isServer && server != null) {
            return server.receiveMessage();
        } else if (!isServer && client != null) {
            return client.receiveMessage();
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    public void sendWinSignal(char winner) {
        if (isServer && server != null) {
            server.sendWinSignal(winner);
        } else if (!isServer && client != null) {
            client.sendWinSignal(winner);
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    public void sendDrawSignal() {
        if (isServer && server != null) {
            server.sendDrawSignal("DRAW_SIGNAL"); // Skicka DRAW_SIGNAL till klienten
        } else if (!isServer && client != null) {
            client.sendDrawSignal("DRAW_SIGNAL"); // Skicka DRAW_SIGNAL till servern
        } else {
            System.err.println("Server eller klient är inte korrekt initierad.");
        }
    }

    public void stop() throws IOException {
        if (isServer && server != null) {
            server.stopServer();
            server = null;
        } else if (!isServer && client != null) {
            client.stopClient();
            client = null;
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    public Servero getServer() {
        if (isServer) {
            return server;
        } else {
            throw new IllegalStateException("Detta är inte en server.");
        }
    }

    public Cliento getClient() {
        if (!isServer) {
            return client;
        } else {
            throw new IllegalStateException("Detta är inte en klient.");
        }
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isClient() {
        return isClient;
    }


}
