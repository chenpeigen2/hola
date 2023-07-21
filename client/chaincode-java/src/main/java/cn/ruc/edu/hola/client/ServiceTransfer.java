package cn.ruc.edu.hola.client;

public interface ServiceTransfer {
    Service CreateService(final String serviceId, final String content);

    String GetAllQueries();
}
