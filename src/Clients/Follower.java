package Clients;

import Requests.RequestMaker;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Follower {
    public static void main(String[] args) {
        Follower follower = new Follower("localhost", 12345);
        //follower.run();
        follower.sendRequest(new RequestMaker().getRequest("RCV_IDS author:@arthur", ""));
    }

    private String address;
    private int port;

    public Follower(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        //get user names
        ArrayList<String> users = new ArrayList<>();
        while (true) {
            System.out.println("Please enter your user name to know their message (to show messages enter 'show' : ");
            String s = scanner.nextLine();
            if (s.equals("show")) {
                System.out.println("show");
                break;
            }
            users.add(s);

        }

        for (String u : users) {
            sendRequest("RCV_IDS " + "author:" + u );
        }
    }

    public void sendRequest(String request) {
        try {
            Socket s = new Socket(address, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            //envoi
            out.println(request);
            out.flush();
            //reception
            String response = in.readLine();
            response = response + "\r\n" + in.readLine() + "\r\n";
            System.out.println(response);
            //traitement réponse
            s.close();
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }
}
