package com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.View;

import com.bitdubai.android_fermat_ccp_loss_protected_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.transformation.CircleTransform;
import com.bitdubai.fermat_android_api.ui.util.FermatAnimationsUtils;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletIntraUserActor;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.holders.IntraUserInfoViewHolder;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.AddConnectionCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created on 22/08/15.
 * Adapter para el RecliclerView del CryptoBrokerIdentityListFragment que muestra el catalogo de Wallets disponibles en el store
 *
 * @author Nelson Ramirez
 */
public class AddConnectionsAdapter extends FermatAdapter<LossProtectedWalletIntraUserActor, IntraUserInfoViewHolder> {


    private final AddConnectionCallback addConnectionCallback;

    public AddConnectionsAdapter(Context context, ArrayList<LossProtectedWalletIntraUserActor> dataSet,AddConnectionCallback addConnectionCallback) {
        super(context, dataSet);
        this.addConnectionCallback = addConnectionCallback;
    }

    @Override
    protected IntraUserInfoViewHolder createHolder(View itemView, int type) {
        return new IntraUserInfoViewHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.loss_intra_user_information_list_item;
    }

    @Override
    protected void bindHolder(final IntraUserInfoViewHolder holder, final LossProtectedWalletIntraUserActor data, final int position) {
        holder.thumbnail.setVisibility(View.VISIBLE);
        holder.container_data.setVisibility(View.VISIBLE);
        holder.checkbox_connection.setVisibility(View.VISIBLE);
        holder.name.setVisibility(View.VISIBLE);

        holder.name.setText(data.getAlias());
        RoundedBitmapDrawable roundedBitmap;
        byte[] profileImage = data.getProfileImage();
        try {
            if (profileImage != null) {
                if (profileImage.length > 0) {
                    roundedBitmap = ImagesUtils.getRoundedBitmap(context.getResources(), profileImage);
                    holder.thumbnail.setImageDrawable(roundedBitmap);
                } else {
                    Picasso.with(context).load(R.drawable.ic_profile_male).transform(new CircleTransform()).into(holder.thumbnail);
                }
            } else {
                Picasso.with(context).load(R.drawable.ic_profile_male).transform(new CircleTransform()).into(holder.thumbnail);
            }
        }catch (Exception e){
            Picasso.with(context).load(R.drawable.ic_profile_male).transform(new CircleTransform()).into(holder.thumbnail);
        }
        holder.container_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = !data.isSelected();
                data.setSelected(selected);
                addConnectionCallback.setSelected(data, selected);
                if (selected) {
                    holder.checkbox_connection.setChecked(true);

                    addConnectionCallback.addMenuEnabled();
                    holder.container_data.setBackgroundColor(Color.parseColor("#666666"));
                    holder.container_data.getBackground().setAlpha(50);

                } else {
                    holder.checkbox_connection.setChecked(false);

                    addConnectionCallback.addMenuDisabled();
                    holder.container_data.getBackground().setAlpha(0);
                }
            }
        });

        holder.checkbox_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = !data.isSelected();
                data.setSelected(selected);
                addConnectionCallback.setSelected(data, selected);
                if (selected) {
                    holder.checkbox_connection.setChecked(true);
                    addConnectionCallback.addMenuEnabled();
                    holder.container_data.setBackgroundColor(Color.parseColor("#666666"));
                    holder.container_data.getBackground().setAlpha(50);

                } else {
                    holder.checkbox_connection.setChecked(false);
                    addConnectionCallback.addMenuDisabled();
                    holder.container_data.getBackground().setAlpha(0);
                }
            }
        });

        if(data.isSelected()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.container_data.setBackground(ContextCompat.getDrawable(context, R.drawable.add_connection_rounded_rectangle_shape));
            }
        }

    }

}
