package Clients;

import java.io.*;
import java.net.Socket;

public abstract class Client {
    private String address;
    private int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String sendRequest(String request) {
        try {
            Socket s = new Socket(address, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            //envoi
            out.println(request);
            out.flush();
            //reception
            String response = in.readLine()+ "\r\n";

            while (in.ready()) {
                response =  response + in.readLine() + " ";
            }
            response = response + "\r\n";
            //traitement réponse
            s.close();
            return response;
        }
        catch (IOException io) {
            io.printStackTrace();
            return null;
        }
    }

    public abstract void run();
}
