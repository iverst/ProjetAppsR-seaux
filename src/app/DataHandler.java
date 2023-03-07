package app;

import requests.Request;
import requests.RequestMaker;

import java.util.*;

public class DataHandler {
    private String data;
    private String response;

    public DataHandler(String data) {
        this.data = data;
    }



    public String getResponse() {
        Request request = new Request(data);
        if (request.isInvalidRequest()) {
            return "ERROR \r\n invalid request format \r\n";
        }

        switch (request.getType()) {
            case "PUBLISH":
                return publish(request);
            case "RCV_IDS":
                return RCV_IDS(request);
            default:
                return "ERROR \r\n request not recognized \r\n";
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private String publish(Request request) {
        //check format
        String[] parametersFormat = request.getParameterFormat();
        if (parametersFormat.length != 1 || !parametersFormat[0].equals("author")) {
            return new RequestMaker().getRequest("ERROR", "Bad request format");
        }
        //publish message
        MessageDataBase.getInstance().publishMessage(request.getParameter("user"), request.getBody());
        //send back a OK response
        return new RequestMaker().getRequest("OK", "");
    }


    public String RCV_IDS(Request request) {
        String[] parametersFormat = request.getParameterFormat();
        String author = null, tag = null;
        int sinceId = -1, limit = 5;

        //check format
        if(parametersFormat.length != 1) {
            int index = 0;
            if (parametersFormat[index].equals("author") && parametersFormat.length > index) {
                author = request.getParameter("author");
                index++;
            }
            if (parametersFormat[index].equals("tag") && parametersFormat.length > index) {
                tag = request.getParameter("tag");
                index++;
            }
            if (parametersFormat[index].equals("since_id") && parametersFormat.length > index) {
                sinceId = Integer.parseInt(request.getParameter("since_id"));
                index++;
            }
            if (parametersFormat[index].equals("limit") && parametersFormat.length > index) {
                limit = Integer.parseInt(request.getParameter("limit"));
                index++;
            }
            if(parametersFormat.length > index) {
                return new RequestMaker().getRequest("ERROR", "Bad request format");
            }
        }

        //messages selection according to parameters
        ArrayList<Message> allMessages = MessageDataBase.getInstance().getMessages();



        if (author != null) {

            ArrayList<Message> messages = MessageDataBase.getInstance().getUserMessages(author);
            if (messages != null) {

                allMessages.retainAll(messages);
            }
            else {
                allMessages = new ArrayList<>();
            }
        }
        if(tag != null) {
            ArrayList<Message> messages = (MessageDataBase.getInstance().getMessagesByTag(tag));
            if (messages != null) {
                allMessages.retainAll(messages);
            }
            else {
                allMessages = new ArrayList<>();
            }
        }
        if (sinceId != -1) {
            ArrayList<Message> messages = (MessageDataBase.getInstance().getMessageSinceId(sinceId));
            if (messages != null) {
                allMessages.retainAll(messages);
            }
            else {
                allMessages = new ArrayList<>();
            }
        }

        if (limit < allMessages.size()) {
            allMessages = new ArrayList<>(allMessages.subList(allMessages.size() - limit,allMessages.size()));
        }

        System.out.println("All messages");
        System.out.println(allMessages);

        return new RequestMaker().getRequest("OK", "");
    }
}

