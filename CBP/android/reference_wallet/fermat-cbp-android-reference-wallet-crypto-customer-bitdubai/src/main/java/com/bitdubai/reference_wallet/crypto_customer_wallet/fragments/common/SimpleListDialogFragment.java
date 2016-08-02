package com.bitdubai.reference_wallet.crypto_customer_wallet.fragments.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet.bank_money.interfaces.BankAccountNumber;
import com.bitdubai.fermat_cbp_api.all_definition.enums.MoneyType;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantGetProviderInfoException;
import com.bitdubai.fermat_cer_api.layer.provider.interfaces.CurrencyExchangeRateProviderManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.InstalledWallet;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nelson on 28/12/15.
 */
public class SimpleListDialogFragment<T> extends DialogFragment {
    private final String LANGUAGE = Resources.getSystem().getConfiguration().locale.getLanguage();

    private String title = null;
    private int titleRes = -1;
    private List<T> choices;
    private ItemSelectedListener listener;

    public SimpleListDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> data = getStringListData(choices);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, data);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (titleRes == -1)
            builder.setTitle(title);
        else
            builder.setTitle(titleRes);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onClick(DialogInterface dialogInterface, int which) {
                listener.onItemSelected(choices.get(which));
            }
        });

        return builder.create();
    }

    private List<String> getStringListData(List<T> choices) {
        List<String> data = new ArrayList<>();

        for (T choice : choices) {
            if (choice instanceof InstalledWallet) {
                InstalledWallet installedWallet = (InstalledWallet) choice;
                data.add(installedWallet.getWalletName());

            } else if (choice instanceof BankAccountNumber) {
                BankAccountNumber bankAccount = (BankAccountNumber) choice;
                data.add(bankAccount.getAlias());

            } else if (choice instanceof CurrencyExchangeRateProviderManager) {
                CurrencyExchangeRateProviderManager provider = (CurrencyExchangeRateProviderManager) choice;
                try {
                    data.add(provider.getProviderName());
                } catch (CantGetProviderInfoException ignored) {
                }

            } else if (choice instanceof Currency) {
                final Currency currency = (Currency) choice;
                data.add(currency.getFriendlyName() + " (" + currency.getCode() + ")");

            } else if (choice instanceof MoneyType) {
                MoneyType moneyType = (MoneyType) choice;

                switch (moneyType) {
                    case BANK:
                        data.add(LANGUAGE.equalsIgnoreCase("es") ? "Tranferencia Bancaria" : "Bank Money");
                        break;
                    case CASH_DELIVERY:
                        data.add(LANGUAGE.equalsIgnoreCase("es") ? "Envio de Efectivo" : "Cash Delivery");
                        break;
                    case CASH_ON_HAND:
                        data.add(LANGUAGE.equalsIgnoreCase("es") ? "Efectivo en Mano":"Cash on Hand");
                        break;
                    case CRYPTO:
                        data.add(LANGUAGE.equalsIgnoreCase("es") ? "Dinero Crypto": "Crypto Money");
                        break;
                }
            } else {
                data.add(choice.toString());
            }
        }

        return data;
    }

    public void setListener(ItemSelectedListener listener) {
        this.listener = listener;
    }

    public void configure(String title, List<T> choices) {
        this.title = title;
        this.choices = choices;
    }

    public void configure(int titleRes, List<T> choices) {
        this.titleRes = titleRes;
        this.choices = choices;
    }

    public interface ItemSelectedListener<T> {
        void onItemSelected(T selectedItem);
    }
}
