package cn.ruc.edu.hola.client.bridge;

import cn.ruc.edu.hola.client.Query;
import cn.ruc.edu.hola.client.entity.RealResponse;
import cn.ruc.edu.hola.client.gateway.impl.QueryTransferImpl;
import cn.ruc.edu.hola.client.impl.SgxConfig;
import cn.ruc.edu.hola.client.json.JsonUtils;

import java.util.List;

public final class ChainCodeBridge {

    public RealResponse invokeQuery(final SgxConfig sgxConfig, final String queryId, final String owner, final String date, final String query, final RealResponse resp) throws Exception {

        String response;
        if (resp.getOkResponse() != null) {
            try (var res = resp.getOkResponse()) {
                response = res.body().string();
                resp.setResponse(response);
            }
        } else {
            // execute the chaincode
            var wrapper = QueryTransferImpl.init();
            var queryTransfer = new QueryTransferImpl(wrapper.gateway);
            response = resp.getResponse();
            Query chaincode_query = queryTransfer.CreateQuery(JsonUtils.INSTANCE.getJson().toJson(sgxConfig), queryId, owner, date, query, response);
            response = chaincode_query.getResp();
            resp.setResponse(response);
            QueryTransferImpl.destroy(wrapper);
        }

        return resp;
    }

    public List<RealResponse> invokeMultiQueries(final SgxConfig sgxConfig, List<Query> queries, final List<RealResponse> resp) throws Exception {
        // only use the chaincode
        var wrapper = QueryTransferImpl.init();
        var queryTransfer = new QueryTransferImpl(wrapper.gateway);
        var qury_string = JsonUtils.INSTANCE.getJson().toJson(queries);
        List<Query> chaincode_query = queryTransfer.CreateMultiQuery(JsonUtils.INSTANCE.getJson().toJson(sgxConfig), qury_string);
        for (int i = 0; i < resp.size(); i++) {
            resp.get(i).setResponse(chaincode_query.get(i).getResp());
        }
        QueryTransferImpl.destroy(wrapper);
        return resp;
    }
}
