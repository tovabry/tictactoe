package org.example.tictactoe;

import org.example.tictactoe.socketo.Cliento;
import org.example.tictactoe.socketo.Servero;

import java.io.IOException;

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
        this.isClient = true;
    }

    public void sendMove(int row, int col) {
        if (isServer && server != null) {
            server.sendMove(row, col, Model.PLAYER_SERVER);
        } else if (isClient && client != null) {
            client.sendMove(row, col, Model.PLAYER_CLIENT);
        }
    }


    public String receiveMessage() throws IOException {
        if (isServer && server != null) {
            return server.receiveMessage();
        } else if (isClient && client != null) {
            return client.receiveMessage();
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    public void sendWinSignal(char winner) {
        if (isServer && server != null) {
            server.sendWinSignal(winner);
        } else if (isClient && client != null) {
            client.sendWinSignal(winner);
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    public void sendDrawSignal() {
        if (isServer && server != null) {
            server.sendDrawSignal("DRAW_SIGNAL");
        } else if (isClient && client != null) {
            client.sendDrawSignal("DRAW_SIGNAL");
        } else {
            System.err.println("Server eller klient är inte korrekt initierad.");
        }
    }

    public void stop() throws IOException {
        if (isServer && server != null) {
            server.stopServer();
            server = null;
        } else if (isClient && client != null) {
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
        if (isClient) {
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
