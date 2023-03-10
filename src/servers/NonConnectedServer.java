package servers;

import app.MessageDataBase;
import requests.Request;
import requests.RequestFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NonConnectedServer {
    public static void main(String[] args) {
        NonConnectedServer nonConnectedServer = new NonConnectedServer(12345, "localhost");
        try {
            nonConnectedServer.run();
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    private int port;
    private String address;

    public NonConnectedServer(int port, String address) {
        this.port = port;
        this.address = address;
    }

    private void run() throws IOException {
        Executor executor = Executors.newCachedThreadPool();
        //initialisation serveur
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Thread thread = new ClientHandler(serverSocket.accept());
            executor.execute(thread);
        }

    }
}

class ClientHandler extends Thread {
    private final Socket socket;
    private int threadNumber;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        threadNumber = Counter.getInstance().getAndIncrement();
        System.out.println("Thread number :" + threadNumber);
    }


    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("client connecte");

            //reception message
            String messageReceived = in.readLine();
            messageReceived = messageReceived + "\r\n" + in.readLine() + "\r\n";

            //interprétation requete
            RequestFactory requestFactory = new RequestFactory();
            Request request = requestFactory.createsRequest(messageReceived);


            //reponse requete
            request.execute();
            out.println(request.getResponse());
            out.flush();
            System.out.println(MessageDataBase.getInstance().getMessages());
            socket.close();

        } catch (Exception e) {
        }

    }
}
