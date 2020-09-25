package utils;

import com.rabbitmq.client.Channel;

public interface ChannelPool {
    Channel getChannel() throws InterruptedException;
    void releaseChannel(Channel channel) throws InterruptedException;
    int getSize();
}