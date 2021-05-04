package syncer;


import bean.QrTerminalPo;
import com.rabbitmq.client.Channel;
import common.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;

public class Producer {
    private static final Logger LOG = LogManager.getLogger(Producer.class);

    //submit into queue
    public static boolean submit(List<QrTerminalPo> listQrTerminalPo) {
        if (listQrTerminalPo == null) {
            LOG.info("listQrTerminalPo is null");
            return false;
        }
        Channel channel = null;
        try {
            channel = CommonPool.basicChannelPool.getChannel();
            if (channel == null || !channel.isOpen()) {
                LOG.info("Not channel is available");
                return false;
            }
            channel.queueDeclare(ChannelCommon.queueName, true, false, false, null);
            for (QrTerminalPo qrTerminalPo : listQrTerminalPo) {
                String messageStr = JsonCustom.convertObjectToJson(qrTerminalPo);
                byte[] message = messageStr.getBytes("UTF-8");
                LOG.info("Begin submit data: " + messageStr);
                channel.basicPublish("", ChannelCommon.queueName, null, message);
                LOG.info("End submit data");
            }
        } catch (Exception e) {
            LOG.error("Error submit: ", e);
            return false;
        } finally {
            CommonPool.basicChannelPool.releaseChannel(channel);
            LOG.debug("End channel");
        }
        return true;
    }
}

