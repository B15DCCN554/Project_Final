package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.RateLimiter;

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

    @Override
    public boolean checkAllowRequest() {
        System.out.println("time next: "+nextAllowedTime);
        System.out.println("cure next: "+System.currentTimeMillis());
        while (!allow()) {
            try {
                //System.out.println("Đợi xíu!");
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOG.error("Error check allow request: "+e.getMessage());
            }
        }
        return true;
    }
}