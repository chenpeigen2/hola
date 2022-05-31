package cn.edu.ruc;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import cn.edu.ruc.hola.utils.Response;

import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testForBuildResponse() {
        Map<String, String> m = new HashMap<>();
        m.put("a", "b");
        m.put("b", "c");
        var str = Response.SuccessCode(m);
        System.out.println(new Gson().toJson(str));
    }
}
