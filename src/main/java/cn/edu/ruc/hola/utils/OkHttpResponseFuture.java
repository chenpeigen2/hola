package cn.edu.ruc.hola.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * callback that implements the future
 */
public final class OkHttpResponseFuture implements Callback {

    private final String condition;
    public final ExecutableFuture future;

    public OkHttpResponseFuture(String condition) {
        this.condition = condition;
        future = new ExecutableFuture(condition);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        future.completeExceptionally(e);
        call.cancel();
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        try {
            future.complete(response);
        } finally {
            response.close();
        }
    }
}
