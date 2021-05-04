package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;


public class LeakyBucket extends RateLimiter {
    private static final Logger LOG = LogManager.getLogger(LeakyBucket.class);

    private long nextAllowedTime;

    private final long REQUEST_INTERVAL_MILLIS;

    public LeakyBucket(int maxRequestPerSec) {
        super(maxRequestPerSec);
        REQUEST_INTERVAL_MILLIS = 1000 / maxRequestPerSec;
        nextAllowedTime = System.currentTimeMillis();
    }

    @Override
    public boolean checkAllowRequest() {
        while (!allow()) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                LOG.error("Error check allow request: ",e);
            }
        }
        return true;
    }

    private boolean allow() {
        long curTime = System.currentTimeMillis();
        synchronized (this) {
            if (curTime >= nextAllowedTime) {
                nextAllowedTime = curTime + REQUEST_INTERVAL_MILLIS;
                return true;
            }
            return false;
        }
    }
}