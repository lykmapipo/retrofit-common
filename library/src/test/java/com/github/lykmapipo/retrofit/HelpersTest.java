package com.github.lykmapipo.retrofit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * HttpService Tests
 *
 * @author lally elias
 */

@RunWith(RobolectricTestRunner.class)
public class HelpersTest {
    @Test
    public void shouldConvertValueToPartMap() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("a", 17);
        params.put("b", "B");

        Map<String, RequestBody> partMap = HttpService.toStringParts(params);
        assertNotNull("should convert to part map", partMap);
        assertTrue("should convert to part map", !partMap.isEmpty());
        assertEquals("should have same size", partMap.size(), params.size());
    }
}
