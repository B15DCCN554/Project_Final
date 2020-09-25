package common;

import com.zaxxer.hikari.HikariDataSource;
import redis.clients.jedis.JedisSentinelPool;
import utils.ChannelPool;
import utils.RateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonPool {
    public static HikariDataSource hikariDataSource;
    public static ChannelPool channelPool;
    public static JedisSentinelPool sentinelPool;
    public static ExecutorService threadPool;
    public static RateLimiter rateLimiter;
}
