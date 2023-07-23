package org.hyperledger.fabric.samples.querytransfer;


import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.hyperledger.fabric.samples.querytransfer.impl.ApiServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public abstract class ApiService {

    private String scheme;

    private String host;

    private int port;

    protected final Map<String, String> headers = new HashMap<>() {{
        put("Accept", "application/json");
        put("Content-Type", "application/json");
    }};


    protected static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build();

    protected HttpUrl.Builder getBaseUrl() {
        return new HttpUrl.Builder().scheme(scheme)
                .host(host).port(port);
    }

    public abstract Response complex(String scheme, String host, int port, String text);

    public void update(final String scheme, final String host, final int port) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
    }

    public static ApiService getInstance() {
        return new ApiServiceImpl();
    }
}
