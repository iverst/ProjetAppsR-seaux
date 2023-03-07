package servers;

import app.MessageDataBase;
import requests.Request;
import requests.RequestFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) {
            try {
                //initialisation serveur
                ServerSocket serverSocket = new ServerSocket(12345);
                int counter = 1;

                while (true) {

                    Socket incomming = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(incomming.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(incomming.getOutputStream()));
                    System.out.println("client connecte");

                    //reception message
                    String messageReceived = in.readLine();
                    messageReceived = messageReceived + "\r\n" + in.readLine() + "\r\n";

                    //interprétation requete
                    RequestFactory requestFactory = new RequestFactory();
                    Request request = requestFactory.createsRequest(messageReceived);


                    request.execute();
                    System.out.println(request.getResponse());
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
