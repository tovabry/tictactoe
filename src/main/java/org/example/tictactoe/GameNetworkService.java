package org.example.tictactoe;

import org.example.tictactoe.socketo.Cliento;
import org.example.tictactoe.socketo.Servero;

import java.io.IOException;

public class GameNetworkService {
    private Servero server;
    private Cliento client;
    private boolean isServer;
    private boolean isClient;

    // Konstruktor för server
    public GameNetworkService(Servero server) {
        this.server = server;
        this.isServer = true;
    }

    // Konstruktor för klient
    public GameNetworkService(Cliento client) {
        this.client = client;
        this.isServer = false;
    }

    // Skicka ett drag till motståndaren
    public void sendMove(int row, int col) {
        if (isServer && server != null) {
            server.sendMove(row, col);
        } else if (!isServer && client != null) {
            client.sendMove(row, col);
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    // Ta emot ett meddelande (drag eller signal)
    public String receiveMessage() throws IOException {
        if (isServer && server != null) {
            return server.receiveMessage();
        } else if (!isServer && client != null) {
            return client.receiveMessage();
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    // Skicka en vinstsignal till motståndaren
    public void sendWinSignal() {
        if (isServer && server != null) {
            server.sendWinSignal();
        } else if (!isServer && client != null) {
            client.sendWinSignal();
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    // Stoppa servern eller klienten
    public void stop() throws IOException {
        if (isServer && server != null) {
            server.stopServer();
        } else if (!isServer && client != null) {
            client.stopClient();
        } else {
            throw new IllegalStateException("Server eller klient är inte korrekt initierad.");
        }
    }

    // Hämta serverinstansen (om denna är server)
    public Servero getServer() {
        if (isServer) {
            return server;
        } else {
            throw new IllegalStateException("Detta är inte en server.");
        }
    }

    // Hämta klientinstansen (om denna är klient)
    public Cliento getClient() {
        if (!isServer) {
            return client;
        } else {
            throw new IllegalStateException("Detta är inte en klient.");
        }
    }

    // Kolla om detta är en server
    public boolean isServer() {
        return isServer;
    }

    public boolean isClient() {
        return isClient;
    }
}
