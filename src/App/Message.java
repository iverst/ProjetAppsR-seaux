package App;

public class Message {
    private String content;
    private int id;
    private static final int CONTENT_MAX_SIZE = 256;
    private int replyTo = -1;
    private boolean republished = false;

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

    public int getReplyTo() {
        return replyTo;
    }

    public boolean isRepublished() {
        return republished;
    }

    public void setReplyTo(int replyTo) {
        this.replyTo = replyTo;
    }

    public void setRepublished(boolean republished) {
        this.republished = republished;
    }

    public String toString() {
        return "Message n°" + id + " : " + content;
    }
}
