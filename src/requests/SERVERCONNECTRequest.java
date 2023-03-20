package requests;

import app.Message;
import app.Subscription;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SERVERCONNECTRequest extends Request {
    private ConcurrentLinkedQueue<Message> queue;

    public SERVERCONNECTRequest(String request) {
        super(request);
        checkFormat();
    }

    @Override
    public boolean checkFormat() {
        setResponse("OK", "");
        return true;
    }

    @Override
    public void execute() {
        Subscription.getInstance().subscribeToAll(queue);
    }

    public void setQueue(ConcurrentLinkedQueue<Message> queue) {
        this.queue = queue;
    }

}
