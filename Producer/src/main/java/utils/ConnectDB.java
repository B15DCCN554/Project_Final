package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ConnectDB {
    private static final Logger LOG = LogManager.getLogger(ConnectDB.class);

    private static final String DB_USERNAME = "db_username";
    private static final String DB_PASSWORD = "db_password";
    private static final String DB_URL = "db_url";
    private static final String CACHE_PREP_STMTS = "cache_prep_stmts";
    private static final String PREP_STMT_CACHE_SIZE = "prep_stmt_cache_size";
    private static final String PREP_STMT_CACHE_SQL_LIMIT = "prep_stmt_cache_sql_limit";
    private static final String MAXIMUM_POOL_SIZE = "maximum_pool_size";
    private static final String MINIMUM_IDLE = "minimum_idle";

    private static HikariConfig jdbcConfig = new HikariConfig();
    private static HikariDataSource ds = null;
    private static Properties properties = Property.getInstance();

    static {
        try {
            LOG.info("Begin create HikariDataSource");
            jdbcConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty(MAXIMUM_POOL_SIZE)));
            jdbcConfig.setMinimumIdle(Integer.parseInt(properties.getProperty(MINIMUM_IDLE)));
            jdbcConfig.setJdbcUrl(properties.getProperty(DB_URL));
            jdbcConfig.setUsername(properties.getProperty(DB_USERNAME));
            jdbcConfig.setPassword(properties.getProperty(DB_PASSWORD));
            jdbcConfig.addDataSourceProperty("cachePrepStmts", Boolean.parseBoolean(properties.getProperty(CACHE_PREP_STMTS)));
            jdbcConfig.addDataSourceProperty("prepStmtCacheSize", Integer.parseInt(properties.getProperty(PREP_STMT_CACHE_SIZE)));
            jdbcConfig.addDataSourceProperty("prepStmtCacheSqlLimit", Integer.parseInt(properties.getProperty(PREP_STMT_CACHE_SQL_LIMIT)));
            LOG.info(" Db url: "+jdbcConfig.getJdbcUrl()+" db user name: "+jdbcConfig.getUsername()+" db password: "+jdbcConfig.getPassword());
            ds = new HikariDataSource(jdbcConfig);
            LOG.info("End create HikariDataSource");
        } catch (Exception e) {
            LOG.error("Error create HikariDataSource: "+e.getMessage());
        }
    }

    public static HikariDataSource getDataSource() {
        return ds;
    }

    private ConnectDB() {}

}
