package cn.ruc.edu;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;


public class Connect {

    // 连接池初始化
    private static CloseableHttpAsyncClient client = null;
    static {
        PoolingAsyncClientConnectionManager paccm = new PoolingAsyncClientConnectionManager();
        paccm.setMaxTotal(10000);
        paccm.setDefaultMaxPerRoute(400);
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setSoTimeout(Timeout.ofSeconds(20)).build();// 设置超时20秒
        client = HttpAsyncClients.custom().setIOReactorConfig(ioReactorConfig).setConnectionManager(paccm).setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).build();
    }



    // get方法
    public static Future<SimpleHttpResponse> doGet(String url)
            throws InterruptedException, ExecutionException {

        client.start();
        SimpleHttpRequest request = SimpleHttpRequest.create("GET", url);// 创建一个get请求

        Future<SimpleHttpResponse> future = client.execute(request, new FutureCallback<SimpleHttpResponse>() {

            @Override
            public void completed(final SimpleHttpResponse response) {
            }

            @Override
            public void failed(final Exception ex) {
                System.out.println(request + "->" + ex);
            }

            @Override
            public void cancelled() {
            }

        });
        return future;
    }

    // post方法
    public static Future<SimpleHttpResponse> doPost(String url,String json_data)
            throws InterruptedException, ExecutionException {

        client.start();
        SimpleHttpRequest request = SimpleHttpRequest.create("POST", url);

        request.setBody(json_data, ContentType.APPLICATION_JSON);

        Future<SimpleHttpResponse> future = client.execute(request, new FutureCallback<SimpleHttpResponse>() {

            @Override
            public void completed(final SimpleHttpResponse response) {
            }

            @Override
            public void failed(final Exception ex) {
                System.out.println(request + "->" + ex);
            }

            @Override
            public void cancelled() {
            }

        });
        return future;
    }

}
