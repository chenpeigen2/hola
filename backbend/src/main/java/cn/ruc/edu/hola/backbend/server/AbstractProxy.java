package cn.ruc.edu.hola.backbend.server;

import cn.ruc.edu.hola.backbend.db.CouchDBUtils;
import okhttp3.Response;

import java.io.IOException;

public abstract class AbstractProxy implements ProxyMethods {

    CouchDBUtils couchDBUtils = new CouchDBUtils("127.0.0.1", 5984, "admin", "password");

    @Override
    public String uuids(int cnt) {
        Response res = couchDBUtils.uuids(cnt);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String createDb(String databaseName) {
        Response res = couchDBUtils.createDb(databaseName);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String allDbs() {
        Response res = couchDBUtils.allDbs();
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String dbsInfo(String data) {
        Response res = couchDBUtils.dbsInfo(data);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String getDbInfo(String databaseName) {
        Response res = couchDBUtils.getDbInfo(databaseName);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String deleteDb(String databaseName) {
        Response res = couchDBUtils.deleteDb(databaseName);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String addDoc(String databaseName, String data) {
        Response res = couchDBUtils.addDoc(databaseName, data);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String addBulkDoc(String databaseName, String data) {
        Response res = couchDBUtils.addBulkDoc(databaseName, data);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String deleteDoc(String databaseName, String docId) {
        Response res = couchDBUtils.deleteDoc(databaseName, docId);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String allDocs(String databaseName) {
        Response res = couchDBUtils.allDocs(databaseName);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String findSpecDoc(String databaseName, String docId) {
        Response res = couchDBUtils.findSpecDoc(databaseName, docId);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String findBulkDoc(String databaseName, String data) {
        Response res = couchDBUtils.findBulkDoc(databaseName, data);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String filterDocs(String databaseName, String filter) {
        Response res = couchDBUtils.filterDocs(databaseName, filter);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    @Override
    public String createIndex(String databaseName, String info) {
        Response res = couchDBUtils.createIndex(databaseName, info);
        if (res.isSuccessful() && res.body() != null) {
            try (res) {
                return res.body().string();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ProxyMethods methods = new AbstractProxy() {

        };
        var xx = methods.createDb("xxxx1");
        System.out.println("================");
        System.out.println(xx);
    }
}
