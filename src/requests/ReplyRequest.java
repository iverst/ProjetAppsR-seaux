package requests;

import app.MessageDataBase;

public class ReplyRequest extends Request {
    public ReplyRequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        //check format
        String[] parametersFormat = getParameterFormat();
        if (parametersFormat.length != 2 || !parametersFormat[0].equals("author") || !parametersFormat[1].equals("reply_to_id")) {
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

        int id;
        try {
            id = Integer.parseInt(getParameter("reply_to_id"));
        }
        catch (Exception e) {
            setInvalidRequest(true);
            setResponse("ERROR", "String in int parameter");
            return;
        }
        if(MessageDataBase.getInstance().replyToMessage(getParameter("author"), getBody(), id)) {
            setResponse("OK");
        }
        else {
            setResponse("ERROR", "Message too long");
        }
    }
}
