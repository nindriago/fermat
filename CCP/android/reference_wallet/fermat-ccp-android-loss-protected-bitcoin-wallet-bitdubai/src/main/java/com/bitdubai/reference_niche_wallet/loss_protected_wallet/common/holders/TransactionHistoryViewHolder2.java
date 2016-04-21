package com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitdubai.android_fermat_ccp_loss_protected_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;

/**
 * Created by Joaquin Carrasquero on 05/04/16.
 */
public class TransactionHistoryViewHolder2 extends FermatViewHolder {
    private ImageView contactIcon;
    private TextView txt_contactName;
    private TextView txt_amount;
    private TextView txt_notes;
    private TextView txt_time;




    public TransactionHistoryViewHolder2(View itemView) {
        super(itemView);

        txt_contactName = (TextView) itemView.findViewById(R.id.txt_contactName);
        txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);
        txt_notes = (TextView) itemView.findViewById(R.id.txt_notes);
        txt_time = (TextView) itemView.findViewById(R.id.txt_time);



    }


    public TextView getTxt_contactName() {
        return txt_contactName;
    }

    public TextView getTxt_amount() {
        return txt_amount;
    }

    public TextView getTxt_notes() {
        return txt_notes;
    }


    public TextView getTxt_time() {
        return txt_time;
    }


}
