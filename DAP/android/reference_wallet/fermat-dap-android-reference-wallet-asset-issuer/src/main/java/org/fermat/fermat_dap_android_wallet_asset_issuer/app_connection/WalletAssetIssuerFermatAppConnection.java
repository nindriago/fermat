package org.fermat.fermat_dap_android_wallet_asset_issuer.app_connection;

import android.content.Context;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.engine.FooterViewPainter;
import com.bitdubai.fermat_android_api.engine.HeaderViewPainter;
import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.engine.NotificationPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.AppConnections;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;

import org.fermat.fermat_dap_android_wallet_asset_issuer.common.header.WalletAssetIssuerHeaderPainter;
import org.fermat.fermat_dap_android_wallet_asset_issuer.common.navigation_drawer.IssuerWalletNavigationViewPainter;
import org.fermat.fermat_dap_android_wallet_asset_issuer.factory.IssuerWalletFragmentFactory;
import org.fermat.fermat_dap_android_wallet_asset_issuer.sessions.AssetIssuerSession;

import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.AssetIssuerSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;

/**
 * Created by Matias Furszyfer on 2015.12.09..
 */
public class WalletAssetIssuerFermatAppConnection extends AppConnections<AssetIssuerSession> {

    IdentityAssetIssuer identityAssetIssuer;
    AssetIssuerWalletSupAppModuleManager manager;
    AssetIssuerSession assetIssuerSession;

    public WalletAssetIssuerFermatAppConnection(Context activity) {
        super(activity);
        this.identityAssetIssuer = identityAssetIssuer;
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new IssuerWalletFragmentFactory();
    }

    @Override
    public PluginVersionReference getPluginVersionReference() {
        return new PluginVersionReference(
                Platforms.DIGITAL_ASSET_PLATFORM,
                Layers.WALLET_MODULE,
                Plugins.ASSET_ISSUER,
                Developers.BITDUBAI,
                new Version()
        );
    }

    @Override
    public AbstractFermatSession getSession() {
        return new AssetIssuerSession();
    }

    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        return new IssuerWalletNavigationViewPainter(getContext(), getActiveIdentity());
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return new WalletAssetIssuerHeaderPainter();
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return null;
    }

    @Override
    public NotificationPainter getNotificationPainter(String code) {
        NotificationPainter notification = null;
        try {
            SettingsManager<AssetIssuerSettings> settingsManager;

            this.assetIssuerSession = this.getFullyLoadedSession();
            boolean enabledNotification = true;

            if (assetIssuerSession != null) {
                if (assetIssuerSession.getModuleManager() != null) {
                    manager = assetIssuerSession.getModuleManager();
                    settingsManager = assetIssuerSession.getModuleManager().getSettingsManager();
                    enabledNotification = settingsManager.loadAndGetSettings(assetIssuerSession.getAppPublicKey()).getNotificationEnabled();
                }
                String[] params = code.split("_");
                String notificationType = params[0];
                String senderActorPublicKey = params[1];

                if (enabledNotification) {
                    switch (notificationType) {
                        case "ASSET-ISSUER-DEBIT":
                            notification = new WalletAssetIssuerNotificationPainter("Wallet Issuer - Debit", senderActorPublicKey, "", "");
                            break;
                        case "ASSET-ISSUER-CREDIT":
                            notification = new WalletAssetIssuerNotificationPainter("Wallet Issuer Credit", senderActorPublicKey, "", "");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notification;
    }
}
