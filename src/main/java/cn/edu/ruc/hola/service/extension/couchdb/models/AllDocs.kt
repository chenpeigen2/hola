package cn.edu.ruc.hola.service.extension.couchdb.models


class AllDocs {
    var total_rows = -1
    var offset = -1
    lateinit var rows: List<Container>


    class Container {
        lateinit var id: String
        lateinit var key: String
        lateinit var value: REV


        class REV {
            var rev: String? = null
            override fun toString(): String {
                return "REV(rev=$rev)"
            }


        }

        override fun toString(): String {
            return "Container(id='$id', key='$key', value=$value)"
        }
    }

    override fun toString(): String {
        return "AllDocs(total_rows=$total_rows, offset=$offset, rows=$rows)"
    }
}