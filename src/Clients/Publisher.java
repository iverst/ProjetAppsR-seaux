package Clients;

import Requests.Request;
import Requests.RequestMaker;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Publisher extends Client {
    public static void main(String[] args) {
        Client publisher = new Publisher("localhost", 12345);
        publisher.run();
    }

    public Publisher(String address, int port) {
        super(address, port);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your user name :");
        String name = scanner.nextLine();
        name = "@" + name;

        while (true) {
            System.out.println("Please enter your message :");
            String s = scanner.nextLine();
            sendRequest(new RequestMaker().getRequest("PUBLISH author:" + name, s));
        }
    }
}
