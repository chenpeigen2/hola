package cn.ruc.edu.hola.client.performance;


import cn.ruc.edu.hola.client.ApiService;
import cn.ruc.edu.hola.client.entity.ChaincodeRequest;
import cn.ruc.edu.hola.client.entity.RealResponse;
import com.google.common.base.Stopwatch;
import io.vertx.core.Handler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public final class PerformanceHandler implements Handler<Long> {

    private final ApiService api = ApiService.getInstance();

    private PerformanceHandler() {}

    private final Queue<Callback> queue = new ArrayDeque<>();

    private static final Handler<Long> handler = new PerformanceHandler();

    public static Handler<Long> getInstance() {
        return handler;
    }

    public synchronized void offer(Callback obj) {
        queue.offer(obj);
    }

    @Override
    public void handle(Long event) {
        Stopwatch watch = Stopwatch.createStarted();

        int size = queue.size();
        final List<Callback> callbacks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            callbacks.add(queue.poll());
        }
        final List<List<ChaincodeRequest>> requests = new ArrayList<>();
        callbacks.forEach(callback -> {
            if (callback.chaincodeRequests != null) {
                requests.add(callback.chaincodeRequests);
            }
        });

        if (!callbacks.isEmpty()) {
            if (requests.isEmpty()) {
                callbacks.forEach(callback -> callback.onFinish(Thread.currentThread().getName() + System.currentTimeMillis()));
                return;
            }
            // random spec uuid
            api.update(SGXEntity.scheme, SGXEntity.host, SGXEntity.port); // select associate
            List<RealResponse> responseList = api.multiComplex(requests);
            int sz = callbacks.size();
            for (int i = 0; i < sz; i++) {
                String specText = responseList.get(i).getDecryptResponse();
                callbacks.get(i).onFinish(specText);
            }
        }


        watch.stop();
        long time = watch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("代码执行时长-client：" + time);

    }
}
