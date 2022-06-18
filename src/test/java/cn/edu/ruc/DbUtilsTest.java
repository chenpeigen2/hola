package cn.edu.ruc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class DbUtilsTest {
    @Test
    public void testForBuildUtils() {
        var app = new DbUtils();
        try (var res = app.allDocs("helloworld")) {
            if (res.body() != null) {
                System.out.println(res.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
