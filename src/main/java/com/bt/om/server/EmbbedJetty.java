package com.bt.om.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author tany 2015年10月5日 下午11:45:15
 */
public class EmbbedJetty extends EmbbedServer {

    private static final Logger logger = LoggerFactory.getLogger(EmbbedJetty.class);

    private Server server;

    @Override
    public void start() throws Exception {
//        logger.info("Tmp:", System.getProperty("java.io.tmpdir"));
        // TODO Auto-generated method stub
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(this.getPort());
        connector.setHost(this.getHostname());
        server.setConnectors(new Connector[] { connector });
        // server.addConnector(connector);

        WebAppContext context = new WebAppContext();
        context.setContextPath(this.getContextPath());
        context.setResourceBase(this.getWebAppDir());

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { context });
        server.setHandler(handlers);
        server.setStopAtShutdown(true);
        server.start();
        server.join();

    }

    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        if (null != server) {
            server.stop();
        }

    }

    public static void main(String[] args) {
        String port = "0";
        String hostname = null;
        String contextPath = null;
        String webAppDir = null;

        EmbbedJetty jetty = new EmbbedJetty();

        for (String arg : args) {
            if (arg.startsWith("-hostname")) {
                hostname = arg.substring(arg.indexOf("=") + 1);
            }

            if (arg.startsWith("-httpPort")) {
                port = arg.substring(arg.indexOf("=") + 1);
            }

            if (arg.startsWith("-contextPath")) {
                contextPath = arg.substring(arg.indexOf("=") + 1);
            }
            if (arg.startsWith("-webAppDir")) {
                webAppDir = arg.substring(arg.indexOf("=") + 1);
            }

        }

        if (hostname != null) {
            jetty.setHostname(hostname);
        }

        if (Integer.valueOf(port) > 0) {
            jetty.setPort(port);
        }

        if (contextPath != null && contextPath.length() > 0) {
            jetty.setContextPath(contextPath);
        }
        if (webAppDir != null && webAppDir.length() > 0) {
            jetty.setWebAppDir(webAppDir);
        }

        try {
            jetty.start();
        } catch (Exception e) {
            logger.error("Server Start Error: ", e);
            System.exit(-1);
        }
    }

}
