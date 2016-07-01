package org.fermat.fermat_dap_android_wallet_asset_issuer.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.SizeUtils;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.ConfirmDialog;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.BitmapWorkerTask;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_dap_android_wallet_asset_issuer_bitdubai.R;
import org.fermat.fermat_dap_android_wallet_asset_issuer.common.adapters.AssetDetailTransactionAdapter;
import org.fermat.fermat_dap_android_wallet_asset_issuer.models.Data;
import org.fermat.fermat_dap_android_wallet_asset_issuer.models.DigitalAsset;
import org.fermat.fermat_dap_android_wallet_asset_issuer.models.Transaction;
import org.fermat.fermat_dap_android_wallet_asset_issuer.sessions.AssetIssuerSession;
import org.fermat.fermat_dap_android_wallet_asset_issuer.sessions.SessionConstantsAssetIssuer;
import org.fermat.fermat_dap_android_wallet_asset_issuer.util.CommonLogger;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.AssetIssuerSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.exceptions.CantGetAssetStatisticException;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.WalletUtilities;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

/**
 * Created by frank on 12/15/15.
 */
public class AssetDetailTransactionsFragment extends FermatWalletListFragment<Transaction>
        implements FermatListItemListeners<Transaction> {

    private AssetIssuerSession assetIssuerSession;
    private AssetIssuerWalletSupAppModuleManager moduleManager;
    private ErrorManager errorManager;

    private View rootView;
    private Toolbar toolbar;
    private Resources res;

    private View assetDetailAvailableLayout;
    private View assetDetailAppropiateLayout;
    private View assetDetailRedeemedLayout;
    private View transactionsLayout;

    private ImageView assetImageDetail;
    private FermatTextView assetDetailNameText;
    private FermatTextView assetDetailExpDateText;
    private FermatTextView availableText;
    private FermatTextView pendingText;
    private FermatTextView assetDetailBtcText;
    private FermatTextView assetDetailRemainingText;
    private FermatTextView assetDetailDelivered;
    private FermatTextView assetDetailRedeemText;
    private FermatTextView assetDetailAppropriatedText;

    private DigitalAsset digitalAsset;

    SettingsManager<AssetIssuerSettings> settingsManager;

    private View noTransactionsView;
    private List<Transaction> transactions;

    public AssetDetailTransactionsFragment() {

    }

    public static AssetDetailTransactionsFragment newInstance() {
        return new AssetDetailTransactionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        assetIssuerSession = (AssetIssuerSession) appSession;
        moduleManager = assetIssuerSession.getModuleManager();
        errorManager = appSession.getErrorManager();

        settingsManager = appSession.getModuleManager().getSettingsManager();

        transactions = (List) getMoreDataAsync(FermatRefreshTypes.NEW, 0);

        configureToolbar();
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        Activity activity = getActivity();
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        rootView = configureActivityHeader(layoutInflater);

        res = rootView.getResources();

        setupUI();
        setupUIData();
        setupRecyclerView();

        configureToolbar();

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    onRefresh();
                }
            });
        }

        noTransactionsView = layout.findViewById(R.id.dap_wallet_asset_issuer_no_transactions);
        transactionsLayout = layout.findViewById(R.id.transactionsLayout);

        showOrHideNoTransactionsView(transactions.isEmpty());
        showStats(false);
    }

    private void setupRecyclerView() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mScrollOffset = 4;
                if (Math.abs(dy) > mScrollOffset) {
                    if (rootView != null) {
                        if (dy > 0) {
                            rootView.setVisibility(View.GONE);
                        } else {
                            rootView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private View configureActivityHeader(LayoutInflater layoutInflater) {
        RelativeLayout header = getToolbarHeader();
        try {
            header.removeAllViews();
        } catch (Exception exception) {
            CommonLogger.exception(TAG, "Error removing all views from header ", exception);
            errorManager.reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, exception);
        }
        header.setVisibility(View.VISIBLE);
        View container = layoutInflater.inflate(R.layout.dap_wallet_asset_issuer_asset_detail, header, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            container.getLayoutParams().height = SizeUtils.convertDpToPixels(602, getActivity());
        }
        return container;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showOrHideNoTransactionsView(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            noTransactionsView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noTransactionsView.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean hasMenu() {
        return true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dap_wallet_asset_issuer_asset_detail_transaction;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.dap_wallet_asset_issuer_asset_detail_transaction_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    private void setupUI() {
        setupBackgroundBitmap();

        assetImageDetail = (ImageView) rootView.findViewById(R.id.asset_image_detail);
        assetDetailNameText = (FermatTextView) rootView.findViewById(R.id.assetDetailNameText);
        assetDetailExpDateText = (FermatTextView) rootView.findViewById(R.id.assetDetailExpDateText);
        availableText = (FermatTextView) rootView.findViewById(R.id.assetAvailable1);
        pendingText = (FermatTextView) rootView.findViewById(R.id.assetAvailable2);
        assetDetailBtcText = (FermatTextView) rootView.findViewById(R.id.assetDetailBtcText);
        //Text = (FermatTextView) rootView.findViewById(R.id.assetDetailRemainingText);
        assetDetailDelivered = (FermatTextView) rootView.findViewById(R.id.assetDetailAvailableText2);
        assetDetailRedeemText = (FermatTextView) rootView.findViewById(R.id.assetDetailRedeemText);
        assetDetailAppropriatedText = (FermatTextView) rootView.findViewById(R.id.assetDetailAppropriatedText);

        assetDetailAvailableLayout = rootView.findViewById(R.id.assetDetailAvailableLayout);
        assetDetailAppropiateLayout = rootView.findViewById(R.id.assetDetailAppropiateLayout);
        assetDetailRedeemedLayout = rootView.findViewById(R.id.assetDetailRedeemedLayout);
    }

    private void doAppropriate(final String assetPublicKey, final String walletPublicKey) {
        final Activity activity = getActivity();
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(getResources().getString(R.string.dap_issuer_wallet_wait));
        dialog.setCancelable(false);
        dialog.show();
        FermatWorker task = new FermatWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                moduleManager.appropriateAsset(assetPublicKey, walletPublicKey);
                return true;
            }
        };
        task.setContext(activity);
        task.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                dialog.dismiss();
                if (activity != null) {
                    Toast.makeText(activity, R.string.dap_issuer_wallet_appropriation_ok, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {
                dialog.dismiss();
                if (activity != null)
                    Toast.makeText(activity, R.string.dap_issuer_wallet_exception_retry,
                            Toast.LENGTH_SHORT).show();
            }
        });
        task.execute();
    }

    private void setupBackgroundBitmap() {
        AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {

            WeakReference<ViewGroup> view;

            @Override
            protected void onPreExecute() {
                view = new WeakReference(rootView) ;
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap drawable = null;
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = true;
                    options.inSampleSize = 5;
                    drawable = BitmapFactory.decodeResource(
                            getResources(), R.drawable.bg_app_image,options);
                }catch (OutOfMemoryError error){
                    error.printStackTrace();
                }
                return drawable;
            }

            @Override
            protected void onPostExecute(Bitmap drawable) {
                if (drawable!= null) {
                    view.get().setBackground(new BitmapDrawable(getResources(),drawable));
                }
            }
        } ;
        asyncTask.execute();
    }

    private void setupUIData() {
        String digitalAssetPublicKey = ((DigitalAsset) appSession.getData("asset_data")).getAssetPublicKey();
        try {
            digitalAsset = Data.getDigitalAsset(moduleManager, digitalAssetPublicKey);
            Data.setStatistics(WalletUtilities.WALLET_PUBLIC_KEY, digitalAsset, moduleManager);
        } catch (CantGetAssetStatisticException e) {
            e.printStackTrace();
        } catch (CantLoadWalletException e) {
            e.printStackTrace();
        }

//        toolbar.setTitle(digitalAsset.getName());

        byte[] img = (digitalAsset.getImage() == null) ? new byte[0] : digitalAsset.getImage();
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(assetImageDetail, res, R.drawable.img_asset_without_image, false);
        bitmapWorkerTask.execute(img); //todo commenting to compile, please review

        assetDetailNameText.setText(digitalAsset.getName());
        assetDetailExpDateText.setText(digitalAsset.getFormattedExpDate());

        long available = digitalAsset.getAvailableBalanceQuantity();
        long book = digitalAsset.getBookBalanceQuantity();
        availableText.setText(availableText(available));
        if (available == book) {
            pendingText.setVisibility(View.INVISIBLE);
        } else {
            long pendingValue = Math.abs(available - book);
            pendingText.setText(pendingText(pendingValue));
            pendingText.setVisibility(View.VISIBLE);
        }

        assetDetailBtcText.setText(digitalAsset.getFormattedAvailableBalanceBitcoin() + " BTC");
        assetDetailDelivered.setText(digitalAsset.getUnused() + "");
        assetDetailRedeemText.setText(digitalAsset.getRedeemed()+"");
        assetDetailAppropriatedText.setText(digitalAsset.getAppropriated() + "");

        assetDetailAvailableLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (digitalAsset.getUnused() > 0) {
                    changeActivity(Activities.DAP_WALLET_ASSET_ISSUER_USER_DELIVERY_LIST, appSession.getAppPublicKey());
                } else {
                    makeText(getActivity(), R.string.dap_issuer_wallet_no_assets_delivered, Toast.LENGTH_SHORT).show();
                }
            }
        });
        assetDetailAppropiateLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (digitalAsset.getAppropriated() > 0) {
                    changeActivity(Activities.DAP_WALLET_ASSET_ISSUER_USER_APPROPIATE_LIST, appSession.getAppPublicKey());
                } else {
                    makeText(getActivity(), R.string.dap_issuer_wallet_no_assets_appropriated, Toast.LENGTH_SHORT).show();
                }
            }
        });
        assetDetailRedeemedLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (digitalAsset.getRedeemed() > 0) {
                    changeActivity(Activities.DAP_WALLET_ASSET_ISSUER_USER_REDEEMED_LIST, appSession.getAppPublicKey());
                } else {
                    makeText(getActivity(), R.string.dap_issuer_wallet_no_assets_redeemed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String pendingText(long pendingValue) {
        return "(" + pendingValue + " pending confirmation)";
    }

    private String availableText(long available) {
        return available + ((available == 1) ? " Asset" : " Assets");
    }

    private void configureToolbar() {
        toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBackgroundColor(Color.TRANSPARENT);
            toolbar.setBottom(Color.WHITE);
            Drawable drawable = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                drawable = getResources().getDrawable(R.drawable.dap_wallet_asset_issuer_action_bar_gradient_colors, null);
            else
                drawable = getResources().getDrawable(R.drawable.dap_wallet_asset_issuer_action_bar_gradient_colors);
            toolbar.setBackground(drawable);
        }
    }

    private void setUpHelpAssetDetail(boolean checkButton) {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setBannerRes(R.drawable.banner_asset_issuer_wallet)
                    .setIconRes(R.drawable.asset_issuer)
                    .setVIewColor(R.color.dap_issuer_view_color)
                    .setTitleTextColor(R.color.dap_issuer_view_color)
                    .setSubTitle(R.string.dap_issuer_wallet_detail_subTitle)
                    .setBody(R.string.dap_issuer_wallet_detail_body)
                    .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setIsCheckEnabled(checkButton)
                    .build();

            presentationDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (digitalAsset != null && digitalAsset.getAvailableBalanceQuantity() > 0) {
            menu.add(0, SessionConstantsAssetIssuer.IC_ACTION_ISSUER_DELIVER, 0, getResources().getString(R.string.dap_issuer_wallet_action_deliver))
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            menu.add(0, SessionConstantsAssetIssuer.IC_ACTION_ISSUER_APPROPRIATE, 1, getResources().getString(R.string.dap_issuer_wallet_action_appropriate))
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        menu.add(0, SessionConstantsAssetIssuer.IC_ACTION_ISSUER_SHOW_STATS, 2, getResources().getString(R.string.dap_issuer_wallet_action_show_statistics))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(0, SessionConstantsAssetIssuer.IC_ACTION_ISSUER_HELP_DETAIL, 3, "Help")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == SessionConstantsAssetIssuer.IC_ACTION_ISSUER_HELP_DETAIL) {
                setUpHelpAssetDetail(settingsManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            } else if (id == SessionConstantsAssetIssuer.IC_ACTION_ISSUER_DELIVER) {
                changeActivity(Activities.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY, appSession.getAppPublicKey());
            } else if (id == SessionConstantsAssetIssuer.IC_ACTION_ISSUER_APPROPRIATE) {
                new ConfirmDialog.Builder(getActivity(), appSession)
                        .setTitle(getResources().getString(R.string.dap_issuer_wallet_confirm_title))
                        .setMessage(getResources().getString(R.string.dap_issuer_wallet_confirm_sure))
                        .setColorStyle(getResources().getColor(R.color.dap_issuer_wallet_principal))
                        .setYesBtnListener(new ConfirmDialog.OnClickAcceptListener() {
                            @Override
                            public void onClick() {
                                doAppropriate(digitalAsset.getAssetPublicKey(), digitalAsset.getWalletPublicKey());
                            }
                        }).build().show();
            } else if (id == SessionConstantsAssetIssuer.IC_ACTION_ISSUER_SHOW_STATS) {
                if (item.getTitle().equals(getResources().getString(R.string.dap_issuer_wallet_action_show_statistics))) {
                    showStats(true);
                    item.setTitle(getResources().getString(R.string.dap_issuer_wallet_action_show_transactions));
                } else {
                    showStats(false);
                    item.setTitle(getResources().getString(R.string.dap_issuer_wallet_action_show_statistics));
                }
            }
        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), R.string.dap_issuer_wallet_system_error,
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStats(boolean show) {
        if (show) {
            assetDetailAppropiateLayout.setVisibility(View.VISIBLE);
            assetDetailAvailableLayout.setVisibility(View.VISIBLE);
            assetDetailRedeemedLayout.setVisibility(View.VISIBLE);
            transactionsLayout.setVisibility(View.GONE);
            rootView.getLayoutParams().height = SizeUtils.convertDpToPixels(602, getActivity());
        } else {
            assetDetailAppropiateLayout.setVisibility(View.GONE);
            assetDetailAvailableLayout.setVisibility(View.GONE);
            assetDetailRedeemedLayout.setVisibility(View.GONE);
            transactionsLayout.setVisibility(View.VISIBLE);
            rootView.getLayoutParams().height = SizeUtils.convertDpToPixels(428, getActivity());
        }
    }

    @Override
    public void onItemClickListener(Transaction data, int position) {

    }

    @Override
    public void onLongItemClickListener(Transaction data, int position) {

    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                transactions = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(transactions);

                showOrHideNoTransactionsView(transactions.isEmpty());
            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            adapter = new AssetDetailTransactionAdapter(getActivity(), transactions, moduleManager);
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
    public List<Transaction> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        List<Transaction> transactions = new ArrayList<>();
        if (moduleManager != null) {
            try {
                transactions = Data.getTransactions(moduleManager, digitalAsset);

                appSession.setData("transactions_sent", transactions);

            } catch (Exception ex) {
                CommonLogger.exception(TAG, ex.getMessage(), ex);
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(
                            Wallets.DAP_ASSET_USER_WALLET,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                            ex);
            }
        } else {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.dap_issuer_wallet_system_error),
                    Toast.LENGTH_SHORT).
                    show();
        }
        return transactions;
    }
}
