package controller;

import bean.QrTerminalPo;
import bean.QrTerminals;
import common.CommonPool;
import common.LogCommon;
import common.RedisCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import service.QrTerminalService;
import syncer.Producer;
import syncer.RedisSyncer;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/qr_terminal")
public class QrTerminalController {

    private static final Logger LOG = LogManager.getLogger(QrTerminalController.class);
    QrTerminalService qrTerminalService = new QrTerminalService();

    @GET
    @Path("get_qr_terminal")
    @Produces(MediaType.APPLICATION_JSON)
    public QrTerminals getQrTerminal() {
        if(CommonPool.rateLimiter.checkAllowRequest()){
            LOG.debug("Begin call api get_qr_terminal");
            ThreadContext.put(LogCommon.token,UUID.randomUUID().toString().replaceAll("-",""));
            QrTerminals qrTerminals = new QrTerminals();
            qrTerminals.setList(qrTerminalService.getAll());
            RedisSyncer.saveData(RedisCommon.MERCHANT_CODE_TERMINAL_ID, qrTerminals.getList());
            try {
                Producer.submit(qrTerminals.getList());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadContext.clearAll();
            LOG.debug("End call api get_qr_terminal");
            return qrTerminals;
        }
        return null;
    }

    @POST
    @Path("add_qr_terminal")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addQrTerminal(QrTerminalPo qrTerminalPo){
        if(CommonPool.rateLimiter.checkAllowRequest()) {
            ThreadContext.put(LogCommon.token, UUID.randomUUID().toString().replaceAll("-", ""));
            QrTerminalPo qrTerminalPoResponse = qrTerminalService.insertQrTerminal(qrTerminalPo);
            ThreadContext.clearAll();
            return Response.ok(qrTerminalPoResponse).build();
        }
        return null;
    }
}
