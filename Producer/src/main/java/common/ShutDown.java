package common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShutDown {
    private static final Logger LOG = LogManager.getLogger(ShutDown.class);

    public static void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (CommonPool.hikariDataSource != null) {
                    CommonPool.hikariDataSource.close();
                }
                if (CommonPool.basicChannelPool != null) {
                    CommonPool.basicChannelPool = null;
                }
                if (CommonPool.sentinelPool != null) {
                    CommonPool.sentinelPool.destroy();
                }
                if (CommonPool.threadPool != null) {
                    CommonPool.threadPool.shutdown();
                }
                if (CommonPool.rateLimiter != null) {
                    CommonPool.rateLimiter = null;
                }
            }
        });
    }
}
