package cn.ruc.edu.hola.index;

import java.util.Set;

public final class StructInfo {
    private final Set<String> dbs;
    private final Set<String> info;

    public StructInfo(Set<String> dbs, Set<String> info) {
        this.dbs = dbs;
        this.info = info;
    }

    public Set<String> getDbs() {
        return dbs;
    }

    public Set<String> getInfo() {
        return info;
    }
}
