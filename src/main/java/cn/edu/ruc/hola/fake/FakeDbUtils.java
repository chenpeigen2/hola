package cn.edu.ruc.hola.fake;

import cn.edu.ruc.hola.config.model.Host;
import cn.edu.ruc.hola.entity.Node;
import cn.edu.ruc.hola.exec.Exec;
import cn.edu.ruc.hola.service.extension.couchdb.models.FilterDocs;
import cn.edu.ruc.hola.utils.DbUtils;
import cn.edu.ruc.hola.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static cn.edu.ruc.hola.utils.DbUtils.checkDbUtils;

@Deprecated
public final class FakeDbUtils {


    private static final Logger logger = Logger.getLogger("FakeDbUtils");


    /**
     * create database named fake
     */
    public void createFakeDb(Host host) {
        DbUtils dbUtils = checkDbUtils(host);
        var res = dbUtils.createDb("fake");
        try {
            assert res.body() != null;
            logger.info("host at : " + host);
            logger.info(res.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res.close();
    }

    public void createFakeDb() {
        JsonUtils.readProps().forEach(this::createFakeDb);
    }

    public void deleteFakeDb() {
        JsonUtils.readProps().forEach(this::deleteFakeDb);
    }

    /**
     * delete the fake database named fake
     */
    public void deleteFakeDb(Host host) {
        DbUtils dbUtils = checkDbUtils(host);
        var res = dbUtils.deleteDb("fake");
        try {
            assert res.body() != null;
            logger.info("host at : " + host);
            logger.info(res.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res.close();
    }

    public void addFakeData() {
        List<String> fakeData = generateFakeData();
        List<Host> hosts = JsonUtils.readProps();
        for (int i = 0; i < fakeData.size(); i++) {
            addFakeData(hosts.get(i), fakeData.get(i));
        }
    }

    public void addFakeData(Host host, String data) {
        DbUtils dbUtils = checkDbUtils(host);
        var res = dbUtils.addDoc("fake", data);
        try {
            assert res.body() != null;
            logger.info("HOST :" + host);
            logger.info("data :" + data);
            logger.info(res.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res.close();
    }

    public List<String> generateFakeData() {
        var l = JsonUtils.readProps();
        return FakeData.basicFake(l.size(), 0.8f, false);
    }

    public void createFakeIndex() {
        JsonUtils.readProps().forEach(host -> {
            DbUtils dbUtils = checkDbUtils(host);
            String info = """
                    {
                        "index": {
                            "fields": ["data.countryid"]
                        },
                        "name" : "data.countryid-index",
                        "type" : "json"
                    }
                    """;
            var res = dbUtils.createIndex("fake", info);
            try {
                assert res.body() != null;
                logger.info("HOST :" + host);
                logger.info(res.body().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            res.close();
        });
    }

    public List<Node> fakeComplexQuery() {

        var query = """
                {
                "selector": {
                        "data.countryid": {"$gt": -1}
                    },
                    "execution_stats": true
                }
                """;

        List<Node> list = new ArrayList<>();

        JsonUtils.readProps().forEach(new Consumer<Host>() {
            @Override
            public void accept(Host host) {
                DbUtils dbUtils = checkDbUtils(host);

                var res = dbUtils.filterDocs("fake", query);
                try {
                    assert res.body() != null;
                    logger.info("HOST :" + host);
                    var body = res.body().string();
                    logger.info(body);
                    var data = JsonUtils.getGson().fromJson(body, FilterDocs.class).getDocs().get(0).toString();
                    System.out.println("=====================");
                    System.out.println(data);
                    list.add(new Node(query, JsonUtils.getGson().fromJson(body, FilterDocs.class).getDocs().get(0).getData().toString()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                res.close();
            }
        });

        return list;
    }

    public static void main(String[] args) throws IOException {
        var app = new FakeDbUtils();
//        app.createFakeDb();
//        app.addFakeData();


//        app.createFakeIndex();

        var l = app.fakeComplexQuery();

        var ex = new Exec();
        var ans = ex.partition(l);


        System.out.println(l);


        System.out.println(ans);

//
//

//        app.generateFakeData();
//        app.createIndex();
//        app.deleteFakeDb();
    }
}
