package Servers;

import App.DataHandler;
import App.MessageDataBase;
import Requests.Request;
import Requests.RequestFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) {
            try {
                //initialisation serveur
                ServerSocket serverSocket = new ServerSocket(12345);
                int counter = 1;
                System.out.println("client connecte");

                while (true) {

                    Socket incomming = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(incomming.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(incomming.getOutputStream()));

                    //reception message
                    String messageReceived = in.readLine();
                    messageReceived = messageReceived + "\r\n" + in.readLine() + "\r\n";

                    //interprétation requete
                    RequestFactory requestFactory = new RequestFactory();
                    Request request = requestFactory.createsRequest(messageReceived);
                    if (! request.isInvalidRequest()) {
                        request.execute();
                    }
                    out.println(request.getResponse());
                    out.flush();
                    incomming.close();

                    System.out.println(MessageDataBase.getInstance().getMessages());
                }


                //serverSocket.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
    }
}
