package org.fermat.fermat_dap_android_sub_app_asset_issuer_community.navigation_drawer;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_dap_android_sub_app_asset_issuer_community_bitdubai.R;

import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.common.views.IssuerCommunityUtils;
import org.fermat.fermat_dap_android_sub_app_asset_issuer_community.sessions.AssetIssuerCommunitySubAppSession;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetIdentityAssetIssuerException;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetIdentityAssetUserException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;

import java.io.ByteArrayInputStream;

/**
 * @author Created by mati on 2015.11.12..
 * @author Modified byJose Manuel De Sousa 08/12/2015
 */
public class IssuerCommunityFragmentsCommons {

    public static View setUpHeaderScreen(LayoutInflater inflater, Context activity,
                                         AssetIssuerCommunitySubAppSession assetIssuerCommunitySubAppSession, ActiveActorIdentityInformation identityAssetIssuer) throws CantGetIdentityAssetUserException {
        /**
         * Navigation view header
         */
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
        relativeLayout.setLayoutParams(layoutParams);
        View view = inflater.inflate(R.layout.dap_navigation_drawer_community_issuer_header, relativeLayout, true);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view_profile);

        try {
            IdentityAssetIssuer identity = assetIssuerCommunitySubAppSession.getModuleManager().getActiveAssetIssuerIdentity();

            if (identity != null) {
                if (identity.getImage() != null) {
                    if (identity.getImage().length > 0) {
                        ByteArrayInputStream bytes = new ByteArrayInputStream(identity.getImage());
                        BitmapDrawable bmd = new BitmapDrawable(bytes);
                        imageView.setImageBitmap(IssuerCommunityUtils.getCircleBitmap(bmd.getBitmap()));
                    } else
                        imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
                } else
                    imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);

//            Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
                FermatTextView fermatTextView = (FermatTextView) view.findViewById(R.id.txt_name);
                fermatTextView.setText(identity.getAlias());
            } else
                imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
        } catch (CantGetIdentityAssetIssuerException e) {
            e.printStackTrace();
        }

        return view;
    }
}
