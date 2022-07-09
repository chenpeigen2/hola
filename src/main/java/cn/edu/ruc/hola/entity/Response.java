package cn.edu.ruc.hola.entity;

public final class Response {

    public final int code;
    public final String msg;
    public final Results data;

    private Response(int code, String msg, Results data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

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
        return new Response(ERROR_CODE, ERROR_MSG, default_results);
    }

    public static Response WeakErrorCode() {
        return new Response(ERROR_CODE, ERROR_MSG, default_results);
    }

    public static Response SuccessCode(Results results) {
        return new Response(SUCCESS_CODE, SUCCESS_MSG, results);
    }
}
