package cn.ruc.edu.hola.client.bridge;

import cn.ruc.edu.hola.client.AssetTransfer;
import cn.ruc.edu.hola.client.entity.RealResponse;
import cn.ruc.edu.hola.client.gateway.AssetTransferImpl;
import cn.ruc.edu.hola.client.gateway.QueryTransferImpl;
import cn.ruc.edu.hola.client.impl.SgxConfig;
import cn.ruc.edu.hola.client.json.JsonUtils;

import java.time.Instant;

public final class ChainCodeBridge {

    public RealResponse invokeQuery(final SgxConfig sgxConfig, final String queryId, final String owner, final String date, final String query, final RealResponse resp) throws Exception {
        var wrapper = QueryTransferImpl.init();
        var queryTransfer = new QueryTransferImpl(wrapper.gateway);

        String response;
        if (resp.getOkResponse() != null) {
            try (var res = resp.getOkResponse()) {
                response = res.body().string();
                resp.setResponse(response);
            }
        } else {
            response = resp.getResponse();
        }

        queryTransfer.CreateQuery(JsonUtils.INSTANCE.getJson().toJson(sgxConfig), queryId, owner, date, query, response);
        QueryTransferImpl.destroy(wrapper);

        return resp;
    }

    public String invokeGetAllQueries() throws Exception {
        var wrapper = QueryTransferImpl.init();
        var queryTransfer = new QueryTransferImpl(wrapper.gateway);

        var result = queryTransfer.GetAllQueries();

        QueryTransferImpl.destroy(wrapper);

        return result;
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

            assetTransfer.assetExists(assetId); // check exist

            assetTransfer.deleteAsset(assetId); // delete asset

        }

        AssetTransferImpl.destroy(wrapper);
    }
}
