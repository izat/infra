package infrastructure.mq.provider;

/**
 * @author joeyh 2019/1/9 14:16
 */
public abstract class MessageReceiver {
    private static MessageReceiver messageReceiver;

    public static MessageReceiver getInstance() {
        return messageReceiver;
    }

    protected MessageReceiver() {
        if (messageReceiver != null) {
            throw new RuntimeException("Multiple message forwarder found: " +
                    messageReceiver.getClass().getName() +
                    ", " + this.getClass().getName());
        }
        messageReceiver = this;
    }

    public abstract boolean onMessageReceived(byte[] messageBytes);
}
