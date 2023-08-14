package cn.ruc.edu.hola.index;


import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public record RegisterInfo(@Expose(serialize = false) String serviceId, @Expose String serviceName, @Expose String info,
                           @Expose String struct,
                           @Expose BackBendConfigInfo backBendConfigInfo) {

    private final static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterInfo that = (RegisterInfo) o;
        return Objects.equal(serviceId, that.serviceId) && Objects.equal(serviceName, that.serviceName) && Objects.equal(info, that.info) && Objects.equal(struct, that.struct) && Objects.equal(backBendConfigInfo, that.backBendConfigInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serviceId, serviceName, info, struct, backBendConfigInfo);
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}

