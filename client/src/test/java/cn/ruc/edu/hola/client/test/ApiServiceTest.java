package cn.ruc.edu.hola.client.test;

import cn.ruc.edu.hola.client.ApiService;
import cn.ruc.edu.hola.client.entity.ChaincodeRequest;
import cn.ruc.edu.hola.client.json.JsonUtils;
import cn.ruc.edu.hola.client.utils.RandomStringGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ApiServiceTest {

    @Test
    public void testFilterFind() {
        ApiService apiService = ApiService.getInstance();
        apiService.update("http", "127.0.0.1", 8888);
        String dataBaseName = "fake1";
        String filter = """
                {
                  "selector": {
                    "data.countryid": {
                      "$gt": -1
                    }
                  },
                  "execution_stats": true
                }""";
        try (var res = apiService.filterDocs(dataBaseName, filter)) {
            System.out.println(res.body().string());
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testSizable() {
        List<ChaincodeRequest> chaincodeRequests = new ArrayList<>();
        chaincodeRequests.add(new ChaincodeRequest(RandomStringGenerator.UID, """
                {
                      "execution_stats": true,
                      "selector": {
                        "data.countryid": {
                          "$gt": -1
                        }
                      }
                    }"""));
        chaincodeRequests.add(new ChaincodeRequest(RandomStringGenerator.UID, """
                {
                      "execution_stats": true,
                      "selector": {
                        "data.countryid": {
                          "$gt": -1
                        }
                      }
                    }"""));

        System.out.println(JsonUtils.INSTANCE.getJson().toJson(chaincodeRequests));
    }

    @Test
    public void testComplex() {
        ApiService apiService = ApiService.getInstance();
        apiService.update("http", "127.0.0.1", 8888);
        List<ChaincodeRequest> chaincodeRequests = new ArrayList<>();
        chaincodeRequests.add(new ChaincodeRequest(RandomStringGenerator.UID, Action.query));
        var text = apiService.complex(chaincodeRequests).getResponse();
        System.out.println(text);
    }
}
