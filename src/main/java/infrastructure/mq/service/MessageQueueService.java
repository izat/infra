package infrastructure.mq.service;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * @author joeyh 2019/1/9 10:34
 */
public interface MessageQueueService {

    /**
     * 发送消息
     *
     * @param type        消息类型
     * @param messageBody 消息体
     */
    void send(String type, Object messageBody);

    /**
     * 发送定时消息
     *
     * @param type        消息类型
     * @param messageBody 消息体
     * @param time        触发时间
     */
    void send(String type, Object messageBody, OffsetDateTime time);

    /**
     * 发送周期性定时消息
     *
     * @param type        消息类型
     * @param messageBody 消息体
     * @param time        首次触发时间
     * @param duration    两次发送的间隔时间
     */
    void send(String type, Object messageBody, OffsetDateTime time, Duration duration);
}
