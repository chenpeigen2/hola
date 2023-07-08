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
    private final String color;

    @Property()
    private final int size;

    @Property()
    private final String owner;

    @Property()
    private final String owner1;

    @Property()
    private final int appraisedValue;

    public String getAssetID() {
        return assetID;
    }

    public String getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwner1() {
        return owner1;
    }

    public int getAppraisedValue() {
        return appraisedValue;
    }

    public Asset(@JsonProperty("assetID") final String assetID, @JsonProperty("color") final String color,
            @JsonProperty("size") final int size, @JsonProperty("owner") final String owner, @JsonProperty("owner1") final String owner1,
            @JsonProperty("appraisedValue") final int appraisedValue) {
        this.assetID = assetID;
        this.color = color;
        this.size = size;
        this.owner = owner;
        this.owner1 = owner1;
        this.appraisedValue = appraisedValue;
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
                new String[] {getAssetID(), getColor(), getOwner(), getOwner1()},
                new String[] {other.getAssetID(), other.getColor(), other.getOwner(), other.getOwner1()})
                &&
                Objects.deepEquals(
                new int[] {getSize(), getAppraisedValue()},
                new int[] {other.getSize(), other.getAppraisedValue()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAssetID(), getColor(), getSize(), getOwner(), getOwner1(), getAppraisedValue());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [assetID=" + assetID + ", color="
                + color + ", size=" + size + ", owner=" + owner + ", appraisedValue=" + appraisedValue + "]";
    }
}
