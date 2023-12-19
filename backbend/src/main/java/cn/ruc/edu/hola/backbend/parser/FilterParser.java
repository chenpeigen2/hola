package cn.ruc.edu.hola.backbend.parser;

import cn.ruc.edu.hola.backbend.entity.StructInfo2;

import java.util.*;
import java.util.function.Consumer;

/**
 * parser the key we are using to indicate what we are looking for
 **/
public final class FilterParser {
    private FilterParser() {}

    public static Map<String, List<StructInfo2>> struct = new HashMap<>();

    /**
     * 可能会误伤其他的struct结构（暂时先不考虑）
     * <p>
     * 可能是伪需求 ！！！
     **/
    public static Set<String> getFilterDbs(String filter, String backbendName) {
        var keys = QueryDump.dumpKeys(filter); // 获取得到这个请求的全部关键字
        var structs = struct.get(backbendName); // 得到对应的bb的对应的所有的数据结构
        if (structs == null) return null;

        Set<String> ret = new HashSet<>();

        structs.forEach(new Consumer<StructInfo2>() {
            @Override
            public void accept(StructInfo2 structInfo) {
                keys.forEach(key -> {
                    // if the query keywords contains the info the struct associate
                    if (structInfo.keyWords().contains(key)) {
                        ret.add(structInfo.db()); // then we need to add the associate db
                    }
                });
            }
        });

        return ret;

    }
}
