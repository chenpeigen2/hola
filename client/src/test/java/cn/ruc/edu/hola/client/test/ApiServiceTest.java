package cn.ruc.edu.hola.client.test;

import cn.ruc.edu.hola.client.ApiService;
import cn.ruc.edu.hola.client.encrypy.EncryptUtils;
import cn.ruc.edu.hola.client.entity.Request;
import cn.ruc.edu.hola.client.json.JsonUtils;
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
        List<Request> requests = new ArrayList<>();
        requests.add(new Request("""
                {
                      "execution_stats": true,
                      "selector": {
                        "data.countryid": {
                          "$gt": -1
                        }
                      }
                    }"""));
        requests.add(new Request("""
                {
                      "execution_stats": true,
                      "selector": {
                        "data.countryid": {
                          "$gt": -1
                        }
                      }
                    }"""));

        System.out.println(JsonUtils.INSTANCE.getJson().toJson(requests));
    }

    @Test
    public void testComplex() {
        ApiService apiService = ApiService.getInstance();
        apiService.update("http", "127.0.0.1", 8888);
        List<Request> requests = new ArrayList<>();
        requests.add(new Request("""
                {
                      "execution_stats": true,
                      "selector": {
                        "data.countryid": {
                          "$gt": -1
                        }
                      }
                    }"""));

        try (var res = apiService.complex(requests)) {
            var text = res.body().string();
            System.out.println(EncryptUtils.decryptAES(text, apiService.getAESKey()));
        } catch (Exception ignored) {

        }
    }
}
