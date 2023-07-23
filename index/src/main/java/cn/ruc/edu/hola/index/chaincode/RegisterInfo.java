package cn.ruc.edu.hola.index.chaincode;


import java.util.Objects;

public final class RegisterInfo {
    public final String serviceId;

    public final String serviceName;

    public final String info;

    public final String struct;

    public RegisterInfo(String serviceId, String serviceName, String info, String struct) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.info = info;
        this.struct = struct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterInfo that)) return false;
        return Objects.equals(serviceId, that.serviceId) && Objects.equals(serviceName, that.serviceName) && Objects.equals(info, that.info) && Objects.equals(struct, that.struct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, serviceName, info, struct);
    }

    @Override
    public String toString() {
        return "RegisterInfo{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", info='" + info + '\'' +
                ", struct='" + struct + '\'' +
                '}';
    }
}
