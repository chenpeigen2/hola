package cn.edu.ruc;

import cn.edu.ruc.hola.config.model.Host;
import org.junit.jupiter.api.Test;
import cn.edu.ruc.hola.service.extension.couchdb.CouchDbExtension;

public class CouchDbExtensionTest {

    @Test
    public void testForAddBulkDocs() {
        Host h = new Host("xxx", "101.201.81.221", 5988);
        CouchDbExtension couchDbExtension = new CouchDbExtension(h);
        couchDbExtension.createDb();
        couchDbExtension.addDocs();
    }


}
