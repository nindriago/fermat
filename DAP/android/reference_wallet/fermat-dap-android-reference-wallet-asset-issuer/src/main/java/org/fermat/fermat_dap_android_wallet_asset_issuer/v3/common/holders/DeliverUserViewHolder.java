package org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.holders;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_dap_android_wallet_asset_issuer_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_asset_issuer.models.User;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;

/**
 * Created by frank on 12/8/15.
 */
public class DeliverUserViewHolder extends FermatViewHolder {
    private AssetIssuerWalletSupAppModuleManager manager;
    private Context context;
    private Resources res;

    private FermatTextView userNameText;
    private ImageView userImage;



    /**
     * Constructor
     *
     * @param itemView
     */
    public DeliverUserViewHolder(View itemView, AssetIssuerWalletSupAppModuleManager manager, Context context) {
        super(itemView);
        this.manager = manager;
        this.context = context;
        res = itemView.getResources();

        userNameText = (FermatTextView) itemView.findViewById(R.id.userNameText);
        userImage = (ImageView) itemView.findViewById(R.id.userImage);
    }

    public void bind(final User user) {
        userNameText.setText(user.getName());

        if (user.isSelected()) {
            userImage.setBackground(res.getDrawable(R.drawable.bg_circular_check));
        } else {
            userImage.setBackground(null);
        }

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.isSelected()) {
                    user.setSelected(true);
                    userImage.setBackground(res.getDrawable(R.drawable.bg_circular_check));
                } else {
                    user.setSelected(false);
                    userImage.setBackground(null);
                }
            }
        });

        if (user.getActorAssetUser().getProfileImage() != null && user.getActorAssetUser().getProfileImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getActorAssetUser().getProfileImage(), 0, user.getActorAssetUser().getProfileImage().length);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
            userImage.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), bitmap));
        }
    }
}
