package utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import common.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

public class BasicChannelPool implements ChannelPool {
    private static final Logger LOG = LogManager.getLogger(BasicChannelPool.class);

    private final static String RABBITMQ_HOST = "rabbitmq_host";
    private final static String RABBITMQ_PORT = "rabbitmq_port";
    private final static String RABBITMQ_USERNAME = "rabbitmq_username";
    private final static String RABBITMQ_PASSWORD = "rabbitmq_password";
    private final static String RABBITMQ_HEARTBEAT = "rabbitmq_heartbeat";
    private final static String VIRTUAL_HOST = "virtual_host";
    private static ConnectionFactory factory;
    private static int INITIAL_POOL_SIZE = 20;
    private BlockingDeque<Channel> channelPool = new LinkedBlockingDeque<>(20);

    private static final Properties properties = Property.getInstance();
    private static BasicChannelPool instance;

    private BasicChannelPool() {
    }

    public static BasicChannelPool createChannelPool() {
        if (instance == null) {
            synchronized (BasicChannelPool.class) {
                LOG.info("Begin create channel poll");
                instance = new BasicChannelPool();
                BlockingDeque<Channel> pool = new LinkedBlockingDeque<>(20);
                try {
                    factory = new ConnectionFactory();
                    factory.setHost(properties.getProperty(RABBITMQ_HOST));
                    factory.setPort(Integer.parseInt(properties.getProperty(RABBITMQ_PORT)));
                    factory.setUsername(properties.getProperty(RABBITMQ_USERNAME));
                    factory.setPassword(properties.getProperty(RABBITMQ_PASSWORD));
                    factory.setVirtualHost(properties.getProperty(VIRTUAL_HOST));
                    factory.setRequestedHeartbeat(Integer.parseInt(properties.getProperty(RABBITMQ_HEARTBEAT)));
                    LOG.info("Host: " + factory.getHost() + " port: " + factory.getPort() + " username: " + factory.getUsername() + " password: " + factory.getPassword() + " virtual host: " + factory.getVirtualHost());
                    Connection conn = factory.newConnection();
                    for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                        pool.put(createChannel(conn));
                    }
                    instance.setChannelPool(pool);
                } catch (IOException e) {
                    LOG.error("Error io create createChannelPool" + e.getMessage());
                } catch (TimeoutException e) {
                    LOG.error("Error timeout create createChannelPool" + e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        LOG.info("End create channel poll");
        return instance;
    }

    @Override
    public synchronized Channel getChannel() throws InterruptedException {
        return channelPool.take();
    }

    @Override
    public synchronized void releaseChannel(Channel channel) throws InterruptedException {
        channelPool.put(channel);
    }

    private static Channel createChannel(Connection conn) throws IOException {
        return conn.createChannel();
    }

    @Override
    public int getSize() {
        System.out.println(channelPool.size());
        return channelPool.size();
    }

    public void setChannelPool(BlockingDeque<Channel> channelPool) {
        this.channelPool = channelPool;
    }

    public BlockingDeque<Channel> getChannelPool() {
        return channelPool;
    }
}