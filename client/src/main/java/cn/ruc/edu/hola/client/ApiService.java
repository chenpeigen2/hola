package cn.ruc.edu.hola.client;


import cn.ruc.edu.hola.client.config.Options;
import cn.ruc.edu.hola.client.entity.RealResponse;
import cn.ruc.edu.hola.client.entity.ChaincodeRequest;
import cn.ruc.edu.hola.client.impl.ApiServiceImpl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ApiService {

    protected String scheme;


    protected String host;

    protected int port;

    protected Map<String, String> headers = new HashMap<>() {{
        put("Accept", "application/json");
        put("Content-Type", "application/json");
    }};


    protected static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(Options.LEVEL))
            .build();

    protected HttpUrl.Builder getBaseUrl() {
        return new HttpUrl.Builder().scheme(scheme)
                .host(host).port(port);
    }

    public abstract Response filterDocs(String dataBaseName, String filter);

    public abstract RealResponse complex(List<ChaincodeRequest> chaincodeRequests);

    public abstract RealResponse direct_sgx(List<ChaincodeRequest> chaincodeRequests);
    public abstract RealResponse direct_complex(String body);

    public abstract List<RealResponse> multiComplex(List<List<ChaincodeRequest>> requests);

    public abstract String getAESKey();

    public void update(String scheme, String host, int port) {
        this.host = host;
        this.port = port;
        this.scheme = scheme;
    }

    public static ApiService getInstance() {
        return new ApiServiceImpl();
    }
}
