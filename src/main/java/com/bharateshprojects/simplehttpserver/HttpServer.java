package com.bharateshprojects.simplehttpserver;

import com.bharateshprojects.simplehttpserver.config.Configuration;
import com.bharateshprojects.simplehttpserver.config.ConfigurationManager;
import com.bharateshprojects.simplehttpserver.core.ServerListenerThread;
import com.bharateshprojects.simplehttpserver.core.io.WebRootNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        LOGGER.info("Server starting....");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        LOGGER.info("Using Port: {}", conf.getPort());
        LOGGER.info("Using Webroot: {}", conf.getWebroot());

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
            // TODO handle later
        } catch(WebRootNotFoundException e){
            LOGGER.error("Webroot folder not found",e);
        }
    }
}
