package com.ljt.katp;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.ljt.katp.server.HttpServer;

import java.io.IOException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        while (true){
            HttpServer server = new HttpServer("127.0.0.1", 8080);
            try {
                server.start();
//            Log.e("Httpd", "The server started.");
            } catch(IOException ioe) {
//            Log.e("Httpd", "The server could not start.");
            }
        }

//        assertEquals(4, 2 + 2);
    }
}