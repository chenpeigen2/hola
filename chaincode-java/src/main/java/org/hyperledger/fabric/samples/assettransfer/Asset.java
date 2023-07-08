/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Asset {

    @Property()
    private final String assetID;


    @Property()
    private final String owner;

    @Property()
    private final String date;

    @Property
    private final String query;

    public String getAssetID() {
        return assetID;
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

    public Asset(@JsonProperty("assetID") final String assetID,
                 @JsonProperty("owner") final String owner, @JsonProperty("date") final String date,
                 @JsonProperty("query") final String query) {
        this.assetID = assetID;
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

        Asset other = (Asset) obj;

        return Objects.deepEquals(
                new String[]{getAssetID(), getOwner(), getDate(), getQuery()},
                new String[]{other.getAssetID(), other.getOwner(), other.getDate(), other.getQuery()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAssetID(), getOwner(), getDate(), getQuery());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [assetID=" + assetID + ", owner=" + owner + "]";
    }
}
