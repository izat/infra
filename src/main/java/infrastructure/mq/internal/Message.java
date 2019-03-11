package infrastructure.mq.internal;

/**
 * @author joeyh 2019/1/10 13:48
 */
public class Message {
    private String type;
    private String body;

    public Message() {
    }

    public Message(String type, String body) {
        this.type = type;
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public String getBody() {
        return body;
    }
}
