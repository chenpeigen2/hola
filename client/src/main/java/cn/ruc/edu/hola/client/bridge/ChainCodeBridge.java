package cn.ruc.edu.hola.client.bridge;

import cn.ruc.edu.hola.client.gateway.App;

public final class ChainCodeBridge {
    public boolean triggerChaincode() {
        try {
            // trigger the chaincode
            App.main(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
