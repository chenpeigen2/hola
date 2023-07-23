package cn.ruc.edu.hola.index.chaincode;


public final class ChainCodeBridge {

    public Service register(final String serviceId, final String content) throws Exception {
        var wrapper = ServiceTransferImpl.init();
        var impl = new ServiceTransferImpl(wrapper.gateway);

        Service service = impl.CreateService(serviceId, content);

        ServiceTransferImpl.destroy(wrapper);

        return service;
    }

    public void unregister(final String serviceId) throws Exception {
        var wrapper = ServiceTransferImpl.init();
        var impl = new ServiceTransferImpl(wrapper.gateway);

        impl.DeleteService(serviceId);

        ServiceTransferImpl.destroy(wrapper);
    }

    public Service getSpecService(final String serviceId) throws Exception {
        var wrapper = ServiceTransferImpl.init();
        var impl = new ServiceTransferImpl(wrapper.gateway);

        Service service = impl.ReadServiceById(serviceId);

        ServiceTransferImpl.destroy(wrapper);
        return service;
    }

    public String getServices() throws Exception {
        var wrapper = ServiceTransferImpl.init();
        var impl = new ServiceTransferImpl(wrapper.gateway);

        String allServices = impl.GetAllServices();

        ServiceTransferImpl.destroy(wrapper);

        return allServices;
    }
}
