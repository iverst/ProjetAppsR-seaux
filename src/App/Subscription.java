package App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Subscription {
    private static Subscription instance = new Subscription();
    private HashMap<String, ArrayList<ConcurrentLinkedQueue<Message>>> userSubscribers = new HashMap<>();
    private HashMap<String, ArrayList<ConcurrentLinkedQueue<Message>>> tagSSubscribers = new HashMap<>();

    public boolean subscribeToAuthor(String author, ConcurrentLinkedQueue<Message> queue) {
        if (MessageDataBase.getInstance().hasAuthor(author)) {
            ArrayList<ConcurrentLinkedQueue<Message>> subscribers = userSubscribers.get(author);
            if (subscribers == null) {
                subscribers = new ArrayList<>();
                userSubscribers.put(author, subscribers);
            }
            subscribers.add(queue);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean subscribeToTag(String tag, ConcurrentLinkedQueue<Message> queue) {
        ArrayList<ConcurrentLinkedQueue<Message>> subscribers = tagSSubscribers.get(tag);
        if (subscribers == null) {
            subscribers = new ArrayList<>();
            tagSSubscribers.put(tag, subscribers);
        }
        subscribers.add(queue);
        return true;
    }

    //////////////////////////////////////////////////////////////////

    public void distributeMessage(Message message) {
        if (userSubscribers.containsKey(message.getUser())) {
            ArrayList<ConcurrentLinkedQueue<Message>> subscribers = userSubscribers.get(message.getUser());
            for (Queue s : subscribers) {
                s.add(message);
            }
        }
    }

    public void distributeMessage(Message message, String tag) {
        if (tagSSubscribers.containsKey(tag)) {
            ArrayList<ConcurrentLinkedQueue<Message>> subscribers = tagSSubscribers.get(tag);
            for (ConcurrentLinkedQueue<Message> s : subscribers) {
                s.add(message);
            }
        }
    }
    //////////////////////////////////////////////////////////////////

    public static Subscription getInstance() {
        return instance;
    }

    public String toString() {
        return userSubscribers.toString() + tagSSubscribers.toString();
    }
}
