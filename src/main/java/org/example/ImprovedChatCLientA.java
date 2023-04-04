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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name : ");
        String name = scanner.next();
        new ImprovedChatCLientA(name).go();
    }

    ImprovedChatCLientA(String name){
        this.name = name;
    }

    String name;
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
            sendMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setUpNetworking() throws IOException{
        sock = new Socket("127.0.0.1", portNo);
        System.out.println(name + " connected to server");

        printWriter = new PrintWriter(sock.getOutputStream());

        inputStreamReader = new InputStreamReader(sock.getInputStream());
        bufferedReader = new BufferedReader(inputStreamReader);
    }

    void sendMessages(){
        Scanner sc;
        String clientMessage;
        while(true) {
            sc = new Scanner(System.in);
            clientMessage = sc.nextLine();

            if(clientMessage.equals("exit"))
                break;

            printWriter.println(name + " : " + clientMessage);
            printWriter.flush();
        }
    }

    public class IncomingReader implements Runnable{
        @Override
        public void run() {
            try {
                String msg;
                while((msg = bufferedReader.readLine())!=null){
                   System.out.println(msg);
                }
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
