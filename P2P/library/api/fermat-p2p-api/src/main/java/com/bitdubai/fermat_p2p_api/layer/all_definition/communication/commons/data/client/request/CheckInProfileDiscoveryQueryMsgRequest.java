package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.google.gson.Gson;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileDiscoveryQueryMsgRequest</code>
 * represent the message of the check in profile list<p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 27/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInProfileDiscoveryQueryMsgRequest extends PackageContent {

    /**
     * Represent the discoveryQueryParameters
     */
    private DiscoveryQueryParameters discoveryQueryParameters;

    /**
     * Constructor with parameter
     *
     * @param discoveryQueryParameters
     */
    public CheckInProfileDiscoveryQueryMsgRequest(DiscoveryQueryParameters discoveryQueryParameters) {
        this.discoveryQueryParameters = discoveryQueryParameters;
    }

    /**
     * Gets the value of discoveryQueryParameters and returns
     *
     * @return discoveryQueryParameters
     */
    public DiscoveryQueryParameters getDiscoveryQueryParameters() {
        return discoveryQueryParameters;
    }

    public static CheckInProfileDiscoveryQueryMsgRequest parseContent(String content) {

        return new Gson().fromJson(content, CheckInProfileDiscoveryQueryMsgRequest.class);
    }
}
