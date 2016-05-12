package org.fermat.fermat_dap_android_sub_app_asset_factory.v3.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatCheckBox;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatEditText;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_dap_android_sub_app_asset_factory_bitdubai.R;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.AssetFactorySession;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.SessionConstantsAssetFactory;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.AssetFactorySettings;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;

import static android.widget.Toast.makeText;

/**
 * Created by frank on 12/15/15.
 */
public class WizardVerifyFragment extends AbstractFermatFragment {

    private Activity activity;
    private AssetFactorySession assetFactorySession;
    private AssetFactoryModuleManager moduleManager;
    private ErrorManager errorManager;

    //UI
    private View rootView;
    private Toolbar toolbar;
    private Resources res;
    private FermatTextView wizardVerifyAssetNameText;
    private ImageView wizardVerifyAssetImage;
    private FermatTextView wizardVerifyDescText;
    private FermatTextView wizardVerifyFeeValue;
    private FermatTextView wizardVerifyExpDateValue;
    private FermatTextView wizardVerifyQuantityValue;
    private FermatCheckBox wizardVerifyRedeemableCheck;
    private FermatCheckBox wizardVerifyTransfereableCheck;
    private FermatCheckBox wizardVerifyExchangeableCheck;
    private FermatTextView wizardVerifyAssetValue;
    private FermatTextView wizardVerifyTotalValue;
    private FermatButton wizardVerifyBackButton;
    private FermatButton wizardVerifyFinishButton;

    SettingsManager<AssetFactorySettings> settingsManager;

    public WizardVerifyFragment() {

    }

    public static WizardVerifyFragment newInstance() {
        return new WizardVerifyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        assetFactorySession = (AssetFactorySession) appSession;
        moduleManager = assetFactorySession.getModuleManager();
        errorManager = appSession.getErrorManager();

        settingsManager = appSession.getModuleManager().getSettingsManager();
        activity = getActivity();

        configureToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dap_v3_factory_wizard_verify, container, false);
        res = rootView.getResources();

        setupUI();
        setupUIData();

        return rootView;
    }

    private void setUpHelpAssetStatistics(boolean checkButton) {
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
                    .setTemplateType((moduleManager.getLoggedIdentityAssetIssuer() == null) ? PresentationDialog.TemplateType.DAP_TYPE_PRESENTATION : PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
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
        menu.add(0, SessionConstantsAssetFactory.IC_ACTION_HELP_FACTORY, 0, "Help")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == SessionConstantsAssetFactory.IC_ACTION_HELP_FACTORY) {
                setUpHelpAssetStatistics(settingsManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), R.string.dap_asset_factory_system_error,
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        wizardVerifyAssetNameText = (FermatTextView) rootView.findViewById(R.id.wizardVerifyAssetNameText);
        wizardVerifyAssetImage = (ImageView) rootView.findViewById(R.id.wizardVerifyAssetImage);
        wizardVerifyDescText = (FermatTextView) rootView.findViewById(R.id.wizardVerifyDescText);
        wizardVerifyFeeValue = (FermatTextView) rootView.findViewById(R.id.wizardVerifyFeeValue);
        wizardVerifyExpDateValue = (FermatTextView) rootView.findViewById(R.id.wizardVerifyExpDateValue);
        wizardVerifyQuantityValue = (FermatTextView) rootView.findViewById(R.id.wizardVerifyQuantityValue);
        wizardVerifyRedeemableCheck = (FermatCheckBox) rootView.findViewById(R.id.wizardVerifyRedeemableCheck);
        wizardVerifyTransfereableCheck = (FermatCheckBox) rootView.findViewById(R.id.wizardVerifyTransfereableCheck);
        wizardVerifyExchangeableCheck = (FermatCheckBox) rootView.findViewById(R.id.wizardVerifyExchangeableCheck);
        wizardVerifyAssetValue = (FermatTextView) rootView.findViewById(R.id.wizardVerifyAssetValue);
        wizardVerifyTotalValue = (FermatTextView) rootView.findViewById(R.id.wizardVerifyTotalValue);
        wizardVerifyBackButton = (FermatButton) rootView.findViewById(R.id.wizardVerifyBackButton);
        wizardVerifyFinishButton = (FermatButton) rootView.findViewById(R.id.wizardVerifyFinishButton);
        wizardVerifyBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(Activities.DAP_SUB_APP_ASSET_FACTORY_WIZARD_CRYPTO.getCode(), appSession.getAppPublicKey());
            }
        });
        wizardVerifyFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO finish action
            }
        });
    }

    private void setupUIData() {
    }

    private void configureToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.card_toolbar));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBottom(Color.WHITE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.card_toolbar));
            }
        }
    }
}
