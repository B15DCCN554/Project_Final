package utils;

import com.rabbitmq.client.Channel;

public interface ChannelPool<T> {
    T getChannel();
    void releaseChannel(T channel);
    int getSize();
}