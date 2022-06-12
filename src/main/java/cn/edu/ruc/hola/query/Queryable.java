package cn.edu.ruc.hola.query;

import cn.edu.ruc.hola.entity.Node;

import java.util.List;

public interface Queryable {
    List<Node> query(String condition, List<String> sids);

    List<Node> query(String condition, List<String> sids, String db_name);

    List<Node> queries(String condition, List<String> sids, List<String> db_names);
}
