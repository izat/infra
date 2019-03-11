package infrastructure.mq.internal;

import infrastructure.bean.CharsetUtils;
import infrastructure.bean.JsonUtils;
import javafx.util.Pair;

import java.util.Map;

/**
 * @author joeyh 2019/1/10 14:35
 */
public class MessageConverter {
    private Map<String, Class> messageTypeClassMap;

    public MessageConverter(Map<String, Class> messageTypeClassMap) {
        this.messageTypeClassMap = messageTypeClassMap;
    }

    public byte[] toBytes(String messageType, Object messageBody) {
        Message message = new Message(messageType, JsonUtils.toJson(messageBody));
        return JsonUtils.toJson(message).getBytes(CharsetUtils.UTF_8);
    }

    @SuppressWarnings("unchecked")
    public Pair<String, Object> fromBytes(byte[] messageBytes) {
        Message message = JsonUtils.toObject(new String(messageBytes, CharsetUtils.UTF_8), Message.class);
        String messageType = message.getType();
        Object messageBody = JsonUtils.toObject(message.getBody(), messageTypeClassMap.get(message.getType()));
        return new Pair<>(messageType, messageBody);
    }
}
