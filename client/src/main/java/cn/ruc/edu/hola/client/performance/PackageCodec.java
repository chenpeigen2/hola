package cn.ruc.edu.hola.client.performance;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

@Deprecated
public class PackageCodec implements MessageCodec<Package, Package> {
    @Override
    public void encodeToWire(Buffer buffer, Package aPackage) {
        JsonObject json = new JsonObject()
                .put("thread_name", aPackage.thread_name);
        String jsonString = json.encode();
        buffer.appendString(jsonString);
    }

    @Override
    public Package decodeFromWire(int pos, Buffer buffer) {
        JsonObject json = buffer.toJsonObject();
        return new Package(json.getString("thread_name"));
    }

    @Override
    public Package transform(Package aPackage) {
        return aPackage;
    }

    @Override
    public String name() {
        return "PackageCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
