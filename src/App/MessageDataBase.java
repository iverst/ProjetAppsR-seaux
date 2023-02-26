package App;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageDataBase {
    private int id = 0;
    private ArrayList<Message> messages = new ArrayList<>();
    private HashMap<String, ArrayList<Message>> userMessages = new HashMap<>();
    private HashMap<String, ArrayList<Message>> tags = new HashMap<>();
    private static MessageDataBase instance;

    public boolean publishMessage(String user, String content) {
        Message message;
        try {
            message = new Message(user, content, id);
        }
        catch (Exception e) {
            return false;
        }
        id++;
        messages.add(message);
        addMessageUser(user, message);
        addMessageTags(message);
        return true;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void addMessageUser(String user, Message message) {
        ArrayList<Message> messages = userMessages.get(user);
        if (messages == null) {
            messages = new ArrayList<>();
            messages.add(message);
            userMessages.put(user, messages);
        }
        else {
            messages.add(message);
            userMessages.put(user, messages);
        }
    }

    private void addMessageTags(Message message) {
        String content = message.getContent();
        String[] words = content.split(" ");
        for (String word :  words) {
            if (word.charAt(0) == '#') {
                addTag(word, message);
            }
        }
    }

    private void addTag(String tag, Message message) {
        ArrayList<Message> messages = tags.get(tag);
        if (messages == null) {
            messages = new ArrayList<>();
            messages.add(message);
            tags.put(tag, messages);
        }
        else {
            messages.add(message);
            tags.put(tag, messages);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Message getMessageById(int id) {
        if(id < this.id) {
            return messages.get(id);
        }
        return null;
    }

    public ArrayList<Message> getUserMessages(String user) {
        return (ArrayList<Message>) userMessages.get(user).clone();
    }

    public ArrayList<Message> getMessagesByTag(String tag) {
        return (ArrayList<Message>) tags.get(tag).clone();
    }

    public ArrayList<Message> getMessages() {
        return (ArrayList<Message>) messages.clone();
    }

    public ArrayList<Message> getMessageSinceId(int sinceId) {

        if(sinceId >= 0 && sinceId <= id - 1) {
            return new ArrayList<>(messages.subList(sinceId, messages.size()));
        }
        else {
            return (ArrayList<Message>) getMessages().clone();
        }
    }


    public ArrayList<Message> getMessages(String author, String tag, int sinceId, int limit) {
        ArrayList<Message> messages = getMessages();

        //author
        if (author != null && userMessages.containsKey(author)) {
            messages.retainAll(getUserMessages(author));
        }
        else if(author != null) {
            return new ArrayList<>();
        }
        //tag part
        if (tag != null && tags.containsKey(tag)) {
            messages.retainAll(getMessagesByTag(tag));
        }
        else if(tag != null) {
            return new ArrayList<>();
        }

        //since id part
        ArrayList<Message> messageSinceID = getMessageSinceId(sinceId);
        if (messageSinceID != null) {
            messages.retainAll(messageSinceID);
        }
        else {
            return new ArrayList<>();
        }

        //limit
        if(limit < 0) {
            limit = 5;
        }
        //messages already sorted by id because getMessages return the messages sorted by id
        //if message size inferior to limit return all messages
        if (messages.size() >= limit) {

            messages = new ArrayList<>(messages.subList(messages.size() - limit, messages.size()));

        }

        return (ArrayList<Message>) messages.clone();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static MessageDataBase getInstance() {
        if (instance == null) {
            instance = new MessageDataBase();
        }
        return instance;
    }
}
