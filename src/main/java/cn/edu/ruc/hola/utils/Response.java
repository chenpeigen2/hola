package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.entity.Results;

import java.util.HashMap;

public final class Response extends HashMap<String, Object> {

    public static final int ERROR_CODE = 303;

    public static final int SUCCESS_CODE = 200;

    private static final String ERROR_MSG = """
            I am very sorry your answer is wrong
            """;

    private static final String SUCCESS_MSG = """
            excellent
            """;

    private static final Results default_results = new Results();

    public static Response ErrorCode() {
        Response response = new Response();
        response.put("code", ERROR_CODE);
        response.put("msg", ERROR_MSG);
        response.put("data", default_results);
        return response;
    }

    public static Response WeakErrorCode() {
        Response response = new Response();
        response.put("code", ERROR_CODE);
        response.put("msg", ERROR_MSG);
        response.put("data", default_results);
        return response;
    }

    public static Response SuccessCode(Results results) {
        Response response = new Response();
        response.put("code", SUCCESS_CODE);
        response.put("msg", SUCCESS_MSG);
        response.put("data", results);
        return response;
    }
}
