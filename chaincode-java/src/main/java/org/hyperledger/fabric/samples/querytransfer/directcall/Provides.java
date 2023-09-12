package org.hyperledger.fabric.samples.querytransfer.directcall;

import java.util.ArrayList;
import java.util.List;

public class Provides {
    public static List<Request> provideQuery(String data) {
        List<Request> requests = new ArrayList<>();
        // only generate the fake data
        requests.add(new Request("http://localhost:8081/_find", "{\"selector\":{\"$or\":[{\"message\":\"success\"}],\"data.country\":\"China\"}}"));
        requests.add(new Request("http://localhost:8080/_find", "{\"selector\":{\"$or\":[{\"data.countryid\":{\"$gt\":-1}}],\"data.country\":\"China\"}}"));
        return requests;
    }
}
