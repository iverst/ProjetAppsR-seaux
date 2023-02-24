package App;

public class Message {
    private String content;
    private int id;
    private static final int CONTENT_MAX_SIZE = 256;
    private String[] tags;

    public Message(String content, int id) throws Exception {
        if (content.length() > CONTENT_MAX_SIZE) {
            throw new Exception("Incorrect message size");
        }
        this.content = content;
        this.id = id;
        this.tags = null;
    }

    public Message(String content, int id, String[] tags) throws Exception {
        if (content.length() > CONTENT_MAX_SIZE) {
            throw new Exception("Incorrect message size");
        }
        this.content = content;
        this.id = id;
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String[] getTags() {
        return tags;
    }
}
