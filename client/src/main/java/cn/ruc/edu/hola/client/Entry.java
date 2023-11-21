package cn.ruc.edu.hola.client;

import cn.ruc.edu.hola.client.config.Options;
import cn.ruc.edu.hola.client.dataservice.DataServiceVerticle;
import cn.ruc.edu.hola.client.performance.PerformanceHandler;
import cn.ruc.edu.hola.client.web.PerformanceVerticle;
import cn.ruc.edu.hola.client.web.Verticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

import java.time.Duration;

import static cn.ruc.edu.hola.client.config.Options.Periodic_Millis;

/**
 * only for trigger the entry
 **/
public class Entry {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(Options.WorkerPoolSize));
        ConfigRetriever retriever = readYaml(vertx);
        retriever.getConfig(event -> {
            DeploymentOptions options = new DeploymentOptions().setConfig(event.result());
            vertx.deployVerticle(Verticle.class.getName(), options);
            vertx.deployVerticle(PerformanceVerticle.class.getName(), options);
            vertx.deployVerticle(DataServiceVerticle.class.getName(), options);
        });
        schedulePingPong(vertx);
    }

    private static ConfigRetriever readYaml(Vertx vertx) {
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setOptional(true)
                .setConfig(new JsonObject().put("path", "vertx-config.yaml"));
        return ConfigRetriever.create(vertx, new ConfigRetrieverOptions()
                .addStore(store));
    }

    private static void schedulePingPong(Vertx vertx) {
        vertx.setPeriodic(Duration.ofMillis(Periodic_Millis).toMillis(), PerformanceHandler.getInstance());
    }
}
