package org.fermat.fermat_dap_android_wallet_asset_issuer.v3.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import static android.widget.Toast.makeText;

import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_dap_android_wallet_asset_issuer_bitdubai.R;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import org.fermat.fermat_dap_android_wallet_asset_issuer.models.Data;
import org.fermat.fermat_dap_android_wallet_asset_issuer.models.DigitalAsset;
import org.fermat.fermat_dap_android_wallet_asset_issuer.sessions.AssetIssuerSession;
import org.fermat.fermat_dap_android_wallet_asset_issuer.sessions.SessionConstantsAssetIssuer;
import org.fermat.fermat_dap_android_wallet_asset_issuer.util.CommonLogger;
import org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.adapters.HomeCardAdapter;
import org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.filters.HomeCardAdapterFilter;
import org.fermat.fermat_dap_api.layer.all_definition.DAPConstants;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetIdentityAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.AssetIssuerSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeCardFragment extends FermatWalletListFragment<DigitalAsset>
        implements FermatListItemListeners<DigitalAsset> {

    // Constants
    private static final String TAG = "MyAssetsActivityFragment";

    // Fermat Managers
    private AssetIssuerWalletSupAppModuleManager moduleManager;
    private ErrorManager errorManager;
    SettingsManager<AssetIssuerSettings> settingsManager;

    // Data
    private List<DigitalAsset> digitalAssets;

    //UI
    private View noAssetsView;
    private SearchView searchView;

    public static HomeCardFragment newInstance() {
        return new HomeCardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            appSession.setData("users", null);

            moduleManager = ((AssetIssuerSession) appSession).getModuleManager();
            errorManager = appSession.getErrorManager();
            settingsManager = appSession.getModuleManager().getSettingsManager();

        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.DAP_ASSET_ISSUER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
        }
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        //Initialize settings
        settingsManager = appSession.getModuleManager().getSettingsManager();
        AssetIssuerSettings settings = null;
        try {
            settings = settingsManager.loadAndGetSettings(appSession.getAppPublicKey());
        } catch (Exception e) {
            settings = null;
        }

        if (settings == null) {
            settings = new AssetIssuerSettings();
            settings.setIsContactsHelpEnabled(true);
            settings.setIsPresentationHelpEnabled(true);

            try {
                settingsManager.persistSettings(appSession.getAppPublicKey(), settings);
                moduleManager.setAppPublicKey(appSession.getAppPublicKey());

                moduleManager.changeNetworkType(settings.getBlockchainNetwork().get(settings.getBlockchainNetworkPosition()));
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } else {
            moduleManager.changeNetworkType(settings.getBlockchainNetwork().get(settings.getBlockchainNetworkPosition()));
        }

        final AssetIssuerSettings assetIssuerSettingsTemp = settings;


        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable() {
            public void run() {
                if (assetIssuerSettingsTemp.isPresentationHelpEnabled()) {
                    setUpPresentation(false);
                }
            }
        }, 500);

        configureToolbar();
        noAssetsView = layout.findViewById(R.id.dap_wallet_no_assets);

        digitalAssets = (List) getMoreDataAsync(FermatRefreshTypes.NEW, 0);
        showOrHideNoAssetsView(digitalAssets.isEmpty());

        onRefresh();
    }

    private void setUpPresentation(boolean checkButton) {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setBannerRes(R.drawable.banner_asset_issuer_wallet)
                    .setIconRes(R.drawable.asset_issuer)
                    .setImageLeft(R.drawable.asset_issuer_identity)
                    .setVIewColor(R.color.dap_issuer_view_color)
                    .setTitleTextColor(R.color.dap_issuer_view_color)
                    .setTextNameLeft(R.string.dap_issuer_wallet_welcome_name_left)
                    .setSubTitle(R.string.dap_issuer_wallet_welcome_subTitle)
                    .setBody(R.string.dap_issuer_wallet_welcome_body)
                    .setTextFooter(R.string.dap_issuer_wallet_welcome_Footer)
                    .setTemplateType((moduleManager.getActiveAssetIssuerIdentity() == null) ? PresentationDialog.TemplateType.DAP_TYPE_PRESENTATION : PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setIsCheckEnabled(checkButton)
                    .build();

            presentationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Object o = appSession.getData(SessionConstantsAssetIssuer.PRESENTATION_IDENTITY_CREATED);
                    if (o != null) {
                        if ((Boolean) (o)) {
                            //invalidate();
                            appSession.removeData(SessionConstantsAssetIssuer.PRESENTATION_IDENTITY_CREATED);
                        }
                    }
                    try {
                        IdentityAssetIssuer identityAssetIssuer = moduleManager.getActiveAssetIssuerIdentity();
                        if (identityAssetIssuer == null) {
                            getActivity().onBackPressed();
                        } else {
                            invalidate();
                        }
                    } catch (CantGetIdentityAssetIssuerException e) {
                        e.printStackTrace();
                    }
                }
            });

            presentationDialog.show();
        } catch (CantGetIdentityAssetIssuerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dap_asset_issuer_home_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.action_wallet_issuer_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.dap_issuer_wallet_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals(searchView.getQuery().toString())) {
                    ((HomeCardAdapterFilter) ((HomeCardAdapter) getAdapter()).getFilter()).filter(s);
                }
                return false;
            }
        });
        menu.add(0, SessionConstantsAssetIssuer.IC_ACTION_ISSUER_HELP_PRESENTATION, 2, "Help")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

