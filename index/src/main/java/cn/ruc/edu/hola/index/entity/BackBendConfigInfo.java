package cn.ruc.edu.hola.index.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
import jdk.jfr.Experimental;

public record BackBendConfigInfo(@Experimental String host, @Expose int port, @Expose String path) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackBendConfigInfo that = (BackBendConfigInfo) o;
        return port == that.port && Objects.equal(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(host, port);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("host", host)
                .add("port", port)
                .add("path", path)
                .toString();
    }
}
