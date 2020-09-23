package com.github.lykmapipo.retrofit;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.github.lykmapipo.common.Common;
import com.github.lykmapipo.retrofit.adapter.TaskCallAdapterFactory;
import com.github.lykmapipo.retrofit.interceptor.AuthInterceptor;
import com.github.lykmapipo.retrofit.interceptor.HeadersInterceptor;
import com.github.lykmapipo.retrofit.provider.AuthProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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
     * Valid instance of {@link GsonConverterFactory} for reuse across
     * retrofit instances.
     *
     * @since 0.1.0
     */
    private static final GsonConverterFactory gsonFactory =
            GsonConverterFactory.create(Common.gson());

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
        return create(service, baseUrl, null, null, null);
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
        return create(service, baseUrl, authToken, null);
    }

    /**
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     *
     * @param service   valid retrofit service definition
     * @param baseUrl   valid service base url
     * @param authToken valid api authentication token(key)
     * @param timeout   valid request(connect, read, write) timeout (in seconds). The default is 10 seconds.
     * @return an object of type S from the {@code service} creation
     */
    @NonNull
    public static <S> S create(
            final Class<S> service, final String baseUrl,
            final String authToken, final Long timeout
    ) {
        // create provided service and return
        AuthProvider authProvider = new AuthProvider() {
            @Override
            public String getToken() {
                return authToken;
            }
        };
        return create(service, baseUrl, authProvider, null, timeout);
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
     * @param timeout      valid request(connect, read, write) timeout (in seconds). The default is 10 seconds.
     * @return an object of type S from the {@code service} creation
     */
    @NonNull
    public static <S> S create(
            final Class<S> service, final String baseUrl,
            final AuthProvider authProvider,
            final Long timeout
    ) {
        // create provided service and return
        return create(
                service, baseUrl,
                authProvider, null,
                timeout
        );
    }

    /**
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     *
     * @param service      valid retrofit service definition
     * @param baseUrl      valid service base url
     * @param authProvider valid authentication provider
     * @param headers      valid http headers to apply on every request
     * @param timeout      valid request(connect, read, write) timeout (in seconds). The default is 10 seconds.
     * @return an object of type S from the {@code service} creation
     */
    @NonNull
    public static <S> S create(
            final Class<S> service, final String baseUrl,
            final AuthProvider authProvider, final Map<String, String> headers,
            final Long timeout
    ) {
        // create provided service and return
        return create(
                service, baseUrl,
                authProvider, headers,
                timeout, timeout, timeout
        );
    }

    /**
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     *
     * @param service        valid retrofit service definition
     * @param baseUrl        valid service base url
     * @param authProvider   valid authentication provider
     * @param headers        valid http headers to apply on every request
     * @param connectTimeout valid connect timeout (in seconds). The default is 10 seconds.
     * @param readTimeout    valid read timeout (in seconds). The default is 10 seconds.
     * @param writeTimeout   valid write timeout (in seconds). The default is 10 seconds.
     * @return an object of type S from the {@code service} creation
     */
    @NonNull
    public static <S> S create(
            final Class<S> service, final String baseUrl,
            final AuthProvider authProvider, final Map<String, String> headers,
            final Long connectTimeout, final Long readTimeout, final Long writeTimeout
    ) {
        //TODO use Provider(auth, headers, baseUrl)

        // build http client with defaults
        OkHttpClient.Builder httpClientBuilder = httpClient.newBuilder();

        // apply timeouts
        httpClientBuilder.connectTimeout(connectTimeout != null ? connectTimeout : 10, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(readTimeout != null ? readTimeout : 10, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(writeTimeout != null ? writeTimeout : 10, TimeUnit.SECONDS);

        // add common interceptors
        httpClientBuilder.addInterceptor(new HeadersInterceptor(headers));
        httpClientBuilder.addInterceptor(new AuthInterceptor(authProvider));

        // build client
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
     * Helper method to convert map of object values to map of request body.
     *
     * @param params {@link Map}
     * @return {@link retrofit2.http.PartMap}
     * @since 0.6.0
     */
    public static synchronized Map<String, RequestBody> createBodyParts(@NonNull Map<String, Object> params) {
        // initialize part map for request body
        HashMap<String, RequestBody> bodyParts = new HashMap<String, RequestBody>();
        // collect request body parts
        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                boolean isAllowedPart =
                        !TextUtils.isEmpty(key) && value != null && !(value instanceof File);
                if (isAllowedPart) {
                    RequestBody part = createBodyPart(String.valueOf(value));
                    bodyParts.put(key, part);
                }
            }
        }
        // return part map request body
        return bodyParts;
    }

    /**
     * Helper method to convert map of files to list of multipart request body.
     *
     * @param files {@link Map}
     * @return {@link okhttp3.MultipartBody.Part}
     * @since 0.6.0
     */
    public static synchronized List<MultipartBody.Part> createFileParts(
            @NonNull Map<String, Object> files
    ) {
        // initialize multipart list
        List<MultipartBody.Part> parts = new ArrayList<MultipartBody.Part>();
        // collect request body parts
        if (!files.isEmpty()) {
            for (String key : files.keySet()) {
                Object value = files.get(key);
                boolean isAllowedPart =
                        !TextUtils.isEmpty(key) && (value instanceof File);
                if (isAllowedPart) {
                    File file = (File) value;
                    MultipartBody.Part part = createFilePart(key, file);
                    parts.add(part);
                }
            }
        }
        // return multipart list
        return parts;
    }

    /**
     * Helper method to convert map of values to list of multipart parts.
     *
     * @param params {@link Map}
     * @return {@link okhttp3.MultipartBody.Part}
     * @since 0.6.0
     */
    public static synchronized List<MultipartBody.Part> createParts(
            @NonNull Map<String, Object> params
    ) {
        // initialize multipart list
        List<MultipartBody.Part> parts = new ArrayList<MultipartBody.Part>();
        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                boolean isAllowedPart =
                        !TextUtils.isEmpty(key) && value != null;
                if (isAllowedPart) {
                    MultipartBody.Part part = createPart(key, value);
                    parts.add(part);
                }
            }

        }
        // return multipart list
        return parts;
    }

    /**
     * Create {@link okhttp3.RequestBody} from given name and value
     *
     * @param value
     * @return
     * @since 0.6.0
     */
    public static synchronized RequestBody createBodyPart(
            @NonNull String value
    ) {
        RequestBody part = RequestBody.create(value, MultipartBody.FORM);
        return part;
    }

    /**
     * Create {@link okhttp3.MultipartBody.Part} for a given name and value
     *
     * @param name
     * @param value
     * @return
     * @since 0.6.0
     */
    public static synchronized MultipartBody.Part createPart(
            @NonNull String name, @NonNull Object value
    ) {
        if (value instanceof File) {
            return createFilePart(name, (File) value);
        } else {
            return createBodyPart(name, value);
        }
    }

    /**
     * Create {@link MultipartBody.Part} from given name and value
     *
     * @param name
     * @param value
     * @return
     * @since 0.6.0
     */
    public static synchronized MultipartBody.Part createBodyPart(
            @NonNull String name, @NonNull Object value
    ) {
        MultipartBody.Part part =
                MultipartBody.Part.createFormData(name, String.valueOf(value));
        return part;
    }

    /**
     * Create {@link okhttp3.MultipartBody.Part} for a given {@link File}
     *
     * @param name
     * @param file
     * @return {@link okhttp3.MultipartBody.Part}
     * @since 0.6.0
     */
    @NonNull
    public static synchronized MultipartBody.Part createFilePart(
            @NonNull String name, @NonNull File file
    ) {
        // create file RequestBody
        String mimeType = mimeTypeFor(file);
        MediaType mediaType = MediaType.parse(mimeType);
        RequestBody bodyFile = RequestBody.create(file, mediaType);

        // create MultipartBody.Part to send actual file name
        MultipartBody.Part part =
                MultipartBody.Part.createFormData(name, file.getName(), bodyFile);
        return part;
    }

    /**
     * Obtain MIME type for the given file.
     *
     * @return
     * @since 0.6.0
     */
    public static synchronized String mimeTypeFor(@NonNull File file) {
        try {
            String extension = extensionOf(file.getName()).substring(1);
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
            if (TextUtils.isEmpty(mimeType)) {
                return "application/octet-stream";
            }
            return mimeType;
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     * @since 0.6.0
     */
    @NonNull
    public static synchronized String extensionOf(@NonNull String uri) {
        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }
}
