package cn.ruc.edu.hola.index;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var ans = Indexer.remappingStruct("struct.json");
        var ans1 = Indexer.remappingUrl("backbend.json");
        System.out.println();
    }
}