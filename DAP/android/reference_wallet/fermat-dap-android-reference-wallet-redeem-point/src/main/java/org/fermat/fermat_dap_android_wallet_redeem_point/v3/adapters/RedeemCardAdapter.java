package org.fermat.fermat_dap_android_wallet_redeem_point.v3.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_redeem_point.models.DigitalAsset;
import org.fermat.fermat_dap_android_wallet_redeem_point.sessions.RedeemPointSession;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.fragments.RedeemHomeCardFragment;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.holders.RedeemCardViewHolder;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;

import java.util.List;

/**
 * Created by madscientist on 19/04/16.
 */
public class RedeemCardAdapter extends FermatAdapter<DigitalAsset, RedeemCardViewHolder> implements Filterable {

    private  RedeemPointSession assetRedeemSession;
    private  AssetRedeemPointWalletSubAppModule manager;
    private  RedeemHomeCardFragment fragment;

    public RedeemCardAdapter(RedeemHomeCardFragment fragment, Context context, List<DigitalAsset> digitalAssets, AssetRedeemPointWalletSubAppModule manager,
                           FermatSession appSession) {
        super(context, digitalAssets);
        this.fragment = fragment;
        this.manager = manager;
        this.dataSet = digitalAssets;
        this.assetRedeemSession = (RedeemPointSession) appSession;
    }
    @Override
    protected RedeemCardViewHolder createHolder(View itemView, int type) {
        return null;
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.dap_v3_wallet_asset_redeem_point_home_card_item;
    }

    @Override
    protected void bindHolder(RedeemCardViewHolder holder, DigitalAsset data, int position) {

    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
