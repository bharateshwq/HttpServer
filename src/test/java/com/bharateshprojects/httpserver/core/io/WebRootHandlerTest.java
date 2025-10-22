package com.bharateshprojects.httpserver.core.io;


import com.bharateshprojects.simplehttpserver.core.io.ReadFileException;
import com.bharateshprojects.simplehttpserver.core.io.WebRootHandler;
import com.bharateshprojects.simplehttpserver.core.io.WebRootNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebRootHandlerTest {

    WebRootHandler webRootHandler;
    private Method checkIfEndsWithSlashMethod;
    private Method checkIfProvidedRelativePathExistsMethod;

    @BeforeAll
    public void beforeClass() throws WebRootNotFoundException, NoSuchMethodException {
        webRootHandler = new WebRootHandler("WebRoot");
        Class<WebRootHandler> cls = WebRootHandler.class;
        checkIfEndsWithSlashMethod = cls.getDeclaredMethod("checkIfEndsWithSlash", String.class);
        checkIfEndsWithSlashMethod.setAccessible(true);

        checkIfProvidedRelativePathExistsMethod = cls.getDeclaredMethod("checkIfProvidedRelativePathExists", String.class);
        checkIfProvidedRelativePathExistsMethod.setAccessible(true);


    }

    @Test
    void constructorGoodPath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("/opt/WebRoot/");
        } catch (WebRootNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void constructorBadPath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("/home/Documents/Projects/simplehttpserver/WebRoot/");
            fail();

        } catch (WebRootNotFoundException e) {
        }
    }

    @Test
    void constructorGoodRelativePath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebRoot");
        } catch (WebRootNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void constructorBadRelativePath() {
        try {
            WebRootHandler webRootHandler = new WebRootHandler("WebGroot");
            fail();

        } catch (WebRootNotFoundException e) {
        }
    }

    @Test
    void checkIfEndsWithSlashMethodFalse() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodFalse1() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodFalse3() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/private/index.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodTrue() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/private/");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void checkIfEndsWithSlashMethodTrue1() {
        try {
            boolean result = (Boolean) checkIfEndsWithSlashMethod.invoke(webRootHandler, "/");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }


    @Test
    void testWebRootFilePathExistsGood() {
        try {
            boolean result = (Boolean) checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/index.html");
            assertTrue(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathDoesNotExist() {
        try {
            boolean result = (Boolean) checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/indexNotHere.html");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testWebRootFilePathDoesExistsInvalid() {
        try {
            boolean result = (Boolean) checkIfProvidedRelativePathExistsMethod.invoke(webRootHandler, "/../LICENSE");
            assertFalse(result);
        } catch (IllegalAccessException e) {
            fail(e);
        } catch (InvocationTargetException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeText() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/");
            assertEquals("text/html", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeJpeg() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/logo.jpg");
            assertEquals("image/jpeg", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileMimeTypeDefault() {
        try {
            String mimeType = webRootHandler.getFileMimeType("/favicon.ico");
            assertEquals("application/octet-stream", mimeType);
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayData() {
        try {
            assertTrue(webRootHandler.getFileByteArrayData("/").length > 0);
        } catch (FileNotFoundException e) {
            fail(e);
        } catch (ReadFileException e) {
            fail(e);
        }
    }

    @Test
    void testGetFileByteArrayDataFiledNotThere() {
        try {
            webRootHandler.getFileByteArrayData("/test.html");
            fail();
        } catch (FileNotFoundException e) {
            //pass
        } catch (ReadFileException e) {
            fail(e);
        }
    }

}
