package cn.edu.ruc.hola.service.extension.couchdb;

import com.google.gson.Gson;
import cn.edu.ruc.hola.config.model.Host;
import cn.edu.ruc.hola.entity.Node;
import kotlin.io.FilesKt;
import kotlin.text.Charsets;
import cn.edu.ruc.hola.service.extension.couchdb.models.AllDocs;
import cn.edu.ruc.hola.service.extension.couchdb.models.Doc;
import cn.edu.ruc.hola.service.extension.couchdb.models.FilterDocs;
import cn.edu.ruc.hola.utils.DbUtils;
import cn.edu.ruc.hola.utils.JsonUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * extension for couch db , for add a default
 * if you are watch this ,it's better for u to make a file like this
 * <p>
 * for further optimize !
 */
public class CouchDbExtension {

    private static final Logger logger = Logger.getLogger("CouchDbExtension");

    private static final Gson gson = JsonUtils.getGson();

    private static final int DEFAULT_THREAD_POOL_SIZE = 12;

    private final DbUtils dbUtils;

    /**
     * only for test
     */
    private static final String db_name = "fake";

    @Deprecated
    public CouchDbExtension(String host, int PORT) {
        dbUtils = new DbUtils();
        dbUtils.setHOST(host);
        dbUtils.setPORT(PORT);
    }

    public CouchDbExtension(Host h) {
        dbUtils = DbUtils.checkDbUtils(h);
    }

    /**
     * {
     * "ok": true
     * }
     */
    public void createDb() {
        try (var res = dbUtils.createDb(db_name)) {
            if (res.body() != null) {
                try {
                    System.out.println(res.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * find doc with the filter condition (case find all docs is useless for the case)
     */
    public void findAllFilterDoc() {

    }

    public void filterFind() {

    }


    /**
     * add bulk docs in one http
     */
    public void addDocs() {

        String data;
        String all = Objects.requireNonNull(this.getClass().getClassLoader().getResource("all.json")).getFile();
        data = FilesKt.readText(new File(all), Charsets.UTF_8);

        try (var res = dbUtils.addBulkDoc("fake", data)) {
            if (res.body() != null) {
                try {
                    System.out.println(res.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /**
     * add single-doc in one http
     */
    public void addDoc() {

    }

    public void findSpecDoc(String docId) {
        try (var res = dbUtils.findSpecDoc(db_name, docId)) {
            if (res.body() != null) {
                try {
                    System.out.println(res.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /* -----------------------------------API Start----------------------------------------*/

    /**
     * @return ids we are need to find the doc , may return null
     */
    public String[] ids() {
        String[] keys = null;
        try (var res = dbUtils.allDocs(db_name)) {
            if (res.body() != null) {
                try {
                    var body = res.body().string();
                    var doc = gson.fromJson(body, AllDocs.class);
                    keys = new String[doc.getTotal_rows()]; //init the keys
                    for (int i = 0; i < doc.getRows().size(); i++) {
                        keys[i] = doc.getRows().get(i).getId();
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return keys;
    }


    public Node query(String condition) {
        Node node = null;
        try (var res = dbUtils.filterDocs(db_name, condition)) {
            if (res.body() != null) {
                logger.info(res.toString());
                try {
                    String body = res.body().string();
                    node = new Node(condition, JsonUtils.getGson().fromJson(body, FilterDocs.class).getDocs().get(0).getData().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return node;
    }

    public Node query(String condition, String db_name) {
        Node node = null;
        try (var res = dbUtils.filterDocs(db_name, condition)) {
            if (res.body() != null) {
                System.out.println(res);
                try {
                    var body = res.body().string();
                    Doc doc = gson.fromJson(body, Doc.class);
                    Doc.Data data = doc.getData();
                    node = new Node(condition, data.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return node;
    }

    @SneakyThrows
    public List<Node> queries(String condition, List<String> db_names) {
        ExecutorService pool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        final CountDownLatch latch = new CountDownLatch(db_names.size());
        List<Node> queriesNode = new CopyOnWriteArrayList<>();
        db_names.forEach(db_name -> {
            Node n = query(condition, db_name);
            queriesNode.add(n);
            latch.countDown();
        });
        latch.await();
        if (!pool.isShutdown()) pool.shutdown();
        return queriesNode;
    }

    /* -----------------------------------API End----------------------------------------*/

}
