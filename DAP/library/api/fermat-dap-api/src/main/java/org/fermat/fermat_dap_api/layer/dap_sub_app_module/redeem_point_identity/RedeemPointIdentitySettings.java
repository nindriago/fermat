package org.fermat.fermat_dap_api.layer.dap_sub_app_module.redeem_point_identity;

import com.bitdubai.fermat_api.layer.modules.interfaces.FermatSettings;

/**
 * Created by Nerio on 21/01/16.
 */
public class RedeemPointIdentitySettings implements FermatSettings {

    private boolean isPresentationHelpEnabled;

    public RedeemPointIdentitySettings() {

    }


    public boolean isPresentationHelpEnabled() {
        return isPresentationHelpEnabled;
    }

    @Override
    public void setIsPresentationHelpEnabled(boolean isPresentationHelpEnabled) {
        this.isPresentationHelpEnabled = isPresentationHelpEnabled;
    }
}
