package cn.edu.ruc.hola.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ConcurrentHashMap;

public final class Response extends ConcurrentHashMap<String, Object> {

    public static final int ERROR_CODE = 303;

    public static final int SUCCESS_CODE = 200;

    private static final String ERROR_MSG = """
            I am very sorry your answer is wrong
            """;

    private static final String SUCCESS_MSG = """
            excellent
            """;

    private static final String DATA_NULL = "null";

    private static final String DATA_WEAK_NULL = "weak null";

    public static Response ErrorCode() {
        Response response = new Response();
        response.put("code", ERROR_CODE);
        response.put("msg", ERROR_MSG);
        response.put("data", DATA_NULL);
        return response;
    }

    public static Response WeakErrorCode() {
        Response response = new Response();
        response.put("code", ERROR_CODE);
        response.put("msg", ERROR_MSG);
        response.put("data", DATA_WEAK_NULL);
        return response;
    }

    public static Response SuccessCode(Object data) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return SuccessCode(gson.toJson(data));
    }

    public static Response SuccessCode(String data) {
        Response response = new Response();
        response.put("code", SUCCESS_CODE);
        response.put("msg", SUCCESS_MSG);
        response.put("data", data);
        return response;
    }

    private Response() {
    }
}
