package cn.ruc.edu.hola.client.gateway;

import cn.ruc.edu.hola.client.Service;
import cn.ruc.edu.hola.client.ServiceTransfer;
import org.hyperledger.fabric.client.*;

import java.util.logging.Logger;

public class ServiceTransferImpl extends BasicTransfer implements ServiceTransfer {
    private static final Logger logger = Logger.getLogger(QueryTransferImpl.class.getName());
    protected static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "service");

    public ServiceTransferImpl(final Gateway gateway) {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(CHANNEL_NAME);

        // Get the smart contract from the network.
        contract = network.getContract(CHAINCODE_NAME);
    }

    @Override
    public Service CreateService(String serviceId, String content) {
        System.out.println("\n--> Submit Transaction: CreateAsset, creates new asset with ID, Color, Size, Owner and AppraisedValue arguments");
        try {
            var result = contract.submitTransaction("CreateService", "serviceId", "content");
            var json_result = prettyJson(result);
            logger.info("CreateQuery ====>");
            logger.info(json_result);
            logger.info("CreateQuery <====");
            return gson.fromJson(json_result, Service.class);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
//            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String GetAllQueries() {
        try {
            var result = contract.evaluateTransaction("GetAllServices");
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
