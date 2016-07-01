package com.bitdubai.fermat_bnk_plugin.layer.wallet_module.bank_money.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_bnk_api.layer.bnk_bank_money_transaction.deposit.interfaces.DepositManager;
import com.bitdubai.fermat_bnk_api.layer.bnk_bank_money_transaction.hold.interfaces.HoldManager;
import com.bitdubai.fermat_bnk_api.layer.bnk_bank_money_transaction.unhold.interfaces.UnholdManager;
import com.bitdubai.fermat_bnk_api.layer.bnk_bank_money_transaction.withdraw.interfaces.WithdrawManager;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet.bank_money.interfaces.BankMoneyWalletManager;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet_module.BankMoneyWalletPreferenceSettings;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet_module.interfaces.BankMoneyWalletModuleManager;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet_module.interfaces.BankingWallet;

import java.util.List;
import java.util.UUID;

/**
 * Created by Yordin Alayn on 19.04.16.
 */
public class BankMoneyWalletModuleManagerImpl implements BankMoneyWalletModuleManager {

    private BankMoneyWalletManager  bankMoneyWalletManager;
    private DepositManager          depositManager;
    private WithdrawManager         withdrawManager;
    private HoldManager             holdManager;
    private UnholdManager           unholdManager;
    private PluginFileSystem        pluginFileSystem;
    private UUID                    pluginId;
    private Broadcaster             broadcaster;

    private BankingWallet bankingWallet;
    private SettingsManager<BankMoneyWalletPreferenceSettings> settingsManager;
    
    public BankMoneyWalletModuleManagerImpl(
            BankMoneyWalletManager  bankMoneyWalletManager,
            DepositManager          depositManager,
            WithdrawManager         withdrawManager,
            HoldManager             holdManager,
            UnholdManager           unholdManager,
            PluginFileSystem        pluginFileSystem,
            UUID                    pluginId,
            Broadcaster             broadcaster
    ){
        this.bankMoneyWalletManager = bankMoneyWalletManager;
        this.depositManager         = depositManager;
        this.withdrawManager        = withdrawManager;
        this.holdManager            = holdManager;
        this.unholdManager          = unholdManager;
        this.pluginFileSystem       = pluginFileSystem;
        this.pluginId               = pluginId;
        this.broadcaster            = broadcaster;
    }

    @Override
    public BankingWallet getBankingWallet() {
        if(bankingWallet == null)
            bankingWallet = new BankingWalletModuleImpl(bankMoneyWalletManager,depositManager,withdrawManager,holdManager,unholdManager,pluginFileSystem,pluginId,broadcaster);
        return bankingWallet;
    }

    @Override
    public SettingsManager<BankMoneyWalletPreferenceSettings> getSettingsManager() {
        if (this.settingsManager != null)
            return this.settingsManager;

        this.settingsManager = new SettingsManager<>(
                pluginFileSystem,
                pluginId
        );

        return this.settingsManager;
    }


    @Override
    public ActiveActorIdentityInformation getSelectedActorIdentity() throws CantGetSelectedActorIdentityException, ActorIdentityNotSelectedException {
        return null;
    }

    @Override
    public void createIdentity(String name, String phrase, byte[] profile_img) throws Exception {

    }

    @Override
    public void setAppPublicKey(String publicKey) {

    }

    @Override
    public int[] getMenuNotifications() {
        return new int[0];
    }
}
