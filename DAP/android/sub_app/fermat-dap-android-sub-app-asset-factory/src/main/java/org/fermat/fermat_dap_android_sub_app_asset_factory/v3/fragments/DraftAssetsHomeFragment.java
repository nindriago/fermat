package org.fermat.fermat_dap_android_sub_app_asset_factory.v3.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Resource;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.BitcoinNetworkConfiguration;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.classes.CryptoVault;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CantListCryptoWalletIntraUserIdentityException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetCryptoLossProtectedWalletException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantListLossProtectedTransactionsException;
import com.bitdubai.fermat_dap_android_sub_app_asset_factory_bitdubai.R;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.InstalledWallet;
import com.software.shell.fab.ActionButton;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.AssetFactorySession;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.SessionConstantsAssetFactory;
import org.fermat.fermat_dap_android_sub_app_asset_factory.util.CommonLogger;
import org.fermat.fermat_dap_android_sub_app_asset_factory.util.Utils;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.adapters.AssetFactoryDraftAdapter;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.filters.AssetFactoryDraftAdapterFilter;
import org.fermat.fermat_dap_api.layer.all_definition.DAPConstants;
import org.fermat.fermat_dap_api.layer.all_definition.enums.State;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions.CantGetAssetFactoryException;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.exceptions.CantPublishAssetFactoy;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.AssetFactory;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.AssetFactorySettings;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;
import org.fermat.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.NotAvailableKeysToPublishAssetsException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.makeText;
import static com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter.Currency.BITCOIN;
import static com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter.Currency.SATOSHI;

/**
 * Created by Jinmy Bohorquez on 19/04/16.
 */
public class DraftAssetsHomeFragment extends FermatWalletListFragment<AssetFactory> implements FermatListItemListeners{


    private AssetFactoryModuleManager manager;
    private ErrorManager errorManager;
    private SettingsManager<AssetFactorySettings> settingsManager;
    private List<AssetFactory> dataSet;

    private ActionButton create;
    private long satoshisWalletBalance;
    private View noAssetsView;
    private Activity activity;
    private boolean isRefreshing = false;
    private SearchView searchView;

//    public AssetFactory getAssetForEdit() {
//        return selectedAsset;
//    }
    public static DraftAssetsHomeFragment newInstance(){
        return new DraftAssetsHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            appSession.setData("asset_data", null);
            appSession.setData("redeem_points", null);

            manager = ((AssetFactorySession) appSession).getModuleManager();
            errorManager = appSession.getErrorManager();
            settingsManager = appSession.getModuleManager().getSettingsManager();


        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);


        activity = new Activity();

        configureToolbar();
        noAssetsView = layout.findViewById(R.id.dap_v3_factory_draft_assets_home_fragment_no_assets);

        try {
            dataSet = (List) getMoreDataAsync();
        } catch (Exception e) {
            CommonLogger.exception(TAG, e.getMessage(), e);
        }
        /*test*/
        if(dataSet != null)
        showOrHideNoAssetsView(dataSet.isEmpty());

        try {
            satoshisWalletBalance = manager.getBitcoinWalletBalance(Utils.getBitcoinWalletPublicKey(manager));
        } catch (Exception e) {
            CommonLogger.exception(TAG, e.getMessage(), e);
            // bitcoinBalanceText.setText(getResources().getString(R.string.dap_user_wallet_buy_no_available));
        }

        onRefresh();


