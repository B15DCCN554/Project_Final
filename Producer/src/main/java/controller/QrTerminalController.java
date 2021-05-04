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
        ThreadContext.put(LogCommon.token, UUID.randomUUID().toString().replaceAll("-", ""));
        QrTerminals qrTerminals = null;
        if (CommonPool.rateLimiter.checkAllowRequest()) {
            LOG.debug("Begin call api get_qr_terminal");
            qrTerminals = new QrTerminals();
            qrTerminals.setList(qrTerminalService.getAll());
            RedisSyncer.saveData(RedisCommon.MERCHANT_CODE_TERMINAL_ID, qrTerminals.getList());
            Producer.submit(qrTerminals.getList());
            LOG.debug("End call api get_qr_terminal");
            ThreadContext.clearAll();
        }
        return qrTerminals;
    }

    @POST
    @Path("add_qr_terminal")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addQrTerminal(QrTerminalPo qrTerminalPo) {
        ThreadContext.put(LogCommon.token, UUID.randomUUID().toString().replaceAll("-", ""));
        QrTerminalPo qrTerminalPoResponse = null;
        if (CommonPool.rateLimiter.checkAllowRequest()) {
            LOG.debug("Begin call api add_qr_terminal");
            qrTerminalPoResponse = qrTerminalService.insertQrTerminal(qrTerminalPo);
            LOG.debug("End call api add_qr_terminal");
        }
        ThreadContext.clearAll();
        return Response.ok(qrTerminalPoResponse).build();
    }
}
