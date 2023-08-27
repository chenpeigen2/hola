/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.querytransfer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Response;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.samples.querytransfer.json.JsonUtils;
import org.hyperledger.fabric.samples.querytransfer.utils.ChaincodeConfig;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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
        CreateQuery(ctx, "{\"scheme\":\"shceme\",\"host\":\"host\",\"port\":56}", "asset1", "Tomoko", "111", "query", null);
        CreateQuery(ctx, "{\"scheme\":\"shceme\",\"host\":\"host\",\"port\":561}", "asset2", "Brad", "111", "query", null);
        CreateQuery(ctx, "{\"scheme\":\"shceme\",\"host\":\"host\",\"port\":562}", "asset3", "Jin Soo", "111", "query", null);
        CreateQuery(ctx, "{\"scheme\":\"shceme\",\"host\":\"host\",\"port\":563}", "asset4", "Max", "111", "query", null);
        CreateQuery(ctx, "{\"scheme\":\"shceme\",\"host\":\"host\",\"port\":564}", "asset5", "Adrian", "111", "query", null);
        CreateQuery(ctx, "{\"scheme\":\"shceme\",\"host\":\"host\",\"port\":565}", "asset6", "Michel", "111", "query", null);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Query CreateQuery(final Context ctx, final String sgxConfig, final String queryID,
                             final String owner, final String date, final String query, String resp) {
        ChaincodeStub stub = ctx.getStub();
        Query asset;

        SgxConfig config = gson.fromJson(sgxConfig, SgxConfig.class);

        if (!ChaincodeConfig.supportNet()) {
            asset = new Query(queryID, owner, date, query, resp);
        } else {
            // here we execute our own http request
            try (Response response = HttpClient.INSTANCE.executeComplex(config, query)) {
                resp = response.body().string();
            } catch (Exception exception) {
                logger.error(exception.toString());
            }

            asset = new Query(queryID, owner, date, query, resp);
        }

        // Use Gson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = gson.toJson(asset);
        stub.putStringState(queryID, sortedJson);

        return asset;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public List<Query> CreateMultiQuery(final Context ctx, final String sgxConfig, final String queries) {
        ChaincodeStub stub = ctx.getStub();
        Type type = new TypeToken<List<Query>>() {
        }.getType();
        List<Query> queryList = gson.fromJson(queries, type);
        for (Query query : queryList) {
            query.setResp("abc");
            // TODO YOUR WORK
//            CreateQuery(ctx, sgxConfig, query.getQueryID(), query.getOwner(),
//                    query.getDate(), query.getQuery(), query.getResp());
        }
        stub.putStringState("aaa", "bbb");
        return queryList;
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

        List<Query> queryResults = new ArrayList<>();

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
