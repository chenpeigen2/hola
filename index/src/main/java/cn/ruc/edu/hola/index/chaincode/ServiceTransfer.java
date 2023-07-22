package cn.ruc.edu.hola.index.chaincode;

public interface ServiceTransfer {
    Service CreateService(final String serviceId, final String content);

    String GetAllServices();
}
