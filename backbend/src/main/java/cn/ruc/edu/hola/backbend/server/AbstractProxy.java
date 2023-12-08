package cn.ruc.edu.hola.backbend.server;

import cn.ruc.edu.hola.backbend.db.CouchDBUtils;
import com.google.common.base.Objects;
import okhttp3.Response;

import java.io.IOException;
import java.util.StringJoiner;

public abstract class AbstractProxy implements Proxy {

    protected CouchDBUtils couchDBUtils;

    protected CouchEntities entities;

    public void updateEntities(CouchEntities b) {
        if (entities == null) {
            entities = new CouchEntities(b.host, b.port, b.name, b.password);
            couchDBUtils = new CouchDBUtils(entities.host, entities.port, entities.name, entities.password);
            System.err.println("update source");
            return;
        }
        if (entities.equals(b)) return;
        entities = new CouchEntities(b.host, b.port, b.name, b.password);
        couchDBUtils = new CouchDBUtils(entities.host, entities.port, entities.name, entities.password);
        System.err.println("update source");
    }


    public static class CouchEntities {
        private final String host;

        private final int port;

        private final String name;

        private final String password;

        public CouchEntities(String host, int port, String name, String password) {
            this.host = host;
            this.port = port;
            this.name = name;
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CouchEntities that = (CouchEntities) o;
            return port == that.port && Objects.equal(host, that.host) && Objects.equal(name, that.name) && Objects.equal(password, that.password);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", CouchEntities.class.getSimpleName() + "[", "]")
                    .add("host='" + host + "'")
                    .add("port=" + port)
                    .add("name='" + name + "'")
                    .add("password='" + password + "'")
                    .toString();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(host, port, name, password);
        }
    }

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
}
