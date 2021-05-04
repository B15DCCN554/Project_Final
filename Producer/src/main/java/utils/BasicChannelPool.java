package utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import common.GsonCustom;
import common.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicChannelPool implements ChannelPool<Channel> {
    private static final Logger LOG = LogManager.getLogger(BasicChannelPool.class);
    private static final Properties properties = Property.getInstance();

    private final static String RABBITMQ_HOST = "rabbitmq_host";
    private final static String RABBITMQ_PORT = "rabbitmq_port";
    private final static String RABBITMQ_USERNAME = "rabbitmq_username";
    private final static String RABBITMQ_PASSWORD = "rabbitmq_password";
    private final static String RABBITMQ_HEARTBEAT = "rabbitmq_heartbeat";
    private final static String VIRTUAL_HOST = "virtual_host";

    private static ConnectionFactory factory;
    private static BasicChannelPool instance;
    private static Connection conn;

    private static final long EXPIRED_TIME_IN_MILISECOND = 10;
    private static final int NUMBER_OF_CHANNEL = 10;

    private final List<Channel> available = Collections.synchronizedList(new ArrayList<>());
    private final List<Channel> inUse = Collections.synchronizedList(new ArrayList<>());

    private AtomicInteger count = new AtomicInteger(0);
    private AtomicBoolean waiting = new AtomicBoolean(false);

    private BasicChannelPool() {
    }

    public static BasicChannelPool getInstance() {
        if (instance == null) {
            synchronized (BasicChannelPool.class) {
                if (instance == null) {
                    LOG.info("Begin create channel pool");
                    instance = new BasicChannelPool();
                    try {
                        factory = new ConnectionFactory();
                        factory.setHost(properties.getProperty(RABBITMQ_HOST));
                        factory.setPort(Integer.parseInt(properties.getProperty(RABBITMQ_PORT)));
                        factory.setUsername(properties.getProperty(RABBITMQ_USERNAME));
                        factory.setPassword(properties.getProperty(RABBITMQ_PASSWORD));
                        factory.setVirtualHost(properties.getProperty(VIRTUAL_HOST));
                        factory.setRequestedHeartbeat(Integer.parseInt(properties.getProperty(RABBITMQ_HEARTBEAT)));
                        LOG.info("Host: " + factory.getHost() + " port: " + factory.getPort() + " username: " + factory.getUsername() + " password: " + factory.getPassword() + " virtual host: " + factory.getVirtualHost());
                        conn = factory.newConnection();
                        System.out.println("con: "+ GsonCustom.getInstance().toJson(conn));
                        LOG.info("End create channel pool");
                    } catch (IOException e) {
                        LOG.error("Error io create createChannelPool", e);
                    } catch (TimeoutException e) {
                        LOG.error("Error timeout create createChannelPool", e);
                    }
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized Channel getChannel() {
        if (!available.isEmpty()) {
            Channel channel = available.remove(0);
            inUse.add(channel);
            return channel;
        }

        if (count.get() == NUMBER_OF_CHANNEL) {
            System.out.println("waitting...");
            this.waitingChannelAvailable();
            return this.getChannel();
        }

        Channel channel = null;
        try {
            channel = this.createChannel(conn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        inUse.add(channel);
        return channel;
    }

    @Override
    public synchronized void releaseChannel(Channel channel) {
        if (channel != null) {
            inUse.remove(channel);
            available.add(channel);
        }
    }

    private Channel createChannel(Connection conn) throws IOException {
        System.out.println("channel_number: "+count.getAndIncrement());
        return conn.createChannel();
    }

    @Override
    public int getSize() {
        System.out.println("channel using: "+inUse.size()+ " channel available: "+available.size());
        return inUse.size() + available.size();
    }

    private void waitingChannelAvailable() {
        if (waiting.get()) {
            waiting.set(false);
            LOG.info("No channel available");
            throw new RuntimeException("No channel available");
        }
        waiting.set(true);
        waiting(EXPIRED_TIME_IN_MILISECOND);
    }

    private void waiting(long numberOfSecond) {
        try {
            TimeUnit.MILLISECONDS.sleep(numberOfSecond);
        } catch (InterruptedException e) {
            LOG.error("Error create channel ",e);
            Thread.currentThread().interrupt();
        }
    }

}