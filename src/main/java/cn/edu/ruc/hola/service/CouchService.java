package cn.edu.ruc.hola.service;

import cn.edu.ruc.hola.entity.Node;
import cn.edu.ruc.hola.service.extension.couchdb.CouchDbExtension;
import cn.edu.ruc.hola.service.extension.couchdb.CouchDbServiceHolder;

import java.util.List;

public class CouchService extends AbsService {

    private final CouchDbExtension dbExtension;

    public CouchService(String sid) {
        super(sid);
        dbExtension = new CouchDbExtension(CouchDbServiceHolder.getHost(getSid()));
    }

    @Override
    public Node query(String condition, String db_name) {
        return dbExtension.query(condition, db_name);
    }

    @Override
    public List<Node> queries(String condition, List<String> db_names) {
        return dbExtension.queries(condition, db_names);
    }
}
