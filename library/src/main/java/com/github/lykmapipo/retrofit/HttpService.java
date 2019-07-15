package com.github.lykmapipo.retrofit;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.lykmapipo.retrofit.adapter.TaskCallAdapterFactory;
import com.github.lykmapipo.retrofit.interceptor.AuthInterceptor;
import com.github.lykmapipo.retrofit.interceptor.HeadersInterceptor;
import com.github.lykmapipo.retrofit.provider.AuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.PartMap;

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
                    .addCallAdapterFactory(TaskCallAdapterFactory.create())
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
     * @param service   valid retrofit service definition
     * @param baseUrl   valid service base url
     * @param authToken valid api authentication token(key)
     * @return an object of type S from the {@code service} creation
     */
    @NonNull
    public static <S> S create(
            final Class<S> service, final String baseUrl,
            final String authToken
    ) {
        // create provided service and return
        AuthProvider authProvider = new AuthProvider() {
            @Override
            public String getToken() {
                return authToken;
            }
        };
        return create(service, baseUrl, authProvider, null);
    }

    /**
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     *
     * @param service      valid retrofit service definition
     * @param baseUrl      valid service base url
     * @param authProvider valid authentication provider
     * @return an object of type S from the {@code service} creation
     */
    @NonNull
    public static <S> S create(
            final Class<S> service, final String baseUrl,
            final AuthProvider authProvider
    ) {
        return create(service, baseUrl, authProvider, null);
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
        //TODO use Provider(auth, headers, baseUrl)

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
     * Valid instance of {@link Gson} for reuse.
     *
     * @since 0.1.0
     */
    @NonNull
    public static Gson getGson() {
        return gson;
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

    /**
     * Helper method to convert map of object values to map of request body.
     *
     * @param params {@link Map}
     * @return {@link retrofit2.http.PartMap}
     * @since 0.2.0
     */
    public static synchronized Map<String, RequestBody> toPartMap(@NonNull Map<String, Object> params) {
        // initialize party map for request body
        HashMap<String, RequestBody> partMap = new HashMap<String, RequestBody>();
        // collect request body parts
        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                boolean isAllowedPart =
                        !TextUtils.isEmpty(key) && value != null && !(value instanceof File);
                if (isAllowedPart) {
                    RequestBody part = RequestBody.create(String.valueOf(value), MultipartBody.FORM);
                    partMap.put(key, part);
                }
            }
        }
        // return part map request body
        return partMap;
    }
}
