package cn.ruc.edu.hola.client;

import cn.ruc.edu.hola.client.entity.ChaincodeRequest;
import cn.ruc.edu.hola.client.performance.*;
import cn.ruc.edu.hola.client.utils.RandomStringGenerator;
import cn.ruc.edu.hola.client.web.PerformanceVerticle;
import cn.ruc.edu.hola.client.web.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * only for trigger the entry
 **/
public class Entry {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(Options.WorkerPoolSize));
        vertx.deployVerticle(Verticle.class.getName());
        vertx.deployVerticle(PerformanceVerticle.class.getName());
        /*vertx.deployVerticle(BackgroundThreadVerticle.class.getName());
        vertx.eventBus().registerDefaultCodec(Package.class, new PackageCodec());
        BackgroudThread.INSTANCE.start();*/
        schedulePingPong(vertx);
    }

    private static void schedulePingPong(Vertx vertx) {
        vertx.setPeriodic(Duration.ofSeconds(1).toMillis(), PerformanceHandler.getInstance());
    }

    @Deprecated
    public static void triggerClient() {
        ApiService apiService = ApiService.getInstance();
        apiService.update("http", "127.0.0.1", 8888);
        List<ChaincodeRequest> chaincodeRequests = new ArrayList<>();
        chaincodeRequests.add(new ChaincodeRequest(RandomStringGenerator.UID, Action.query));
        var text = apiService.complex(chaincodeRequests).getResponse();
        System.out.println(text);

    }

    @Deprecated
    public interface Action {
        String query = """
                {
                  "selector": {
                    "$or": [
                      {
                        "data.countryid": {
                          "$gt": -1
                        }
                      },
                      {
                        "message": "success"
                      }
                    ],
                    "data.country": "China"
                  }
                }""";
    }
}
