package org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_dap_android_wallet_asset_issuer_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_asset_issuer.common.filters.MyAssetsAdapterFilter;
import org.fermat.fermat_dap_android_wallet_asset_issuer.holders.MyAssetsViewHolder;
import org.fermat.fermat_dap_android_wallet_asset_issuer.models.DigitalAsset;
import org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.filters.HomeCardAdapterFilter;
import org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.holders.HomeCardViewHolder;
import org.fermat.fermat_dap_android_wallet_asset_issuer.v3.fragments.HomeCardFragment;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;

import java.util.List;

/**
 * Created by frank on 12/8/15.
 */
public class HomeCardAdapter extends FermatAdapter<DigitalAsset, HomeCardViewHolder> implements Filterable {

    private HomeCardFragment homeCardFragment;
    private AssetIssuerWalletSupAppModuleManager manager;
    private FermatSession appSession;

    public HomeCardAdapter(HomeCardFragment homeCardFragment, Context context, List<DigitalAsset> digitalAssets, AssetIssuerWalletSupAppModuleManager manager, FermatSession appSession) {
        super(context, digitalAssets);
        this.homeCardFragment = homeCardFragment;
        this.manager = manager;
        this.dataSet = digitalAssets;
        this.appSession = appSession;
    }

    @Override
    protected HomeCardViewHolder createHolder(View itemView, int type) {
        return new HomeCardViewHolder(itemView, manager, context, appSession);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.dap_wallet_asset_issuer_home_card;
    }

    @Override
    protected void bindHolder(HomeCardViewHolder holder, DigitalAsset data, int position) {
        holder.bind(data, homeCardFragment);
    }

    @Override
    public Filter getFilter() {
        return new HomeCardAdapterFilter(this.dataSet, this);
    }
}
