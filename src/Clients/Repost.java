package Clients;

import Requests.RequestMaker;

import java.util.ArrayList;
import java.util.Scanner;

public class Repost {

    public static void main(String[] args) {
        Repost client = new Repost("localhost", 12345, "repost");
        client.run();
    }

    private String address, name;
    private int port;
    private Client client;

    public Repost(String address, int port, String name) {
        this.address = address;
        this.port = port;
        this.client = client = new Client(address, port);
        this.name = name;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        //get user names
        ArrayList<String> users = new ArrayList<>();
        while (true) {
            System.out.println("Please enter your user name to republish their message (to show messages enter 'show') : ");
            String s = scanner.nextLine();
            if (s.equals("show")) {
                System.out.println("show");
                break;
            }
            users.add(s);
        }

        //republish part
        for (String u : users) {
            String response = client.sendRequest(new RequestMaker().getRequest("RCV_IDS author:@"+ u , ""));
            String[] ids = response.split("\r\n")[1].split(" ");

            for (String s :ids) {
                String mes2 = client.sendRequest(new RequestMaker().getRequest("REPUBLISH author:" + name +  " msg_id:" + s, ""));
                System.out.println(mes2);
            }
        }
    }
}
