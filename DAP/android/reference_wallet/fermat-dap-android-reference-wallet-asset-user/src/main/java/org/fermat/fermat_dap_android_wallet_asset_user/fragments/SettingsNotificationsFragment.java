package org.fermat.fermat_dap_android_wallet_asset_user.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantGetSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.SettingsNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import org.fermat.fermat_dap_android_wallet_asset_user.sessions.AssetUserSession;
import org.fermat.fermat_dap_android_wallet_asset_user.sessions.SessionConstantsAssetUser;

import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_user.AssetUserSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_user.interfaces.AssetUserWalletSubAppModuleManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;
import com.mati.fermat_preference_settings.drawer.FermatPreferenceFragment;
import com.mati.fermat_preference_settings.drawer.interfaces.PreferenceSettingsItem;
import com.mati.fermat_preference_settings.drawer.models.PreferenceSettingsSwithItem;


import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;


/**
 *
 * Modified by Penelope Quintero for Asset User Wallet on 2016.02.02
 */
public class SettingsNotificationsFragment extends FermatPreferenceFragment<AssetUserSession, WalletResourcesProviderManager> {

    private View rootView;
    private AssetUserSession assetUserSession;
    private Spinner spinner;
    private Switch notificationSwitch;

    private AssetUserWalletSubAppModuleManager moduleManager;
    SettingsManager<AssetUserSettings> settingsManager;
    private ErrorManager errorManager;
    AssetUserSettings settings = null;


    public static SettingsNotificationsFragment newInstance() {
        return new SettingsNotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        moduleManager = ((AssetUserSession) appSession).getModuleManager();
        try {
            errorManager = appSession.getErrorManager();
            settingsManager = appSession.getModuleManager().getSettingsManager();

            configureToolbar();

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } catch (Exception e) {
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.DAP_ASSET_USER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, e);
            e.printStackTrace();
        }
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        try {
//            rootView = inflater.inflate(R.layout.dap_wallet_asset_user_settings_notifications, container, false);
//            setUpUi();
//            configureToolbar();
//            return rootView;
//        } catch (Exception e) {
//            makeText(getActivity(), R.string.dap_user_wallet_opps_system_error, Toast.LENGTH_SHORT).show();
//            assetUserSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
//        }
//
//        return null;
//    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected List<PreferenceSettingsItem> setSettingsItems() {
        BlockchainNetworkType blockchainNetworkType = null;
        List<PreferenceSettingsItem> list = new ArrayList<>();
        try {
            settings = settingsManager.loadAndGetSettings(appSession.getAppPublicKey());
            list.add(new PreferenceSettingsSwithItem(1, "Enabled Notifications", settings.getNotificationEnabled()));
            final Bundle dataDialog = new Bundle();
        } catch (CantGetSettingsException e) {
            e.printStackTrace();
        } catch (SettingsNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void onSettingsTouched(PreferenceSettingsItem preferenceSettingsItem, int position) {
        try {
            try {
                settings = settingsManager.loadAndGetSettings(appSession.getAppPublicKey());
            } catch (CantGetSettingsException e) {
                e.printStackTrace();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }
            settings.setIsPresentationHelpEnabled(false);
            try {
                settingsManager.persistSettings(appSession.getAppPublicKey(), settings);
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSettingsTouched(String item, int position) {

    }

    @Override
    public void onSettingsChanged(PreferenceSettingsItem preferenceSettingsItem, int position, boolean isChecked) {
        try {
            try {
                settings = settingsManager.loadAndGetSettings(appSession.getAppPublicKey());
            } catch (CantGetSettingsException e) {
                e.printStackTrace();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }

            if (preferenceSettingsItem.getId() == 1) {
                //enable notifications settings
                settings.setNotificationEnabled(isChecked);
            }

            try {
                settingsManager.persistSettings(appSession.getAppPublicKey(), settings);
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public int getBackgroundAlpha() {
        return 100;
    }

    @Override
    public void optionSelected(PreferenceSettingsItem preferenceSettingsItem, int position) {

    }

    @Override
    public void dialogOptionSelected(String item, int position) {
        // CustomDialogFragment customDialogFragment = (CustomDialogFragment) dialog;
        // previousSelectedItem = customDialogFragment.getPreviousSelectedItem();
        // Toast.makeText(this, "OK button pressed", Toast.LENGTH_SHORT).show();

        BlockchainNetworkType blockchainNetworkType;

        //        switch (item) {
        //
        //            case "MainNet":
        //                blockchainNetworkType = BlockchainNetworkType.PRODUCTION;
        //                break;
        //            case "TestNet":
        //                blockchainNetworkType = BlockchainNetworkType.TEST_NET;
        //                break;
        //            case "RegTest":
        //                blockchainNetworkType = BlockchainNetworkType.REG_TEST;
        //                break;
        //            default:
        //                blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();
        //                break;
        //        }
        //        System.out.println("NETWORK TYPE TO BE SAVED IS  " + blockchainNetworkType.getCode());
        //        if (blockchainNetworkType == null) {
        //            if (settings.getBlockchainNetworkType() != null) {
        //                blockchainNetworkType = settings.getBlockchainNetworkType();
        //            } else {
        //                blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();
        //            }
        //        }
        //        settings.setBlockchainNetworkType(blockchainNetworkType);
        //        try {
        //            settingsManager.persistSettings(appSession.getAppPublicKey(), settings);
        //        } catch (CantPersistSettingsException e) {
        //            e.printStackTrace();
        //        }
    }

//    public void setUpUi() {
//        notificationSwitch = (Switch) rootView.findViewById(R.id.switch_notification);
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, SessionConstantsAssetUser.IC_ACTION_USER_HELP_SETTINGS_NOTIFICATION, 0, "Help")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == SessionConstantsAssetUser.IC_ACTION_USER_HELP_SETTINGS_NOTIFICATION) {
                setUpSettings(settingsManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), R.string.dap_user_wallet_system_error,
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpSettings(boolean checkButton) {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setBannerRes(R.drawable.banner_asset_user_wallet)
                    .setIconRes(R.drawable.asset_user_wallet)
                    .setVIewColor(R.color.dap_user_view_color)
                    .setTitleTextColor(R.color.dap_user_view_color)
                    .setSubTitle(R.string.dap_user_wallet_detail_subTitle)
                    .setBody(R.string.dap_user_wallet_detail_body)
                    .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setIsCheckEnabled(checkButton)
                    .build();

            presentationDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            Drawable drawable = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getResources().getDrawable(R.drawable.dap_wallet_asset_user_action_bar_gradient_colors, null);
                toolbar.setElevation(0);
            } else {
                drawable = getResources().getDrawable(R.drawable.dap_wallet_asset_user_action_bar_gradient_colors);
            }
            toolbar.setBackground(drawable);
        }
    }
}
