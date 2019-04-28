package com.github.lykmapipo.retrofit.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http authentication interceptor
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class AuthenticationBearerInterceptor implements Interceptor {

    private String token;

    public AuthenticationBearerInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        String bearer = "Bearer " + this.token;
        Request.Builder builder = original.newBuilder()
                .header("Authorization", bearer);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
