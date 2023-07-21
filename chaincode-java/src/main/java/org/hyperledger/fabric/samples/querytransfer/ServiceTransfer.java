package org.hyperledger.fabric.samples.querytransfer;

import com.google.gson.Gson;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.samples.querytransfer.json.JsonUtils;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;

@Contract(
        name = "service",
        info = @Info(
                title = "Service Transfer",
                description = "The hyperlegendary query transfer",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "O1831938181O@gmail.com",
                        name = "chenpeigen",
                        url = "https://gitlab.com/o1831938181o")))
public class ServiceTransfer implements ContractInterface {

    private final Gson gson = JsonUtils.INSTANCE.getJson();

    private final Logger logger = Logger.getLogger(ServiceTransfer.class.getName());

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Service CreateService(final Context ctx, final String serviceId, final String content) {
        ChaincodeStub stub = ctx.getStub();
        ClientIdentity identity = ctx.getClientIdentity();

        Service service = new Service(serviceId, content);
        String sortedJson = gson.toJson(service);
        stub.putStringState(serviceId, sortedJson);
        return service;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllServices(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        ClientIdentity identity = ctx.getClientIdentity();

        List<Service> queryResults = new ArrayList<Service>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result : results) {
            Service query = gson.fromJson(result.getStringValue(), Service.class);
            System.out.println(query);
            queryResults.add(query);
        }

        final String response = gson.toJson(queryResults);

        return response;
    }

    @Override
    public Context createContext(ChaincodeStub stub) {
        return ContractInterface.super.createContext(stub);
    }

    @Override
    public void beforeTransaction(Context ctx) {
        ContractInterface.super.beforeTransaction(ctx);
    }

    @Override
    public void afterTransaction(Context ctx, Object result) {
        ContractInterface.super.afterTransaction(ctx, result);
    }

    @Override
    public void unknownTransaction(Context ctx) {
        ContractInterface.super.unknownTransaction(ctx);
    }
}
