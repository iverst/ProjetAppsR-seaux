package requests;

import app.Message;
import app.MessageDataBase;

import java.util.ArrayList;
import java.util.LinkedList;

public class RCV_IDSRequest extends Request {
    public RCV_IDSRequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {

        String[] parametersFormat = getParameterFormat();
        //check format
        if(parametersFormat.length != 1) {
            int index = 0;
            if (parametersFormat.length > index && parametersFormat[index].equals("author")) {
                index++;
            }
            if (parametersFormat.length > index && parametersFormat[index].equals("tag")) {
                index++;
            }
            if (parametersFormat.length > index && parametersFormat[index].equals("since_id")) {
                index++;
            }
            if (parametersFormat.length > index && parametersFormat[index].equals("limit")) {
                index++;
            }
            if(parametersFormat.length > index) {
                setResponse("ERROR",  "Invalid Request Format");
                return false;
            }
        }
        return true;
    }

    @Override
    public void execute() {
        String author = null, tag = null;
        int sinceId = -1, limit = -1;
        //initialisation paramater
        if(getParameter("author") != null) {
            author = getParameter("author");
        }
        if (getParameter("tag") != null) {
            tag = getParameter("tag");
        }
        if(getParameter("since_id") != null) {
            sinceId = Integer.parseInt(getParameter("since_id"));
        }
        if(getParameter("limit") != null) {
            limit = Integer.parseInt(getParameter("limit"));
        }

        //get message from database
        ArrayList<Message> messages = MessageDataBase.getInstance().getMessages(author, tag, sinceId, limit);
        LinkedList<Integer> ids = new LinkedList<>();
        //get their id in reverse order
        for (Message m : messages) {
            ids.addFirst(m.getId());
        }
        String body =  "";
        for (Integer id : ids) {
            body = body + String.valueOf(id) + "\n";
        }
        setResponse("MSG_IDS", body);
    }
}
