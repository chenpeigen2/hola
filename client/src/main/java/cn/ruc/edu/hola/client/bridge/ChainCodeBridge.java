package cn.ruc.edu.hola.client.bridge;

import cn.ruc.edu.hola.client.AssetTransfer;
import cn.ruc.edu.hola.client.gateway.App;
import cn.ruc.edu.hola.client.gateway.AssetTransferImpl;

import java.time.Instant;

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


    public void trigger() throws Exception {
        var wrapper = AssetTransferImpl.init();

        {
            String assetId = "asset" + Instant.now().toEpochMilli();
            // execute yr code here
            AssetTransfer assetTransfer = new AssetTransferImpl(wrapper.gateway);

            assetTransfer.getAllAssets();

            assetTransfer.createAsset(assetId, "yellow", 5, "Tom", 1300);

            assetTransfer.transferAsset(assetId, "Saptha");

            assetTransfer.readAsset(assetId);

            assetTransfer.updateAsset("asset70", "blue", 5, "Tomoko", 300);

        }

        AssetTransferImpl.destroy(wrapper);
    }
}