//        super.onCreateOptionsMenu(menu, inflater);
//        searchView = (SearchView) menu.getItem(1).getActionView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == SessionConstantsAssetIssuer.IC_ACTION_ISSUER_HELP_PRESENTATION) {
                setUpPresentation(settingsManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), R.string.dap_issuer_wallet_system_error,
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void configureToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.dap_issuer_wallet_v3_toolbar));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBottom(Color.WHITE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.dap_issuer_wallet_v3_toolbar));
            }
        }
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dap_wallet_asset_issuer_home;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.dap_wallet_asset_issuer_my_assets_activity_recycler_view;
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
                digitalAssets = (ArrayList) result[0];
                if (adapter != null) {
                    adapter.changeDataSet(digitalAssets);
                    ((HomeCardAdapterFilter) ((HomeCardAdapter) getAdapter()).getFilter()).filter(searchView.getQuery().toString());
                }
                showOrHideNoAssetsView(digitalAssets.isEmpty());
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
            adapter = new HomeCardAdapter(this, getActivity(), digitalAssets, moduleManager, appSession);
            adapter.setFermatListEventListener(this);
        } else {
            adapter.changeDataSet(digitalAssets);
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
    public void onItemClickListener(DigitalAsset data, int position) {
//        appSession.setData("asset_data", data);
//        changeActivity(Activities.DAP_ASSET_ISSUER_WALLET_ASSET_DETAIL, appSession.getAppPublicKey());
    }

    @Override
    public void onLongItemClickListener(DigitalAsset data, int position) {
    }

    @Override
    public List<DigitalAsset> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        List<DigitalAsset> digitalAssets = new ArrayList<>();
        if (moduleManager != null) {
            try {
                digitalAssets = Data.getAllDigitalAssets(moduleManager);

            } catch (Exception ex) {
                CommonLogger.exception(TAG, ex.getMessage(), ex);
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(
                            Wallets.DAP_ASSET_ISSUER_WALLET,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT,
                            ex);
            }
        } else {
            Toast.makeText(getActivity(),
                    R.string.dap_issuer_wallet_system_error,
                    Toast.LENGTH_SHORT).
                    show();
        }
        return digitalAssets;
    }

    private void showOrHideNoAssetsView(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.GONE);
            noAssetsView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noAssetsView.setVisibility(View.GONE);
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

    public void doDeliverAction() {
        changeActivity(Activities.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_SELECT_USERS_GROUPS, appSession.getAppPublicKey());
    }
}