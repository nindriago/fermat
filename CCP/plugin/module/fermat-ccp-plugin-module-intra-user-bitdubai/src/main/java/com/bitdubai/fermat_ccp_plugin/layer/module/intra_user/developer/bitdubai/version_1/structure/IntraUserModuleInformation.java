package com.bitdubai.fermat_ccp_plugin.layer.module.intra_user.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserInformation;

import java.io.Serializable;

/**
 * The class <code>com.bitdubai.fermat_dmp_plugin.layer.module.intra_user.developer.bitdubai.version_1.structure.IntraUserModuleInformation</code>
 * is the implementation of IntraUserInformation interface.
 * And provides the method to extract information about an intra user.
 *
 * Created by natalia on 11/08/15.
 */
public class IntraUserModuleInformation implements IntraUserInformation,Serializable {

    private String name;
    private String phrase;
    private String publicKey;
    private byte[] profileImage;
    private ConnectionState connectionState;
    private String state = "Online";



    public IntraUserModuleInformation(String name,String phrase,String publicKey,byte[] profileImage, ConnectionState connectionState,String state)
    {
        this.name = name;
        this.publicKey = publicKey;
        this.profileImage = profileImage.clone();
        this.connectionState = connectionState;
        this.phrase = phrase;
        this.state = state;

    }
    /**
     * That method returns the public key of the represented Intra User
     * @return the public key of the intra user
     */
    @Override
    public String getPublicKey() {
        return this.publicKey;
    }

    /**
     * That method returns the name of the represented intra user
     *
     * @return the name of the intra user
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * That method returns the Phrase of the intra user
     * @return the Phrase of the intra user
     */
    @Override
    public String getPhrase() {
        return this.phrase;
    }

    /**
     * That method returns the profile image of the represented intra user
     *
     * @return the profile image
     */
    @Override
    public byte[] getProfileImage() {
        return (profileImage!=null) ? this.profileImage.clone() : null;
    }

    /**
     * That method returns the ConnectionState status
     * @return ConnectionState object
     */

    @Override
    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    @Override
    public String getState() {
        return this.state ;
    }
}
