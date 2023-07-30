package cn.ruc.edu.hola.index;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public record RegisterInfo(String serviceId, String serviceName, String info, String struct,
                           BackBendConfigInfo backBendConfigInfo) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterInfo that = (RegisterInfo) o;
        return Objects.equal(serviceId, that.serviceId) && Objects.equal(serviceName, that.serviceName) && Objects.equal(info, that.info) && Objects.equal(struct, that.struct) && Objects.equal(backBendConfigInfo, that.backBendConfigInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serviceId, serviceName, info, struct, backBendConfigInfo);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("serviceId", serviceId)
                .add("serviceName", serviceName)
                .add("info", info)
                .add("struct", struct)
                .add("backBendConfigInfo", backBendConfigInfo)
                .toString();
    }
}

