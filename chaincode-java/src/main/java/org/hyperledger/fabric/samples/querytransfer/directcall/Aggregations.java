package org.hyperledger.fabric.samples.querytransfer.directcall;

import java.util.List;
import java.util.function.Consumer;

public class Aggregations {
    public static String aggregate(List<String> resp) {
        StringBuilder sb = new StringBuilder();
        resp.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                sb.append(s);
            }
        });
        return sb.toString();
    }
}
