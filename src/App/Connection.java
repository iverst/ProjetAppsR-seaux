package App;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Connection {
    private String userName;
    private ConcurrentLinkedQueue<Message>  queue = new ConcurrentLinkedQueue<>();


    public Message getLastMessage() {
        return queue.poll();
    }
    public boolean subscribeToAuthor(String author) {
        return Subscription.getInstance().subscribeToAuthor(author, queue);
    }

    public boolean subscribeToTag(String tag) {
        return Subscription.getInstance().subscribeToTag(tag, queue);
    }

    public boolean unsubscribeToAuthor(String author) {
        return Subscription.getInstance().unsubscribeToAuthor(author, queue);
    }

    public boolean unsubscribeToTag(String tag) {
        return Subscription.getInstance().unsubscribeToTag(tag, queue);
    }
}
