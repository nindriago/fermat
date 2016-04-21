package org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_user.exceptions;

import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CryptoWalletException;

/**
 * The interface <code>CantGetCryptoWalletException</code>
 * is thrown when i cant RETURN the wallet.
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 18/06/15.
 * @version 1.0
 */
public class CantGetUserWalletModuleException extends CryptoWalletException {

    public static final String DEFAULT_MESSAGE = "CAN'T GET USER WALLET MODULE REQUESTED EXCEPTION";

    public CantGetUserWalletModuleException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantGetUserWalletModuleException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantGetUserWalletModuleException(final String message) {
        this(message, null);
    }

    public CantGetUserWalletModuleException() {
        this(DEFAULT_MESSAGE);
    }
}
