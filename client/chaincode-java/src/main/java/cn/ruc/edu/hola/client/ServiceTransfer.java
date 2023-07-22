package cn.ruc.edu.hola.client;

@Deprecated
public interface ServiceTransfer {
    Service CreateService(final String serviceId, final String content);

    String GetAllServices();
}
