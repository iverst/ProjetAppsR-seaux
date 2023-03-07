package clients;

import requests.RequestMaker;

import java.util.ArrayList;
import java.util.Scanner;

public class Follower extends Client {
    public static void main(String[] args) {
        Follower follower = new Follower("localhost", 12345);
        follower.run();
        //follower.sendRequest(new RequestMaker().getRequest("RCV_IDS author:@arthur", ""));
    }



    public Follower(String address, int port) {
        super(address, port);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        //get user names
        ArrayList<String> users = new ArrayList<>();
        while (true) {
            System.out.println("Please enter your user name to know their message (to show messages enter 'show') : ");
            String s = scanner.nextLine();
            if (s.equals("show")) {
                System.out.println("show");
                break;
            }
            users.add(s);

        }

        for (String u : users) {
            String response = sendRequest(new RequestMaker().getRequest("RCV_IDS author:@"+ u , ""));
            String[] ids = response.split("\r\n")[1].split(" ");

            for (String s :ids) {
                String mes = sendRequest(new RequestMaker().getRequest("RCV_MSG msg_id:" + s, ""));
                System.out.println(mes);
            }
        }
    }

}
