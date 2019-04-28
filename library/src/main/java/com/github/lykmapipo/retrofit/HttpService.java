package com.github.lykmapipo.retrofit;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Service Retrofit HTTP Service creator
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class HttpService {
    /**
     * Valid instance of {@link Gson} for converting values to json and from json
     *
     * @since 0.1.0
     */
    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(
                    Modifier.FINAL, Modifier.TRANSIENT,
                    Modifier.STATIC
            )
            .excludeFieldsWithoutExposeAnnotation()
            .serializeNulls()
            .create();

    /**
     * Valid instance of {@link okhttp3.OkHttpClient.Builder}
     *
     * @since 0.1.0
     */
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    /**
     * Helper method to convert a generic object value to a json string
     *
     * @param value {@link java.lang.Object}
     * @return json {@link java.lang.String}
     */
    @Nullable
    public static synchronized <T> String toJson(@NonNull T value) {
        try {
            String json = gson.toJson(value);
            return json;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Helper method to convert a json string to generic object value.
     *
     * @param value {@link java.lang.String}
     * @param type  {@link java.lang.Class}
     * @return object {@link java.lang.Object}
     */
    @Nullable
    public static synchronized <T> T fromJson(@NonNull String value, @NonNull Class<T> type) {
        try {
            T val = gson.fromJson(value, type);
            return val;
        } catch (Exception e) {
            return null;
        }
    }
}
