package org.fermat.fermat_dap_android_wallet_redeem_point.app_connection;

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

import org.fermat.fermat_dap_android_wallet_redeem_point.common.header.WalletRedeemPointHeaderPainter;
import org.fermat.fermat_dap_android_wallet_redeem_point.factory.WalletRedeemPointFragmentFactory;
import org.fermat.fermat_dap_android_wallet_redeem_point.navigation_drawer.RedeemPointWalletNavigationViewPainter;
import org.fermat.fermat_dap_android_wallet_redeem_point.sessions.RedeemPointSession;
import org.fermat.fermat_dap_api.layer.dap_identity.redeem_point.interfaces.RedeemPointIdentity;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.RedeemPointSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;

/**
 * Created by Matias Furszyfer on 2015.12.09..
 */
public class WalletRedeemPointFermatAppConnection extends AppConnections<RedeemPointSession> {


    RedeemPointIdentity redeemPointIdentity;
    AssetRedeemPointWalletSubAppModule manager;
    RedeemPointSession redeemPointSession;

    public WalletRedeemPointFermatAppConnection(Context activity) {
        super(activity);
        this.redeemPointIdentity = redeemPointIdentity;
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new WalletRedeemPointFragmentFactory();
    }

    @Override
    public PluginVersionReference getPluginVersionReference() {
        return new PluginVersionReference(
                Platforms.DIGITAL_ASSET_PLATFORM,
                Layers.WALLET_MODULE,
                Plugins.REDEEM_POINT,
                Developers.BITDUBAI,
                new Version()
        );
    }

    @Override
    public AbstractFermatSession getSession() {
        return new RedeemPointSession();
    }


    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        return new RedeemPointWalletNavigationViewPainter(getContext(), getActiveIdentity());
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return new WalletRedeemPointHeaderPainter();
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return null;
    }

    @Override
    public NotificationPainter getNotificationPainter(String code) {
        NotificationPainter notification = null;
        try {
            SettingsManager<RedeemPointSettings> settingsManager;

            this.redeemPointSession = this.getFullyLoadedSession();
            boolean enabledNotification = true;

            if (redeemPointSession != null) {
                if (redeemPointSession.getModuleManager() != null) {
                    manager = redeemPointSession.getModuleManager();
                    settingsManager = redeemPointSession.getModuleManager().getSettingsManager();
                    enabledNotification = settingsManager.loadAndGetSettings(redeemPointSession.getAppPublicKey()).getNotificationEnabled();
                }
                String[] params = code.split("_");
                String notificationType = params[0];
                String senderActorPublicKey = params[1];

                if (enabledNotification) {
                    switch (notificationType) {
                        case "ASSET-REDEEM-DEBIT":
                            notification = new WalletRedeemPointNotificationPainter("Wallet Redeem Point - Debit", senderActorPublicKey, "", "");
                            break;
                        case "ASSET-REDEEM-CREDIT":
                            notification = new WalletRedeemPointNotificationPainter("Wallet Redeem Point Credit", senderActorPublicKey, "", "");
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
