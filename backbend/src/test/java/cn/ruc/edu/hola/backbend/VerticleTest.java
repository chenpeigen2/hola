package cn.ruc.edu.hola.backbend;

import cn.ruc.edu.hola.backbend.encrypy.EncryptUtils;
import cn.ruc.edu.hola.backbend.encrypy.dh.DHCoder;
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

    final static String AES_KEY = "0123456789abcdefghijklmn";

    @Test
    public void testAuth() {
        var url = getBaseUrlBuilder().addPathSegment("_auth")
                .addQueryParameter("key", EncryptUtils.rsaEncrypt(AES_KEY, EncryptUtils.RSA_PUBLIC_KEY))
                .build();
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
            complexQuery = "{\"selector\": {\"data.countryid\": {\"$gt\": -1}},\"execution_stats\": true}";
            complexQuery = EncryptUtils.encryptAES(complexQuery, AES_KEY);
            System.out.println("filter=" + complexQuery);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //                                         P9kMC8Zp/s 917GG7P0LCl1wlBMMmFFzqa2CNQeYsxsp/8xWCL6zkZlNVwB oZHClEt9wWFQ7TF8sOJCOM1tlX1BjYb XxtsztatwIzPJJk=
    // db_name=MjRpTSD5v4iCxUTzs+brYw==&filter=P9kMC8Zp/s+917GG7P0LCl1wlBMMmFFzqa2CNQeYsxsp/8xWCL6zkZlNVwB+oZHClEt9wWFQ7TF8sOJCOM1tlX1BjYb+XxtsztatwIzPJJk=
    // db_name=MjRpTSD5v4iCxUTzs+brYw==&filter=P9kMC8Zp/s+917GG7P0LCl1wlBMMmFFzqa2CNQeYsxsp/8xWCL6zkZlNVwB+oZHClEt9wWFQ7TF8sOJCOM1tlX1BjYb+XxtsztatwIzPJJk=
    // http://127.0.0.1:8080/_find?db_name=MjRpTSD5v4iCxUTzs+brYw==&filter=P9kMC8Zp/s+917GG7P0LCl1wlBMMmFFzqa2CNQeYsxsp/8xWCL6zkZlNVwB+oZHClEt9wWFQ7TF8sOJCOM1tlX1BjYb+XxtsztatwIzPJJk=
    String complex_db_name = "fake1";

    {
        try {
            complex_db_name = EncryptUtils.encryptAES(complex_db_name, AES_KEY);
            System.out.println("db_name=" + complex_db_name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testComplexQuery() {
        System.out.println("bbbbbbbbbbbbbbbb");
        System.out.println(complex_db_name);
        System.out.println(complexQuery);
        System.out.println("bbbbbbbbbbbbbbbbb");
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
            str = "MHWAz7lun/x9nQDf64ncsRN0Wp1EJ1v6sPqkSlMF43/jYyz4++ZOX/J0QfIR/WwvGE/roh12tcff6KvPrCf08ZFXb52pwMOQHM/YkeX0EIEPS6+PGGApU/h3QBO9g5+i4Tar9tZRhkVKE0oBqOoB5fPjF5PETPxRsrPyfNRSj0GQLfCcqslyDy6UPCrHQHZtUJjZzpc9EIcYqdhFNM5MCMI+1t7l4UDXRWE1m8xmc7QguCLHNaLRCOWkee7M/lhKeWqRbJTAOTx6EAnvlkb1BbKlGQC/c/MsUJJFcJ6Aa7DJAq6P6j1BTbckA2FwP7vwkr6pAAICFpvykmgUUpVvqDZd8xn/Dc8fLiAjcvlE516ty8yrofG/Buq3CSeS7Yl5NGrKCjNwgQCSfs+YvsKh1RqG/GPkjHG/CRGAfkYkIi9aCbzhybe58tO8qJGbhLBDX8OEbaZATnqZ5HDWKXRiUFkdnDZMzhJogb7Dv50rRT8RKaQTmUMJNuSmwgBy4u4E9IE+uL89qWZlxWZf9cXeG/5g54wvL4AGyt7zXGc35CLsLCUzoEAvn5b/t2nABy/XraMJYiMvh8Q7dMNm9SWj8DU2VUxuBeTx8HWOB6syCoSBOrheV8ZuRgEy1mSJHvd0gYNpIKzN8T+FpZHGiX2ALA==";
            str = "MHWAz7lun/x9nQDf64ncsRN0Wp1EJ1v6sPqkSlMF43/jYyz4++ZOX/J0QfIR/WwvGE/roh12tcff6KvPrCf08ZFXb52pwMOQHM/YkeX0EIEPS6+PGGApU/h3QBO9g5+i4Tar9tZRhkVKE0oBqOoB5fPjF5PETPxRsrPyfNRSj0GQLfCcqslyDy6UPCrHQHZtUJjZzpc9EIcYqdhFNM5MCMI+1t7l4UDXRWE1m8xmc7QguCLHNaLRCOWkee7M/lhKeWqRbJTAOTx6EAnvlkb1BbKlGQC/c/MsUJJFcJ6Aa7DJAq6P6j1BTbckA2FwP7vwkr6pAAICFpvykmgUUpVvqDZd8xn/Dc8fLiAjcvlE516ty8yrofG/Buq3CSeS7Yl5NGrKCjNwgQCSfs+YvsKh1RqG/GPkjHG/CRGAfkYkIi9aCbzhybe58tO8qJGbhLBDX8OEbaZATnqZ5HDWKXRiUFkdnDZMzhJogb7Dv50rRT8RKaQTmUMJNuSmwgBy4u4E9IE+uL89qWZlxWZf9cXeG/5g54wvL4AGyt7zXGc35CLsLCUzoEAvn5b/t2nABy/XraMJYiMvh8Q7dMNm9SWj8DU2VUxuBeTx8HWOB6syCoSBOrheV8ZuRgEy1mSJHvd0a+qCm9/YkHAOCtpxvli7rw==";
            System.out.println(EncryptUtils.decryptAES(str, AES_KEY));
            System.out.println("query decrypt end");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAuthAndComplexQuery() {
        testAuth();
        testComplexQuery();
    }

}
