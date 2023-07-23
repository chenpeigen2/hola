package cn.ruc.edu.hola.backbend.entity;


import java.util.Objects;

public record RegisterInfo(String serviceId, String serviceName, String info, String struct) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterInfo that)) return false;
        return Objects.equals(serviceId, that.serviceId) && Objects.equals(serviceName, that.serviceName) && Objects.equals(info, that.info) && Objects.equals(struct, that.struct);
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
