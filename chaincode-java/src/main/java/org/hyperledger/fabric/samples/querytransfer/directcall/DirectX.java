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
//        for (Request request : requests) {
//            String resp = Curl.post(request.url, request);
//            respList.add(resp);
//        }
        // aggregation process

        Request request = requests.get(0);
        String resp = Curl.post(request.url, request);
        respList.add(resp);
        String response = Aggregations.aggregate(respList);
//        response = EncryptUtils.encryptAES(response, Keys.AES_KEY);
        return response;
//        return "+1S37mgE9Q+Q6BXFVI87eDaFzIGXzGh8ZgTQ5x7DII/WmTener0C6t6YPCai1PC+rKmfejJTIK11CPrNvXDKR2c+RVOF4m63+KznOQMctLZE4fPh7wEI3DhpzvWD4UaGawfsqeOy6c89rt8C1w5qpU4JbmpLlRwkxZiaJySlMNdaiRsDHWS546LRQfbnx/Y1Bsqk6f+44oMgJLOLehDauXpekO5YGiiCncZRildiCy7IzfcoykM1e0M0KQeTCA0hmd+WABoOK78JB7BYAXZIElhGe3TMAMVxeXLc8830JZ0AuQ+2VeLvyr6EVipDVCCOsoGGjkeTzvncn1pxo8lwDq/hsCnssZ/UDfzwrggPDZjzwPl5pgScqJAdJPfxSNIymwIaSP7BvVFAfAAm2oHjKyv+iT25kkIxO0b8d6Iilw0eaSBddS8U2YxHOyk+nAAyg67/zKo/6gbX7iCulezQLJEGrp8TtiTxT0cTmu0Fm/SvUE/WB9EXPmrN0+ggULrNF1uN9lK1CaQaFXxAO2DQAr2yzOZxZV23kII2swfmvj7H2m1fgQkk/Q24MQ5Ekea0+CRzWwZp+Aq2F72uJsbzlTD8czLS/rNS7U6cLcUN0OfhtyM/eoq0E1DqGPbchZtHs9keVp8Odsmjfh27TZHmww==+1S37mgE9Q+Q6BXFVI87eDaFzIGXzGh8ZgTQ5x7DII/WmTener0C6t6YPCai1PC+rKmfejJTIK11CPrNvXDKR2c+RVOF4m63+KznOQMctLZE4fPh7wEI3DhpzvWD4UaGawfsqeOy6c89rt8C1w5qpU4JbmpLlRwkxZiaJySlMNdaiRsDHWS546LRQfbnx/Y1Bsqk6f+44oMgJLOLehDauXpekO5YGiiCncZRildiCy7IzfcoykM1e0M0KQeTCA0hmd+WABoOK78JB7BYAXZIElhGe3TMAMVxeXLc8830JZ0AuQ+2VeLvyr6EVipDVCCOsoGGjkeTzvncn1pxo8lwDq/hsCnssZ/UDfzwrggPDZjzwPl5pgScqJAdJPfxSNIymwIaSP7BvVFAfAAm2oHjKyv+iT25kkIxO0b8d6Iilw0eaSBddS8U2YxHOyk+nAAyg67/zKo/6gbX7iCulezQLJEGrp8TtiTxT0cTmu0Fm/SvUE/WB9EXPmrN0+ggULrNF1uN9lK1CaQaFXxAO2DQAr2yzOZxZV23kII2swfmvj7H2m1fgQkk/Q24MQ5Ekea0+CRzWwZp+Aq2F72uJsbzlb5cTGyfspZ9sK75en0H/qJbyQVjslHEVbD9HNaBUJzu";
    }
}
