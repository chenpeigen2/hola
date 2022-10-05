package cn.edu.ruc.hola.utils.httpcient;

import cn.edu.ruc.hola.config.Host;
import cn.edu.ruc.hola.fake.FakeConfig;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * impl for okhttp client5
 *
 * https://www.wdbyte.com/tool/httpclient5.html#httpclient-5-post-%E8%AF%B7%E6%B1%82
 */
@Deprecated
public final class CouchDbUtils {

    private final String host;

    private final int port;

    public CouchDbUtils(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private static final CloseableHttpAsyncClient client;

    static {
        PoolingAsyncClientConnectionManager paccm = new PoolingAsyncClientConnectionManager();
        paccm.setMaxTotal(10000);
        paccm.setDefaultMaxPerRoute(400);
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setSoTimeout(Timeout.ofSeconds(20)).build();// 设置超时20秒
        client = HttpAsyncClients.custom().setIOReactorConfig(ioReactorConfig).setConnectionManager(paccm).setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).build();
    }

    private static final ConcurrentHashMap<String, CouchDbUtils> dbs = new ConcurrentHashMap<>();

    public static CouchDbUtils checkDbUtils(Host host) {
        return dbs.computeIfAbsent(host.getSid(), s -> new CouchDbUtils(host.getHost(), host.getPort()));
    }

    private static final DateFormat sdf = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);

    static {
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * init the cookie
     **/
    private String mCookie = null;

    private Map<String, String> m = new HashMap<>();

    {
        m.put("Accept", "application/json");
        m.put("Content-Type", "application/json");
    }

    private URIBuilder getBseURI() {
        return new URIBuilder().setScheme("http").setHost(host)
                .setPort(port);
    }

    private volatile boolean isAuthed = false;


    private void auth() {
        client.start();
        try {
            URI uri = getBseURI().appendPath("_session").build();
            HttpPost httpPost = new HttpPost(uri);
            List<NameValuePair> parms = new ArrayList<>();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", FakeConfig.name);
            jsonObject.addProperty("password", FakeConfig.password);
            StringEntity entity = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
            parms.add(new BasicNameValuePair("name", FakeConfig.name));
            parms.add(new BasicNameValuePair("password", FakeConfig.password));
            httpPost.setEntity(entity);

            Future<SimpleHttpResponse> auth = client.execute(SimpleRequestBuilder.copy(httpPost).build(), new FutureCallback<SimpleHttpResponse>() {
                @Override
                public void completed(SimpleHttpResponse result) {

                }

                @Override
                public void failed(Exception ex) {

                }

                @Override
                public void cancelled() {

                }
            });

            SimpleHttpResponse response = auth.get();

            Header[] cookieList = response.getHeaders("Set-Cookie");
            Header cookie = cookieList[0];
            if (cookie != null) {
                String str = cookie.toString();
                mCookie = str;
                m.put("Cookie", str);
            }

        } catch (URISyntaxException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isExpired() {
        return false;
    }

    public Future<SimpleHttpResponse> filterDocs(String db_name, String filter) {
        client.start();
        if (!isAuthed) auth();
        try {
            URI uri = getBseURI()
                    .appendPath(db_name)
                    .appendPath("_find")
                    .build();
            HttpPost httpPost = new HttpPost(uri);
            m.forEach(httpPost::setHeader);
            ByteArrayEntity entity;
            entity = new ByteArrayEntity(filter.getBytes(StandardCharsets.UTF_8), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            return client.execute(SimpleRequestBuilder.copy(httpPost).build(), new FutureCallback<SimpleHttpResponse>() {
                @Override
                public void completed(SimpleHttpResponse result) {

                }

                @Override
                public void failed(Exception ex) {
                }

                @Override
                public void cancelled() {

                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
