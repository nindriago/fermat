package com.bitdubai.fermat_cht_api.layer.identity.interfaces;

import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;

/**
 * Created by franklin on 29/03/16.
 */
public interface ChatIdentity  extends ActiveActorIdentityInformation {
    /**
     * The method <code>setNewProfileImage</code> let the user set a new profile image
     *
     * @param newProfileImage the new profile image to set
     * @throws
     */
    void setNewProfileImage(byte[] newProfileImage);

    /**
     * This method let an intra user sign a message with his unique private key
     * @param message the message to sign
     * @return the signature
     */
    String createMessageSignature(String message) ;

    /**
     * This method return boolean
     * @return the boolena
     */
    boolean getIsPaymetForChat();
}
