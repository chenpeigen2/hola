package cn.ruc.edu.hola.client.test;

import cn.ruc.edu.hola.client.ApiService;
import org.junit.jupiter.api.Test;

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
}
