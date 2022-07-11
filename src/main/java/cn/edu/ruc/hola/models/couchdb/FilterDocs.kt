package cn.edu.ruc.hola.models.couchdb

class FilterDocs {
    var docs: List<BaseDoc>? = null
    lateinit var bookmark: String

    lateinit var execution_stats: State

    class State {
        var total_keys_examined = -1
        var total_docs_examined = -1
        var total_quorum_docs_examined = -1
        var results_returned = -1
        var execution_time_ms = -1f
        override fun toString(): String {
            return "State(total_keys_examined=$total_keys_examined, total_docs_examined=$total_docs_examined, total_quorum_docs_examined=$total_quorum_docs_examined, results_returned=$results_returned, execution_time_ms=$execution_time_ms)"
        }
    }

    override fun toString(): String {
        return "FilterDocs(docs=$docs, bookmark='$bookmark', execution_stats=$execution_stats)"
    }
}