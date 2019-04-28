package com.github.lykmapipo.retrofit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.http.GET;

import static org.junit.Assert.assertEquals;

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
        User user = new User("John Doe");
        String json = "{\"name\":\"John Doe\"}";
        User converted = HttpService.fromJson(json, User.class);
        assertEquals("should convert json to value", converted, user);
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
}