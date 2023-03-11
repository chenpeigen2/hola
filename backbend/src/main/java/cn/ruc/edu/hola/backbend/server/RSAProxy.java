package cn.ruc.edu.hola.backbend.server;

import cn.ruc.edu.hola.backbend.rsa.Keys;
import cn.ruc.edu.hola.backbend.rsa.RSAUtils;

public class RSAProxy extends AbstractProxy {
    @Override
    public String createDb(String databaseName) {
        try {
            return super.createDb(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE));
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String allDbs() {
        try {
            String result = super.allDbs();
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String dbsInfo(String data) {
        try {
            String result = super.dbsInfo(RSAUtils.decrypt(data, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String getDbInfo(String databaseName) {
        try {
            String result = super.getDbInfo(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String deleteDb(String databaseName) {
        try {
            return super.deleteDb(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE));
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String addDoc(String databaseName, String data) {
        try {
            String result = super.addDoc(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE), RSAUtils.decrypt(data, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String addBulkDoc(String databaseName, String data) {
        try {
            String result = super.addBulkDoc(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE), RSAUtils.decrypt(data, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String deleteDoc(String databaseName, String docId) {
        try {
            String result = super.deleteDoc(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE), RSAUtils.decrypt(docId, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String allDocs(String databaseName) {
        try {
            String result = super.allDocs(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String findSpecDoc(String databaseName, String docId) {
        try {
            String result = super.findSpecDoc(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE), RSAUtils.decrypt(docId, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String findBulkDoc(String databaseName, String data) {
        try {
            String result = super.findBulkDoc(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE), RSAUtils.decrypt(data, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String filterDocs(String databaseName, String filter) {
        try {
            String result = super.filterDocs(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE), RSAUtils.decrypt(filter, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }

    @Override
    public String createIndex(String databaseName, String info) {
        try {
            String result = super.createIndex(RSAUtils.decrypt(databaseName, Keys.SELF_PRIVATE), RSAUtils.decrypt(info, Keys.SELF_PRIVATE));
            if (result != null) {
                return RSAUtils.encrypt(result.getBytes(), Keys.PUBLIC);
            }
        } catch (Exception ignored) {

        }
        return null;
    }
}
