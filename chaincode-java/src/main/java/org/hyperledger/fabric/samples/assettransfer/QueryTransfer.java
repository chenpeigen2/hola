/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;


@Contract(
        name = "basic",
        info = @Info(
                title = "Asset Transfer",
                description = "The hyperlegendary asset transfer",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "a.transfer@example.com",
                        name = "Adrian Transfer",
                        url = "https://hyperledger.example.com")))
@Default
public final class QueryTransfer implements ContractInterface {

    private final Gson gson = new Gson();

    private final Logger logger = Logger.getLogger("QueryTransfer");

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        CreateAsset(ctx, "asset1", "Tomoko");
        CreateAsset(ctx, "asset2", "Brad");
        CreateAsset(ctx, "asset3", "Jin Soo");
        CreateAsset(ctx, "asset4", "Max");
        CreateAsset(ctx, "asset5", "Adrian");
        CreateAsset(ctx, "asset6", "Michel");
    }

    public static String main() {
        String res = "placeholder";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            res = response.body().string(); // can only call once
            System.out.println(res);
            System.out.println("8888889999999999777777777777");
            System.out.println("executing the okhttp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Query CreateAsset(final Context ctx, final String assetID,
                             final String owner) {
        ChaincodeStub stub = ctx.getStub();

        String date = "UUUUUUUUDDDDDDDDDDD";
        String query = "query";

        Query asset = new Query(assetID, owner, date, query);
        // Use Gson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = gson.toJson(asset);
        stub.putStringState(assetID, sortedJson);

        return asset;
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Query ReadAsset(final Context ctx, final String assetID) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(assetID);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", assetID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Query query = gson.fromJson(assetJSON, Query.class);
        return query;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Query> queryResults = new ArrayList<Query>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            Query query = gson.fromJson(result.getStringValue(), Query.class);
            System.out.println(query);
            queryResults.add(query);
        }

        final String response = gson.toJson(queryResults);

        // execute the okhttp3
        try {
            main();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("444444444444443333333333333338888888888888888888");
            logger.error(e.toString());
        }

        return response;
    }
}
