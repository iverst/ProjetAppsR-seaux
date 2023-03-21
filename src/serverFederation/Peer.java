package serverFederation;

import app.PropertiesGetter;
import app.ServerAddress;
import requests.RequestMaker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Peer {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new Exception("Incorrect Number of arguments");
        }
        String fileName = "pairs.cfg";
        String[] properties  = PropertiesGetter.getProperty(fileName, "peer" + args[0]).split(" ");
        int port = Integer.parseInt(properties[1]);
        String address = properties[0];

        String[] masterProperties = PropertiesGetter.getProperty(fileName, "master").split(" ");
        ServerAddress masterAddress = new ServerAddress(masterProperties[0], Integer.parseInt(masterProperties[1]));

        Peer peer = new Peer(12346, "localhost", new ServerAddress("localhost", 12345));
        try {
            peer.run();
        }
        catch (IOException io) {
            io.printStackTrace();
        }

    }

    private int port;
    private String address;
    private ServerAddress master;

    public Peer(int port, String address, ServerAddress master) {
        this.port = port;
        this.address = address;
        this.master = master;
    }

    public void run() throws IOException {
        Executor executor = Executors.newFixedThreadPool(100);
        //initialisation serveur
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            executor.execute(new ClientHandlerPeer(serverSocket.accept(), executor, master));
        }
    }
}

class ClientHandlerPeer extends Thread {

    private Socket socket, masterSocket;
    private Executor executor;
    private ServerAddress master;
    private boolean isConnected =  false;

    public ClientHandlerPeer(Socket socket, Executor executor, ServerAddress master) throws IOException {
        this.socket = socket;
        this.executor = executor;
        this.master = master;
        masterSocket = new Socket(master.getAddress(), master.getPort());
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            BufferedReader inMaster = new BufferedReader(new InputStreamReader(masterSocket.getInputStream()));
            PrintWriter outMaster = new PrintWriter(new OutputStreamWriter(masterSocket.getOutputStream()));

            while (true) {
                //receive a message from a client
                //and send it to the master
                String messageReceived = "";

                do {
                    if (in.ready()) {
                        messageReceived = in.readLine();
                    }
                } while (messageReceived == null || messageReceived.length() < 1);
                String newLine = in.readLine();
                messageReceived = messageReceived + "\r\n" + newLine + "\r\n";

                if (messageReceived != null && messageReceived.length() != 0) {
                    System.out.println("sent :");
                    System.out.println(messageReceived);
                    outMaster.println(messageReceived);
                    outMaster.flush();

                    //receive a message from the master
                    //and send it back to the client
                    String response = "";
                    do {
                        if (inMaster.ready()) {
                            response = inMaster.readLine();
                        }
                    } while (response == null || response.length() < 1);

                    response = response + "\r\n" + inMaster.readLine() + "\r\n";


                    System.out.println("request to master :");
                    System.out.print(response);
                    out.println(response);
                    out.flush();

                    if (messageReceived.startsWith("CONNECT") && response.startsWith("OK")) {
                        executor.execute(new PeerConnectionHandler(out, inMaster));
                        isConnected = true;
                    }
                    if(! isConnected) {
                        socket.close();
                    }
                }
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }
}

class PeerConnectionHandler extends Thread {
    private PrintWriter out;
    private BufferedReader in;

    public PeerConnectionHandler(PrintWriter out, BufferedReader in) {
        System.out.println("New Connection");
        this.out = out;
        this.in = in;
    }


    @Override
    public void run() {
        try{
        while (true) {
            /*
                        String message = "";
                        message += in.readLine() + "\n\n";
                        message += in.readLine() + "\n\n";

             */
                        String response = RequestMaker.getRequest(in.readLine(), in.readLine());
                        System.out.println("Message to sub " + response);
                        out.println(response);
                        out.flush();

            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }
}