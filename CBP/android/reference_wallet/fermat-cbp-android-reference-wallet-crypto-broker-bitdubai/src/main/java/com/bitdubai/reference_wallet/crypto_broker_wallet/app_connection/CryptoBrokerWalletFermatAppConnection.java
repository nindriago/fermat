package com.bitdubai.reference_wallet.crypto_broker_wallet.app_connection;

import android.content.Context;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.engine.FooterViewPainter;
import com.bitdubai.fermat_android_api.engine.HeaderViewPainter;
import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.engine.NotificationPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.AppConnections;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.footer.CryptoBrokerWalletFooterPainter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.header.CryptoBrokerWalletHeaderPainter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.navigationDrawer.CryptoBrokerNavigationViewPainter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.notifications.CryptoBrokerNotificationPainter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.fragmentFactory.CryptoBrokerWalletFragmentFactory;
import com.bitdubai.reference_wallet.crypto_broker_wallet.session.CryptoBrokerWalletSession;

import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CANCEL_NEGOTIATION_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CONTRACT_COMPLETED_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CONTRACT_CUSTOMER_SUBMITTED_PAYMENT_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CONTRACT_EXPIRATION_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_NEW_CONTRACT_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_NEW_NEGOTIATION_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_WAITING_FOR_BROKER_NOTIFICATION;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CCW_CONTRACT_CANCELLED_NOTIFICATION;



/**
 * Created by Nelson Ramirez
 *
 * @since 2015.12.17
 */
public class CryptoBrokerWalletFermatAppConnection extends AppConnections<CryptoBrokerWalletSession> {

    public CryptoBrokerWalletFermatAppConnection(Context activity) {
        super(activity);
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new CryptoBrokerWalletFragmentFactory();
    }

    @Override
    public PluginVersionReference getPluginVersionReference() {
        return new PluginVersionReference(
                Platforms.CRYPTO_BROKER_PLATFORM,
                Layers.WALLET_MODULE,
                Plugins.CRYPTO_BROKER,
                Developers.BITDUBAI,
                new Version()
        );

    }

    @Override
    protected CryptoBrokerWalletSession getSession() {
        return new CryptoBrokerWalletSession();
    }

    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        return new CryptoBrokerNavigationViewPainter(getContext(), getFullyLoadedSession());
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return new CryptoBrokerWalletHeaderPainter(getContext(), getFullyLoadedSession());
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return new CryptoBrokerWalletFooterPainter(getContext(), getFullyLoadedSession());
    }

    @Override
    public NotificationPainter getNotificationPainter(String code) {
        switch (code){
            case CBW_CONTRACT_EXPIRATION_NOTIFICATION:
                return new CryptoBrokerNotificationPainter("Expiring contract.","A contract is about to expire, check your wallet.","");
            case CBW_NEW_NEGOTIATION_NOTIFICATION:
                return new CryptoBrokerNotificationPainter("New Negotiation","You have a new negotiation! Please check your wallet.","");
            case CBW_WAITING_FOR_BROKER_NOTIFICATION:
                return new CryptoBrokerNotificationPainter("Negotiation Update","You have received a negotiation update, check your wallet.","");
            case CBW_CANCEL_NEGOTIATION_NOTIFICATION:
                return new CryptoBrokerNotificationPainter("Negotiation Canceled","Check the Contract History, a customer has canceled a negotiation.","");
            case CBW_NEW_CONTRACT_NOTIFICATION:
                return new CryptoBrokerNotificationPainter("New Contract.","A new contract has been created, check your wallet.","");
            case CBW_CONTRACT_CUSTOMER_SUBMITTED_PAYMENT_NOTIFICATION:
                return new CryptoBrokerNotificationPainter("Contract Update","You just received a payment.","");
            case CBW_CONTRACT_COMPLETED_NOTIFICATION:
                return new CryptoBrokerNotificationPainter("Contract Completed","The contract has been completed.","");
            case CCW_CONTRACT_CANCELLED_NOTIFICATION:
                return new CryptoBrokerNotificationPainter("Contract Cancelled","The contract has been cancellede.","");
            default:
                return super.getNotificationPainter(code);
        }
    }
}
