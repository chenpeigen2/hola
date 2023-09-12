package org.hyperledger.fabric.samples.querytransfer.directcall;

import okhttp3.Response;
import org.hyperledger.fabric.samples.querytransfer.HttpClient;
import org.hyperledger.fabric.samples.querytransfer.json.JsonUtils;
import org.hyperledger.fabric.samples.querytransfer.utils.EncryptUtils;

import java.io.IOException;

public class Curl {
    public static String post(String url, Request request) {
        String filter = request.filter;
        filter = EncryptUtils.encryptAES(filter, Keys.AES_KEY);
        String body = JsonUtils.INSTANCE.getJson().toJson(new Body("uuid", filter));
        // post response;
        Response response = HttpClient.INSTANCE.executeHello(url, body);
        String text = null;
        try (response) {
            text = response.body().string();
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
        if (text != null) {
            text = EncryptUtils.encryptAES(text, Keys.AES_KEY);
        }
        return text;
    }
}
