package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.config.model.Host;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import okhttp3.Response;
import okhttp3.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * every couch db instance should have its own dbutils ,
 * just for design ,do not change .
 * if you are trying to change to static ,make sure Thread-safe, use ThreadLocal instead [not recommend !!!]
 */
public final class DbUtils {
    private static final ConcurrentHashMap<String, DbUtils> m1 = new ConcurrentHashMap<>();

    public static DbUtils checkDbUtils(Host h) {
        return m1.computeIfAbsent(h.getSid(), sid -> {
            DbUtils u = new DbUtils();
            u.setHOST(h.getHost());
            u.setPORT(h.getPort());
            return u;
        });
    }

    /**
     * may return null
     */
    public static DbUtils checkDbUtils(String sid) {
        return m1.get(sid);
    }

    public String Cookie;

    private static final OkHttpClient client;

    private final Map<String, String> m;

    private volatile boolean authed = false;

    @Setter
    @Getter
    private String HOST = "127.0.0.1";

    @Setter
    @Getter
    private int PORT = 5984;

    static {
        client = new OkHttpClient.Builder()
                .build();
    }

    {
        m = new HashMap<>() {{
            put("Accept", "application/json");
            put("Content-Type", "application/json");
        }};
    }

    private HttpUrl.Builder getBaseUrlBuilder() {
        return new HttpUrl.Builder().scheme("http")
                .host(HOST)
                .port(PORT);
    }

    @SneakyThrows
    public Response up() {
        var url = getBaseUrlBuilder()
                .addPathSegment("_up")
                .build();
        var r = new Request.Builder().url(url)
                .get()
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response uuids(int cnt) {
        var url = getBaseUrlBuilder()
                .addPathSegment("_uuids")
                .addQueryParameter("count", String.valueOf(cnt)).build();

        var r = new Request.Builder().url(url)
                .get()
                .build();
        return client.newCall(r).execute();
    }

    /**
     * auth
     */
    @SneakyThrows
    public void auth() {

//        // fix configs for the client
//        if (config != null && config.getTime_interval() != -1) {
//            client = client.newBuilder()
//                    .connectTimeout(config.getTime_interval(), TimeUnit.MILLISECONDS)
//                    .readTimeout(config.getTime_interval(), TimeUnit.MILLISECONDS)
//                    .writeTimeout(config.getTime_interval(), TimeUnit.MILLISECONDS)
//                    .build();
//        }

        RequestBody body = new FormBody.Builder()
                .add("name", "admin")
                .add("password", "123456").build();

        var url = getBaseUrlBuilder()
                .addPathSegment("_session")
                .build();

        var r = new Request.Builder().url(url)
                .headers(Headers.of(m))
                .method("POST", body)
                .build();
        try (var res = client.newCall(r).execute()) {
            var cookie_list = res.headers().values("Set-Cookie");
            Cookie = cookie_list.get(0);
            authed = true;
            m.put("Cookie", Cookie);
        }
    }

    @SneakyThrows
    public Response createDb(String db_name) {
        if (!authed) auth();
        var headers = Headers.of(m);
        RequestBody body = new FormBody.Builder().build();
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .build();
        var r = new Request.Builder().url(url).headers(headers)
                .method("PUT", body)
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response allDbs() {
        if (!authed) auth();
        var headers = Headers.of(m);
        var url = getBaseUrlBuilder()
                .addPathSegment("_all_dbs")
                .build();
        var r = new Request.Builder().url(url).headers(headers)
                .get()
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response dbsInfo(String data) {
        if (!authed) auth();
        var headers = Headers.of(m);
        RequestBody body = RequestBody.create(data, MediaType.get("application/json"));
        var url = getBaseUrlBuilder()
                .addPathSegment("_dbs_info")
                .build();
        var r = new Request.Builder().url(url).headers(headers)
                .get()
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response getDbInfo(String db_name) {
        if (!authed) auth();
        var headers = Headers.of(m);
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .build();
        var r = new Request.Builder().url(url).headers(headers).build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response deleteDb(String db_name) {
        if (!authed) auth();
        var headers = Headers.of(m);
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .build();
        var r = new Request.Builder().url(url).headers(headers)
                .delete()
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public <T> Response addDoc(String db_name, T data) {
        if (!authed) auth();
        var header = Headers.of(m);
        RequestBody body = null;
        if (data instanceof String s) {
            body = RequestBody.create(s, MediaType.get("application/json"));
        }
        if (data instanceof File f) {
            body = RequestBody.create(f, MediaType.get("application/json"));
        }
        if (body == null) throw new IllegalArgumentException("Type error");
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .method("POST", body)
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response addBulkDoc(String db_name, String data) {
        if (!authed) auth();
        var header = Headers.of(m);
        RequestBody body = RequestBody.create(data, MediaType.get("application/json"));
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .addPathSegment("_bulk_docs")
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .method("POST", body)
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response deleteDoc(String db_name, String docId) {
        if (!authed) auth();
        var header = Headers.of(m);
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .addPathSegment(docId)
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .delete()
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response allDocs(String db_name) {
        if (!authed) auth();
        var header = Headers.of(m);
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .addPathSegment("_all_docs")
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response allDocs(String db_name, String condition) {
        if (!authed) auth();
        var header = Headers.of(m);
        var body = RequestBody.create(condition, MediaType.get("application/json"));
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .addPathSegment("_all_docs")
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .method("POST", body)
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response findSpecDoc(String db_name, String docId) {
        if (!authed) auth();
        var header = Headers.of(m);
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .addPathSegment(docId)
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .get()
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response findBulkDoc(String db_name, String data) {
        if (!authed) auth();
        var header = Headers.of(m);
        var body = RequestBody.create(data, MediaType.get("application/json"));
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .addPathSegment("_bulk_get")
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .method("POST", body)
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response filterDocs(String db_name, String filter) {
        if (!authed) auth();
        var header = Headers.of(m);
        var body = RequestBody.create(filter, MediaType.get("application/json"));
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .addPathSegment("_find")
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .method("POST", body)
                .build();
        return client.newCall(r).execute();
    }

    @SneakyThrows
    public Response createIndex(String db_name, String info) {
        if (!authed) auth();
        var header = Headers.of(m);
        var body = RequestBody.create(info, MediaType.get("application/json"));
        var url = getBaseUrlBuilder()
                .addPathSegment(db_name)
                .addPathSegment("_index")
                .build();
        var r = new Request.Builder().url(url).headers(header)
                .method("POST", body)
                .build();
        return client.newCall(r).execute();
    }
}
