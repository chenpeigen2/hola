/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.querytransfer;

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
import org.hyperledger.fabric.samples.querytransfer.json.JsonUtils;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;


@Contract(
        name = "basic",
        info = @Info(
                title = "Query Transfer",
                description = "The hyperlegendary query transfer",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "O1831938181O@gmail.com",
                        name = "chenpeigen",
                        url = "https://gitlab.com/o1831938181o")))
@Default
public final class QueryTransfer implements ContractInterface {

    private final Gson gson = JsonUtils.INSTANCE.getJson();

    private final Logger logger = Logger.getLogger(QueryTransfer.class.getName());

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }


    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        CreateQuery(ctx, "asset1", "Tomoko");
        CreateQuery(ctx, "asset2", "Brad");
        CreateQuery(ctx, "asset3", "Jin Soo");
        CreateQuery(ctx, "asset4", "Max");
        CreateQuery(ctx, "asset5", "Adrian");
        CreateQuery(ctx, "asset6", "Michel");
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
    public Query CreateQuery(final Context ctx, final String queryID,
                             final String owner) {
        ChaincodeStub stub = ctx.getStub();

        String date = "UUUUUUU111";
        String query = "query";

        Query asset = new Query(queryID, owner, date, query, null);
        // Use Gson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = gson.toJson(asset);
        stub.putStringState(queryID, sortedJson);

        return asset;
    }


    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Query ReadQuery(final Context ctx, final String queryID) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(queryID);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", queryID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Query query = gson.fromJson(assetJSON, Query.class);
        return query;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllQueries(final Context ctx) {
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

        // exe use kotlin
        System.out.println(Hello.INSTANCE.getA());

        return response;
    }

    @Override
    public void beforeTransaction(final Context ctx) {
        ContractInterface.super.beforeTransaction(ctx);
    }

    @Override
    public void afterTransaction(final Context ctx, final Object result) {
        ContractInterface.super.afterTransaction(ctx, result);
    }

    @Override
    public void unknownTransaction(final Context ctx) {
        ContractInterface.super.unknownTransaction(ctx);
    }
}
