package org.fermat.fermat_dap_android_wallet_redeem_point.v3.holders;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_android_api.ui.util.BitmapWorkerTask;
import com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_redeem_point.models.DigitalAsset;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;


/**
 * Created by Jinmy Bohorquez on 19/04/16.
 */
public class RedeemCardViewHolder extends FermatViewHolder {

    private AssetRedeemPointWalletSubAppModule manager;
    private Context context;
    public Resources res;

    public ImageView cardAssetImage;
    public FermatTextView cardAssetName;
    public FermatTextView cardTime;
    public ImageView cardActorUserImage;
    public FermatTextView cardActorName;
    public ImageView cardConfirmedImage;
    public ImageView cardStatusImage;

    public ImageButton cardDeliverButton;
    public ImageButton cardAcceptButton;
    public ImageButton cardRejectButton;

    public View redeemNegotiationV3Asset;
    public View confirmedV3Asset;


    public RedeemCardViewHolder(View itemView, AssetRedeemPointWalletSubAppModule manager, Context context) {
        super(itemView);
        this.manager = manager;
        this.context = context;
        res = itemView.getResources();

        cardAssetImage = (ImageView) itemView.findViewById(R.id.cardAssetImage);
        cardAssetName = (FermatTextView) itemView.findViewById(R.id.cardAssetName);
        cardTime = (FermatTextView) itemView.findViewById(R.id.cardTime);
        cardActorUserImage = (ImageView) itemView.findViewById(R.id.cardActorUserImage);
        cardActorName = (FermatTextView) itemView.findViewById(R.id.cardActorName);
        cardConfirmedImage = (ImageView) itemView.findViewById(R.id.cardConfirmedImage);
        cardStatusImage = (ImageView) itemView.findViewById(R.id.cardStatusImage);
        cardDeliverButton = (ImageButton) itemView.findViewById(R.id.cardDeliverButton);
        cardAcceptButton = (ImageButton) itemView.findViewById(R.id.cardAcceptButton);
        cardRejectButton = (ImageButton) itemView.findViewById(R.id.cardRejectButton);
        redeemNegotiationV3Asset = itemView.findViewById(R.id.redeemPendingV3Asset);
        confirmedV3Asset = itemView.findViewById(R.id.confirmedV3Asset);
        
    }
    
    public void Bind(final DigitalAsset asset){
        
        Bitmap bitmap;
        if (asset.getImage() != null && asset.getImage().length > 0) {
            bitmap = BitmapFactory.decodeByteArray(asset.getImage(), 0, asset.getImage().length);
        } else {
            bitmap = BitmapFactory.decodeResource(res, R.drawable.img_asset_without_image);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 45, 45, true);
        cardAssetImage.setImageDrawable(ImagesUtils.getRoundedBitmap(res, bitmap));

        cardAssetName.setText(asset.getName());
//        cardTime.setText(asset.getFormattedDate()); agregasr este metodo al modelo digital asset
        cardTime.setText(asset.getFormattedExpDate());

        byte[] img = (asset.getImageActorUserFrom() == null) ? new byte[0] : asset.getImage(); /*modificar modelo Digital Asset*/
        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(cardAssetImage,
                res, R.drawable.img_asset_without_image, false);
        bitmapWorkerTask.execute(img);

        cardActorName.setText(asset.getActorUserNameFrom());

        /*agragar logica de negotiation */
//        if( not negotiation whatever){
//            confirmedV3Asset.setVisibility(View.VISIBLE);
//            redeemNegotiationV3Asset.setVisibility(View.GONE);
//
//        }else {}

    }


}
