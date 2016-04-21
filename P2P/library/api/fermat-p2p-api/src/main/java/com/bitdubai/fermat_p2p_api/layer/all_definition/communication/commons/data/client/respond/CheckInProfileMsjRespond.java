package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond;

import com.google.gson.Gson;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 21/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInProfileMsjRespond extends MsgRespond {

    /**
     * Represent the identityPublicKey of the profile
     */
    private String identityPublicKey;

    /**
     * Constructor with parameters
     *
     * @param status
     * @param details
     * @param identityPublicKey
     */
    public CheckInProfileMsjRespond(STATUS status, String details, String identityPublicKey) {
        super(status, details);
        this.identityPublicKey = identityPublicKey;
    }

    /**
     * Gets the value of identityPublicKey and returns
     *
     * @return identityPublicKey
     */
    public String getIdentityPublicKey() {
        return identityPublicKey;
    }

    public static CheckInProfileMsjRespond parseContent(String content) {

        return new Gson().fromJson(content, CheckInProfileMsjRespond.class);
    }

}
