package cn.ruc.edu.hola.client.gateway;

import cn.ruc.edu.hola.client.Asset;
import cn.ruc.edu.hola.client.AssetTransfer;
import org.hyperledger.fabric.client.*;

import java.nio.charset.StandardCharsets;

public class AssetTransferImpl extends BasicTransfer implements AssetTransfer {

    public AssetTransferImpl(final Gateway gateway) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(CHANNEL_NAME);

        // Get the smart contract from the network.
        contract = network.getContract(CHAINCODE_NAME);

        initLedger(); // init the ledger
    }


    /**
     * This type of transaction would typically only be run once by an application
     * the first time it was started after its initial deployment. A new version of
     * the chaincode deployed later would likely not need to run an "init" function.
     */
    private void initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: InitLedger, function creates the initial set of assets on the ledger");

        contract.submitTransaction("InitLedger");

        System.out.println("*** Transaction committed successfully");
    }


    @Override
    public Asset createAsset(String assetID, String color, int size, String owner, int appraisedValue) {
        System.out.println("\n--> Submit Transaction: CreateAsset, creates new asset with ID, Color, Size, Owner and AppraisedValue arguments");

//        contract.submitTransaction("CreateAsset", assetId, "yellow", "5", "Tom", "1300");

        try {
            contract.submitTransaction("CreateAsset", assetID, color, String.valueOf(size), owner, String.valueOf(appraisedValue));
        } catch (EndorseException | SubmitException | CommitStatusException e) {
            throw new RuntimeException(e);
        } catch (CommitException e) {
            throw new RuntimeException(e);
        }

        System.out.println("*** Transaction committed successfully");
        return null;
    }

    @Override
    public Asset readAsset(String assetID) {
        System.out.println("\n--> Evaluate Transaction: ReadAsset, function returns all the current assets on the ledger");

        byte[] result;
        try {
            result = contract.evaluateTransaction("ReadAsset", assetID);
        } catch (GatewayException e) {
            throw new RuntimeException(e);
        }

        System.out.println("*** Result: " + prettyJson(result));
        return null;
    }

    @Override
    public Asset updateAsset(String assetID, String color, int size, String owner, int appraisedValue) {

        try {
            System.out.println("\n--> Submit Transaction: UpdateAsset asset70, asset70 does not exist and should return an error");

//            contract.submitTransaction("UpdateAsset", "asset70", "blue", "5", "Tomoko", "300");
            contract.submitTransaction("UpdateAsset", assetID, color, String.valueOf(size), owner, String.valueOf(appraisedValue));

            System.out.println("******** FAILED to return an error");
        } catch (EndorseException | SubmitException | CommitStatusException e) {
            System.out.println("*** Successfully caught the error: ");
            e.printStackTrace(System.out);
            System.out.println("Transaction ID: " + e.getTransactionId());

            var details = e.getDetails();
            if (!details.isEmpty()) {
                System.out.println("Error Details:");
                for (var detail : details) {
                    System.out.println("- address: " + detail.getAddress() + ", mspId: " + detail.getMspId()
                            + ", message: " + detail.getMessage());
                }
            }
        } catch (CommitException e) {
            System.out.println("*** Successfully caught the error: " + e);
            e.printStackTrace(System.out);
            System.out.println("Transaction ID: " + e.getTransactionId());
            System.out.println("Status code: " + e.getCode());
        }

        return null;
    }

    @Override
    public void deleteAsset(String assetID) {
        System.out.println("\n--> Submit Transaction: DeleteAsset");
        try {
            contract.submitTransaction("DeleteAsset", assetID);
        } catch (EndorseException | SubmitException | CommitStatusException e) {
            throw new RuntimeException(e);
        } catch (CommitException e) {
            throw new RuntimeException(e);
        }
        System.out.println("*** Transaction committed successfully");
    }

    @Override
    public boolean assetExists(String assetID) {
        System.out.println("\n--> Evaluate Transaction: assetExists");

        byte[] result;
        try {
            result = contract.evaluateTransaction("AssetExists", assetID);
        } catch (GatewayException e) {
            throw new RuntimeException(e);
        }

        System.out.println("*** Result: " + prettyJson(result));
        return false;
    }

    @Override
    public String transferAsset(String assetID, String newOwner) {
        System.out.println("\n--> Async Submit Transaction: TransferAsset, updates existing asset owner");

        try {
            var commit = contract.newProposal("TransferAsset")
                    .addArguments(assetID, newOwner)
                    .build()
                    .endorse()
                    .submitAsync();

            var result = commit.getResult();
            var oldOwner = new String(result, StandardCharsets.UTF_8);

            System.out.println("*** Successfully submitted transaction to transfer ownership from " + oldOwner + " to Saptha");
            System.out.println("*** Waiting for transaction commit");

            var status = commit.getStatus();
            if (!status.isSuccessful()) {
                throw new RuntimeException("Transaction " + status.getTransactionId() +
                        " failed to commit with status code " + status.getCode());
            }

            System.out.println("*** Transaction committed successfully");
        } catch (EndorseException e) {
            throw new RuntimeException(e);
        } catch (SubmitException e) {
            throw new RuntimeException(e);
        } catch (CommitStatusException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String getAllAssets() {
        System.out.println("\n--> Evaluate Transaction: GetAllAssets, function returns all the current assets on the ledger");

        byte[] result;
        try {
            result = contract.evaluateTransaction("GetAllAssets");
        } catch (GatewayException e) {
            throw new RuntimeException(e);
        }

        System.out.println("*** Result: " + prettyJson(result));
        return null;
    }
}
