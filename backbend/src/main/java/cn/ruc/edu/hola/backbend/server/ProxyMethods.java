package cn.ruc.edu.hola.backbend.server;

public interface ProxyMethods {
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

    String createIndex(String databaseName, String info);
}
