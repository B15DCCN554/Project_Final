package startup;

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

    public static void main(String[] argv) {
        ThreadContext.put(LogCommon.token, UUID.randomUUID().toString().replaceAll("-", ""));
        ShutDown.shutdownHook();
        //create pool
        CommonPool.threadPool = Executors.newCachedThreadPool();
        CommonPool.basicChannelPool = BasicChannelPool.getInstance();
        CommonPool.hikariDataSource = ConnectDB.getDataSource();
        LOG.info("Begin read message from queue");
        CommonPool.threadPool.submit(new MessageService());
        LOG.info("End read message from queue");
        CommonPool.threadPool.shutdown();
        ThreadContext.clearAll();
    }
}
