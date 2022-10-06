package cn.edu.ruc.hola.utils.httpcient;

import cn.edu.ruc.hola.config.Host;
import cn.edu.ruc.hola.fake.FakeConfig;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * impl for http client5
 * <p>
 * <a href="https://www.wdbyte.com/tool/httpclient5.html#httpclient-5-post-%E8%AF%B7%E6%B1%82">...</a>
 */
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

    private URIBuilder getBseURI() {
        return new URIBuilder().setScheme("http").setHost(host)
                .setPort(port);
    }

    public Future<SimpleHttpResponse> filterDocAsync(String db_name, String filter) {
        if (isExpired()) auth();
        try (CloseableHttpAsyncClient httpclient = client) {
            httpclient.start();
            URI uri = getBseURI()
                    .appendPath(db_name)
                    .appendPath("_find")
                    .build();
            var simpleHttpRequest = SimpleRequestBuilder.post(uri).build();
            simpleHttpRequest.setBody(filter, ContentType.APPLICATION_JSON);
            return httpclient.execute(simpleHttpRequest, localContext, null);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public String filterDocs(String db_name, String filter) {
        client.start();
        if (isExpired()) auth();
        try {
            URI uri = getBseURI()
                    .appendPath(db_name)
                    .appendPath("_find")
                    .build();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(filter
                    , ContentType.APPLICATION_JSON));
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                try (CloseableHttpResponse response = httpclient.execute(httpPost, localContext)) {
                    HttpEntity entity = response.getEntity();
                    // 获取响应信息
                    return EntityUtils.toString(entity);
                } catch (org.apache.hc.core5.http.ParseException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    final HttpClientContext localContext = HttpClientContext.create();

    private long expiresTime = -1L;

    private boolean isExpired() {
        if (expiresTime == -1) return true;
        return expiresTime <= new Date().getTime();
    }

    public void auth() {
        HttpPost httpPost;
        final CookieStore cookieStore = new BasicCookieStore();
        localContext.setCookieStore(cookieStore);
        try {
            httpPost = new HttpPost(getBseURI().appendPath("_session").build());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", FakeConfig.name);
            jsonObject.addProperty("password", FakeConfig.password);
            httpPost.setEntity(new StringEntity(jsonObject.toString()
                    , ContentType.APPLICATION_JSON));
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                try (CloseableHttpResponse response = httpclient.execute(httpPost, localContext)) {
                    final List<Cookie> cookies = cookieStore.getCookies();
                    Cookie cookie = cookies.get(0);
                    expiresTime = sdf.parse(cookie.getAttribute("expires")).getTime();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * test impl for httpclient5
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var couchDbUtils = new CouchDbUtils("127.0.0.1", 5985);
        var query = """
                {
                  "selector": {
                    "data.countryid": {
                      "$gt": -1
                    }
                  },
                  "execution_stats": true
                }
                """;
        var ccc = couchDbUtils.filterDocAsync("fake0", query);
        System.out.println(ccc.get().getBodyText());
    }
}
