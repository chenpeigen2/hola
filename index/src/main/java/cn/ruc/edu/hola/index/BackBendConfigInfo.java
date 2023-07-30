package cn.ruc.edu.hola.index;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public record BackBendConfigInfo(String host, int port, String path) {

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
