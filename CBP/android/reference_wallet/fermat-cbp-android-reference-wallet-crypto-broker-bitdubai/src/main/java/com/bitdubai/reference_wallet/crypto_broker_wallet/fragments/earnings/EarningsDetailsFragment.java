package com.bitdubai.reference_wallet.crypto_broker_wallet.fragments.earnings;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.layer.all_definition.enums.TimeFrequency;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces.EarningsPair;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces.EarningsSearch;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;
import com.bitdubai.reference_wallet.crypto_broker_wallet.R;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.adapters.EarningsOverviewAdapter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.models.EarningsDetailData;
import com.bitdubai.reference_wallet.crypto_broker_wallet.session.CryptoBrokerWalletSession;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets.CBP_CRYPTO_BROKER_WALLET;
import static com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT;


/**
 * A simple {@link Fragment} subclass.
 */
public class EarningsDetailsFragment extends AbstractFermatFragment<CryptoBrokerWalletSession, WalletResourcesProviderManager> {

    // Constants
    private static final String TAG = "EarningsDetailsFrag";

    // Data
    private EarningsPair earningsPair;
    private TimeFrequency frequency;
    private List<EarningsDetailData> data;

    public static EarningsDetailsFragment newInstance(CryptoBrokerWalletSession session) {
        final EarningsDetailsFragment earningsDetailsFragment = new EarningsDetailsFragment();
        earningsDetailsFragment.appSession = session;

        return earningsDetailsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.cbw_fragment_earnings_details, container, false);

        final NumberFormat instance = DecimalFormat.getInstance();
        final EarningsDetailData currentEarning = data.isEmpty() ? null : data.get(0);
        final EarningsDetailData previousEarning = (!data.isEmpty() && data.size() > 1 ? data.get(1) : null);

        final FermatTextView currentEarningValue = (FermatTextView) layout.findViewById(R.id.cbw_current_earning_value);
        currentEarningValue.setText(currentEarning != null ? instance.format(currentEarning.getAmount()) : "No current earnings");

        final FermatTextView currentEarningText = (FermatTextView) layout.findViewById(R.id.cbw_current_earning_text);
        currentEarningText.setText(getCurrentEarningText(frequency));

        final FermatTextView previousEarningValue = (FermatTextView) layout.findViewById(R.id.cbw_previous_earning_value);
        previousEarningValue.setText(previousEarning != null ? instance.format(previousEarning.getAmount()) : "No previous earnings");

        final FermatTextView previousEarningText = (FermatTextView) layout.findViewById(R.id.cbw_previous_earning_text);
        previousEarningText.setText(getPreviousEarningText(frequency));

        final FermatTextView timeFieldTextView = (FermatTextView) layout.findViewById(R.id.cbw_earning_time_field);
        timeFieldTextView.setText(frequency.getFriendlyName());

        final EarningsOverviewAdapter earningsOverviewAdapter = new EarningsOverviewAdapter(getActivity(), data, earningsPair.getEarningCurrency());
        earningsOverviewAdapter.setTimeFrecuency(frequency);
        final RecyclerView earningsOverviewRecyclerView = (RecyclerView) layout.findViewById(R.id.earning_overview_recycler_view);
        earningsOverviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        earningsOverviewRecyclerView.setAdapter(earningsOverviewAdapter);

        return layout;
    }

    public void bindData(EarningsPair earningsPair, TimeFrequency frequency) {
        this.earningsPair = earningsPair;
        this.frequency = frequency;

        final ErrorManager errorManager = appSession.getErrorManager();
        try {
            final EarningsSearch search = this.earningsPair.getSearch();
            data = EarningsDetailData.generateEarningsDetailData(search.listResults(), frequency);

        } catch (Exception e) {
            //data = TestData.getEarnings(earningCurrency, frequency);  //TODO: just for test purposes
            data = new ArrayList<>();
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(CBP_CRYPTO_BROKER_WALLET, DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            else
                Log.e(TAG, e.getMessage(), e);
        }
    }


    private String getCurrentEarningText(TimeFrequency frequency) {
        switch (frequency) {
            case DAILY:
                return "Today";
            case WEEKLY:
                return "This Week";
            case MONTHLY:
                return "This Month";
            case YEARLY:
                return "This Year";
        }
        return "";
    }

    private String getPreviousEarningText(TimeFrequency frequency) {
        switch (frequency) {
            case DAILY:
                return "Yesterday";
            case WEEKLY:
                return "Last Week";
            case MONTHLY:
                return "Last Month";
            case YEARLY:
                return "Last Year";
        }
        return "";
    }
}
