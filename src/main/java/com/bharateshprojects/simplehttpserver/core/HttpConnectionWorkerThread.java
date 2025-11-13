package com.bharateshprojects.simplehttpserver.core;

import com.bharateshprojects.http.*;
import com.bharateshprojects.simplehttpserver.core.io.ReadFileException;
import com.bharateshprojects.simplehttpserver.core.io.WebRootHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private Socket socket;
    private WebRootHandler webRootHandler;
    private HttpParser httpParser = new HttpParser();

    public HttpConnectionWorkerThread(Socket socket, WebRootHandler webRootHandler) {
        this.socket = socket;
        this.webRootHandler = webRootHandler;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            //TODO we would read

            HttpRequest request = httpParser.parseHttpRequest(inputStream);
            HttpResponse response = handleRequest(request);

            outputStream.write(response.getResponseBytes());

            LOGGER.info(" * Connection Processing Finished.");


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

    private HttpResponse handleRequest(HttpRequest request) {

        switch (request.getMethod()) {
            case GET:
                LOGGER.info(" * GET Request");
                return handleGetRequest(request, true);
            case HEAD:
                LOGGER.info(" * HEAD Request");
                return handleGetRequest(request, false);
            default:
                return new HttpResponse.Builder()
                        .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                        .statusCode(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED)
                        .build();
        }

    }

    private HttpResponse handleGetRequest(HttpRequest request, boolean setMessageBody) {
        try {

            HttpResponse.Builder builder = new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                    .statusCode(HttpStatusCode.OK)
                    .addHeader(HttpHeaderName.CONTENT_TYPE.headerName, webRootHandler.getFileMimeType(request.getRequestTarget()));

            if (setMessageBody) {
                byte[] messageBody = webRootHandler.getFileByteArrayData(request.getRequestTarget());
                builder.addHeader(HttpHeaderName.CONTENT_LENGTH.headerName, String.valueOf(messageBody.length))
                        .messageBody(messageBody);
            }

            return builder.build();

        } catch (FileNotFoundException e) {

            return new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                    .statusCode(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND)
                    .build();

        } catch (ReadFileException e) {

            return new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                    .statusCode(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR)
                    .build();
        }

    }
}
