/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;


@DataType()
public final class Query {

    @SerializedName("queryID")
    @Property()
    private final String queryID;

    @SerializedName("owner")
    @Property()
    private final String owner;

    @SerializedName("date")
    @Property()
    private final String date;

    @SerializedName("query")
    @Property
    private final String query;

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

    public Query(final String queryID,
                 final String owner, final String date,
                 final String query) {
        this.queryID = queryID;
        this.owner = owner;
        this.date = date;
        this.query = query;
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
