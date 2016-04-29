package org.fermat.fermat_dap_android_sub_app_asset_factory.v3.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;

import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.fragments.DraftAssetsHomeFragment;
import org.fermat.fermat_dap_android_sub_app_asset_factory.v3.holders.AssetFactoryDraftHolder;
import org.fermat.fermat_dap_api.layer.dap_middleware.dap_asset_factory.interfaces.AssetFactory;
import org.fermat.fermat_dap_api.layer.dap_module.asset_factory.interfaces.AssetFactoryModuleManager;

import java.util.List;

/**
 * Created by Jinmy Bohorquez on 29/04/16.
 */
public class AssetFactoryDraftAdapter extends FermatAdapter<AssetFactory, AssetFactoryDraftHolder> implements
        Filterable{


        public AssetFactoryDraftAdapter(DraftAssetsHomeFragment fragment, Context context, List<AssetFactory> dataSet, AssetFactoryModuleManager manager,
                                           FermatSession appSession) {
                super(context);
        }

        @Override
        protected AssetFactoryDraftHolder createHolder(View itemView, int type) {
                return null;
        }

        @Override
        protected int getCardViewResource() {
                return 0;
        }

        @Override
        protected void bindHolder(AssetFactoryDraftHolder holder, AssetFactory data, int position) {

        }

        @Override
        public Filter getFilter() {
                return null;
        }
}
