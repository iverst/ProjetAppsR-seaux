package servers;

import app.Message;
import app.MessageDataBase;
import app.Subscription;
import requests.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
        Executor executor = Executors.newFixedThreadPool(100);
        //initialisation serveur
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Thread thread = new ClientHandlerMicroblogCentral(serverSocket.accept(), executor);
            thread.start();
            executor.execute(thread);
        }

    }
}

class ClientHandlerMicroblogCentral extends Thread {
    private final Socket socket;
    private int threadNumber;
    private ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();
    private Executor executor;
    private boolean isConnected = false;

    public ClientHandlerMicroblogCentral(Socket socket, Executor executor) {
        this.executor = executor;
        this.socket = socket;
        threadNumber = Counter.getInstance().getAndIncrement();
        System.out.println("Thread number :" + threadNumber);
    }

    @Override
    public void run() {
        try {
            boolean isNull = false;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("client connecte");

            //reception message
            while (true) {
                String messageReceived = "";

                do {
                    if (in.ready()) {
                        messageReceived = in.readLine();
                    }
                } while (messageReceived == null || messageReceived.length() < 1);


                    String newLine = in.readLine();
                    messageReceived = messageReceived + "\r\n" + newLine + "\r\n";




                if (messageReceived != null && messageReceived.length() != 0) {
                    System.out.println("Message received :");
                    System.out.println(messageReceived);

                    //interprétation requete
                    RequestFactory requestFactory = new RequestFactory();


                    Request request = requestFactory.createsRequest(messageReceived);


                    //Connection case
                    if (messageReceived.startsWith("CONNECT") && request.getResponse().startsWith("OK")) {
                        System.out.println("Connection establish with :" + socket.getInetAddress());
                        Thread thread = new ConnectionHandler(queue, socket);
                        thread.start();
                        executor.execute(thread);
                        isConnected = true;
                        //queue.add(new Message(request.getResponse(), 499));

                        out.println(request.getResponse());
                        out.flush();

                    } else if (messageReceived.startsWith("SUBSCRIBE") && request.getResponse().startsWith("OK")) {
                        if (isConnected) {
                            System.out.println("Subscribe to " + messageReceived);

                            SUBSCRIBERequest subscriberRequest = (SUBSCRIBERequest) request;
                            subscriberRequest.setQueue(queue);
                            subscriberRequest.execute();
                            //queue.add(new Message(subscriberRequest.getResponse(), 499));

                            out.println(request.getResponse());
                            out.flush();


                        } else {
                            out.println(RequestMaker.getRequest("ERROR", "Trying to subscribe while not connected"));
                            out.flush();
                        }

                    } else if (messageReceived.startsWith("UNSUBSCRIBE") && request.getResponse().startsWith("OK")) {
                        if (isConnected) {
                            System.out.println("Unsubscribe to " + messageReceived);
                            SUBSCRIBERequest unsubRequest = (SUBSCRIBERequest) request;
                            unsubRequest.setQueue(queue);
                            unsubRequest.execute();
                            queue.add(new Message(unsubRequest.getResponse(), 499));


                            out.println(request.getResponse());
                            out.flush();
                        } else {
                            out.println(RequestMaker.getRequest("ERROR", "Trying to unsubscribe while not connected"));
                            out.flush();
                        }

                    }
                    //reponse requete
                    else {
                        request.execute();
                        out.print(request.getResponse());
                        out.flush();
                        //socket.close();
                        System.out.println("---------------------------------------");
                        System.out.println(request.getResponse());
                        System.out.println(MessageDataBase.getInstance().getMessages());
                        System.out.println(Subscription.getInstance());
                        break;
                    }

                    System.out.println("---------------------------------------");
                    System.out.println(request.getResponse());
                    System.out.println(MessageDataBase.getInstance().getMessages());
                    System.out.println(Subscription.getInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class ConnectionHandler extends Thread {
    private ConcurrentLinkedQueue<Message> queue;
    private Socket socket;
    private PrintWriter out;

    ConnectionHandler(ConcurrentLinkedQueue<Message> queue, Socket socket) {
        System.out.println("New connection handler !");
        this.socket = socket;
        this.queue = queue;
        try {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!queue.isEmpty()) {
                    Message message;
                    message = queue.poll().clone();
                    String response = RequestMaker.getRequest(message.getHeader(), message.getContent());
                    System.out.println(response);
                    out.print(response);
                    out.flush();
                }
            }
        }
        catch (Exception e) {

        }
    }


}
