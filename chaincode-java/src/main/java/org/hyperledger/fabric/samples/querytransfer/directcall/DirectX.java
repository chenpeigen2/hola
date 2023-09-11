package org.hyperledger.fabric.samples.querytransfer.directcall;

import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;

public final class DirectX {
    public static Response executeComplex(String data) {
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

        // encrypt
//        std::string uid = "e51dce84-f2a4-43db-9b8f-311250ff0ba1";
//        std::string aesKey = client_get_aes_key(uid);
//        response = ecb_encrypt(response.c_str(), reinterpret_cast<const unsigned char *>(aesKey.c_str()),
//                aesKey.length(), PaddingModel::PKCS5OR7);
//        response = encode(response.c_str(), response.length());

        // assemble
//        int len = response.length();
//        delete[]p_response; // delete the pointer
//        p_response = new char[len + 1];
//        response.copy(p_response, len);
//        p_response[len] = '\0';
//        return p_response;
        return null;
    }
}
