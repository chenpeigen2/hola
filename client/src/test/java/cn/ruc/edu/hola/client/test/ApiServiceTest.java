package cn.ruc.edu.hola.client.test;

import cn.ruc.edu.hola.client.ApiService;
import org.junit.jupiter.api.Test;

public class ApiServiceTest {

    @Test
    public void testFilterFind() {
        ApiService apiService = ApiService.getInstance();
        apiService.update("http", "127.0.0.1", 8888);
        String dataBaseName = "";
        String filter = "";
        try (var res = apiService.filterDocs(dataBaseName, filter)) {
            System.out.println("555");
        } catch (Exception ignored) {

        }
    }
}
