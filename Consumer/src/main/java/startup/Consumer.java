package startup;

import com.rabbitmq.client.Channel;
import common.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import service.MessageService;
import utils.BasicChannelPool;
import utils.ConnectDB;

import java.util.UUID;
import java.util.concurrent.Executors;

public class Consumer {
    private static final Logger LOG = LogManager.getLogger(Consumer.class);

    public static void main(String[] argv) throws Exception {
        ThreadContext.put(LogCommon.token, UUID.randomUUID().toString().replaceAll("-", ""));
        //create pool
        CommonPool.threadPool = Executors.newFixedThreadPool(5);
        CommonPool.channelPool = BasicChannelPool.createChannelPool();
        CommonPool.hikariDataSource = ConnectDB.getDataSource();
        //
        System.out.println("Begin read messages... (ctrl+c to exit)");
        LOG.info("Begin read message from queue");
        for (Shark shark : Shark.values()) {
            Channel channel = CommonPool.channelPool.getChannel();
            channel.queueDeclare(shark.getQueueName(), true, false, false, null);
            CommonPool.threadPool.submit(new MessageService(shark.getQueueName(),channel));
        }
        LOG.info("Begin read message from queue");
        CommonPool.threadPool.shutdown();
        ThreadContext.clearAll();
        ShutDown.shutdownHook();
    }
}
