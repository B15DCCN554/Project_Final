package server;

import common.*;
import controller.QrTerminalController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import service.QrTerminalService;
import utils.LeakyBucket;
import utils.BasicChannelPool;
import utils.ConnectDB;
import utils.ConnectRedis;

import java.util.Properties;
import java.util.UUID;


public class App {

    private static final Logger LOG = LogManager.getLogger(App.class);
    private static final String SERVER_PORT = "server_port";
    private static final Properties properties = Property.getInstance();

    public static void main(String[] args) {
        ThreadContext.put(LogCommon.token, UUID.randomUUID().toString().replaceAll("-", ""));
        ShutDown.shutdownHook();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        Server jettyServer = new Server(Integer.parseInt(properties.getProperty(SERVER_PORT)));
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(
                ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                QrTerminalController.class.getCanonicalName());

        // create pool
        CommonPool.hikariDataSource = ConnectDB.getDataSource();
        CommonPool.basicChannelPool = BasicChannelPool.getInstance();
        CommonPool.sentinelPool = ConnectRedis.getJedisSentinelPool();
        CommonPool.rateLimiter = new LeakyBucket(RateConfig.MAX_REQUEST_PER_SECOND);
        // start server
        try {
            jettyServer.start();
            LOG.info("Server started with port: "+properties.getProperty(SERVER_PORT));
            ThreadContext.clearAll();
            //new QrTerminalService().addData();
            jettyServer.join();
        } catch (Exception e) {
            LOG.error("Error start server: ",e);
        } finally {
            jettyServer.destroy();
        }
    }
}