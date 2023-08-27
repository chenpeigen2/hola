package cn.ruc.edu.hola.client;

@Deprecated
public interface AssetTransfer {
    Asset createAsset(final String assetID, final String color, final int size,
                      final String owner, final int appraisedValue);

    Asset readAsset(final String assetID);

    Asset updateAsset(final String assetID, final String color, final int size,
                      final String owner, final int appraisedValue);

    void deleteAsset(final String assetID);

    boolean assetExists(final String assetID);

    String transferAsset(final String assetID, final String newOwner);

    String getAllAssets();
}
