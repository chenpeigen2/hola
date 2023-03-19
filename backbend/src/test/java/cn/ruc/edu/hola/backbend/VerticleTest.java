package cn.ruc.edu.hola.backbend;

import cn.ruc.edu.hola.backbend.dh.DHCoder;
import cn.ruc.edu.hola.backbend.encrypy.Keys;
import cn.ruc.edu.hola.backbend.encrypy.RSAUtils;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class VerticleTest {

    String host = "127.0.0.1";

    int port = 8080;

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build();

    private HttpUrl.Builder getBaseUrlBuilder() {
        return new HttpUrl.Builder().scheme("http")
                .host(host)
                .port(port);
    }

    @Test
    public void testUUID() {
        var url = getBaseUrlBuilder().addPathSegment("_uuids")
                .addQueryParameter("count", new String(String.valueOf(10))).build();
        var r = new Request.Builder().url(url).get().build();
        try {
            var res = client.newCall(r).execute();
            System.out.println(res.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String db_name;

    {
        try {
            db_name = RSAUtils.encrypt("hello_world".getBytes(), Keys.SELF_PUBLIC);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateDB() {
        var url = getBaseUrlBuilder().addPathSegment("create_db")
                .addQueryParameter("name", db_name).build();
        var r = new Request.Builder().url(url).get().build();
        try {
            var res = client.newCall(r).execute();
            System.out.println(res.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void before() {
        System.getProperties().setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true");
    }

    @Test
    public void testDeleteDB() {
        var url = getBaseUrlBuilder().addPathSegment("delete_db")
                .addQueryParameter("name", db_name).build();
        var r = new Request.Builder().url(url).get().build();
        try {
            var res = client.newCall(r).execute();
            System.out.println(res.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String complexQuery = """
            {
            "selector": {
                    "data.countryid": {"$gt": -1}
                },
                "execution_stats": true
            }
            """;

    {
        try {
            complexQuery = DHCoder.encrypt(complexQuery, cn.ruc.edu.hola.backbend.dh.Keys.PUB, cn.ruc.edu.hola.backbend.dh.Keys.SELF_PRIV);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String complex_db_name = "fake1";

    {
        try {
            complex_db_name = DHCoder.encrypt(complex_db_name, cn.ruc.edu.hola.backbend.dh.Keys.PUB, cn.ruc.edu.hola.backbend.dh.Keys.SELF_PRIV);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testComplexQuery() {
        var url = getBaseUrlBuilder().addPathSegment("_find")
                .addQueryParameter("db_name", complex_db_name)
                .addQueryParameter("filter", complexQuery)
                .build();
        var r = new Request.Builder().url(url).get().build();
        try {
            var res = client.newCall(r).execute();
            var str = res.body().string();
            System.out.println("query start");
            System.out.println(str);
            System.out.println("query end");
            System.out.println("query decrypt start");
            System.out.println(DHCoder.decrypt(str, cn.ruc.edu.hola.backbend.dh.Keys.PUB, cn.ruc.edu.hola.backbend.dh.Keys.SELF_PRIV));
            System.out.println("query decrypt end");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
