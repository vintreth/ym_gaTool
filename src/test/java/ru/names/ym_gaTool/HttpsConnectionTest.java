package ru.names.ym_gaTool;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author kbogdanov 18.03.16
 */
public class HttpsConnectionTest {

    private AbstractHttpConnection connection;

    public void initConnection() throws Exception {
        connection = new HttpsConnection("https://www.google-analytics.com/collect");
    }

    @Test
    public void testConnection() throws Exception {

    }

    @Test
    public void testAddHeaders() throws Exception {

    }

    @Test
    public void testDoGet() throws Exception {

    }

    @Test
    public void testDoPost() throws Exception {

    }

    @Test
    public void testGetInputStream() throws Exception {

    }

    @Test
    public void testIsSuccess() throws Exception {

    }

    @Test
    public void testIsError() throws Exception {

    }

    @Test
    public void testGetResponseCode() throws Exception {

    }
}