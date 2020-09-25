package utils;

import common.Property;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ConnectRedis {
    private static final Logger LOG = LogManager.getLogger(ConnectRedis.class);

    private static final String REDIS_MASTER_NAME = "redis_master_name";
    private static final String REDIS_PASSWORD = "redis_password";
    private static final String REDIS_HOST_MASTER = "redis_host_master";
    private static final String REDIS_PORT_MASTER = "redis_port_master";
    private static final String REDIS_HOST_SLAVE = "redis_host_slave";
    private static final String REDIS_PORT_SLAVE = "redis_port_slave";
    private static final String MAX_DATABASE = "max_database";

    private static Properties properties = Property.getInstance();
    private static GenericObjectPoolConfig config = null;
    private static JedisSentinelPool sentinelPool = null;
    static {
        config = new GenericObjectPoolConfig();
        try {
            LOG.info("Begin create sentinelPool");
            Set<String> sentinels = new HashSet<String>();
            sentinels.add(new HostAndPort(properties.getProperty(REDIS_HOST_MASTER), Integer.parseInt(properties.getProperty(REDIS_PORT_MASTER))).toString());
            sentinels.add(new HostAndPort(properties.getProperty(REDIS_HOST_SLAVE), Integer.parseInt(properties.getProperty(REDIS_PORT_SLAVE))).toString());
            sentinelPool = new JedisSentinelPool(properties.getProperty(REDIS_MASTER_NAME), sentinels,config, Protocol.DEFAULT_TIMEOUT, properties.getProperty(REDIS_PASSWORD), Integer.parseInt(properties.getProperty(MAX_DATABASE)));
            LOG.info("Host and Port: "+sentinelPool.getCurrentHostMaster().toString());
            LOG.info("End create sentinelPool");
        } catch (Exception e) {
            LOG.error("Error create sentinelPool: "+e.getMessage());
        }
    }

    public static JedisSentinelPool getJedisSentinelPool() { return sentinelPool; }

    private ConnectRedis(){}


}
