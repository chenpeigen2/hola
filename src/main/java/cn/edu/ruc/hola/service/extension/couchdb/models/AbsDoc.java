package cn.edu.ruc.hola.service.extension.couchdb.models;

import lombok.Data;

@Data
public abstract class AbsDoc {
    public String _id;
    public String _rev;
}
