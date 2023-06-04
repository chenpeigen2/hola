package cn.ruc.edu.hola.backbend.server;

import cn.ruc.edu.hola.backbend.entity.Auth;

public interface Proxy {
    String uuids(int cnt);

    String createDb(String databaseName);

    String allDbs();

    String dbsInfo(String data);

    String getDbInfo(String databaseName);

    String deleteDb(String databaseName);

    String addDoc(String databaseName, String data);

    String addBulkDoc(String databaseName, String data);

    String deleteDoc(String databaseName, String docId);

    String allDocs(String databaseName);

    String findSpecDoc(String databaseName, String docId);

    String findBulkDoc(String databaseName, String data);

    String filterDocs(String databaseName, String filter);

    default String filterDocsByBB(String uuid, String filter, String bb) {return null;}

    String createIndex(String databaseName, String info);

    /** auth for get the aes key **/
    String acquireAESKey(Auth auth);
}
