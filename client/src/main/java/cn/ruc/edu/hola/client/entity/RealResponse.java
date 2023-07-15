package cn.ruc.edu.hola.client.entity;

import cn.ruc.edu.hola.client.encrypy.EncryptUtils;
import okhttp3.Response;

public final class RealResponse {
    private String response;

    private Response okResponse;

    private final String aesKey;

    public RealResponse(String response, String aesKey) {
        this.aesKey = aesKey;
        this.response = response;
    }

    public RealResponse(Response okResponse, String aesKey) {
        this.aesKey = aesKey;
        this.okResponse = okResponse;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public String getAesKey() {
        return aesKey;
    }

    public String getDecryptResponse() {
        return EncryptUtils.decryptAES(getResponse(), getAesKey());
    }

    public Response getOkResponse() {
        return okResponse;
    }
}
