package infrastructure.mq.internal;

import infrastructure.mq.provider.MessageReceiver;
import infrastructure.mq.service.MessageListener;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author joeyh 2019/1/9 14:16
 */
public class MessageReceiverImpl extends MessageReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverImpl.class);

    private Map<String, MessageListener> listenerMap;
    private MessageConverter messageConverter;

    public MessageReceiverImpl(MessageConverter messageConverter, List<MessageListener> messageListeners) {
        this.messageConverter = messageConverter;
        listenerMap = new HashMap<>();
        messageListeners.forEach(l -> {
            String messageType = l.forMessageType();
            if (listenerMap.containsKey(messageType)) {
                throw new RuntimeException("Multiple listeners for message type[" + messageType + "] found: " +
                        listenerMap.get(messageType).getClass().getName() +
                        ", " + l.getClass().getName());
            }
            listenerMap.put(messageType, l);
        });
    }

    @SuppressWarnings("unchecked")
    public boolean onMessageReceived(byte[] messageBytes) {
        Pair<String, Object> message = messageConverter.fromBytes(messageBytes);
        MessageListener l = listenerMap.get(message.getKey());
        Object body = message.getValue();
        try {
            l.handle(body);
            return true;
        } catch (Exception ex) {
            logger.error("on message received error", ex);
            return false;
        }
    }
}
