package cn.edu.ruc.hola.query;

import cn.edu.ruc.hola.entity.Node;

import java.util.List;

public interface Queryable {
    List<Node> query(String condition, List<String> sids);
}
