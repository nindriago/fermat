package org.fermat.fermat_dap_android_sub_app_asset_factory.v3.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatEditText;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter;
import com.bitdubai.fermat_dap_android_sub_app_asset_factory_bitdubai.R;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import org.fermat.fermat_dap_android_sub_app_asset_factory.adapters.BitcoinsSpinnerAdapter;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.AssetFactorySession;
import org.fermat.fermat_dap_android_sub_app_asset_factory.sessions.SessionConstantsAssetFactory;
import org.fermat.fermat_dap_android_sub_app_asset_factory.util.Utils;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.adapters.AssetValueSpinnerAdapter;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.adapters.FeeSpinnerAdapter;
import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPFeeType;
import org.fermat.fermat_dap_api.layer.all_definition.util.DAPStandardFormats;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.AssetFactory;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.AssetFactorySettings;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;

import java.text.ParseException;

import static android.widget.Toast.makeText;
import static com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter.Currency.BITCOIN;
import static com.bitdubai.fermat_api.layer.all_definition.util.BitcoinConverter.Currency.SATOSHI;

/**
 * Created by frank on 12/15/15.
 */
public class WizardCryptoFragment extends AbstractFermatFragment {

    private Activity activity;
    private AssetFactorySession assetFactorySession;
    private AssetFactoryModuleManager moduleManager;
    private ErrorManager errorManager;

    //UI
    private View rootView;
    private Toolbar toolbar;
    private Resources res;
    private FermatEditText wizardCryptoValueEditText;
    private Spinner wizardCryptoValueSpinner;
    private Spinner wizardCryptoFeeSpinner;
    private FermatEditText wizardCryptoQuantityEditText;
    private FermatTextView wizardCryptoTotalValue;
    private FermatTextView wizardCryptoBalanceValue;
    private FermatButton wizardCryptoBackButton;
    private FermatButton wizardCryptoNextButton;

    SettingsManager<AssetFactorySettings> settingsManager;
    private AssetFactory asset;

    public WizardCryptoFragment() {

    }

