package startup;

import bean.QrTerminalPo;
import bean.QrTerminals;
import common.LogCommon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import service.QrTerminalService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

public class ClientStartup {
    private static final Logger LOG = LogManager.getLogger(ClientStartup.class);

    private final static String uri = "http://localhost:8080/qr_terminal";
    private final static String getQrTerminalPath = "get_qr_terminal";
    final static QrTerminalService qrTerminalService = new QrTerminalService();

    public static void main(String[] args) {
        ThreadContext.put(LogCommon.token, UUID.randomUUID().toString().replaceAll("-", ""));
        LOG.info("Begin build client insert data with uri: "+uri+" path: "+getQrTerminalPath);
        Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
        WebTarget webTarget = client.target(uri).path(getQrTerminalPath);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        LOG.info("Build client insert data success");
        Response response = invocationBuilder.get();
        QrTerminals qrTerminals = response.readEntity(QrTerminals.class);
        List<QrTerminalPo> listQrTerminalPo = qrTerminals.getList();
        if(listQrTerminalPo == null) return;
        System.out.println(listQrTerminalPo.size());
        LOG.info("Begin client insert data with: "+listQrTerminalPo.size()+ " data");
        for (QrTerminalPo qrTerminalPo: listQrTerminalPo){
            qrTerminalPo.setMasterMerchant((qrTerminalPo.getId().intValue()+1)+"");
            qrTerminalPo.setMerchantCode((qrTerminalPo.getId().intValue()+1)+"");
            //insert data into qr_terminal_test
            qrTerminalService.insertQrTerminalTest(qrTerminalPo);
        }
        LOG.info("End client insert data");
    }
}
