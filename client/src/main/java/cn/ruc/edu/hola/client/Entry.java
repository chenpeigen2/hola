package cn.ruc.edu.hola.client;

import cn.ruc.edu.hola.client.bridge.ChainCodeBridge;
import cn.ruc.edu.hola.client.encrypy.EncryptUtils;
import cn.ruc.edu.hola.client.entity.Request;
import cn.ruc.edu.hola.client.utils.RandomStringGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * only for trigger the entry
 **/
public class Entry {
    public static void main(String[] args) {
        System.out.println("hello world");
        ChainCodeBridge bridge = new ChainCodeBridge();
        triggerClient(); // trigger the query
        bridge.triggerChaincode(); // trigger the chaincode
    }

    public static void triggerClient() {
        ApiService apiService = ApiService.getInstance();
        apiService.update("http", "127.0.0.1", 8888);
        List<Request> requests = new ArrayList<>();
        requests.add(new Request(RandomStringGenerator.UID, Action.query));

        try (var res = apiService.complex(requests)) {
            var text = res.body().string();
            System.out.println(EncryptUtils.decryptAES(text, apiService.getAESKey()));
        } catch (Exception ignored) {

        }
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
