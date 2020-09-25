package syncer;


import bean.QrTerminalPo;
import com.rabbitmq.client.Channel;
import common.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class Producer {
    private static final Logger LOG = LogManager.getLogger(Producer.class);

    //submit into queue
    public static void submit(List<QrTerminalPo> listQrTerminalPo) throws InterruptedException {
        if(listQrTerminalPo == null) return;
        int shark = 0;
        for (QrTerminalPo qrTerminalPo : listQrTerminalPo) {
            LOG.debug("Begin init channel");
            Channel channel = CommonPool.channelPool.getChannel();
            String queueName = DivisionQueue.getQueueName(shark);
            System.out.println(queueName);
            try {
                if (channel == null) return;
                String messageStr = JsonCustom.convertObjectToJson(qrTerminalPo);
                byte[] message = messageStr.getBytes("UTF-8");
                LOG.info("Begin submit data: " + messageStr);
                channel.queueDeclare(queueName, true, false, false, null);
                channel.basicPublish("", queueName, null, message);
                LOG.info("End submit data");
                ++shark;
                System.out.println("size:"+CommonPool.channelPool.getSize());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                LOG.error("Error submit data: " + ioException.getMessage());
            } finally {
                try {
                    CommonPool.channelPool.releaseChannel(channel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOG.debug("End channel");
            }
        }
    }
}

