package requests;

import app.MessageDataBase;

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
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        if(isInvalidRequest())
            return;

        if(MessageDataBase.getInstance().publishMessage(getParameter("author"), getBody())) {
            setResponse("OK");
        }
        else {
            setResponse("ERROR", "Message too long");
        }
    }
}
