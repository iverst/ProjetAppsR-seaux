package Requests;

import App.Message;
import App.Subscription;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SUBSCRIBERequest extends Request {
    private ConcurrentLinkedQueue<Message> queue;

    public SUBSCRIBERequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        String[] parameters = getParameterFormat();
        if (parameters.length != 1 || ! parameters[0].equals("author") && ! parameters[0].equals("tag")) {
            setInvalidRequest(true);
            setResponse("ERROR", "Bad request format");
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        if(isInvalidRequest()) {
            return;
        }
        if (queue == null) {
            setInvalidRequest(true);
            setResponse("ERROR", "No subscription queue was given");
            return;
        }

        if (getParameterFormat()[0].equals("author")) {
            boolean result = Subscription.getInstance().subscribeToAuthor(getParameter("author"), queue);
            if(! result) {
                setInvalidRequest(true);
                setResponse("ERROR", "Author doesn't exist");
                return;
            }
        }
        else if (getParameterFormat()[0].equals("tag")) {
            Subscription.getInstance().subscribeToTag(getParameter("tag"), queue);
        }
    }

    public void setQueue(ConcurrentLinkedQueue<Message> queue) {
        this.queue = queue;
    }
}
