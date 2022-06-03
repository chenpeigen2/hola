package cn.edu.ruc.hola.service.extension.couchdb.models;

import lombok.Data;

import java.util.List;

@Data
public class AllDocs {
    public int total_rows;
    public int offset;
    public List<Container> rows;


    @Data
    public static
    class Container {
        public String id;
        public String key;
        public REV value;

        @Data
        public static
        class REV {
            public String rev;
        }
    }
}
