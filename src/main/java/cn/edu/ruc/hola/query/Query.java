package cn.edu.ruc.hola.query;

import cn.edu.ruc.hola.config.Config;
import cn.edu.ruc.hola.config.ServerNode;
import cn.edu.ruc.hola.entity.Node;
import cn.edu.ruc.hola.service.AbsService;
import cn.edu.ruc.hola.service.Service;
import cn.edu.ruc.hola.service.ServiceHelper;
import cn.edu.ruc.hola.service.ServiceType;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * query layer call the service
 *
 * @param config the config
 */
public record Query(@NonNull Config config) implements Serviceable, Queryable {
    private static final Logger logger = Logger.getLogger("Query");

    /**
     * the service
     */
    private static final ThreadLocal<Service> service;

    static {
        service = new ThreadLocal<>();
    }

    /**
     * temporary define the thread counts
     */
    private static final int DEFAULT_LEN = 12;

    @Override
    public List<Node> query(String condition, List<ServerNode> sids) {
        List<Node> query = new CopyOnWriteArrayList<>();
        executeAsync(query, condition, sids);
        return query;
    }

    @SneakyThrows
    private void executeAsync(List<Node> query, String condition, List<ServerNode> serverNodes) {
        ExecutorService pool = Executors.newFixedThreadPool(DEFAULT_LEN);
        final CountDownLatch latch = new CountDownLatch(serverNodes.size());
        serverNodes.forEach(serverNode -> pool.submit(() -> {
            loadService(serverNode.getSid());
            Node node = service.get().query(condition, serverNode.getDatabaseName());
            if (node != null) {
                logger.info(node.toString());
                query.add(node);
            }
            latch.countDown();
        }));
        latch.await();
        if (!pool.isShutdown()) pool.shutdown();
    }

    /**
     * load the service we are calling
     */
    public synchronized void loadService(String sid) {
        if (!isNull() && checkSid(sid)) return;
        service.set(createService(sid));
    }

    /**
     * @return true if the service ,false service is not null
     */
    private synchronized boolean isNull() {
        return service.get() == null;
    }

    /**
     * @param sid service id
     * @return if thread hold the same return true,else false
     */
    private boolean checkSid(String sid) {
        if (service.get() == null) return false;
        AbsService s = (AbsService) service.get();
        return s.getSid().equals(sid);
    }

    /**
     * create the given service according to the service id
     *
     * @param sid service id
     * @return the service
     */
    @Override
    public Service createService(String sid) {
        return ServiceHelper.INSTANCE.createService(sid, ServiceType.TYPE_COUCHDB);
    }
}