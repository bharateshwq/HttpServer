package com.bharateshprojects.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HttpHeaderParserTest {
    private HttpParser httpParser;
    private Method parseHttpHeaderMethod;

    @BeforeAll
    public void beforeClass() throws NoSuchMethodException {
        httpParser = new HttpParser();
        Class<HttpParser> cls = HttpParser.class;
        parseHttpHeaderMethod = cls.getDeclaredMethod("parseHeaders", InputStreamReader.class, HttpRequest.class);
        parseHttpHeaderMethod.setAccessible(true);
    }

    @Test
    public void testSimpleSingleHeader() throws InvocationTargetException, IllegalAccessException {
        HttpRequest request = new HttpRequest();
        parseHttpHeaderMethod.invoke(httpParser, generateSimpleSingleHeader(), request);
        assertEquals(1, request.getHeaderNames().size());
        assertEquals("localhost:8080", request.getHeader("Host"));
    }

    @Test
    public void testSimpleMultipleHeaders() throws InvocationTargetException, IllegalAccessException {
        HttpRequest request = new HttpRequest();
        parseHttpHeaderMethod.invoke(httpParser, generateSimpleMultipleHeader(), request);
        assertEquals(11, request.getHeaderNames().size());
        assertEquals("localhost:8080", request.getHeader("Host"));
    }

    @Test
    public void testErrorSpaceBeforeColonHeader() throws IllegalAccessException {
        HttpRequest request = new HttpRequest();
        try {
            parseHttpHeaderMethod.invoke(httpParser, generateSpaceBeforeColonErrorHeaderMessage(), request);
            fail();
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof HttpParsingException) {
                assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, ((HttpParsingException) e.getCause()).getErrorCode());
            }
        }
    }

    private InputStreamReader generateSimpleSingleHeader() {
        String rawData = "Host: localhost:8080\r\n";


        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        return reader;
    }

    private InputStreamReader generateSimpleMultipleHeader() {
        String rawData = "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:128.0) Gecko/20100101 Firefox/128.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: cross-site\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        return reader;
    }

    private InputStreamReader generateSpaceBeforeColonErrorHeaderMessage() {
        String rawData = "Host  : localhost:8080\r\n";


        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        return reader;
    }

}
