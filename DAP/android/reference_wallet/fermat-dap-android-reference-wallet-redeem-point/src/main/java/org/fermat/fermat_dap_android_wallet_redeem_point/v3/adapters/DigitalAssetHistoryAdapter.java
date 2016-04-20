package org.fermat.fermat_dap_android_wallet_redeem_point.v3.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_redeem_point.filters.MyAssetsAdapterFilter;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.filters.DigitalAssetHistoryAdapterFilter;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.holders.DigitalAssetHistoryItemViewHolder;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.models.DigitalAssetHistory;
import org.fermat.fermat_dap_api.layer.all_definition.util.DAPStandardFormats;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Penny on 19/04/16.
 */
public class DigitalAssetHistoryAdapter extends FermatAdapter<DigitalAssetHistory, DigitalAssetHistoryItemViewHolder> implements Filterable {

    private AssetRedeemPointWalletSubAppModule manager;
    private LinkedHashMap<String, Integer> mapIndex;
    private LinkedHashMap<String, Integer> mapAssetQuantity;
    private ArrayList<String> sectionList;
    private String[] sections;

    public DigitalAssetHistoryAdapter(Context context, List<DigitalAssetHistory> dataSet, AssetRedeemPointWalletSubAppModule manager) {
        super(context, dataSet);
        this.manager = manager;
        fillSections();
    }

    private void fillSections() {
        mapIndex = new LinkedHashMap<String, Integer>();
        mapAssetQuantity = new LinkedHashMap<String, Integer>();

        //Sort dataSet for accurate date display
        Collections.sort(dataSet);
        for (int x = 0; x < dataSet.size(); x++) {
            String dateAccepted = DAPStandardFormats.DATE_FORMAT.format(dataSet.get(x).getAcceptedDate());
            if (dateAccepted != null) {
                if (!mapIndex.containsKey(dateAccepted)) {
                    mapIndex.put(dateAccepted, x);
                    mapAssetQuantity.put(dateAccepted,1);
                }else {
                    mapAssetQuantity.put(dateAccepted, mapAssetQuantity.get(dateAccepted) + 1);
                }
            }
        }
        /*Set<Timestamp> sectionDates = mapIndex.keySet();
        // create a list from the set to sort
        sectionList = new ArrayList<>(sectionDates);
        Collections.sort(sectionList);

        sections = new String[sectionList.size()];
        sectionList.toArray(sections);*/
    }

    private Timestamp getSection(DigitalAssetHistory digitalAssetHistory) {
        return digitalAssetHistory.getAcceptedDate();
    }

    @Override
    protected DigitalAssetHistoryItemViewHolder createHolder(View itemView, int type) {
        return new DigitalAssetHistoryItemViewHolder(itemView, manager, context);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.dap_v3_wallet_asset_redeem_point_asset_user_history_item;
    }

    @Override
    protected void bindHolder(DigitalAssetHistoryItemViewHolder holder, DigitalAssetHistory data, int position) {
        holder.bind(data, data.getFormattedAcceptedDate(), mapAssetQuantity.get(data.getFormattedAcceptedDate()) , mapIndex.get(data.getFormattedAcceptedDate()) == position);
    }

    @Override
    public Filter getFilter() {
        return new DigitalAssetHistoryAdapterFilter(this.dataSet, this);
    }

    @Override
    public void changeDataSet(List<DigitalAssetHistory> dataSet) {
        super.changeDataSet(dataSet);
        fillSections();
    }
}
