package service;

import bean.QrTerminalPo;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import common.CommonPool;
import common.GsonCustom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MessageService implements Runnable{
    private static final Logger LOG = LogManager.getLogger(MessageService.class);

    final static QrTerminalService qrTerminalService = new QrTerminalService();
    private String queueName;
    private Channel channel;

    public MessageService(String queueName, Channel channel) {
        this.queueName = queueName;
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            getMessage(channel,queueName);
        } catch (Exception e) {
            LOG.error("Error get message from queue: "+e.getMessage());
            e.printStackTrace();
        }
    }

    //get message from queue
    private void getMessage(Channel channel,String queueName) throws IOException, InterruptedException {
        LOG.info("Begin read messages");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            LOG.debug("queueName: "+queueName+" message: " + message);
            try {
                QrTerminalPo qrTerminalPo = GsonCustom.getInstance().fromJson(message, QrTerminalPo.class);
                qrTerminalPo.setMasterMerchant(qrTerminalPo.getId().toString());
                qrTerminalPo.setMerchantCode(qrTerminalPo.getId().toString());
                //insert data into qr_terminal_test
                qrTerminalService.insertQrTerminalTest(qrTerminalPo);
                Thread.sleep(5);
                System.out.println("insert data: "+qrTerminalPo.getId());
            } catch (Exception e) {
                LOG.error("Error read message: " + e.getMessage());
                e.printStackTrace();
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
        CommonPool.channelPool.releaseChannel(channel);
        LOG.info("End read messages");
    }
}
