package org.fermat.fermat_dap_android_wallet_redeem_point.v3.fragments;

import android.support.v7.widget.RecyclerView;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_redeem_point.models.DigitalAsset;

/**
 * Created by Jinmy Bohorquez on 15/04/16.
 */
public class RedeemHomeCardFragment extends FermatWalletListFragment<DigitalAsset> implements FermatListItemListeners<DigitalAsset> {

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected int getLayoutResource() {return R.layout.dap_v3_wallet_asset_redeem_point_home_card;}

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.redeem_swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.dap_v3_wallet_asset_redeem_home_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return false;
    }

    @Override
    public void onPostExecute(Object... result) {

    }

    @Override
    public void onErrorOccurred(Exception ex) {

    }

    @Override
    public FermatAdapter getAdapter() {
        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    public void onItemClickListener(DigitalAsset data, int position) {

    }

    @Override
    public void onLongItemClickListener(DigitalAsset data, int position) {

    }
}
