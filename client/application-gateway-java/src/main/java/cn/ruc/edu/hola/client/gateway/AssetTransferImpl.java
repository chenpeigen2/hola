package cn.ruc.edu.hola.client.gateway;

import cn.ruc.edu.hola.client.Asset;
import cn.ruc.edu.hola.client.AssetTransfer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import org.hyperledger.fabric.client.*;
import org.hyperledger.fabric.client.identity.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class AssetTransferImpl implements AssetTransfer {

    private static final String MSP_ID = System.getenv().getOrDefault("MSP_ID", "Org1MSP");
    private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");
    private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "basic");

    // Path to crypto materials.
    private static final Path CRYPTO_PATH = Paths.get("../fabric-samples/test-network/organizations/peerOrganizations/org1.example.com");
    // Path to user certificate.
    private static final Path CERT_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/signcerts/cert.pem"));
    // Path to user private key directory.
    private static final Path KEY_DIR_PATH = CRYPTO_PATH.resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
    // Path to peer tls certificate.
    private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));

    // Gateway peer end point.
    private static final String PEER_ENDPOINT = "localhost:7051";
    private static final String OVERRIDE_AUTH = "peer0.org1.example.com";

    private final Contract contract;
    private final String assetId = "asset" + Instant.now().toEpochMilli();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static ManagedChannel newGrpcConnection() throws IOException {
        var credentials = TlsChannelCredentials.newBuilder()
                .trustManager(TLS_CERT_PATH.toFile())
                .build();
        return Grpc.newChannelBuilder(PEER_ENDPOINT, credentials)
                .overrideAuthority(OVERRIDE_AUTH)
                .build();
    }

    private static Identity newIdentity() throws IOException, CertificateException {
        var certReader = Files.newBufferedReader(CERT_PATH);
        var certificate = Identities.readX509Certificate(certReader);

        return new X509Identity(MSP_ID, certificate);
    }

    private static Signer newSigner() throws IOException, InvalidKeyException {
        var keyReader = Files.newBufferedReader(getPrivateKeyPath());
        var privateKey = Identities.readPrivateKey(keyReader);

        return Signers.newPrivateKeySigner(privateKey);
    }

    private static Path getPrivateKeyPath() throws IOException {
        try (var keyFiles = Files.list(KEY_DIR_PATH)) {
            return keyFiles.findFirst().orElseThrow();
        }
    }


    public static ConfigWrapper init() throws Exception {
        // The gRPC client connection should be shared by all Gateway connections to
        // this endpoint.
        var channel = newGrpcConnection();

        var builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
                // Default timeouts for different gRPC calls
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
        return new ConfigWrapper(channel, builder.connect());
    }

    public static void destroy(ConfigWrapper wrapper) throws Exception {
        wrapper.gateway.close();
        wrapper.channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }

    public AssetTransferImpl(final Gateway gateway) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        // Get a network instance representing the channel where the smart contract is
        // deployed.
        var network = gateway.getNetwork(CHANNEL_NAME);

        // Get the smart contract from the network.
        contract = network.getContract(CHAINCODE_NAME);

        initLedger(); // init the ledger
    }

    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
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
