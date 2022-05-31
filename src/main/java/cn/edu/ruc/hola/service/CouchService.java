package cn.edu.ruc.hola.service;

import cn.edu.ruc.hola.entity.Node;
import cn.edu.ruc.hola.service.extension.couchdb.CouchDbExtension;
import cn.edu.ruc.hola.service.extension.couchdb.CouchDbServiceHolder;

public class CouchService extends AbsService {

    private final CouchDbExtension dbExtension;

    public CouchService(String sid) {
        super(sid);
        dbExtension = new CouchDbExtension(CouchDbServiceHolder.getHost(getSid()));
    }

    /**
     * for simple query only
     *
     * @param condition query-condition
     * @return the query answer
     */
    @Override
    public Node query(String condition) {
        return dbExtension.query(condition);
    }

    @Override
    public Node query(String condition, String db_name) {
        return dbExtension.query(condition, db_name);
    }
}
