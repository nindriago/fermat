package com.bitdubai.reference_wallet.crypto_customer_wallet.common.models;

import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_bnk_api.all_definition.enums.BankAccountType;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet.bank_money.interfaces.BankAccountNumber;

/**
 * Created by nelson on 03/01/16.
 */
public class BankAccountData implements BankAccountNumber {


    private FiatCurrency currencyType;
    private BankAccountType accountType;
    private String bankName;
    private String account;
    private String alias;


    public BankAccountData(FiatCurrency currencyType, BankAccountType accountType, String bankName, String accountNumber, String alias) {
        this.currencyType = currencyType;
        this.accountType = accountType;
        this.bankName = bankName;
        this.account = accountNumber;
        this.alias = alias;
    }

    @Override
    public String getBankName() {
        return bankName;
    }

    @Override
    public BankAccountType getAccountType() {
        return accountType;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public FiatCurrency getCurrencyType() {
        return currencyType;
    }

    public boolean isAllDataFilled() {
        return currencyType != null &&
                accountType != null &&
                bankName != null && !bankName.isEmpty() &&
                account != null && !account.isEmpty() &&
                alias != null && !alias.isEmpty();
    }

    @Override
    public String toString() {
        return "Bank: " + bankName +
                "\nAccount Type: " + accountType.getFriendlyName() +
                "\nNumber: " + account;
    }
}
