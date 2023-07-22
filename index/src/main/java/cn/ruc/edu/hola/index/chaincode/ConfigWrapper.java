package cn.ruc.edu.hola.index.chaincode;

import io.grpc.ManagedChannel;
import org.hyperledger.fabric.client.Gateway;

public final class ConfigWrapper {
    public final ManagedChannel channel;

    public final Gateway gateway;

    public ConfigWrapper(ManagedChannel channel, Gateway gateway) {
        this.channel = channel;
        this.gateway = gateway;
    }
}
