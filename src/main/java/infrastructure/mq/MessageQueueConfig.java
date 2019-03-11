package infrastructure.mq;

import infrastructure.mq.internal.MessageConverter;
import infrastructure.mq.internal.MessageQueueServiceImpl;
import infrastructure.mq.internal.MessageReceiverImpl;
import infrastructure.mq.provider.MessageSender;
import infrastructure.mq.provider.threading.ThreadingMessageSender;
import infrastructure.mq.service.MessageListener;
import infrastructure.mq.service.MessageQueueService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author joeyh 2019/1/9 14:17
 */
@Configuration
public class MessageQueueConfig {

    @Bean
    public MessageQueueService messageQueueService(MessageSender messageSender, List<MessageListener> listeners) {
        Map<String, Class> typeToBodyClassMap = listeners.stream().collect(Collectors.toMap(
                MessageListener::forMessageType, MessageListener::forMessageClass));
        MessageConverter messageConverter = new MessageConverter(typeToBodyClassMap);

        //build and register a message receiver to MessageReceiver.getInstance()
        new MessageReceiverImpl(messageConverter, listeners);

        return new MessageQueueServiceImpl(messageSender, messageConverter);
    }

    @Bean
    @ConditionalOnMissingBean(MessageSender.class)
    public MessageSender messageSender() {
        return new ThreadingMessageSender();
    }
}
