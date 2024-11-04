package org.example.tictactoe.socketo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliento {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public void startClient(String ip, int port) {
           try{
               socket = new Socket(ip, port);
               out = new PrintWriter(socket.getOutputStream(), true);
               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               System.out.println("Connected to server.");
            }catch (IOException e){
               e.printStackTrace(); //för att se var problemet uppstått
               System.err.println("Unable to connect to server. Could be because the host hasn't hosted yet... " + e.getMessage());
            }
        }

        public void sendMove(int row, int col) {
            out.println(row + "," + col);
        }

        public int[] receiveMove() throws IOException {
            String move = in.readLine();
            String[] parts = move.split(",");
            return new int[] {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
        }

        public void stopClient() throws IOException {
            in.close();
            out.close();
            socket.close();
        }
    }

