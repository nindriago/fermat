package org.fermat.fermat_dap_android_sub_app_asset_user_community.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_dap_android_sub_app_asset_user_community_bitdubai.R;
import org.fermat.fermat_dap_android_sub_app_asset_user_community.holders.UserViewHolder;
import org.fermat.fermat_dap_android_sub_app_asset_user_community.interfaces.AdapterChangeListener;
import org.fermat.fermat_dap_android_sub_app_asset_user_community.models.Actor;
import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserCommunityAdapter extends FermatAdapter<Actor, UserViewHolder> {

    private AdapterChangeListener<Actor> adapterChangeListener;

    public UserCommunityAdapter(Context context) {
        super(context);
    }

    public UserCommunityAdapter(Context context, List<Actor> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected UserViewHolder createHolder(View itemView, int type) {
        return new UserViewHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.row_actor;
    }

    @Override
    protected void bindHolder(final UserViewHolder holder, final Actor data, final int position) {
        try {
            holder.name.setText(String.format("%s", data.getName()));
            if (data.getCryptoAddress() != null) {
                holder.connectedStateConnected.setVisibility(View.VISIBLE);
                holder.connectedStateDenied.setVisibility(View.GONE);
                holder.connectedStateWaiting.setVisibility(View.GONE);
                holder.connect.setVisibility(View.GONE);
                //holder.crypto.setText("CryptoAddress: YES");
            } else {
                switch (data.getDapConnectionState()){
                    case CONNECTING:
                    case PENDING_LOCALLY:
                    case PENDING_REMOTELY:
                        holder.connectedStateWaiting.setVisibility(View.VISIBLE);
                        holder.connectedStateDenied.setVisibility(View.GONE);
                        break;
                    case DENIED_LOCALLY:
                    case DENIED_REMOTELY:
                    case CANCELLED_LOCALLY:
                    case CANCELLED_REMOTELY:
                        holder.connectedStateWaiting.setVisibility(View.GONE);
                        holder.connectedStateDenied.setVisibility(View.VISIBLE);
                        break;
                    default:
                        holder.connectedStateWaiting.setVisibility(View.GONE);
                        holder.connectedStateDenied.setVisibility(View.GONE);

                }
                holder.connectedStateConnected.setVisibility(View.GONE);
                holder.connect.setVisibility(View.VISIBLE);
                //holder.crypto.setText("CryptoAddress: NO");
            }

            if (data.getDapConnectionState() == DAPConnectionState.REGISTERED_ONLINE || data.getDapConnectionState() == DAPConnectionState.CONNECTED_ONLINE) {
                holder.status.setText(R.string.status_online);
                holder.status.setBackgroundColor(holder.status.getResources().getColor(R.color.background_status_online));
            }
            if (data.getDapConnectionState() == DAPConnectionState.REGISTERED_OFFLINE || data.getDapConnectionState() == DAPConnectionState.CONNECTED_OFFLINE) {
                holder.status.setText(R.string.status_offline);
                holder.status.setBackgroundColor(holder.status.getResources().getColor(R.color.background_status_offline));
            }
            if (data.getDapConnectionState() == DAPConnectionState.CONNECTING) {
                holder.status.setText(R.string.status_connecting);
            }

            if (data.getDapConnectionState() == DAPConnectionState.DENIED_LOCALLY || data.getDapConnectionState() == DAPConnectionState.DENIED_REMOTELY) {
                holder.status.setText(R.string.status_denied);
            }

            if (data.getDapConnectionState() == DAPConnectionState.CANCELLED_LOCALLY || data.getDapConnectionState() == DAPConnectionState.CANCELLED_REMOTELY) {
                holder.status.setText(R.string.status_canceled);
            }

            holder.connect.setChecked(data.selected);
            holder.connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataSet.get(position).selected = !dataSet.get(position).selected;
                    notifyItemChanged(position);
                    if (adapterChangeListener != null)
                        adapterChangeListener.onDataSetChanged(dataSet);
                }
            });

            byte[] profileImage = data.getProfileImage();


            if (profileImage != null) {
                if (profileImage.length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.length);
                    holder.thumbnail.setImageBitmap(bitmap);
                } else Picasso.with(context).load(R.drawable.asset_user_comunity).into(holder.thumbnail);
            } else Picasso.with(context).load(R.drawable.asset_user_comunity).into(holder.thumbnail);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setAdapterChangeListener(AdapterChangeListener<Actor> adapterChangeListener) {
        this.adapterChangeListener = adapterChangeListener;
    }

    public AdapterChangeListener<Actor> getAdapterChangeListener() {
        return adapterChangeListener;
    }

    public int getSize() {
        if (dataSet != null)
            return dataSet.size();
        return 0;
    }
}
