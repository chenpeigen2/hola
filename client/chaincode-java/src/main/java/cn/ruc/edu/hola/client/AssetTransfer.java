package cn.ruc.edu.hola.client;

public interface AssetTransfer {
    Asset CreateAsset(final String assetID, final String color, final int size,
                      final String owner, final int appraisedValue);

    Asset ReadAsset(final String assetID);

    Asset UpdateAsset(final String assetID, final String color, final int size,
                      final String owner, final int appraisedValue);

    void DeleteAsset(final String assetID);

    boolean AssetExists(final String assetID);

    String TransferAsset(final String assetID, final String newOwner);

    String GetAllAssets();
}
