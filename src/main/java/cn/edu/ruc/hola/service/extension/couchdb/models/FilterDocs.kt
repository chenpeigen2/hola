package cn.edu.ruc.hola.service.extension.couchdb.models

class FilterDocs {
    lateinit var docs: List<Doc>
    lateinit var bookmark: String

    lateinit var execution_stats: FilterDocs.State

    class State {
        var total_keys_examined = -1
        var total_docs_examined = -1
        var total_quorum_docs_examined = -1
        var results_returned = -1
        var execution_time_ms = -1f
    }
}