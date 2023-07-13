package cn.ruc.edu.hola.client.gateway;

import cn.ruc.edu.hola.client.Query;
import cn.ruc.edu.hola.client.QueryTransfer;
import org.hyperledger.fabric.client.*;

import java.util.logging.Logger;

public class QueryTransferImpl extends BasicTransfer implements QueryTransfer {

    private static final Logger logger = Logger.getLogger(QueryTransferImpl.class.getName());

    public QueryTransferImpl(final Gateway gateway) {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(CHANNEL_NAME);

        // Get the smart contract from the network.
        contract = network.getContract(CHAINCODE_NAME);
    }

    @Override
    public Query CreateQuery(String config, String queryID, String owner, String date, String query, String resp) {
        System.out.println("\n--> Submit Transaction: CreateAsset, creates new asset with ID, Color, Size, Owner and AppraisedValue arguments");
        try {
            var result = contract.submitTransaction("CreateQuery", config, queryID, owner, date, query, resp);
            var json_result = prettyJson(result);
            logger.info("CreateQuery ====>");
            logger.info(json_result);
            logger.info("CreateQuery <====");
            return gson.fromJson(json_result, Query.class);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
//            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Query ReadQuery(String queryID) {
        return null;
    }

    @Override
    public String GetAllQueries() {
        try {
            var result = contract.evaluateTransaction("GetAllQueries");
            var json_result = prettyJson(result);
            logger.info("GetAllQueries ====>");
            logger.info(json_result);
            logger.info("GetAllQueries <====");
            return json_result;
        } catch (GatewayException e) {
//            throw new RuntimeException(e);
        }
        return null;
    }
}
