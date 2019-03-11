package infrastructure.mq.internal;

import infrastructure.mq.provider.MessageSender;
import infrastructure.mq.service.MessageQueueService;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * @author joeyh 2019/1/9 15:29
 */
public class MessageQueueServiceImpl implements MessageQueueService {
    private MessageSender messageSender;
    private MessageConverter messageConverter;

    public MessageQueueServiceImpl(MessageSender messageSender, MessageConverter messageConverter) {
        this.messageSender = messageSender;
        this.messageConverter = messageConverter;
    }

    @Override
    public void send(String messageType, Object messageBody) {
        byte[] bytes = messageConverter.toBytes(messageType, messageBody);
        messageSender.send(messageType, bytes);
    }

    @Override
    public void send(String messageType, Object messageBody, OffsetDateTime time) {
        byte[] bytes = messageConverter.toBytes(messageType, messageBody);
        messageSender.send(messageType, bytes, time);
    }

    @Override
    public void send(String messageType, Object messageBody, OffsetDateTime time, Duration duration) {
        byte[] bytes = messageConverter.toBytes(messageType, messageBody);
        messageSender.send(messageType, bytes, time, duration);
    }
}
