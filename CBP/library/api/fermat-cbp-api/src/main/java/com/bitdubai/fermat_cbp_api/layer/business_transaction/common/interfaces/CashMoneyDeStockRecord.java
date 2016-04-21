package com.bitdubai.fermat_cbp_api.layer.business_transaction.common.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_cbp_api.all_definition.enums.OriginTransaction;


/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 22/12/15.
 */
public class CashMoneyDeStockRecord extends AbstractDeStockRecord {

    FiatCurrency fiatCurrency;
    String cshWalletPublicKey;
    String cashReference;

    public CashMoneyDeStockRecord(BusinessTransactionRecord businessTransactionRecord) {

        this.publicKeyActor = businessTransactionRecord.getBrokerPublicKey();
        this.fiatCurrency = businessTransactionRecord.getFiatCurrency();
        this.cbpWalletPublicKey = businessTransactionRecord.getCBPWalletPublicKey();
        this.cshWalletPublicKey = businessTransactionRecord.getExternalWalletPublicKey();
        cashReference = businessTransactionRecord.getFiatCurrency().getCode();
        this.amount = parseLongToBigDecimal(businessTransactionRecord.getPaymentAmount());
        this.memo = generateMemo(businessTransactionRecord.getContractHash());
        this.priceReference = businessTransactionRecord.getPriceReference();
        this.originTransaction = OriginTransaction.SALE;

    }

    public FiatCurrency getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(FiatCurrency fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public String getCshWalletPublicKey() {
        return cshWalletPublicKey;
    }

    public void setCshWalletPublicKey(String cshWalletPublicKey) {
        this.cshWalletPublicKey = cshWalletPublicKey;
    }

    public String getCashReference() {
        return cashReference;
    }

    public void setCashReference(String cashReference) {
        this.cashReference = cashReference;
    }

    @Override
    public String toString() {
        return "CashMoneyDeStockRecord{" +
                abstractToString() +
                "fiatCurrency=" + fiatCurrency +
                ", cshWalletPublicKey='" + cshWalletPublicKey + '\'' +
                ", cashReference='" + cashReference + '\'' +
                '}';
    }
}
