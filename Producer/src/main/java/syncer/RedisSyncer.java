package syncer;

import bean.QrTerminalPo;
import common.CommonPool;
import common.JsonCustom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisSyncer {
    private static final Logger LOG = LogManager.getLogger(RedisSyncer.class);
    private static final int TIME = 3000;

    public static void saveData(String key, List<QrTerminalPo> listQrTerminal) {
        if (listQrTerminal == null) return;
        try (Jedis redis = CommonPool.sentinelPool.getResource()) {
            //redis.expire(key, TIME);
            redis.del(key);
            LOG.info("Start write data into redis");
            for (QrTerminalPo qrTerminalPo : listQrTerminal) {
                LOG.info("Data with key: " + key + " value: " + JsonCustom.convertObjectToJson(qrTerminalPo));
                redis.lpush(key, JsonCustom.convertObjectToJson(qrTerminalPo));
                LOG.info("Insert success");
            }
            LOG.info("End write data into redis");
        } catch (Exception e) {
            LOG.error("Error insert data to redis: ",e);
        }
    }
}
