package org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer;

import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.exceptions.CantGetDefaultLanguageException;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.exceptions.CantGetDefaultSkinException;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.exceptions.CantLoadWalletSettings;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.exceptions.CantSetDefaultLanguageException;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.exceptions.CantSetDefaultSkinException;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_settings.interfaces.WalletSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AssetIssuerSettings
 *
 * @author Francisco Vasquez
 * @version 1.0
 */
public class AssetIssuerSettings implements WalletSettings {

    private boolean isPresentationHelpEnabled;
    private boolean isContactsHelpEnabled;
    private List<BlockchainNetworkType> blockchainNetwork;
    private int blockchainNetworkPosition;
    private boolean notificationEnabled;

    public int getBlockchainNetworkPosition() {
        return blockchainNetworkPosition;
    }

    public void setBlockchainNetworkPosition(int blockchainNetworkPosition) {
        this.blockchainNetworkPosition = blockchainNetworkPosition;
    }

    public List<BlockchainNetworkType> getBlockchainNetwork() {
        return blockchainNetwork;
    }

    public void setBlockchainNetwork(List<BlockchainNetworkType> blockchainNetwork) {
        this.blockchainNetwork = blockchainNetwork;
    }

    public boolean isContactsHelpEnabled() {
        return isContactsHelpEnabled;
    }

    public boolean isPresentationHelpEnabled() {
        return isPresentationHelpEnabled;
    }

    @Override
    public UUID getDefaultLanguage() throws CantGetDefaultLanguageException, CantLoadWalletSettings {
        return null;
    }

    @Override
    public UUID getDefaultSkin() throws CantGetDefaultSkinException, CantLoadWalletSettings {
        return null;
    }

    @Override
    public void setDefaultLanguage(UUID languageId) throws CantSetDefaultLanguageException, CantLoadWalletSettings {

    }

    @Override
    public void setDefaultSkin(UUID skinId) throws CantSetDefaultSkinException, CantLoadWalletSettings {

    }

    public void setIsContactsHelpEnabled(boolean isContactsHelpEnabled) {
        this.isContactsHelpEnabled = isContactsHelpEnabled;
    }

    @Override
    public void setIsPresentationHelpEnabled(boolean isPresentationHelpEnabled) {
        this.isPresentationHelpEnabled = isPresentationHelpEnabled;
    }

    public boolean getNotificationEnabled() {
        return this.notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }
}
