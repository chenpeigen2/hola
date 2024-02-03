package cn.ruc.edu.hola.backbend;


import cn.ruc.edu.hola.backbend.server.CouchVerticle;
import cn.ruc.edu.hola.backbend.server.CouchVerticle1;
import cn.ruc.edu.hola.backbend.server.CouchVerticle2;
import cn.ruc.edu.hola.backbend.server.ext.*;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public final class Entry {

    public static void main(String[] args) {
        System.getProperties().setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true");
        Vertx vertx = Vertx.vertx();
        ConfigRetriever retriever = readYaml(vertx);
        retriever.getConfig(event -> {
            DeploymentOptions options = new DeploymentOptions().setConfig(event.result());
//            vertx.deployVerticle(CouchVerticle.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle1.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle2.class.getName(), options);

            vertx.deployVerticle(CouchVerticle3.class.getName(), options);
            vertx.deployVerticle(CouchVerticle4.class.getName(), options);
            vertx.deployVerticle(CouchVerticle5.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle6.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle7.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle8.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle9.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle10.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle11.class.getName(), options);
//            vertx.deployVerticle(CouchVerticle12.class.getName(), options);
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
}
