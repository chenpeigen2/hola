package cn.edu.ruc.hola.main;

import cn.edu.ruc.hola.config.Config;

public class App {
    private final static Config config = new Config(0.65f, 1000, 10, 8);

    public static void main(String[] args) {
        // the condition we are using , and now is couchdb
        var query = """
                {
                "selector": {
                        "data.countryid": {"$gt": -1}
                    },
                    "execution_stats": true
                }
                """;

        QueryWithMulti queryWithMulti = new QueryWithMulti();
        var ans = queryWithMulti.queryWithTry(config, query);
        if (ans != null) {
            System.out.println("============ result ============");
            System.out.println(ans);
        }
    }

}
