package com.github.lykmapipo.retrofit.interceptor;

import android.text.TextUtils;

import com.github.lykmapipo.retrofit.provider.AuthProvider;

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
public class AuthInterceptor implements Interceptor {

    private AuthProvider provider;

    public AuthInterceptor(AuthProvider authProvider) {
        this.provider = authProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder();

        // obtain auth credentials
        if (this.provider != null) {
            String scheme = this.provider.getScheme();
            String token = this.provider.getToken();

            // set auth headers
            if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(token)) {
                String credential = scheme + " " + token;
                builder.header("Authorization", credential);
            }
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
