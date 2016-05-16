package org.fermat.fermat_dap_android_sub_app_asset_factory.v3.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatCheckBox;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatEditText;
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
import org.fermat.fermat_dap_android_sub_app_asset_factory.util.CommonLogger;
import org.fermat.fermat_dap_api.layer.all_definition.contracts.ContractProperty;
import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetContractPropertiesConstants;
import org.fermat.fermat_dap_api.layer.all_definition.util.DAPStandardFormats;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.AssetFactory;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.AssetFactorySettings;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.makeText;

/**
 * Created by frank on 12/15/15.
 */
public class WizardPropertiesFragment extends AbstractFermatFragment {

    private Activity activity;
    private AssetFactorySession assetFactorySession;
    private AssetFactoryModuleManager moduleManager;
    private ErrorManager errorManager;

    //UI
    private View rootView;
    private Toolbar toolbar;
    private Resources res;
    private FermatEditText wizardPropertiesAssetNameEditText;
    private FermatEditText wizardPropertiesAssetDescEditText;
    private FermatCheckBox wizardPropertiesRedeemableCheck;
    private FermatCheckBox wizardPropertiesTransfereableCheck;
    private FermatCheckBox wizardPropertiesExchangeableCheck;
    private FermatEditText wizardPropertiesExpDateEditText;
    private ImageButton wizardPropertiesDateButton;
    private FermatButton wizardPropertiesBackButton;
    private FermatButton wizardPropertiesNextButton;

    SettingsManager<AssetFactorySettings> settingsManager;
    private AssetFactory asset;
    private Calendar expirationTimeCalendar;

    public WizardPropertiesFragment() {

    }

    public static WizardPropertiesFragment newInstance() {
        return new WizardPropertiesFragment();
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

        expirationTimeCalendar = Calendar.getInstance();
        expirationTimeCalendar.setTime(new Date());

        configureToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dap_v3_factory_wizard_properties, container, false);
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
        wizardPropertiesAssetNameEditText = (FermatEditText) rootView.findViewById(R.id.wizardCryptoValueEditText);
        wizardPropertiesAssetDescEditText = (FermatEditText) rootView.findViewById(R.id.wizardPropertiesAssetDescEditText);
        wizardPropertiesRedeemableCheck = (FermatCheckBox) rootView.findViewById(R.id.wizardVerifyRedeemableCheck);
        wizardPropertiesTransfereableCheck = (FermatCheckBox) rootView.findViewById(R.id.wizardVerifyTransfereableCheck);
        wizardPropertiesExchangeableCheck = (FermatCheckBox) rootView.findViewById(R.id.wizardVerifyExchangeableCheck);
        wizardPropertiesExpDateEditText = (FermatEditText) rootView.findViewById(R.id.wizardPropertiesQuantityEditText);
        wizardPropertiesDateButton = (ImageButton) rootView.findViewById(R.id.wizardPropertiesDateButton);
        wizardPropertiesBackButton = (FermatButton) rootView.findViewById(R.id.wizardVerifyBackButton);
        wizardPropertiesNextButton = (FermatButton) rootView.findViewById(R.id.wizardVerifyFinishButton);

        wizardPropertiesDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        expirationTimeCalendar.set(Calendar.YEAR, year);
                        expirationTimeCalendar.set(Calendar.MONTH, month);
                        expirationTimeCalendar.set(Calendar.DAY_OF_MONTH, day);
                        wizardPropertiesExpDateEditText.setText(DAPStandardFormats.DATE_FORMAT.format(expirationTimeCalendar.getTime()));
                    }
                }, expirationTimeCalendar.get(Calendar.YEAR), expirationTimeCalendar.get(Calendar.MONTH), expirationTimeCalendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
                CommonLogger.debug("DatePickerDialog", "Showing DatePickerDialog...");
            }
        });
        wizardPropertiesBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProperties();
                changeActivity(Activities.DAP_SUB_APP_ASSET_FACTORY_WIZARD_MULTIMEDIA.getCode(), appSession.getAppPublicKey());
            }
        });
        wizardPropertiesNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProperties();
                changeActivity(Activities.DAP_SUB_APP_ASSET_FACTORY_WIZARD_CRYPTO.getCode(), appSession.getAppPublicKey());
            }
        });
    }

    private void saveProperties() {
        if (asset != null) {
            if (wizardPropertiesAssetNameEditText.getText().toString().length() > 0) {
                asset.setName(wizardPropertiesAssetNameEditText.getText().toString());
            }
            if (wizardPropertiesAssetDescEditText.getText().toString().length() > 0) {
                asset.setDescription(wizardPropertiesAssetDescEditText.getText().toString());
            }

            List<ContractProperty> contractProperties = asset.getContractProperties();
            if (contractProperties != null) {
                for (ContractProperty contractProperty : contractProperties) {
                    if (contractProperty.getName().equals(DigitalAssetContractPropertiesConstants.REDEEMABLE)) {
                        contractProperty.setValue(wizardPropertiesRedeemableCheck.isChecked());
                    }
                    if (contractProperty.getName().equals(DigitalAssetContractPropertiesConstants.TRANSFERABLE)) {
                        contractProperty.setValue(wizardPropertiesTransfereableCheck.isChecked());
                    }
                    if (contractProperty.getName().equals(DigitalAssetContractPropertiesConstants.SALEABLE)) {
                        contractProperty.setValue(wizardPropertiesExchangeableCheck.isChecked());
                    }
                }
            } else {
                contractProperties = new ArrayList<>();
                contractProperties.add(new ContractProperty(DigitalAssetContractPropertiesConstants.REDEEMABLE, wizardPropertiesRedeemableCheck.isChecked()));
                contractProperties.add(new ContractProperty(DigitalAssetContractPropertiesConstants.TRANSFERABLE, wizardPropertiesTransfereableCheck.isChecked()));
                contractProperties.add(new ContractProperty(DigitalAssetContractPropertiesConstants.SALEABLE, wizardPropertiesExchangeableCheck.isChecked()));
                asset.setContractProperties(contractProperties);
            }

            try {
                if (wizardPropertiesExpDateEditText.getText().toString().length() > 0) {
                    asset.setExpirationDate(new Timestamp(DAPStandardFormats.DATE_FORMAT.parse(wizardPropertiesExpDateEditText.getText().toString()).getTime()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupUIData() {
        if (appSession.getData("asset_factory") != null) {
            asset = (AssetFactory) appSession.getData("asset_factory");
            loadProperties();
        }
    }

    private void loadProperties() {
        if (asset.getName() != null && asset.getName().length() > 0) {
            wizardPropertiesAssetNameEditText.setText(asset.getName());
        } else {
            wizardPropertiesAssetNameEditText.setText("");
        }
        if (asset.getDescription() != null && asset.getDescription().length() > 0) {
            wizardPropertiesAssetDescEditText.setText(asset.getDescription());
        } else {
            wizardPropertiesAssetDescEditText.setText("");
        }
        List<ContractProperty> properties = asset.getContractProperties();
        wizardPropertiesRedeemableCheck.setChecked(false);
        wizardPropertiesTransfereableCheck.setChecked(false);
        wizardPropertiesExchangeableCheck.setChecked(false);
        if (properties != null && properties.size() > 0) {
            for (ContractProperty property : properties) {
                if (property.getName().equals(DigitalAssetContractPropertiesConstants.REDEEMABLE)) {
                    wizardPropertiesRedeemableCheck.setChecked(((Boolean) property.getValue()).booleanValue());
                }
                if (property.getName().equals(DigitalAssetContractPropertiesConstants.TRANSFERABLE)) {
                    wizardPropertiesTransfereableCheck.setChecked(((Boolean) property.getValue()).booleanValue());
                }
                if (property.getName().equals(DigitalAssetContractPropertiesConstants.SALEABLE)) {
                    wizardPropertiesExchangeableCheck.setChecked(((Boolean) property.getValue()).booleanValue());
                }
            }
        }
        if (asset.getExpirationDate() != null) {
            wizardPropertiesExpDateEditText.setText(DAPStandardFormats.DATE_FORMAT.format(new Date(asset.getExpirationDate().getTime())));
        } else {
            wizardPropertiesExpDateEditText.setText("");
        }
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
