package cn.edu.ruc.hola.service.extension.couchdb.models;

import lombok.Data;

import java.util.List;

@Data
public class FilterDocs {
    private List<Doc> docs;
    private String bookmark;

    private State execution_stats;

    @Data
    public static class State {
        private int total_keys_examined;
        private int total_docs_examined;
        private int total_quorum_docs_examined;
        private int results_returned;
        private float execution_time_ms;
    }
}
