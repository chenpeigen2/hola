/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.querytransfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;


@DataType()
public final class Query {

    @Expose
    @SerializedName("queryID")
    @Property()
    private final String queryID; // 查询的唯一标识

    @Expose
    @SerializedName("owner")
    @Property()
    private final String owner; // 查询的所属owner

    @Expose
    @SerializedName("date")
    @Property()
    private final String date; // 查询的时间

    @Expose
    @SerializedName("query")
    @Property
    private final String query; // 查询的请求内容

    @Expose
    @SerializedName("resp")
    @Property
    private final String resp;

    public String getQueryID() {
        return queryID;
    }

    public String getOwner() {
        return owner;
    }

    public String getDate() {
        return date;
    }


    public String getQuery() {
        return query;
    }

    public String getResp() {
        return resp;
    }

    public Query(String queryID, String owner, String date, String query, String resp) {
        this.queryID = queryID;
        this.owner = owner;
        this.date = date;
        this.query = query;
        this.resp = resp;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Query other = (Query) obj;

        return Objects.deepEquals(
                new String[]{getQueryID(), getOwner(), getDate(), getQuery()},
                new String[]{other.getQueryID(), other.getOwner(), other.getDate(), other.getQuery()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQueryID(), getOwner(), getDate(), getQuery());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [queryID=" + queryID + ", owner=" + owner + "]";
    }
}
