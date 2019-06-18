package com.github.lykmapipo.retrofit.interceptor;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Default Http providedHeaders interceptor
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class HeadersInterceptor implements Interceptor {

    private Map<String, String> providedHeaders;

    public HeadersInterceptor(@Nullable Map<String, String> providedHeaders) {
        this.providedHeaders = providedHeaders;
    }

    public HeadersInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // prepare default providedHeaders
        Map<String, String> defaultHeaders = new HashMap<String, String>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Accept", "application/json");

        // merge custom providedHeaders
        if (providedHeaders != null && !providedHeaders.isEmpty()) {
            defaultHeaders.putAll(providedHeaders);
        }

        // add headers
        Request.Builder builder = original.newBuilder();
        for (String headerKey : defaultHeaders.keySet()) {
            String headerValue = defaultHeaders.get(headerKey);
            boolean shouldSetHeader = TextUtils.isEmpty(original.header(headerKey)) && !TextUtils.isEmpty(headerKey) && !TextUtils.isEmpty(headerValue);
            if (shouldSetHeader) {
                original.header(headerKey);
                builder.header(headerKey, headerValue);
            }
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
