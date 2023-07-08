package org.hyperledger.fabric.samples.assettransfer;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.owlike.genson.Genson;
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
                        email = "a.transfer@example.com",
                        name = "Adrian Transfer",
                        url = "https://hyperledger.example.com")))
@Default
public final class QueryTransfer implements ContractInterface {

    private final Genson genson = new Genson();

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Query CreateQuery(final Context ctx, final String req, final String owner) {
        ChaincodeStub stub = ctx.getStub();

        String queryId = UUID.randomUUID().toString();
        String date = String.valueOf(System.currentTimeMillis());
        Query query = new Query(queryId, date, req, "", owner);
        // Use Genson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(query);

        stub.putStringState(queryId, sortedJson);

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
            Query query = genson.deserialize(result.getStringValue(), Query.class);
            System.out.println(query);
            queryResults.add(query);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }
}
