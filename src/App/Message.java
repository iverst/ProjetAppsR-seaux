package App;

public class Message {
    private String content;
    private int id;
    private static final int CONTENT_MAX_SIZE = 256;

    public Message(String content, int id) throws Exception {
        if (content.length() > CONTENT_MAX_SIZE) {
            throw new Exception("Incorrect message size");
        }
        this.content = content;
        this.id = id;
    }



    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }


    public String toString() {
        return "Message n°" + id + " : " + content;
    }
}
