package clients;

import requests.RequestMaker;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MicroblogClient extends Client {

    public static void main(String[] args) {
        MicroblogClient client = new MicroblogClient("localhost", 12351);
        client.run();
    }

    private boolean isConnected = false;
    private String name;
    private Scanner scanner = new Scanner(System.in);

    public MicroblogClient(String address, int port) {
        super(address, port);
    }

    @Override
    public void run() {
        try {
            Socket socket = getSocket();
            System.out.println("Please enter your user name :");
            name = scanner.nextLine();
            name = "@" + name;
            System.out.println("Please enter the next instruction :");

            while (true) {
                //if (scanner.hasNext()) {

                    String instruction = scanner.nextLine();
                    switch (instruction) {
                        case "PUBLISH":
                            publish();
                            break;
                        case "REPLY":
                            replyTo();
                            break;
                        case "REPUBLISH":
                            repost();
                            break;
                        case "SUBSCRIBE":
                            subscribe(socket);
                            break;
                        case "UNSUBSCRIBE":
                            unsubscribe(socket);
                            break;
                        default:
                            System.out.println("Instruction is not recognized");
                    }
                //}
                System.out.println("Please enter the next instruction :");

            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    private void publish() {
        System.out.println("Please enter your message :");
        String message = scanner.nextLine();
        System.out.println("REPONSE :");
        System.out.println(sendRequest(RequestMaker.getRequest("PUBLISH author:" + name, message)));
    }


    private void replyTo() {
        System.out.println("Please enter your message :");
        String message = scanner.nextLine();
        System.out.println("Please enter the message id you are replying to :");
        String replyToID = scanner.nextLine();
        System.out.println(sendRequest(RequestMaker.getRequest("REPLY author:" + name + " reply_to_id:" + replyToID, message)));
    }

    private void repost() {
        System.out.println("Please enter the message id you are republishing :");
        String msgId = scanner.nextLine();
        System.out.println(sendRequest(RequestMaker.getRequest("REPUBLISH author:" + name + " msg_id:" + msgId, "")));

    }

    private void connect(Socket socket) {
        System.out.println(sendRequest(RequestMaker.getRequest("CONNECT user:" + name, ""), socket));
        new Listener(socket).start();
        isConnected = true;
    }

    private void subscribe(Socket socket) {
        if(! isConnected) {
            connect(socket);
        }
        System.out.println("Please enter the author username or the tag you want to subscribe to : ");
        String data = scanner.nextLine();
        if(data.startsWith("#")) {
            System.out.println(sendRequest(RequestMaker.getRequest("SUBSCRIBE tag:" + data, ""), socket));
        }
        else if(data.startsWith("@")) {
            System.out.println(sendRequest(RequestMaker.getRequest("SUBSCRIBE author:" + data, ""), socket));
        }
        else {
            System.out.println("Request invalid author must start with a @ and tags with a # !!!");
        }
    }

    private void unsubscribe(Socket socket) {
        if(! isConnected) {
            System.out.println("You aren't subscribe to anything");
            return;
        }

        System.out.println("Please enter the author username or the tag you want to unsubscribe to : ");
        String data = scanner.nextLine();
        System.out.println("data");
        System.out.println(data);
        if(data.startsWith("#")) {
            System.out.println("tag");
            System.out.println(sendRequest(RequestMaker.getRequest("UNSUBSCRIBE tag:" + data, ""), socket));
        }
        else if(data.startsWith("@")) {
            System.out.println("author");
            System.out.println(sendRequest(RequestMaker.getRequest("UNSUBSCRIBE author:" + data, ""), socket));
        }
        else {
            System.out.println("Request invalid author must start with a @ and tags with a # !!!");
        }
        System.out.println("endpoint");
    }

    //////////////////////////////////////////////////////////////////////////////////////////
}

class Listener extends Thread {
    private Socket socket;
    private int messageNumber = 0;

    Listener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                if (socket.isClosed()) {
                    return;
                }
                messageNumber++;
                String message = "";
                message += in.readLine() + "\n";
                message += in.readLine();

                if(message.equals("null")){
                    System.out.println(message);
                    return;
                }
                System.out.println(message);
            }

        }
        catch (IOException io) {
            io.printStackTrace();
        }

    }
}
