package org.fermat.fermat_dap_android_wallet_redeem_point.v3.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.util.BitmapWorkerTask;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantGetSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.SettingsNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_redeem_point.models.DigitalAsset;
import org.fermat.fermat_dap_android_wallet_redeem_point.sessions.RedeemPointSession;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.filters.RedeemHomeCardAdapterFilter;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.fragments.RedeemHomeCardFragment;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.holders.RedeemCardViewHolder;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.RedeemPointSettings;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;

import java.util.List;

/**
 * Created by madscientist on 19/04/16.
 */
public class RedeemCardAdapter extends FermatAdapter<DigitalAsset, RedeemCardViewHolder> implements Filterable {

    private RedeemPointSession assetRedeemSession;
    private AssetRedeemPointWalletSubAppModule manager;
    private RedeemHomeCardFragment fragment;
    private List<DigitalAsset> allDigitalAssets;


    public RedeemCardAdapter(RedeemHomeCardFragment fragment, Context context, List<DigitalAsset> digitalAssets, AssetRedeemPointWalletSubAppModule manager,
                             FermatSession appSession) {
        super(context, digitalAssets);
        this.fragment = fragment;
        this.manager = manager;
        this.dataSet = digitalAssets;
        this.assetRedeemSession = (RedeemPointSession) appSession;
        this.allDigitalAssets = digitalAssets;
    }

    @Override
    protected RedeemCardViewHolder createHolder(View itemView, int type) {

        return new RedeemCardViewHolder(itemView, manager, context);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.dap_v3_wallet_asset_redeem_point_home_card_item;
    }

    @Override
    protected void bindHolder(RedeemCardViewHolder holder, DigitalAsset data, int position) {
        bind(holder, data);
    }

    @Override
    public Filter getFilter() {
        return new RedeemHomeCardAdapterFilter(this.allDigitalAssets, this);
    }

    public void bind(RedeemCardViewHolder holder, final DigitalAsset asset) {
        RedeemPointSettings settings = null;
        Boolean assetNotificationEnabled = false;
        try {
            settings = assetRedeemSession.getModuleManager().getSettingsManager().loadAndGetSettings(assetRedeemSession.getAppPublicKey());
            assetNotificationEnabled = settings.getAssetNotificationEnabled();
        } catch (Exception e) {
            settings = null;
        }

        Bitmap bitmap;
        if (asset.getImage() != null && asset.getImage().length > 0) {
            bitmap = BitmapFactory.decodeByteArray(asset.getImage(), 0, asset.getImage().length);
            bitmap = Bitmap.createScaledBitmap(bitmap, 42, 42, true);
            holder.cardAssetImage.setImageDrawable(ImagesUtils.getRoundedBitmap(holder.res, bitmap));
        } else {
            holder.cardAssetImage.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), R.drawable.img_asset_without_image));
        }

        holder.cardAssetName.setText(asset.getName());
        holder.cardTime.setText(asset.getFormattedDate());

        byte[] img = (asset.getImageActorUserFrom() == null) ? new byte[0] : asset.getImageActorUserFrom(); /*modificar modelo Digital Asset*/
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(holder.cardActorUserImage,
                holder.res, R.drawable.img_asset_without_image, false);
        bitmapWorkerTask.execute(img);

        holder.cardActorName.setText(asset.getActorUserNameFrom());

        holder.cardActorUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assetRedeemSession.setData("asset_data", asset);
                fragment.activityChange();

            }
        });


        if (asset.getStatus() == DigitalAsset.Status.PENDING && assetNotificationEnabled) {

            holder.actionButtons.setVisibility(View.VISIBLE);
            holder.confirmedV3Asset.setVisibility(View.GONE);
            holder.cardStatusImage.setVisibility(View.VISIBLE);

            holder.cardAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assetRedeemSession.setData("asset_data", asset);
                    fragment.doAcceptAsset();
                }
            });
            holder.cardRejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assetRedeemSession.setData("asset_data", asset);
                    fragment.doRejectAsset();
                }
            });

        } else if (asset.getStatus() == DigitalAsset.Status.PENDING && !assetNotificationEnabled) {
            holder.actionButtons.setVisibility(View.GONE);
            holder.confirmedV3Asset.setVisibility(View.GONE);
            holder.cardStatusImage.setVisibility(View.VISIBLE);

        } else if (asset.getStatus() == DigitalAsset.Status.CONFIRMED) {
            holder.cardStatusImage.setVisibility(View.GONE);
            holder.actionButtons.setVisibility(View.GONE);
            holder.confirmedV3Asset.setVisibility(View.VISIBLE);

        }
    }
}
