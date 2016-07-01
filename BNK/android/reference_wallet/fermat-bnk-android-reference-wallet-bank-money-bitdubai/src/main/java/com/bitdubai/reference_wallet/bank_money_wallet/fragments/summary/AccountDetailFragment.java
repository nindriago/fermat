package com.bitdubai.reference_wallet.bank_money_wallet.fragments.summary;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_api.layer.all_definition.enums.WalletsPublicKeys;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_bnk_api.all_definition.constants.BankWalletBroadcasterConstants;
import com.bitdubai.fermat_bnk_api.all_definition.enums.BankTransactionStatus;
import com.bitdubai.fermat_bnk_api.all_definition.enums.TransactionType;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet.bank_money.interfaces.BankAccountNumber;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet.bank_money.interfaces.BankMoneyTransactionRecord;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet_module.interfaces.BankMoneyWalletModuleManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.reference_wallet.bank_money_wallet.R;
import com.bitdubai.reference_wallet.bank_money_wallet.common.adapters.TransactionListAdapter;
import com.bitdubai.reference_wallet.bank_money_wallet.common.dialogs.CreateTransactionFragmentDialog;
import com.bitdubai.reference_wallet.bank_money_wallet.session.BankMoneyWalletSession;
import com.bitdubai.reference_wallet.bank_money_wallet.util.CommonLogger;
import com.bitdubai.reference_wallet.bank_money_wallet.util.ReferenceWalletConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by memo on 08/12/15.
 */
public class AccountDetailFragment extends FermatWalletListFragment<BankMoneyTransactionRecord> implements FermatListItemListeners<BankMoneyTransactionRecord>, DialogInterface.OnDismissListener {


    private BankMoneyWalletModuleManager moduleManager;
    private ErrorManager errorManager;
    private ArrayList<BankMoneyTransactionRecord> transactionList;
    private String walletPublicKey = WalletsPublicKeys.BNK_BANKING_WALLET.getCode();//"banking_wallet";
    private BankAccountNumber bankAccountNumber;

    com.getbase.floatingactionbutton.FloatingActionsMenu fab;
    CreateTransactionFragmentDialog dialog;

    private View emtyView;

    private FermatTextView bookTextView;
    private FermatTextView availableTextView;
    private FermatTextView balanceText;
    private FermatTextView availableText;
    private FermatTextView bookText;
    private FermatTextView aliasText;
    private FermatTextView accountText;
    private ImageView imageView;
    private FermatTextView header;
    private int imageAccount;

    private PresentationDialog presentationDialog;

    private static final String TAG = "AccountListActivityFragment";

    public AccountDetailFragment() {
    }

