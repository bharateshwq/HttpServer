package com.bharateshprojects.simplehttpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            //TODO we would read

            int _byte;
            while ((_byte = inputStream.read()) >= 0) {
                System.out.print((char) _byte);
//                System.out.println((_byte = inputStream.read()));

            }

            //TODO we would do writing
            String html = "<html><head><title>Simple JAVA HTTP server</title></head><body>This page was served from the JAVA https server</body></html>";
            final String CRLF = "\r\n"; //13 10
            String response =
                    "HTTP/1.1 200 OK" + CRLF
                            + "Content-Length: " + html.getBytes(StandardCharsets.UTF_8).length + CRLF
                            + CRLF
                            + html
                            + CRLF + CRLF; //Status line : HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
            outputStream.write(response.getBytes());


            LOGGER.info("Connection processing finished");
        } catch (Exception e) {
            LOGGER.error("Problem with communication", e);
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }

            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {

                }

            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {

                }

            }
        }
    }
}
