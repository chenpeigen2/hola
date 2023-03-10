package cn.ruc.edu.hola.backbend.server;

public interface ProxyMethods {
    String uuids();

    String createDb();

    String allDbs();

    String dbsInfo();

    String getDbInfo();

    String deleteDb();

    String addDoc();

    String addBulkDoc();

    String deleteDoc();

    String allDocs();

    String findSpecDoc();

    String findBulkDoc();

    String filterDocs();

    String createIndex();
}
