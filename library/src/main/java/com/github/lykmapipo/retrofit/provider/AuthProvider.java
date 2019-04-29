package com.github.lykmapipo.retrofit.provider;

/**
 * Http authentication interceptor
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class AuthProvider {
    public String getScheme() {
        return "Bearer";
    }

    public abstract String getToken();
}
