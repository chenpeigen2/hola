package cn.edu.ruc.hola.service.extension.couchdb.models

import lombok.Data

class AllDocs {
    var total_rows = -1
    var offset = -1
    lateinit var rows: List<Container>

    class Container {
        lateinit var id: String
        lateinit var key: String
        lateinit var value: REV

        @Data
        class REV {
            var rev: String? = null
        }
    }
}