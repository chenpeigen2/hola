package cn.edu.ruc.hola.query;

import cn.edu.ruc.hola.config.Config;
import cn.edu.ruc.hola.entity.Node;
import lombok.NonNull;
import lombok.SneakyThrows;
import cn.edu.ruc.hola.service.AbsService;
import cn.edu.ruc.hola.service.Service;
import cn.edu.ruc.hola.service.ServiceHelper;
import cn.edu.ruc.hola.service.ServiceType;

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

    /**
     * @param condition condition the query use
     * @return we find all the data,and prepare the partition
     */
    @Override
    @SneakyThrows
    public List<Node> query(String condition, List<String> sids) {
        List<Node> query = new CopyOnWriteArrayList<>();
        executeAsync(query, condition, sids);
        return query;
    }

    @Override
    public List<Node> query(String condition, List<String> sids, String db_name) {
        List<Node> query = new CopyOnWriteArrayList<>();
        executeAsync(query, condition, sids, db_name);
        return query;
    }

    @Override
    public List<Node> queries(String condition, List<String> sids, List<String> db_names) {
        List<Node> query = new CopyOnWriteArrayList<>();
        db_names.forEach(db_name -> {
            List<Node> nodes = query(condition, sids, db_name);
            query.addAll(nodes);
        });
        return query;
    }

    /**
     * query sync with single-thread
     *
     * @param query     execute result data holder
     * @param condition query condition
     * @param sids      the service-ids
     */
    private void executeSync(List<Node> query, String condition, List<String> sids) {
        for (String sid : sids) {
            Node n = createService(sid).query(condition);
            System.out.println(n);
            query.add(n);
        }
    }

    private void executeSync(List<Node> query, String condition, List<String> sids, String db_name) {
        for (String sid : sids) {
            Node n = createService(sid).query(condition, db_name);
            System.out.println(n);
            query.add(n);
        }
    }

    /**
     * query async with multi-thread
     *
     * @param query     execute result data holder
     * @param condition query condition
     * @param sids      the service-ids
     */
    @SneakyThrows
    private void executeAsync(List<Node> query, String condition, List<String> sids) {
        ExecutorService pool = Executors.newFixedThreadPool(DEFAULT_LEN);
        final CountDownLatch latch = new CountDownLatch(sids.size());
        sids.forEach(sid -> pool.submit(() -> {
            loadService(sid);
            Node node = service.get().query(condition);
            if (node != null) {
                logger.info(node.toString());
                query.add(node);
            }
            latch.countDown();
        }));
        latch.await();
        if (!pool.isShutdown()) pool.shutdown();
    }

    @SneakyThrows
    private void executeAsync(List<Node> query, String condition, List<String> sids, String db_name) {
        ExecutorService pool = Executors.newFixedThreadPool(DEFAULT_LEN);
        final CountDownLatch latch = new CountDownLatch(sids.size());
        sids.forEach(sid -> pool.submit(() -> {
            loadService(sid);
            Node node = service.get().query(condition, db_name);
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
        return ServiceHelper.createService(sid, ServiceType.TYPE_COUCHDB);
    }
}
