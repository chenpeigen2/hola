package cn.edu.ruc.hola.service.extension.couchdb.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Doc extends AbsDoc {

    // custom specific
    public int code;
    public String message;
    public Data data;

    @lombok.Data
    public static class Data {
        public int id;
        public String name;
        public String bfirstletter;
        public String logo;
        public String country;
        public int countryid;
    }
}