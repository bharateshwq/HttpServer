package com.bharateshprojects.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateValidTestCase());
        } catch (HttpParsingException e) {
            fail(e);
        }
        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/", request.getRequestTarget());
        assertEquals("HTTP/1.1", request.getOriginalHttpVersion());
        assertEquals(HttpVersion.HTTP_1_1, request.getBestCompatibleHttpVersion());
    }

    @Test
    void parseHttpRequestBadMethod() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseMethodName());
            fail();

        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }
/*
        assertEquals(request.getMethod(),HttpMethod.GET);
*/
    }


    @Test
    void parseHttpRequestBadMethod2() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestCaseMethodName2());
            fail();

        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }
/*
        assertEquals(request.getMethod(),HttpMethod.GET);
*/
    }

    @Test
    void parseHttpRequestBadRequestLineValidNumberItems() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestRequestLineValidNumberItems());
            fail();

        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());


        }

    }

    @Test
    void parseHttpRequestBadRequestLineEmpty() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestRequestLineEmpty());
            fail();

        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());


        }

    }

    @Test
    void parseHttpRequestBadRequestNoLineFeed() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadTestRequestNoLineFeed());
            fail();

        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());


        }

    }

    //    http version tests
    @Test
    void parseHttpBadVersion() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateBadHttpVersion());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }

    @Test
    void parseHttpHigherVersion() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateHigherVersion());
            assertNotNull(request);
            assertEquals("HTTP/1.5", request.getOriginalHttpVersion());
            assertEquals(HttpVersion.HTTP_1_1, request.getBestCompatibleHttpVersion());
        } catch (HttpParsingException e) {
            fail(e);
        }

    }

    @Test
    void parseHttpUnsupportedVersion() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateUnsupportedVersion());
            fail();

        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_505_VERSION_NOT_SUPPORTED, e.getErrorCode());

        }

    }

    private InputStream generateValidTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
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
        return inputStream;
    }

    private InputStream generateBadTestCaseMethodName() {
        String rawData = "Get / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:128.0) Gecko/20100101 Firefox/128.0\r\n" +

                "Sec-Fetch-Site: cross-site\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestCaseMethodName2() {
        String rawData = "GETAAAA / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:128.0) Gecko/20100101 Firefox/128.0\r\n" +

                "Sec-Fetch-Site: cross-site\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestRequestLineValidNumberItems() {
        String rawData = "GET AAAA / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:128.0) Gecko/20100101 Firefox/128.0\r\n" +

                "Sec-Fetch-Site: cross-site\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestRequestLineEmpty() {
        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:128.0) Gecko/20100101 Firefox/128.0\r\n" +

                "Sec-Fetch-Site: cross-site\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestRequestNoLineFeed() {
        String rawData = "GET / HTTP/1.1\r" + // <---
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:128.0) Gecko/20100101 Firefox/128.0\r\n" +

                "Sec-Fetch-Site: cross-site\r\n" +
                "Priority: u=0, i\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }


    //    Http version generators
    private InputStream generateBadHttpVersion() {
        String rawData = "GET / HTtp/1.1\r\n" +
                "Host: localhost:8080\r\n" +
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
        return inputStream;
    }

    private InputStream generateHigherVersion() {
        String rawData = "GET / HTTP/1.5\r\n" +
                "Host: localhost:8080\r\n" +
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
        return inputStream;
    }

    private InputStream generateUnsupportedVersion() {
        String rawData = "GET / HTTP/2.0\r\n" +
                "Host: localhost:8080\r\n" +
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
        return inputStream;
    }

}


