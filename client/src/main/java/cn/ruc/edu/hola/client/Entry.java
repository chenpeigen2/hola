package cn.ruc.edu.hola.client;

import cn.ruc.edu.hola.client.encrypy.EncryptUtils;
import cn.ruc.edu.hola.client.entity.Request;
import cn.ruc.edu.hola.client.utils.ChaincodeConfig;
import cn.ruc.edu.hola.client.utils.RandomStringGenerator;
import cn.ruc.edu.hola.client.web.Verticle;
import io.vertx.core.Vertx;

import java.util.ArrayList;
import java.util.List;

/**
 * only for trigger the entry
 **/
public class Entry {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Verticle.class.getName());
    }

    public static void triggerClient() {
        ApiService apiService = ApiService.getInstance();
        apiService.update("http", "127.0.0.1", 8888);
        List<Request> requests = new ArrayList<>();
        requests.add(new Request(RandomStringGenerator.UID, Action.query));
        var text = apiService.complex(requests).getResponse();
        System.out.println(text);

    }

    interface Action {
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
                  },
                  "execution_stats": true
                }""";
    }
}
