package Clients;

import Requests.Request;
import Requests.RequestMaker;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Publisher {
    public static void main(String[] args) {
        String address =  "localhost";
        int port = 12345;
        Scanner scanner = new Scanner(System.in);
        //initialisation nom author
        System.out.println("Please enter your user name : ");
        String userName = scanner.nextLine();
        System.out.println(userName);
        while(true) {
            //creation message
            System.out.println("Please enter your message :");
            String message= scanner.nextLine();
            String request = new RequestMaker().getRequest("PUBLISH " + "author:@" + userName, message);

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
}
