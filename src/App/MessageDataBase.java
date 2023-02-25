package App;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageDataBase {
    private int id = 0;
    private ArrayList<Message> messages = new ArrayList<>();
    private HashMap<String, ArrayList<Message>> userMessages = new HashMap<>();
    private HashMap<String, ArrayList<Message>> tags = new HashMap<>();
    private static MessageDataBase instance;

    public boolean publishMessage(String user, String content) {
        Message message;
        try {
            message = new Message(content, id);
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

    public ArrayList<Message> getUserMessages(String user) {
        return userMessages.get(user);
    }

    public ArrayList<Message> getMessagesByTag(String tag) {
        return tags.get(tag);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public static MessageDataBase getInstance() {
        if (instance == null) {
            instance = new MessageDataBase();
        }
        return instance;
    }
}