    public static WizardCryptoFragment newInstance() {
        return new WizardCryptoFragment();
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
        rootView = inflater.inflate(R.layout.dap_v3_factory_wizard_crypto, container, false);
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
        wizardCryptoValueEditText = (FermatEditText) rootView.findViewById(R.id.wizardCryptoValueEditText);
        wizardCryptoValueSpinner = (Spinner) rootView.findViewById(R.id.wizardCryptoValueSpinner);
        wizardCryptoFeeSpinner = (Spinner) rootView.findViewById(R.id.wizardCryptoFeeSpinner);
        wizardCryptoQuantityEditText = (FermatEditText) rootView.findViewById(R.id.wizardCryptoQuantityEditText);
        wizardCryptoTotalValue = (FermatTextView) rootView.findViewById(R.id.wizardCryptoTotalValue);
        wizardCryptoBalanceValue = (FermatTextView) rootView.findViewById(R.id.wizardCryptoBalanceValue);
        wizardCryptoBackButton = (FermatButton) rootView.findViewById(R.id.wizardCryptoBackButton);
        wizardCryptoNextButton = (FermatButton) rootView.findViewById(R.id.wizardCryptoNextButton);
        wizardCryptoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCrypto();
                changeActivity(Activities.DAP_SUB_APP_ASSET_FACTORY_WIZARD_PROPERTIES.getCode(), appSession.getAppPublicKey());
            }
        });
        wizardCryptoNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCrypto();
                changeActivity(Activities.DAP_SUB_APP_ASSET_FACTORY_WIZARD_VERIFY.getCode(), appSession.getAppPublicKey());
            }
        });
    }

    private void saveCrypto() {
        if (asset != null) {
            try {
                if (wizardCryptoValueEditText.getText().toString().length() > 0) {
                    double amount = DAPStandardFormats.BITCOIN_FORMAT.parse(wizardCryptoValueEditText.getText().toString()).doubleValue();
                    BitcoinConverter.Currency from = (BitcoinConverter.Currency) wizardCryptoValueSpinner.getSelectedItem();
                    long amountSatoshi = ((Double) BitcoinConverter.convert(amount, from, SATOSHI)).longValue();
                    asset.setAmount(amountSatoshi);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            asset.setFee(((DAPFeeType) wizardCryptoFeeSpinner.getSelectedItem()).getFeeValue());
            if (wizardCryptoQuantityEditText.getText().toString().length() > 0) {
                asset.setQuantity(Integer.valueOf(wizardCryptoQuantityEditText.getText().toString()));
            }
        }
    }

    private void setupUIData() {
        final BitcoinConverter.Currency[] data = BitcoinConverter.Currency.values();
        final ArrayAdapter<BitcoinConverter.Currency> spinnerAdapter = new AssetValueSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item,
                data);
        wizardCryptoValueSpinner.setAdapter(spinnerAdapter);
        wizardCryptoValueSpinner.setSelection(3);
        wizardCryptoValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateBitcoins();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        wizardCryptoValueEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                updateBitcoins();
                return false;
            }
        });
        wizardCryptoTotalValue.setText(String.format("%.6f BTC", 0.0));
        final DAPFeeType[] feeTypes = DAPFeeType.values();
        final ArrayAdapter<DAPFeeType> spinnerAdapterFee = new FeeSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item,
                feeTypes);
        wizardCryptoFeeSpinner.setAdapter(spinnerAdapterFee);
        wizardCryptoFeeSpinner.setSelection(1);
        wizardCryptoFeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateBitcoins();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        wizardCryptoQuantityEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                updateBitcoins();
                return false;
            }
        });

        try {
            long satoshis = moduleManager.getBitcoinWalletBalance(Utils.getBitcoinWalletPublicKey(moduleManager));
            double bitcoinWalletBalance = BitcoinConverter.convert(satoshis, SATOSHI, BITCOIN);
            wizardCryptoBalanceValue.setText(String.format("%.6f BTC", bitcoinWalletBalance));
        } catch (Exception e) {
            wizardCryptoBalanceValue.setText(getResources().getString(R.string.dap_asset_factory_no_available));
        }

        if (appSession.getData("asset_factory") != null) {
            asset = (AssetFactory) appSession.getData("asset_factory");
            loadProperties(spinnerAdapterFee);
        }
    }

    private void loadProperties(ArrayAdapter<DAPFeeType> spinnerAdapterFee) {
        if (asset.getAmount() > 0) {
            wizardCryptoValueEditText.setText(Long.toString(asset.getAmount()));
        }
        if (asset.getFee() > 0) {
            try {
                int pos = spinnerAdapterFee.getPosition(DAPFeeType.findByFeeValue(asset.getFee()));
                wizardCryptoFeeSpinner.setSelection(pos);
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            }
        }
        if (asset.getQuantity() > 0) {
            wizardCryptoQuantityEditText.setText(Integer.toString(asset.getQuantity()));
        }
        updateBitcoins();
    }

    private void updateBitcoins() {
        Object selectedItem = wizardCryptoValueSpinner.getSelectedItem();
        String bitcoinViewStr = wizardCryptoValueEditText.getText().toString();
        if (selectedItem != null && bitcoinViewStr != null && bitcoinViewStr.length() > 0) {
            BitcoinConverter.Currency from = (BitcoinConverter.Currency) wizardCryptoValueSpinner.getSelectedItem();
            double amount = Double.parseDouble(wizardCryptoValueEditText.getText().toString());
            double amountBTC = BitcoinConverter.convert(amount, from, BITCOIN);
            String qstr = wizardCryptoQuantityEditText.getText().toString();
            int quantity = (qstr != null && qstr.length() > 0) ? Integer.parseInt(qstr) : 0;
            double total = quantity * amountBTC;
            wizardCryptoTotalValue.setText(String.format("%.6f BTC", total));
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
