package org.hyperledger.fabric.samples.querytransfer.directcall;

import org.hyperledger.fabric.samples.querytransfer.utils.EncryptUtils;

import java.util.ArrayList;
import java.util.List;

public final class DirectX {
    public static String executeComplex(String data) {
        // provide the requests
        List<Request> requests = Provides.provideQuery(data);
        // the result
        List<String> respList = new ArrayList<>();
        // perform execution
        for (Request request : requests) {
            String resp = Curl.post(request.url, request);
            respList.add(resp);
        }
        // aggregation process
        String response = Aggregations.aggregate(respList);

        response = EncryptUtils.encryptAES(response, Keys.AES_KEY);
        return response;
    }
}
