package org.fermat.fermat_dap_android_wallet_asset_user.app_connection;

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

import org.fermat.fermat_dap_android_wallet_asset_user.common.header.WalletAssetUserHeaderPainter;
import org.fermat.fermat_dap_android_wallet_asset_user.factory.WalletAssetUserFragmentFactory;
import org.fermat.fermat_dap_android_wallet_asset_user.navigation_drawer.UserWalletNavigationViewPainter;
import org.fermat.fermat_dap_android_wallet_asset_user.sessions.AssetUserSession;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_user.interfaces.IdentityAssetUser;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_user.AssetUserSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_user.interfaces.AssetUserWalletSubAppModuleManager;

/**
 * Created by Matias Furszyfer on 2015.12.09..
 */
public class WalletAssetUserFermatAppConnection extends AppConnections<AssetUserSession> {

    IdentityAssetUser identityAssetUser;
    AssetUserWalletSubAppModuleManager manager;
    AssetUserSession assetUserSession;

    public WalletAssetUserFermatAppConnection(Context activity) {
        super(activity);
        this.identityAssetUser = identityAssetUser;
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new WalletAssetUserFragmentFactory();
    }

    @Override
    public PluginVersionReference getPluginVersionReference() {
        return new PluginVersionReference(
                Platforms.DIGITAL_ASSET_PLATFORM,
                Layers.WALLET_MODULE,
                Plugins.ASSET_USER,
                Developers.BITDUBAI,
                new Version()
        );
    }

    @Override
    public AbstractFermatSession getSession() {
        return new AssetUserSession();
    }


    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        return new UserWalletNavigationViewPainter(getContext(), getActiveIdentity());
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return new WalletAssetUserHeaderPainter();
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return null;
    }

    @Override
    public NotificationPainter getNotificationPainter(String code) {
        NotificationPainter notification = null;
        try {
            SettingsManager<AssetUserSettings> settingsManager;

            this.assetUserSession = this.getFullyLoadedSession();
            boolean enabledNotification = true;

            if (assetUserSession != null) {
                if (assetUserSession.getModuleManager() != null) {
                    manager = assetUserSession.getModuleManager();
                    settingsManager = assetUserSession.getModuleManager().getSettingsManager();
                    enabledNotification = settingsManager.loadAndGetSettings(assetUserSession.getAppPublicKey()).getNotificationEnabled();
                }
                String[] params = code.split("_");
                String notificationType = params[0];
                String senderActorPublicKey = params[1];

                if (enabledNotification) {
                    switch (notificationType) {
                        case "ASSET-USER-DEBIT":
                            notification = new WalletAssetUserNotificationPainter("Wallet User - Debit", senderActorPublicKey, "", "");
                            break;
                        case "ASSET-USER-CREDIT":
                            notification = new WalletAssetUserNotificationPainter("Wallet User Credit", senderActorPublicKey, "", "");
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
