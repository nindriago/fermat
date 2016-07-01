package com.bitdubai.fermat_ccp_api.layer.identity.intra_user.interfaces;

import com.bitdubai.fermat_api.layer.modules.interfaces.FermatSettings;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserLoginIdentity;

import java.io.Serializable;

/**
 * Created by Joaquin C. on 11/01/16.
 */
public class IntraUserIdentitySettings  implements FermatSettings ,Serializable{

    private IntraUserLoginIdentity lastSelectedIdentity;
    private boolean isPresentationHelpEnabled;
    public IntraUserIdentitySettings() {
        this.lastSelectedIdentity = null;
    }

    public IntraUserLoginIdentity getLastSelectedIdentity() {
        return lastSelectedIdentity;
    }

    public void setLastSelectedIdentity(IntraUserLoginIdentity lastSelectedIdentity) {
        this.lastSelectedIdentity = lastSelectedIdentity;
    }

    public boolean isPresentationHelpEnabled() {
        return isPresentationHelpEnabled;
    }

    public void setIsPresentationHelpEnabled(boolean isPresentationHelpEnabled) {
        this.isPresentationHelpEnabled = isPresentationHelpEnabled;
    }

}
