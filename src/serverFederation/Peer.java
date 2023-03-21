package serverFederation;

import app.ServerAddress;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Peer {

    public static void main(String[] args) {
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


                System.out.println("Response");
                System.out.println(response);
                out.println(response);
                out.flush();
                if (messageReceived.startsWith("CONNECT") && response.startsWith("OK")) {
                    executor.execute(new PeerConnectionHandler(socket, masterSocket));
                }
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }
}

class PeerConnectionHandler extends Thread {
    private Socket socket, masterSocket;
    private PrintWriter out;
    private BufferedReader in;

    public PeerConnectionHandler(Socket socket, Socket masterSocket) {
        System.out.println("New subscription");
        this.socket = socket;
        this.masterSocket = masterSocket;
        try {
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(masterSocket.getInputStream()));
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }


    @Override
    public void run() {
        try{
        while (true) {
                    String message = "";
                    message += in.readLine() + "\n\n";
                    message += in.readLine() + "\n\n";
                    System.out.println("Message to sub " + message);
                    out.println(message);
                    out.flush();
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }
}