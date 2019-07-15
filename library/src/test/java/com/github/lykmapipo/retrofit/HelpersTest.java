package com.github.lykmapipo.retrofit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * HttpService Tests
 *
 * @author lally elias
 */

@RunWith(RobolectricTestRunner.class)
public class HelpersTest {
    @Test
    public void shouldObtainFileExtension() throws IOException {
        File file = File.createTempFile("test_", ".png");
        String extension = HttpService.extensionOf(file.getName());
        assertNotNull("should obtain file extension", extension);
    }

    @Test
    public void shouldObtainFileMimeType() throws IOException {
        File file = File.createTempFile("test_", ".png");
        String mimeType = HttpService.mimeTypeFor(file);
        assertNotNull("should obtain file mime type", mimeType);
    }

    @Test
    public void shouldCreateFilePart() throws IOException {
        File file = File.createTempFile("test_", ".png");
        MultipartBody.Part part = HttpService.createPart("test", file);
        assertNotNull("should create file part", part);
        assertNotNull("should have headers", part.headers());
        assertNotNull("should have body", part.body());
    }

    @Test
    public void shouldCreateBodyPart() {
        MultipartBody.Part part = HttpService.createPart("test", "test");
        assertNotNull("should create body part", part);
        assertNotNull("should have headers", part.headers());
        assertNotNull("should have body", part.body());
    }

    @Test
    public void shouldCreateRequestBodyPart() {
        RequestBody part = HttpService.createBodyPart("test");
        assertNotNull("should create body part", part);
    }

    @Test
    public void shouldCreateParts() throws IOException {
        HashMap<String, Object> params = new HashMap<String, Object>();
        File file = File.createTempFile("test_", ".png");
        params.put("test_file", file);
        params.put("test_value", "test");

        List<MultipartBody.Part> parts = HttpService.createParts(params);
        assertNotNull("should create parts", parts);
        assertNotNull("should have file part", parts.get(0));
        assertNotNull("should have body part", parts.get(1));
    }

    @Test
    public void shouldCreateFileParts() throws IOException {
        HashMap<String, Object> params = new HashMap<String, Object>();
        File file = File.createTempFile("test_", ".png");
        params.put("test", file);

        List<MultipartBody.Part> parts = HttpService.createFileParts(params);
        assertNotNull("should create parts", parts);
        assertNotNull("should have file part", parts.get(0));
    }

    @Test
    public void shouldCreateBodyParts() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("a", 17);
        params.put("b", "B");

        Map<String, RequestBody> parts = HttpService.createBodyParts(params);
        assertNotNull("should create body part", parts);
        assertEquals("should have same size", parts.size(), params.size());
    }
}
