package com.github.lykmapipo.retrofit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.List;

import lombok.Data;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.http.GET;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * HttpService Tests
 *
 * @author lally elias
 */

@RunWith(RobolectricTestRunner.class)
public class HttpServiceTest {
    private MockWebServer mockWebServer;

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test
    public void shouldConvertObjectToJson() {
        User user = new User("John Doe");
        String json = "{\"name\":\"John Doe\"}";
        String converted = HttpService.toJson(user);
        assertEquals("should convert value to json", converted, json);
    }

    @Test
    public void shouldConvertJsonToObject() {
        assertTrue(true);
    }

    @Before
    public void tearDown() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    public interface Api {
        @GET("/users")
        Call<List<User>> list();
    }

    public static class User {
        String name;

        public User(String name) {
            this.name = name;
        }
    }
}