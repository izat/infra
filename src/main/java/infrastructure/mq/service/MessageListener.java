package infrastructure.mq.service;

/**
 * @author joeyh 2019/1/9 14:05
 */
public interface MessageListener<T> {
    /**
     * 获取监听的消息类型
     *
     * @return message type
     */
    String forMessageType();

    /**
     * 获取消息体类型
     *
     * @return message class
     */
    Class<T> forMessageClass();

    /**
     * 处理收到的消息
     * <p>如接收失败需抛出异常，将重新投递消息。
     *
     * @param messageBody 消息体
     */
    void handle(T messageBody);
}
