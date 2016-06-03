package org.fermat.fermat_dap_android_sub_app_redeem_point_community.navigation_drawer;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_dap_android_sub_app_redeem_point_community_bitdubai.R;

import org.fermat.fermat_dap_android_sub_app_redeem_point_community.common.views.RedeemCommunityUtils;
import org.fermat.fermat_dap_android_sub_app_redeem_point_community.sessions.AssetRedeemPointCommunitySubAppSession;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetIdentityRedeemPointException;
import org.fermat.fermat_dap_api.layer.dap_identity.redeem_point.interfaces.RedeemPointIdentity;

import java.io.ByteArrayInputStream;

/**
 * @author Created by mati on 2015.11.12..
 * @author Modified byJose Manuel De Sousa 08/12/2015
 */
public class RedeemPointCommunityFragmentsCommons {

    public static View setUpHeaderScreen(LayoutInflater inflater, Context activity,
                                         AssetRedeemPointCommunitySubAppSession assetRedeemPointCommunitySubAppSession, ActiveActorIdentityInformation identityAssetRedeem) throws CantGetIdentityRedeemPointException {
        /**
         * Navigation view header
         */
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
        relativeLayout.setLayoutParams(layoutParams);
        View view = inflater.inflate(R.layout.dap_navigation_drawer_community_redeem_point_header, relativeLayout, true);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view_profile);

        try {
            RedeemPointIdentity identity = assetRedeemPointCommunitySubAppSession.getModuleManager().getActiveAssetRedeemPointIdentity();

            if (identity != null) {
                if (identity.getImage() != null) {
                    if (identity.getImage().length > 0) {
                        ByteArrayInputStream bytes = new ByteArrayInputStream(identity.getImage());
                        BitmapDrawable bmd = new BitmapDrawable(bytes);
                        imageView.setImageBitmap(RedeemCommunityUtils.getCircleBitmap(bmd.getBitmap()));
                    } else
                        imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
                } else
                    imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);

//            Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
                FermatTextView fermatTextView = (FermatTextView) view.findViewById(R.id.txt_name);
                fermatTextView.setText(identity.getAlias());
            } else
                imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);

        } catch (CantGetIdentityRedeemPointException e) {
            e.printStackTrace();
        }

        return view;
    }
}
