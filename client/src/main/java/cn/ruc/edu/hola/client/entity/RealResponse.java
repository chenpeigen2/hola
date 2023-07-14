package cn.ruc.edu.hola.client.entity;

import okhttp3.Response;

public final class RealResponse {
    private String response;

    private Response okResponse;

    public RealResponse(String response) {
        this.response = response;
    }

    public RealResponse(Response okResponse) {
        this.okResponse = okResponse;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public Response getOkResponse() {
        return okResponse;
    }
}
