package infrastructure.mq.provider;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * 实现此接口，发送消息
 */
public interface MessageSender {
    /**
     * 发送消息
     *
     * @param messageType  消息类型
     * @param messageBytes 消息体
     */
    void send(String messageType, byte[] messageBytes);

    /**
     * 发送定时消息。时间精确到秒。
     *
     * @param messageType  消息类型
     * @param messageBytes 消息体
     * @param time         触发时间
     */
    void send(String messageType, byte[] messageBytes, OffsetDateTime time);

    /**
     * 以指定的周期发送定时消息。时间精确到秒。
     *
     * @param messageType  消息类型
     * @param messageBytes 消息体
     * @param firstTime    首次触发时间
     * @param duration     两次触发的时间间隔
     */
    void send(String messageType, byte[] messageBytes, OffsetDateTime firstTime, Duration duration);
}