    public static AccountDetailFragment newInstance() {
        return new AccountDetailFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        BankAccountNumber data = (BankAccountNumber) appSession.getData("account_data");
        imageAccount = (int) appSession.getData("account_image");
        try {

            moduleManager = ((BankMoneyWalletSession) appSession).getModuleManager();
            errorManager = appSession.getErrorManager();
        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(
                        Wallets.BNK_BANKING_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
        }
        System.out.println("DATA =" + data.getAccount());
        bankAccountNumber = data;
        transactionList = (ArrayList) getMoreDataAsync(FermatRefreshTypes.NEW, 0);
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);
        this.emtyView = layout.findViewById(R.id.no_transactions);
        imageView = (ImageView) layout.findViewById(R.id.bw_account_image);
        imageView.setImageResource(imageAccount);
        imageView.setVisibility(View.VISIBLE);

        header = (FermatTextView) layout.findViewById(R.id.textView_header_text);
        header.setText(moduleManager.getBankingWallet().getBankName());
        this.fab = (com.getbase.floatingactionbutton.FloatingActionsMenu) layout.findViewById(R.id.bw_fab_multiple_actions);
        this.availableTextView = (FermatTextView) layout.findViewById(R.id.available_balance);
        this.bookTextView = (FermatTextView) layout.findViewById(R.id.book_balance);
        presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                .setBannerRes(R.drawable.bw_banner)
                .setBody(R.string.bnk_bank_money_wallet_account_body)
                .setTitle("prueba Title")
                .setSubTitle(R.string.bnk_bank_money_wallet_account_subTitle)
                .setTextFooter(R.string.bnk_bank_money_wallet_account_footer).setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                .build();
        List<BankAccountNumber> tempList = new ArrayList<>();
        tempList.add(bankAccountNumber);


        layout.findViewById(R.id.bw_fab_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateTransactionDialog(TransactionType.DEBIT);
            }
        });

        layout.findViewById(R.id.bw_fab_deposit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCreateTransactionDialog(TransactionType.CREDIT);
            }
        });
        configureToolbar();

        accountText = (FermatTextView) layout.findViewById(R.id.account);
        aliasText = (FermatTextView) layout.findViewById(R.id.account_alias);
        balanceText = (FermatTextView) layout.findViewById(R.id.balance_text);
        availableText = (FermatTextView) layout.findViewById(R.id.available_text);
        bookText = (FermatTextView) layout.findViewById(R.id.book_text);
        updateBalance();
        showOrHideNoTransactionsView(transactionList.isEmpty());
    }

    private void launchCreateTransactionDialog(TransactionType transactionType) {
        dialog = new CreateTransactionFragmentDialog( errorManager,getActivity(), (BankMoneyWalletSession) appSession, getResources(), transactionType, bankAccountNumber.getAccount(), bankAccountNumber.getCurrencyType());
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    private void updateBalance() {

        accountText.setText(bankAccountNumber.getAccount());
        aliasText.setText(bankAccountNumber.getAlias());
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        availableTextView.setText(df.format(moduleManager.getBankingWallet().getAvailableBalance(bankAccountNumber.getAccount())) + " " + bankAccountNumber.getCurrencyType().getCode());
        bookTextView.setText(df.format(moduleManager.getBankingWallet().getBookBalance(bankAccountNumber.getAccount())) + " " + bankAccountNumber.getCurrencyType().getCode());
        balanceText.setTextColor(getResources().getColor(R.color.text_color_soft_blue));
        if (availableTextView.getText().equals(bookTextView.getText())) {
            bookTextView.setVisibility(View.GONE);
            bookText.setVisibility(View.GONE);
        } else {
            bookTextView.setVisibility(View.VISIBLE);
            bookText.setVisibility(View.VISIBLE);
        }
    }

    private void configureToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getToolbar().setBackground(getResources().getDrawable(R.drawable.bw_header_gradient_background, null));
        else
            getToolbar().setBackground(getResources().getDrawable(R.drawable.bw_header_gradient_background));
        getToolbar().setNavigationIcon(R.drawable.bw_back_icon_action_bar);
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.bw_account_detail_summary;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.transactions_swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.transactions_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return false;
    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                transactionList = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(transactionList);
                showOrHideNoTransactionsView(transactionList.isEmpty());
                updateBalance();
            }
        }
    }

    private void showOrHideNoTransactionsView(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            emtyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emtyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            //TODO: show error, toast?
        }
    }

    @Override
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            adapter = new TransactionListAdapter(getActivity(), transactionList);
            adapter.setFermatListEventListener(this);
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }

    @Override
    public void onItemClickListener(BankMoneyTransactionRecord data, int position) {
        appSession.setData("transaction_data", data);
        appSession.setData("bank_account_number_data",bankAccountNumber);
        System.out.println("(bank) cancel transaction");
        cancelTransaction(data);
        changeActivity(Activities.BNK_BANK_MONEY_WALLET_UPDATE_RECORD, appSession.getAppPublicKey());
    }

    @Override
    public void onLongItemClickListener(BankMoneyTransactionRecord data, int position) {

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        fab.collapse();
        updateBalance();
        onRefresh();
    }

    @Override
    public List<BankMoneyTransactionRecord> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        List<BankMoneyTransactionRecord> data = new ArrayList<>();
        if (moduleManager != null) {
            try {
                data.addAll(moduleManager.getBankingWallet().getPendingTransactions());
                data.addAll(moduleManager.getBankingWallet().getTransactions(bankAccountNumber.getAccount()));

            } catch (Exception ex) {
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(Wallets.BNK_BANKING_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
            }
        } else {
            Toast.makeText(getActivity(), "Sorry, an error happened in BalanceSummaryFragment (Module == null)", Toast.LENGTH_SHORT).show();
        }
        return data;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, ReferenceWalletConstants.HELP_ACTION, 0, "help").setIcon(R.drawable.bw_help_icon_action_bar)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == ReferenceWalletConstants.HELP_ACTION) {
            presentationDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onUpdateView(String code) {
        switch (code) {
            case BankWalletBroadcasterConstants.BNK_REFERENCE_WALLET_UPDATE_TRANSACTION_VIEW:
                onRefresh();
                break;
            default:
                super.onUpdateView(code);
        }
    }*/

    @Override
    public void onUpdateViewOnUIThread(String code) {
        switch (code) {
            case BankWalletBroadcasterConstants.BNK_REFERENCE_WALLET_UPDATE_TRANSACTION_VIEW:
                onRefresh();
                break;
            default:
                super.onUpdateViewOnUIThread(code);
        }
    }

    private void cancelTransaction(BankMoneyTransactionRecord data){
        if (data.getStatus()== BankTransactionStatus.PENDING){
            //TODO: cancel transction
            moduleManager.getBankingWallet().cancelAsyncBankTransaction(data);
        }
    }
}
