package infrastructure.mq.provider.threading;

import infrastructure.mq.provider.MessageReceiver;
import infrastructure.mq.provider.MessageSender;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author joeyh 2019/1/22 15:25
 */
public class ThreadingMessageSender implements MessageSender {
    private ScheduledExecutorService executorService =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void send(String messageType, byte[] messageBytes) {
        executorService.submit(() -> forward(messageBytes));
    }

    @Override
    public void send(String messageType, byte[] messageBytes, OffsetDateTime time) {
        executorService.schedule(() -> forward(messageBytes),
                OffsetDateTime.now().until(time, ChronoUnit.SECONDS), TimeUnit.SECONDS);
    }

    @Override
    public void send(String messageType, byte[] messageBytes, OffsetDateTime firstTime, Duration duration) {
        executorService.scheduleAtFixedRate(() -> forward(messageBytes),
                OffsetDateTime.now().until(firstTime, ChronoUnit.SECONDS), duration.getSeconds(), TimeUnit.SECONDS);
    }

    private void forward(byte[] messageBytes) {
        MessageReceiver.getInstance().onMessageReceived(messageBytes);
    }
}
