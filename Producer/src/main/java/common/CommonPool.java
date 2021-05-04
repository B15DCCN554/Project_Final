package common;

import com.zaxxer.hikari.HikariDataSource;
import redis.clients.jedis.JedisSentinelPool;
import utils.BasicChannelPool;
import utils.ChannelPool;
import utils.RateLimiter;

import java.util.concurrent.ExecutorService;

public class CommonPool {
    public static HikariDataSource hikariDataSource;
    public static BasicChannelPool basicChannelPool;
    public static JedisSentinelPool sentinelPool;
    public static ExecutorService threadPool;
    public static RateLimiter rateLimiter;
}