        // fab action button create
        create = (ActionButton) layout.findViewById(R.id.draftCreateButton);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* create new asset factory project */
                appSession.setData("asset_data", null);
//                menuHelp = menu.findItem(R.id.action_asset_factory_help);
//                menuHelp.setVisible(false);
                changeActivity(Activities.DAP_ASSET_EDITOR_ACTIVITY.getCode(), appSession.getAppPublicKey(),(AssetFactory) appSession.getData("asset_data"));
            }
        });
        create.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_jump_from_down));
        create.setVisibility(View.VISIBLE);
        create.setEnabled(false);

        initSettings();
    }

    private void initSettings() {
        settingsManager = appSession.getModuleManager().getSettingsManager();
        AssetFactorySettings settings = null;
        try {
            settings = settingsManager.loadAndGetSettings(appSession.getAppPublicKey());
        } catch (Exception e) {
            settings = null;
        }
        if (settings == null) {
            settings = new AssetFactorySettings();
            settings.setIsContactsHelpEnabled(true);
            settings.setIsPresentationHelpEnabled(true);

            try {
                settingsManager.persistSettings(appSession.getAppPublicKey(), settings);
                manager.setAppPublicKey(appSession.getAppPublicKey());

                manager.changeNetworkType(settings.getBlockchainNetwork().get(settings.getBlockchainNetworkPosition()));
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } else {
            manager.changeNetworkType(settings.getBlockchainNetwork().get(settings.getBlockchainNetworkPosition()));
        }

        final AssetFactorySettings assetFactorySettingsTemp = settings;

        if (manager.getLoggedIdentityAssetIssuer() == null) {
            Handler handlerTimer = new Handler();
            handlerTimer.postDelayed(new Runnable() {
                public void run() {
                    if (assetFactorySettingsTemp.isPresentationHelpEnabled()) {
                        setUpPresentation(false);
                    }
                }
            }, 500);
        } else {
            create.setEnabled(true);
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = getPaintActivtyFeactures().getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundColor(Color.parseColor("#1d1d25"));
            toolbar.setTitleTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.setStatusBarColor(Color.parseColor("#1d1d25"));
            }
        }
    }

    private void setUpPresentation(boolean checkButton) {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setBannerRes(R.drawable.banner_asset_factory)
                    .setIconRes(R.drawable.asset_factory)
                    .setImageLeft(R.drawable.asset_issuer_identity)
                    .setVIewColor(R.color.dap_asset_factory_view_color)
                    .setTitleTextColor(R.color.dap_asset_factory_view_color)
                    .setTextNameLeft(R.string.dap_asset_factory_welcome_name_left)
                    .setSubTitle(R.string.dap_asset_factory_welcome_subTitle)
                    .setBody(R.string.dap_asset_factory_welcome_body)
                    .setTextFooter(R.string.dap_asset_factory_welcome_Footer)
                    .setTemplateType((manager.getLoggedIdentityAssetIssuer() == null) ? PresentationDialog.TemplateType.DAP_TYPE_PRESENTATION : PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setIsCheckEnabled(checkButton)
                    .build();

            presentationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Object o = appSession.getData(SessionConstantsAssetFactory.PRESENTATION_IDENTITY_CREATED);
                    if (o != null) {
                        if ((Boolean) (o)) {
                            //invalidate();
                            appSession.removeData(SessionConstantsAssetFactory.PRESENTATION_IDENTITY_CREATED);
                        }
                    }
                    IdentityAssetIssuer identityAssetIssuer = manager.getLoggedIdentityAssetIssuer();
                    if (identityAssetIssuer == null) {
                        getActivity().onBackPressed();
                    } else {
                        invalidate();
                    }
                    create.setEnabled(true);
                }
            });

            presentationDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOrHideNoAssetsView(boolean empty) {
        if (empty) {
            recyclerView.setVisibility(View.GONE);
            noAssetsView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noAssetsView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dap_wallet_asset_factory_draft_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.action_dap_factory_draft_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.dap_factory_draft_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals(searchView.getQuery().toString())) {
                    ((AssetFactoryDraftAdapterFilter) ((AssetFactoryDraftAdapter) getAdapter()).getFilter()).filter(s);
                }
                return false;
            }
        });

        menu.add(0, SessionConstantsAssetFactory.IC_ACTION_HELP_FACTORY, 0, "help").setIcon(R.drawable.dap_asset_factory_help_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == SessionConstantsAssetFactory.IC_ACTION_HELP_FACTORY) {
                setUpPresentation(settingsManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Asset Issuer system error",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dap_v3_factory_draft_assets_home_fragment;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.factory_draft_swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.dap_v3_factory_draft_assets_home_fragment_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                dataSet = (ArrayList) result[0];
                if (adapter != null) {
                    adapter.changeDataSet(dataSet);
                    ((AssetFactoryDraftAdapterFilter) ((AssetFactoryDraftAdapter) getAdapter()).getFilter()).filter(searchView.getQuery().toString());
                }
                showOrHideNoAssetsView(dataSet.isEmpty());
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
            adapter = new AssetFactoryDraftAdapter(this, getActivity(), dataSet, manager, appSession);
            adapter.setFermatListEventListener(this);
        } else {
            adapter.changeDataSet(dataSet);
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
    public void onItemClickListener(Object data, int position) {

    }

    @Override
    public void onLongItemClickListener(Object data, int position) {

    }

    public List<AssetFactory> getMoreDataAsync() throws CantGetAssetFactoryException, CantCreateFileException, FileNotFoundException {
        List<AssetFactory> items = new ArrayList<>();
        List<AssetFactory> draftItems = manager.getAssetFactoryByState(State.DRAFT);
        List<AssetFactory> pendingFinalItems = manager.getAssetFactoryByState(State.PENDING_FINAL);
        if (draftItems != null && !draftItems.isEmpty())
            items.addAll(draftItems);
        if (pendingFinalItems != null && !pendingFinalItems.isEmpty())
            items.addAll(pendingFinalItems);
        List<Resource> resources;
        for (AssetFactory item : items) {
            resources = item.getResources();
            for (Resource resource : resources) {
                resource.setResourceBinayData(manager.getAssetFactoryResource(resource).getContent());
            }
        }
        return items;
    }

    @Override
    public void onRefresh() {
        if (!isRefreshing) {
            isRefreshing = true;
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(true);
            FermatWorker worker = new FermatWorker(getActivity(), this) {
                @Override
                protected Object doInBackground() throws Exception {
                    return getMoreDataAsync();
                }
            };
            worker.execute();
        }
    }

    @Override
    public void onUpdateViewOnUIThread(String code) {
        switch (code) {
            case DAPConstants.DAP_UPDATE_VIEW_ANDROID:
                onRefresh();
                break;
            default:
                super.onUpdateViewOnUIThread(code);
        }
    }



    public boolean validate() {
        try {
            AssetFactory assetFactory = (AssetFactory) appSession.getData("asset_data");
            long satoshis = assetFactory.getAmount();
            if (CryptoVault.isDustySend(satoshis)) {
                Toast.makeText(getActivity(), "The minimum monetary amount for any Asset is " + BitcoinNetworkConfiguration.MIN_ALLOWED_SATOSHIS_ON_SEND + " satoshis.\n" +
                        " \n This is needed to pay the fee of bitcoin transactions during delivery of the assets.", Toast.LENGTH_LONG).show();
                return false;
            }
            long quantity = assetFactory.getQuantity();
            if (satoshis * quantity > satoshisWalletBalance) {
                double bitcoinWalletBalance = BitcoinConverter.convert(satoshisWalletBalance, SATOSHI, BITCOIN);
                Toast.makeText(getActivity(), String.format("There are insufficient available funds to perform the transaction. The bitcoin wallet balance is %.2f BTC", bitcoinWalletBalance), Toast.LENGTH_SHORT).show();
                return false;
            }
            String description = assetFactory.getDescription();
            if (description.length() == 0)
            {
                Toast.makeText(getActivity(), "Invalid Asset Description.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (quantity == 0)
            {
                Toast.makeText(getActivity(), "Invalid Quantity of Assets", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (assetFactory.getExpirationDate() != null && assetFactory.getExpirationDate().before(new Date())) {
                Toast.makeText(getActivity(), "Expiration date can't be in the past. Please modify the expiration date.", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } catch (NumberFormatException ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            Toast.makeText(getActivity(), "Invalid monetary amount.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void doEditAsset() {
        final AssetFactory selectedAsset = (AssetFactory) appSession.getData("asset_data");
       if (selectedAsset != null ) {
            changeActivity(Activities.DAP_ASSET_EDITOR_ACTIVITY.getCode(), appSession.getAppPublicKey(), selectedAsset);
        }else {
            appSession.setData("asset_data", null);
       }
    }

    public void doPublishAsset() {

        final AssetFactory selectedAsset = (AssetFactory) appSession.getData("asset_data");

        try {
            if (manager.isReadyToPublish(selectedAsset.getAssetPublicKey())) {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setTitle("Asset Factory");
                dialog.setMessage("Publishing asset, please wait...");
                dialog.setCancelable(false);
                dialog.show();
                FermatWorker worker = new FermatWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        // for test
                        for (InstalledWallet wallet : manager.getInstallWallets()) {
                            selectedAsset.setWalletPublicKey(wallet.getWalletPublicKey());
                            break;
                        }
                        manager.publishAsset(selectedAsset);
                        appSession.setData("asset_data", null);
                        return true;
                    }
                };
                worker.setContext(getActivity());
                worker.setCallBack(new FermatWorkerCallBack() {
                    @Override
                    public void onPostExecute(Object... result) {
                        dialog.dismiss();
                        appSession.setData("asset_data", null);
                        if (getActivity() != null) {
                            onRefresh();
                        }
                        Toast.makeText(getActivity(), "The publishing process has been started successfully.\n\n " +
                                "You will be able to distribute this asset in a few minutes from your Asset Issuer Wallet.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onErrorOccurred(Exception ex) {
                        dialog.dismiss();
                        appSession.setData("asset_data", null);

                        /**
                         * If there was an exception, I will search first if I ran out of keys
                         * to show the appropiated message
                         */
                        Throwable rootException = ExceptionUtils.getRootCause(ex);
                        if (rootException instanceof NotAvailableKeysToPublishAssetsException) {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), rootException.getMessage(), Toast.LENGTH_LONG).show();
                                onRefresh();
                            }
                        } else {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "You need to define all mandatory properties in your asset before publishing it.", Toast.LENGTH_LONG).show();
                                onRefresh();
                            }
                        }
                        ex.printStackTrace();
                    }
                });
                worker.execute();
            }
        } catch (CantPublishAssetFactoy cantPublishAssetFactoy) {
            cantPublishAssetFactoy.printStackTrace();
            Toast.makeText(getActivity(), "You cannot publish this asset", Toast.LENGTH_SHORT).show();
        }
    }

    public void doDeleteAsset() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        final AssetFactory selectedAsset = (AssetFactory) appSession.getData("asset_data");
        dialog.setTitle("Deleting asset");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        FermatWorker worker = new FermatWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                manager.removeAssetFactory(selectedAsset.getAssetPublicKey());
                return true;
            }
        };
        worker.setContext(getActivity());
        worker.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                dialog.dismiss();
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Asset deleted successfully", Toast.LENGTH_SHORT).show();
                    changeActivity(Activities.DAP_MAIN.getCode(), appSession.getAppPublicKey());
                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {
                dialog.dismiss();
                if (getActivity() != null) {
                    CommonLogger.exception(TAG, ex.getMessage(), ex);
                    Toast.makeText(getActivity(), "There was an error deleting this asset", Toast.LENGTH_SHORT).show();
                }
            }
        });
        worker.execute();
    }
}
