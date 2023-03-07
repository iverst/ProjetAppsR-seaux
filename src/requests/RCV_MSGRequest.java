package requests;

import app.Message;
import app.MessageDataBase;

public class RCV_MSGRequest extends Request {
    public RCV_MSGRequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        String[] paramFormat = getParameterFormat();
        if (paramFormat.length !=  1 || ! paramFormat[0].equals("msg_id")) {
            setInvalidRequest(true);
            setResponse("ERROR", "Bad request format");
            return false;
        }
        return false;
    }

    @Override
    public void execute() {
        int id;
        try {
            id = Integer.parseInt(getParameter("msg_id"));
        }
        catch (Exception e) {
            setInvalidRequest(true);
            setResponse("ERROR", "Id is not a number");
            return;
        }
        Message message = MessageDataBase.getInstance().getMessageById(id);
        if (message == null) {
            setInvalidRequest(true);
            setResponse("ERROR", "Incorrect id value");
            return;
        }
        String header = "MSG author:" + message.getUser() + " msg_id:" + message.getId();
        if (message.getReplyTo() != -1) {
            header = header + " reply_to_id:id";
        }
        header = header + " republished:" + message.isRepublished();

        setResponse(header, message.getContent());
    }
}
