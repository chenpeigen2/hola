package cn.ruc.edu.hola.index.chaincode;

import org.hyperledger.fabric.client.*;

import java.util.logging.Logger;

public class ServiceTransferImpl extends BasicTransfer implements ServiceTransfer {
    private static final Logger logger = Logger.getLogger(ServiceTransferImpl.class.getName());

    private static final String contractName = "service";

    public ServiceTransferImpl(final Gateway gateway) {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(CHANNEL_NAME);

        // Get the smart contract from the network.
        contract = network.getContract(CHAINCODE_NAME, contractName);
    }

    @Override
    public Service CreateService(String serviceId, String content) {
        System.out.println("\n--> Submit Transaction: CreateService, creates new asset with ID, Color, Size, Owner and AppraisedValue arguments");
        try {
            var result = contract.submitTransaction("CreateService", "serviceId", "content");
            var json_result = prettyJson(result);
            logger.info("CreateService ====>");
            logger.info(json_result);
            logger.info("CreateService <====");
            return gson.fromJson(json_result, Service.class);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
//            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String GetAllServices() {
        try {
            var result = contract.evaluateTransaction("GetAllServices");
            var json_result = prettyJson(result);
            logger.info("GetAllServices ====>");
            logger.info(json_result);
            logger.info("GetAllServices <====");
            return json_result;
        } catch (GatewayException e) {
//            throw new RuntimeException(e);
        }
        return null;
    }
}
