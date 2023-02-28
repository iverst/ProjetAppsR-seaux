package Servers;

import App.Message;
import App.MessageDataBase;
import Requests.Request;
import Requests.RequestFactory;
import Requests.RequestMaker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MicroblogCentral {
    public static void main(String[] args) {
        MicroblogCentral mircoblog = new MicroblogCentral(12345, "localhost");
        try {
            mircoblog.run();
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    private int port;
    private String address;

    public MicroblogCentral(int port, String address) {
        this.port = port;
        this.address = address;
    }

    private void run() throws IOException {
        Executor executor = Executors.newCachedThreadPool();
        //initialisation serveur
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Thread thread = new ClientHandlerMicroblogCentral(serverSocket.accept());
            thread.start();
            executor.execute(thread);
        }

    }
}

class ClientHandlerMicroblogCentral extends Thread {
    private final Socket socket;
    private int threadNumber;

    public ClientHandlerMicroblogCentral(Socket socket) {
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
            //subscription case
            if(messageReceived.startsWith("SUBSCRIBE") && request.getResponse().startsWith("OK")) {
                manageSubscription();
            }
            else {
                out.println(request.getResponse());
                out.flush();
                System.out.println(MessageDataBase.getInstance().getMessages());
                socket.close();
            }

        } catch (Exception e) {

        }
    }

    private void manageSubscription() throws IOException {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        //recupérer le message
        RequestFactory factory = new RequestFactory();
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

        while (true) {
            Message message = queue.poll();
            if(message == null) {
                socket.close();
                return;
            }
            Request request = factory.createsRequest(new RequestMaker().getRequest("RCV_MSG msg_id:" + message.getId(), ""));
            System.out.println(request.getResponse());
            out.println(request.getResponse());
            out.flush();
        }
    }
}