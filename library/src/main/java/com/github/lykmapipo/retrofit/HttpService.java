package com.github.lykmapipo.retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.lykmapipo.retrofit.interceptor.AuthInterceptor;
import com.github.lykmapipo.retrofit.interceptor.HeadersInterceptor;
import com.github.lykmapipo.retrofit.provider.AuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Sensible retrofit http service(s) creator
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @version 0.1.0
 * @since 0.1.0
 */
public class HttpService {
    /**
     * Valid instance of {@link Gson} for reuse across retrofit instances.
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
     * Valid instance of {@link GsonConverterFactory} for reuse across
     * retrofit instances.
     *
     * @since 0.1.0
     */
    private static final GsonConverterFactory gsonFactory =
            GsonConverterFactory.create(gson);

    /**
     * Valid instance of {@link okhttp3.OkHttpClient} for reuse across
     * retrofit instances.
     *
     * @since 0.1.0
     */
    private static final OkHttpClient httpClient = new OkHttpClient();

    /**
     * Valid instance of {@link retrofit2.Retrofit.Builder} for reuse across
     * retrofit instances.
     *
     * @since 0.1.0
     */
    private static final Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .addConverterFactory(gsonFactory);

    /**
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     *
     * @param service valid retrofit service definition
     * @param baseUrl valid service base url
     * @return an object of type S from the {@code service} creation
     */
    @NonNull
    public static <S> S create(
            final Class<S> service, final String baseUrl
    ) {
        // create provided service and return
        return create(service, baseUrl, null, null);
    }

    /**
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     *
     * @param service      valid retrofit service definition
     * @param baseUrl      valid service base url
     * @param authProvider valid authentication provider
     * @param headers      valid http headers to apply on every request
     * @return an object of type S from the {@code service} creation
     */
    @NonNull
    public static <S> S create(
            final Class<S> service, final String baseUrl,
            final AuthProvider authProvider, final Map<String, String> headers
    ) {
        // build http client with defaults
        OkHttpClient.Builder httpClientBuilder = httpClient.newBuilder();
        httpClientBuilder.addInterceptor(new HeadersInterceptor(headers));
        httpClientBuilder.addInterceptor(new AuthInterceptor(authProvider));
        OkHttpClient client = httpClientBuilder.build();

        // create retrofit client with defaults
        Retrofit retrofit =
                retrofitBuilder
                        .client(client)
                        .baseUrl(baseUrl)
                        .build();

        // create provided service and return
        return retrofit.create(service);
    }

    /**
     * Helper method to convert a generic object value to a json string
     *
     * @param value the object for which Json representation is to be created
     *              setting for Gson.
     * @return json representation of {@code value}.
     * @since 0.1.0
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
     * @param value valid json string value
     * @param type  valid type of the desired object
     * @return an object of type T from the string. Returns {@code null} if {@code value}
     * is null or if {@code value} is empty.
     * @since 0.1.0
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
