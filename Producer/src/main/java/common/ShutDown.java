package common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShutDown {
    private static final Logger LOG = LogManager.getLogger(ShutDown.class);

    public static void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Begin shut down hook");
            if (CommonPool.hikariDataSource != null) {
                CommonPool.hikariDataSource.close();
            }
            if (CommonPool.channelPool != null) {
                CommonPool.channelPool = null;
            }
            if (CommonPool.sentinelPool != null) {
                CommonPool.sentinelPool.destroy();
            }
            if (CommonPool.threadPool != null) {
                CommonPool.threadPool.shutdown();
            }

            LOG.info("End shut down hook");
        }));
    }
}
