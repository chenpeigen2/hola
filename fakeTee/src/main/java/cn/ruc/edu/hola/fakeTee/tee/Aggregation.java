package cn.ruc.edu.hola.fakeTee.tee;

import java.util.List;

public class Aggregation {
    public static String SimpleAggregation(List<String> resp) {
        StringBuilder ret = new StringBuilder();
        resp.forEach(ret::append);
        return ret.toString();
    }
}
