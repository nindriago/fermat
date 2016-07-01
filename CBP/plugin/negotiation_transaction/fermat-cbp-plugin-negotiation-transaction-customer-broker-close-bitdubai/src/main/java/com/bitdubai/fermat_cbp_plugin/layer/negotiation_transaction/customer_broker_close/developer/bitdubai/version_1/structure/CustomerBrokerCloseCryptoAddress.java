package com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_close.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.VaultType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_bch_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_close.exceptions.CantCryptoAddressesNewException;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_close.exceptions.CantGenerateAndRegisterCryptoAddressException;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_close.interfaces.AbstractCryptoAddress;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_close.interfaces.CustomerBrokerCloseCryptoAddressRequest;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_close.utils.CryptoVaultSelector;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_close.utils.WalletManagerSelector;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

/**
 * Created by Yordin Alayn on 27.12.15.
 */
public class CustomerBrokerCloseCryptoAddress extends AbstractCryptoAddress{

    /*Represent the Error Manager*/
    private ErrorManager                errorManager;

    /*Represent the Plugins Version*/
    private PluginVersionReference      pluginVersionReference;

    public CustomerBrokerCloseCryptoAddress(
        final CryptoAddressBookManager  cryptoAddressBookManager,
        final CryptoVaultSelector       cryptoVaultSelector,
        final WalletManagerSelector     walletManagerSelector,
        final ErrorManager              errorManager,
        final PluginVersionReference    pluginVersionReference
    ){
        super(cryptoAddressBookManager, cryptoVaultSelector, walletManagerSelector);
        this.errorManager           = errorManager;
        this.pluginVersionReference = pluginVersionReference;
    }

    @Override
    public CryptoAddress CryptoAddressesNew(final CustomerBrokerCloseCryptoAddressRequest request) throws CantCryptoAddressesNewException {

        try {
            CryptoAddress cryptoAddress = null;

            Platforms platform = Platforms.CRYPTO_BROKER_PLATFORM;
            VaultType vaultType = VaultType.CRYPTO_CURRENCY_VAULT;

            cryptoAddress = this.generateAndRegisterCryptoAddress(
                    platform,
                    vaultType,
                    request
            );

            return cryptoAddress;

        } catch (CantGenerateAndRegisterCryptoAddressException e){
            errorManager.reportUnexpectedPluginException(this.pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
            throw new CantCryptoAddressesNewException(e, "", "There was an error trying to generate the crypto address.");
        }
    }
}
