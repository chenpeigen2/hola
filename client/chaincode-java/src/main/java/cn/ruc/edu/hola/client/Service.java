package cn.ruc.edu.hola.client;

import java.util.Objects;

@Deprecated
public class Service {
    private final String serviceId;

    private final String content;

    public Service(String serviceId, String content) {
        this.serviceId = serviceId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getServiceId() {
        return serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(serviceId, service.serviceId) && Objects.equals(content, service.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, content);
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
