package org.fermat.fermat_dap_android_wallet_asset_user.navigation_drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.R;

import org.fermat.fermat_dap_android_wallet_asset_user.common.views.WalletUserUtils;
import org.fermat.fermat_dap_android_wallet_asset_user.sessions.AssetUserSession;
import org.fermat.fermat_dap_android_wallet_asset_user.util.BitmapWorkerTask;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantGetIdentityAssetUserException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_user.interfaces.IdentityAssetUser;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;

/**
 * Created by Matias Furszyfer on 2015.11.12..
 */
public class FragmentsCommons {

    public static View setUpHeaderScreen(LayoutInflater inflater, Context activity,
                                         AssetUserSession assetUserSession, ActiveActorIdentityInformation identityAssetUser) throws CantGetIdentityAssetUserException {

        //DAP V3
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
        relativeLayout.setLayoutParams(layoutParams);
        View view = inflater.inflate(R.layout.dap_v3_navigation_drawer_user_wallet_header, relativeLayout, true);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view_profile);

        try {
            IdentityAssetUser identity = assetUserSession.getModuleManager().getActiveAssetUserIdentity();

            if (identity != null) {
                if (identity.getImage() != null) {
                    if (identity.getImage().length > 0) {
                        ByteArrayInputStream bytes = new ByteArrayInputStream(identity.getImage());
                        BitmapDrawable bmd = new BitmapDrawable(bytes);
                        imageView.setImageBitmap(WalletUserUtils.getCircleBitmap(bmd.getBitmap()));
                    } else
                        imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
                } else
                    imageView.setImageResource(R.drawable.profile_actor); //Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);

//            Picasso.with(activity).load(R.drawable.banner_asset_issuer_community).into(imageView);
                FermatTextView fermatTextView = (FermatTextView) view.findViewById(R.id.txt_name);
                fermatTextView.setText(identity.getAlias());
            }
        } catch (CantGetIdentityAssetUserException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error: out of memory ", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}
