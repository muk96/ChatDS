package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class VerySimpleChatServer {
    ArrayList clientOutputStreams;
    ServerSocket serverSocket;

    public static void main (String[] args) {
        new VerySimpleChatServer().go();
    }

    public void go(){
        clientOutputStreams = new ArrayList();
        try {
            serverSocket = new ServerSocket(12345);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
                System.out.println("Got a connection!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void tellEveryone(String message) {
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        } // end while
    }

    class ClientHandler implements Runnable{
        Socket socket;
        BufferedReader reader;
        ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(isReader);
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read ->" + message);
                    tellEveryone(message);
                } // close while
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
