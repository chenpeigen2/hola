package cn.edu.ruc.hola.service.extension.couchdb.models;

import lombok.Data;

import java.util.List;

@Data
public class FilterDocs {
    public List<Doc> docs;
    public String bookmark;

    public State execution_stats;

    @Data
    public static class State {
        public int total_keys_examined;
        public int total_docs_examined;
        public int total_quorum_docs_examined;
        public int results_returned;
        public float execution_time_ms;
    }
}
