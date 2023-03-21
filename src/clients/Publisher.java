package clients;

import requests.RequestMaker;

import java.util.Scanner;

public class Publisher extends Client {
    public static void main(String[] args) {
        Client publisher = new Publisher("localhost", 12352);
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
            System.out.println(sendRequest(new RequestMaker().getRequest("PUBLISH author:" + name, s)));
        }
    }
}
