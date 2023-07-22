package org.hyperledger.fabric.samples.querytransfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class Service {

    @Expose
    @SerializedName("serviceId")
    @Property()
    private final String serviceId;


    @Expose
    @SerializedName("content")
    @Property()
    private final String content;


    public Service(String queryID, String content) {
        this.serviceId = queryID;
        this.content = content;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(serviceId, service.serviceId) && Objects.equals(content, service.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, content);
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
