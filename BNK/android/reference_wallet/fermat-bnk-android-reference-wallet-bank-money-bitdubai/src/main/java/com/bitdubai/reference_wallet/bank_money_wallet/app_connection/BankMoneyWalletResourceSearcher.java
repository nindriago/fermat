package com.bitdubai.reference_wallet.bank_money_wallet.app_connection;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bitdubai.fermat_android_api.core.ResourceSearcher;
import com.bitdubai.fermat_android_api.utils.DrawableUtils;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.FermatDrawable;
import com.bitdubai.reference_wallet.bank_money_wallet.R;
import com.bitdubai.reference_wallet.bank_money_wallet.util.ReferenceWalletConstants;


/**
 * Created by nelsonalfo on 02/07/16.
 */
public class BankMoneyWalletResourceSearcher extends ResourceSearcher {



    @Override
    public Drawable obtainDrawable(Context context, FermatDrawable fermatDrawable) {
        int res = 0;
        switch (fermatDrawable.getId()) {
            case ReferenceWalletConstants.ADD_ACCOUNT_ACTION:
                res = R.drawable.bw_add_icon_action_bar;
                break;

            case ReferenceWalletConstants.EDIT_ACCOUNT_ACTION:
                res = R.drawable.bw_ic_action_edit;
                break;

            case ReferenceWalletConstants.SAVE_ACTION:
                res = R.drawable.bw_ic_action_edit;
                break;

            case ReferenceWalletConstants.HELP_ACTION:
                res = R.drawable.bw_help_icon_action_bar;
                break;

            default:
                res = 0;
                break;
        }
        return DrawableUtils.resToDrawable(context, res);
    }
}
