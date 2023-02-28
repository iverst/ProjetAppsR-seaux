package Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TestServer {
    public static void main(String[] args) {
        try {
            String string = new Scanner(System.in).nextLine();

            ServerSocket ss = new ServerSocket(1234);
            Socket s = ss.accept();
            System.out.println("client connecte");
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            new ServerHandler(in).start();
            //envoi
            while (true) {
                System.out.println("sending");
                out.println(string);
                out.flush();
                if(string.equals("improbable")) {
                    break;
                }
            }
            //reception
            String response = in.readLine() + "\r\n";

            while (in.ready()) {
                response = response + in.readLine() + " ";
            }
            response = response + "\r\n";
            //traitement réponse
            s.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}

class ServerHandler extends Thread {
    BufferedReader in;

    ServerHandler(BufferedReader in) {
        this.in = in;
    }

    public void run() {
        while (true) {
            try {
                if(in.ready()) {
                    System.out.println(in.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
