package cn.ruc.edu.hola.index;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.time.Duration;

public class Entry {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        schedulePingPong(vertx);
        ConfigRetriever retriever = readYaml(vertx);
        retriever.getConfig(event -> {
            DeploymentOptions options = new DeploymentOptions().setConfig(event.result());
            vertx.deployVerticle(Verticle.class.getName(), options);
            vertx.deployVerticle(ServiceRegisterVerticel.class.getName(), options);
        });
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

    // schedule 1 minus delay to ping the backbend server
    private static void schedulePingPong(Vertx vertx) {
        vertx.setPeriodic(Duration.ofMinutes(1).toMillis(), event -> Ping.pong());
    }
}