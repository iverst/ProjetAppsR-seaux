package Requests;

import App.Message;
import App.Subscription;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UNSUBSCRIBERequest extends SUBSCRIBERequest {
    private ConcurrentLinkedQueue<Message> queue;

    public UNSUBSCRIBERequest(String request) {
        super(request);
    }

    @Override
    public boolean checkFormat() {
        return super.checkFormat();
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
            boolean result = Subscription.getInstance().unsubscribeToAuthor(getParameter("author"), queue);
            if(! result) {
                setInvalidRequest(true);
                setResponse("ERROR", "Queue isn't in subscription");
                return;
            }
        }
        else if (getParameterFormat()[0].equals("tag")) {
            if(! Subscription.getInstance().unsubscribeToTag(getParameter("tag"), queue)) {
                setInvalidRequest(true);
                setResponse("ERROR", "Queue isn't in subscription");
                return;
            }
        }
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<Message> queue) {
        this.queue = queue;
    }
}

