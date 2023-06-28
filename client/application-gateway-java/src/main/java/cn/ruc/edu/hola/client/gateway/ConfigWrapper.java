package cn.ruc.edu.hola.client.gateway;

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
