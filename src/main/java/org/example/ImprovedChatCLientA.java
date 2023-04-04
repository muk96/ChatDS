package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//This is the improved version of a client. THis sends messages to the server and
//read messages from any other participants.
public class ImprovedChatCLientA {

    public static void main(String[] args){
        new ImprovedChatCLientA().go();
    }
    int portNo = 12345; //Server port to connect to
    Socket sock;
    PrintWriter printWriter;
    InputStreamReader inputStreamReader;

    BufferedReader bufferedReader;

    void go() {
        try {
            setUpNetworking();
            IncomingReader incomingReader = new IncomingReader();
            Thread t1 = new Thread(incomingReader);
            t1.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setUpNetworking() throws IOException{
        sock = new Socket("127.0.0.1", portNo);
        System.out.println("Client A connected to server");

        printWriter = new PrintWriter(sock.getOutputStream());

        inputStreamReader = new InputStreamReader(sock.getInputStream());
        bufferedReader = new BufferedReader(inputStreamReader);

        sendMessages();
    }

    void sendMessages(){
        while(true) {
            Scanner sc = new Scanner(System.in);
            String clientMessage = sc.nextLine();

            printWriter.println("Client A : " + clientMessage);
            printWriter.flush();
        }
    }

    public class IncomingReader implements Runnable{
        @Override
        public void run() {
            try {
                while(bufferedReader.readLine()!=null){
                   System.out.println(bufferedReader.readLine());
                }
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
