package org.fermat.fermat_dap_android_wallet_asset_issuer.v3.common.holders;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_dap_android_wallet_asset_issuer_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_asset_issuer.models.Group;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;

/**
 * Created by frank on 12/8/15.
 */
public class DeliverGroupViewHolder extends FermatViewHolder {
    private AssetIssuerWalletSupAppModuleManager manager;
    private Context context;
    private Resources res;

    private FermatTextView groupNameText;
    private ImageView groupImage;



    /**
     * Constructor
     *
     * @param itemView
     */
    public DeliverGroupViewHolder(View itemView, AssetIssuerWalletSupAppModuleManager manager, Context context) {
        super(itemView);
        this.manager = manager;
        this.context = context;
        res = itemView.getResources();

        groupNameText = (FermatTextView) itemView.findViewById(R.id.groupNameText);
        groupImage = (ImageView) itemView.findViewById(R.id.groupImage);
    }

    public void bind(final Group group) {
        groupNameText.setText(group.getName());

        if (group.isSelected()) {
            groupImage.setBackground(res.getDrawable(R.drawable.bg_circular_check));
        } else {
            groupImage.setBackground(null);
        }

        groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!group.isSelected()) {
                    group.setSelected(true);
                    groupImage.setBackground(res.getDrawable(R.drawable.bg_circular_check));
                } else {
                    group.setSelected(false);
                    groupImage.setBackground(null);
                }
            }
        });

//        if (group.getActorAssetUserGroup().getProfileImage() != null && group.getActorAssetUserGroup().getProfileImage().length > 0) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(group.getActorAssetUserGroup().getProfileImage(), 0, group.getActorAssetUserGroup().getProfileImage().length);
//            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
//            groupImage.setImageDrawable(ImagesUtils.getRoundedBitmap(context.getResources(), bitmap));
//        }
    }
}
