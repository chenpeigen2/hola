package cn.ruc.edu.hola.client.performance;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;

import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Deprecated
public class BackgroundThreadVerticle extends AbstractVerticle {

    Set<Package> set = new HashSet<>();

    @Override
    public void start() {
        EventBus eb = vertx.eventBus();
        // aggregate the object
        eb.consumer("ping-address", message -> {
            Package cargo = (Package) message.body();
            set.add(cargo);
        });

        vertx.setPeriodic(Duration.ofMillis(5).toMillis(), v -> {
            Iterator<Package> iterator = set.iterator();
            while (iterator.hasNext()) {
                Package cargo = iterator.next();
                iterator.remove();
                eb.publish(cargo.thread_name, "Some news!");
            }
        });
    }
}
