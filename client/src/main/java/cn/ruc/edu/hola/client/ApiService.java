package cn.ruc.edu.hola.client;


import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.HashMap;
import java.util.Map;

public abstract class ApiService {

    protected String scheme;


    protected String host;

    protected int port;

    protected Map<String, String> headers = new HashMap<>() {{
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
    }};


    protected static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build();

    protected HttpUrl.Builder getBaseUrl() {
        return new HttpUrl.Builder().scheme(scheme)
                .host(host).port(port);
    }

    public abstract Response filterDocs(String dataBaseName, String filter);

    public void update(String scheme, String host, int port) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
    }
}
