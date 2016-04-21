package org.fermat.fermat_dap_api.layer.dap_wallet.asset_user_wallet.interfaces;

import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;

/**
 * Created by franklin on 05/10/15.
 */
public interface AssetUserWalletTransaction {
    String getAssetPublicKey();

    String getActualTransactionHash();

    String getTransactionHash();

    String getGenesisTransaction();

    DAPActor getActorFrom();

    DAPActor getActorTo();

    org.fermat.fermat_dap_api.layer.dap_wallet.common.enums.BalanceType getBalanceType();

    org.fermat.fermat_dap_api.layer.dap_wallet.common.enums.TransactionType getTransactionType();

    long getTimestamp();

    long getAmount();

    long getRunningBookBalance();

    long getRunningAvailableBalance();

    String getMemo();

    boolean isLocked();
}
