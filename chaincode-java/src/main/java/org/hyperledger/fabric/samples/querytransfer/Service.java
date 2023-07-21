package org.hyperledger.fabric.samples.querytransfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class Service {

    @Expose
    @SerializedName("queryID")
    @Property()
    private final String queryID;


    @Expose
    @SerializedName("content")
    @Property()
    private final String content;


    public Service(String queryID, String content) {
        this.queryID = queryID;
        this.content = content;
    }

    public String getQueryID() {
        return queryID;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(queryID, service.queryID) && Objects.equals(content, service.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryID, content);
    }

    @Override
    public String toString() {
        return "Service{" +
                "queryID='" + queryID + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
