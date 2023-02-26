package Requests;

import App.MessageDataBase;

public class PUBLISHRequest extends Request {
    public PUBLISHRequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        //check format
        String[] parametersFormat = getParameterFormat();
        if (parametersFormat.length != 1 || !parametersFormat[0].equals("author")) {
            setInvalidRequest(true);
            setResponse("ERROR", "Bad request format");
        }
        return false;
    }

    @Override
    public void execute() {
        if(MessageDataBase.getInstance().publishMessage(getParameter("user"), getBody())) {
            setResponse("OK");
        }
        else {
            setResponse("ERROR", "Message too long");
        }
    }
}
