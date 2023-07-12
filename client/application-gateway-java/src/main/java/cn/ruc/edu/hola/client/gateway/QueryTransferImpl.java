package cn.ruc.edu.hola.client.gateway;

import cn.ruc.edu.hola.client.Query;
import cn.ruc.edu.hola.client.QueryTransfer;
import org.hyperledger.fabric.client.*;

public class QueryTransferImpl extends BasicTransfer implements QueryTransfer {

    public QueryTransferImpl(final Gateway gateway) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(CHANNEL_NAME);

        // Get the smart contract from the network.
        contract = network.getContract(CHAINCODE_NAME);
    }

    @Override
    public Query CreateQuery(String config, String queryID, String owner, String date, String query, String resp) {
        return null;
    }

    @Override
    public Query ReadQuery(String queryID) {
        return null;
    }

    @Override
    public String GetAllQueries() {
        return null;
    }
}
