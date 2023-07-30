package cn.ruc.edu.hola.index;

import java.util.List;
import java.util.function.BiConsumer;

public final class Ping {

    private Ping() {}

    public static void pong() {
        Indexer2.struct.forEach(new BiConsumer<String, List<StructInfo>>() {
            @Override
            public void accept(String s, List<StructInfo> structInfos) {
                System.out.println(structInfos);
            }
        });

        Indexer2.registerPath.forEach(new BiConsumer<BackBendConfigInfo, String>() {
            @Override
            public void accept(BackBendConfigInfo backBendConfigInfo, String s) {
                System.out.println(backBendConfigInfo);
            }
        });

        System.out.println("pong !");
    }
}
