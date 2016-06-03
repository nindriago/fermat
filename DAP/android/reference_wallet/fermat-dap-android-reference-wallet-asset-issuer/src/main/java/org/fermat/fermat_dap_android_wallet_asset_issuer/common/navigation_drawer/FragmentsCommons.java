package org.fermat.fermat_dap_android_wallet_asset_issuer.common.navigation_drawer;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_dap_android_wallet_asset_issuer_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_asset_issuer.common.views.WalletIssuerUtils;
import org.fermat.fermat_dap_android_wallet_asset_issuer.sessions.AssetIssuerSession;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetIdentityAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;

import java.io.ByteArrayInputStream;

/**
 * Created by Matias Furszyfer on 2015.11.12..
 */
public class FragmentsCommons {

    public static View setUpHeaderScreen(LayoutInflater inflater, Context activity,
                                         AssetIssuerSession assetIssuerSession, ActiveActorIdentityInformation identityAssetIssuer) throws CantGetIdentityAssetIssuerException {

        //Dap v3
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
        relativeLayout.setLayoutParams(layoutParams);
        View view = inflater.inflate(R.layout.dap_v3_navigation_drawer_issuer_wallet_header, relativeLayout, true);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view_profile);

        try {
            IdentityAssetIssuer identity = assetIssuerSession.getModuleManager().getActiveAssetIssuerIdentity();

            if (identity != null) {
                if (identity.getImage() != null) {
                    if (identity.getImage().length > 0) {
                        ByteArrayInputStream bytes = new ByteArrayInputStream(identity.getImage());
                        BitmapDrawable bmd = new BitmapDrawable(bytes);
                        imageView.setImageBitmap(WalletIssuerUtils.getCircleBitmap(bmd.getBitmap()));
                    } else
                        imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
                } else
                    imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);

//            Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
                FermatTextView fermatTextView = (FermatTextView) view.findViewById(R.id.txt_name);
                fermatTextView.setText(identity.getAlias());
            }
        } catch (CantGetIdentityAssetIssuerException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error: out of memory ", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
