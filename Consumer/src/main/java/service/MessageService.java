package service;

import bean.QrTerminalPo;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import common.ChannelCommon;
import common.CommonPool;
import common.GsonCustom;
import common.LogCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.io.IOException;
import java.util.UUID;

public class MessageService implements Runnable{
    private static final Logger LOG = LogManager.getLogger(MessageService.class);
    final static QrTerminalService qrTerminalService = new QrTerminalService();
    private Channel channel;

    public MessageService() { }

    @Override
    public void run() {
        try {
            channel = CommonPool.basicChannelPool.getChannel();
            channel.queueDeclare("ott_message_marketing", true, false, false, null);
            getMessage(channel);
        } catch (Exception e) {
            LOG.error("Error get message from queue: "+e.getMessage());
        }
    }

    private void getMessage(Channel channel) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            ThreadContext.put(LogCommon.token, UUID.randomUUID().toString().replaceAll("-", ""));
            String message = new String(delivery.getBody(), "UTF-8");
            LOG.debug("queueName: "+ChannelCommon.queueName+" message: " + message);
            try {
                QrTerminalPo qrTerminalPo = GsonCustom.getInstance().fromJson(message, QrTerminalPo.class);
                qrTerminalPo.setMasterMerchant(qrTerminalPo.getId().toString());
                qrTerminalPo.setMerchantCode(qrTerminalPo.getId().toString());
                qrTerminalService.insertQrTerminalTest(qrTerminalPo);
                System.out.println("insert data: "+qrTerminalPo.getId());
                ThreadContext.clearAll();
            } catch (Exception e) {
                LOG.error("Error getMessage: ", e);
                e.printStackTrace();
            }
            ThreadContext.clearAll();
        };
        channel.basicConsume("ott_message_marketing", true, deliverCallback, consumerTag -> {
        });
    }
}
