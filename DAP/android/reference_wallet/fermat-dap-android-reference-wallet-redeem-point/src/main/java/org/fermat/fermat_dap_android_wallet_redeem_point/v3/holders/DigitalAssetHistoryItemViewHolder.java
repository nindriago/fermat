package org.fermat.fermat_dap_android_wallet_redeem_point.v3.holders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_dap_android_wallet_redeem_point_bitdubai.R;
import com.squareup.picasso.Picasso;

import org.fermat.fermat_dap_android_wallet_redeem_point.models.UserRedeemed;
import org.fermat.fermat_dap_android_wallet_redeem_point.v3.models.DigitalAssetHistory;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;

/**
 * Created by Penny on 19/04/16.
 */
public class DigitalAssetHistoryItemViewHolder extends FermatViewHolder {
    private AssetRedeemPointWalletSubAppModule manager;
    private Context context;

    private final LinearLayout historySectionHeader;
    private final FermatTextView historyDateHeader;
    private final FermatTextView historyAssetsQuantity;

    private final FermatTextView historyAssetName;
    private final FermatTextView historyUserName;

    private final ImageView imageViewAssetRedeemedAvatar;
    private final ImageView imageViewUserRedeemedAvatar;

    /**
     * Constructor
     *
     * @param itemView
     */
    public DigitalAssetHistoryItemViewHolder(View itemView, AssetRedeemPointWalletSubAppModule manager, Context context) {
        super(itemView);
        this.manager = manager;
        this.context = context;

        historySectionHeader = (LinearLayout) itemView.findViewById(R.id.historySectionHeader);

        historyDateHeader = (FermatTextView) itemView.findViewById(R.id.historyDateHeader);
        historyAssetsQuantity = (FermatTextView) itemView.findViewById(R.id.historyAssetsQuantity);
        historyAssetName = (FermatTextView) itemView.findViewById(R.id.historyAssetName);
        historyUserName = (FermatTextView) itemView.findViewById(R.id.historyUserName);

        imageViewAssetRedeemedAvatar = (ImageView) itemView.findViewById(R.id.imageViewAssetRedeemedAvatar);
        imageViewUserRedeemedAvatar = (ImageView) itemView.findViewById(R.id.imageViewUserRedeemedAvatar);

    }

    public void bind(final DigitalAssetHistory digitalAssetHistory, String sectionTextDate, Integer assetsQuantity, boolean showSection) {

        if (showSection)
        {
            historySectionHeader.setVisibility(View.VISIBLE);
            historyDateHeader.setText(sectionTextDate);
            String textSectionRight = (assetsQuantity > 1 ? (assetsQuantity+" Assets"):(assetsQuantity+" Asset"));
            historyAssetsQuantity.setText(textSectionRight);
        }
        else
            historySectionHeader.setVisibility(View.GONE);

        historyAssetName.setText(digitalAssetHistory.getHistoryNameAsset());
        historyUserName.setText(digitalAssetHistory.getHistoryNameUser());

        if (digitalAssetHistory.getImageAsset() != null && digitalAssetHistory.getImageAsset().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(digitalAssetHistory.getImageAsset(), 0, digitalAssetHistory.getImageAsset().length);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
            imageViewAssetRedeemedAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), bitmap));
        }
        else
            imageViewAssetRedeemedAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), R.drawable.img_asset_without_image));

        if (digitalAssetHistory.getImageActorUserFrom() != null && digitalAssetHistory.getImageActorUserFrom().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(digitalAssetHistory.getImageActorUserFrom(), 0, digitalAssetHistory.getImageActorUserFrom().length);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
            imageViewUserRedeemedAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), bitmap));
        }else
            imageViewUserRedeemedAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), R.drawable.asset_user_identity_history));

    }


}
