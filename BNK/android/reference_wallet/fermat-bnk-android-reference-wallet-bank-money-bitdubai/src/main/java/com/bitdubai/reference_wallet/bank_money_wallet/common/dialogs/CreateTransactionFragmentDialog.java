package com.bitdubai.reference_wallet.bank_money_wallet.common.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.enums.WalletsPublicKeys;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_bnk_api.all_definition.bank_money_transaction.BankTransactionParameters;
import com.bitdubai.fermat_bnk_api.all_definition.enums.TransactionType;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;
import com.bitdubai.reference_wallet.bank_money_wallet.R;
import com.bitdubai.reference_wallet.bank_money_wallet.session.BankMoneyWalletSession;
import com.bitdubai.reference_wallet.bank_money_wallet.util.NumberInputFilter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Alejandro Bicelis on 12/15/2015.
 */

public class CreateTransactionFragmentDialog extends Dialog implements
        View.OnClickListener {

    public Activity activity;
    public Dialog d;

    //private CreateContactDialogCallback createContactDialogCallback;


    /**
     * Resources
     */
    private WalletResourcesProviderManager walletResourcesProviderManager;
    private BankMoneyWalletSession bankMoneyWalletSession;
    private Resources resources;
    private TransactionType transactionType;

    /**
     *  Contact member
     */
    //private WalletContact walletContact;
    //private String user_address_wallet = "";

    /**
     *  UI components
     */
    FermatTextView dialogTitle;
    LinearLayout dialogTitleLayout;
    EditText amountText;
    AutoCompleteTextView memoText;
    Button applyBtn;
    Button cancelBtn;
    String account;
    FiatCurrency fiatCurrency;
    ErrorManager errorManager;

    /**
     * Allow the zxing engine use the default argument for the margin variable
     */
    //private Bitmap contactPicture;
    //private EditText txt_address;

   //private Typeface tf;
    /**
     *
     * @param a
     * @param
     */


    public CreateTransactionFragmentDialog(ErrorManager errorManager,Activity a, BankMoneyWalletSession bankMoneyWalletSession, Resources resources, TransactionType transactionType,String account,FiatCurrency fiatCurrency) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
        this.bankMoneyWalletSession = bankMoneyWalletSession;
        this.transactionType = transactionType;
        this.resources = resources;
        this.account=account;
        this.fiatCurrency =fiatCurrency;
        this.errorManager = errorManager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupScreenComponents();

    }

    private void setupScreenComponents(){

        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.bw_create_transaction_dialog);


            dialogTitleLayout = (LinearLayout) findViewById(R.id.bnk_ctd_title_layout);
            dialogTitle = (FermatTextView) findViewById(R.id.bnk_ctd_title);
            amountText = (EditText) findViewById(R.id.bnk_ctd_amount);
            memoText = (AutoCompleteTextView) findViewById(R.id.bnk_ctd_memo);
            applyBtn = (Button) findViewById(R.id.bnk_ctd_apply_transaction_btn);
            //cancelBtn = (Button) findViewById(R.id.bnk_ctd_cancel_transaction_btn);

            //dialogTitleLayout.setBackgroundColor(getTransactionTitleColor());
            dialogTitle.setText(getTransactionTitleText());
            amountText.setFilters(new InputFilter[]{new NumberInputFilter(9, 2)});

            //cancelBtn.setOnClickListener(this);
            applyBtn.setOnClickListener(this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private String getTransactionTitleText()
    {
        if (transactionType == TransactionType.DEBIT)
            return resources.getString(R.string.bw_withdrawal_transaction_text);
        else
            return resources.getString(R.string.bw_deposit_transaction_text);
    }

    private int getTransactionTitleColor()
    {
        if (transactionType == TransactionType.DEBIT)
            return resources.getColor(R.color.bnk_fab_color_normal_w);
        else
            return resources.getColor(R.color.bnk_fab_color_normal_d);
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        /*if (i == R.id.bnk_ctd_cancel_transaction_btn) {
            dismiss();
        }*/
        if( i == R.id.bnk_ctd_apply_transaction_btn){
            applyTransaction();
        }
    }


    private void applyTransaction() {
        try {

            final String memo = memoText.getText().toString();
            String amount = amountText.getText().toString();

            if (amount.equals("")) {
                Toast.makeText(activity.getApplicationContext(), "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if(new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
            {
                Toast.makeText(activity.getApplicationContext(), "Amount cannot be zero", Toast.LENGTH_SHORT).show();
                return;
            }

            if (transactionType == TransactionType.DEBIT) {
                System.out.println("DIALOG = "+TransactionType.DEBIT.getCode());
                BankTransactionParameters t = new BankTransactionParameters() {

                    @Override
                    public UUID getTransactionId() {
                        return UUID.randomUUID();
                    }

                    @Override
                    public String getPublicKeyPlugin() {
                        return null;
                    }

                    @Override
                    public String getPublicKeyWallet() {
                        return WalletsPublicKeys.BNK_BANKING_WALLET.getCode();//"banking_wallet";
                    }

                    @Override
                    public String getPublicKeyActor() {
                        return "pkeyActorRefWallet";
                    }

                    @Override
                    public BigDecimal getAmount() {
                        BigDecimal bAmount=new BigDecimal(amountText.getText().toString());
                        return bAmount;
                    }

                    @Override
                    public String getAccount() {
                        return account;
                    }

                    @Override
                    public FiatCurrency getCurrency() {
                        return fiatCurrency;
                    }

                    @Override
                    public String getMemo() {
                        return  memoText.getText().toString();
                    }
                };
                bankMoneyWalletSession.getModuleManager().getBankingWallet().makeAsyncWithdraw(t);
            }
            if (transactionType == TransactionType.CREDIT) {
                System.out.println("DIALOG = "+TransactionType.CREDIT.getCode());
                BankTransactionParameters t = new BankTransactionParameters() {

                    @Override
                    public UUID getTransactionId() {
                        return UUID.randomUUID();
                    }

                    @Override
                    public String getPublicKeyPlugin() {
                        return null;
                    }

                    @Override
                    public String getPublicKeyWallet() {
                        return WalletsPublicKeys.BNK_BANKING_WALLET.getCode();//"banking_wallet";
                    }

                    @Override
                    public String getPublicKeyActor() {
                        return "pkeyActorRefWallet";
                    }

                    @Override
                    public BigDecimal getAmount() {
                        BigDecimal bAmount=new BigDecimal(amountText.getText().toString());
                        return bAmount;
                    }

                    @Override
                    public String getAccount() {
                        return account;
                    }

                    @Override
                    public FiatCurrency getCurrency() {
                        return fiatCurrency;
                    }

                    @Override
                    public String getMemo() {
                        return  memoText.getText().toString();
                    }
                };
                bankMoneyWalletSession.getModuleManager().getBankingWallet().makeAsyncDeposit(t);
            }
        } catch (Exception e) {
            errorManager.reportUnexpectedWalletException(Wallets.BNK_BANKING_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, e);
            bankMoneyWalletSession.getErrorManager().reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(e));
            Toast.makeText(activity.getApplicationContext(), "There's been an error, please try again" +  e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        dismiss();
    }

}