package serverFederation;

import app.MessageDataBase;
import app.Message;
import app.ServerAddress;
import app.Subscription;
import clients.Client;
import requests.*;
import servers.Counter;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FederateServer {
    public static void main(String[] args) {


        FederateServer server = new FederateServer(12345, "localhost");
        server.addConnectedServer("localhost", 12346);
        try {
            server.run();
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    private int port;
    private String address;
    private ArrayList<ServerAddress> connectedServers = new ArrayList<>();

    public FederateServer(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public void run() throws IOException {
        Executor executor = Executors.newFixedThreadPool(100);
        //initialisation serveur
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Thread thread = new ClientHandlerMicroblogCentral(serverSocket.accept(), executor, this);
            executor.execute(thread);
        }
    }

    public void addConnectedServer(String address, int port) {
        connectedServers.add(new ServerAddress(address, port));
    }

    public ArrayList<ServerAddress> getConnectedServers() {
        return connectedServers;
    }
}

class ClientHandlerMicroblogCentral extends Thread {
    private final Socket socket;
    private int threadNumber;
    private ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();
    private Executor executor;
    private boolean isConnected = false;
    private FederateServer server;

    public ClientHandlerMicroblogCentral(Socket socket, Executor executor, FederateServer server) {
        this.executor = executor;
        this.socket = socket;
        this.server = server;
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

                    //si le message debute par serveur connect supprimer le SERVERCONNECT de la requête
                    //sinon s'il s'agit d'une requête de publication envoyer la requête aux autres serveurs connectés
                    if(messageReceived.startsWith("SERVERCONNECT")) {
                        //"SERVERCONNECT".length() + 1 =
                        messageReceived = messageReceived.substring(14);
                    }
                    else {
                        if(messageReceived.startsWith("PUBLISH") || messageReceived.startsWith("REPLY") || messageReceived.startsWith("REPUBLISH")) {
                            String newRequest = "SERVERCONNECT " + messageReceived;
                            for (ServerAddress address : server.getConnectedServers()) {
                                executor.execute(new RequestSender(address, newRequest));
                            }
                        }
                    }

                    //interprétation requete
                    RequestFactory requestFactory = new RequestFactory();


                    Request request = requestFactory.createsRequest(messageReceived);

                    //Connection case
                    if (messageReceived.startsWith("CONNECT") && request.getResponse().startsWith("OK")) {
                        System.out.println("Connection establish with :" + socket.getInetAddress());
                        executor.execute(new ConnectionHandler(queue, socket));
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

class RequestSender extends Thread {
    private String request;
    private Client client;

    public RequestSender(ServerAddress address, String request) {
        this.request = request;
        this.client = new Client(address.getAddress(), address.getPort());
    }

    @Override
    public void run() {
        client.sendRequest(request);
    }
}