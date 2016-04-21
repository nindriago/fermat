package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.respond;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.google.gson.Gson;


/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.respond.ReceiveActorCatalogTransactionsMsjRespond</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 05/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ReceiveActorCatalogTransactionsMsjRespond extends MsgRespond {

    /**
     * Represent the lateNotificationsCounter
     */
    private Integer lateNotificationsCounter;

    /**
     * Constructor with parameters
     *
     * @param status
     * @param details
     * @param lateNotificationsCounter
     */
    public ReceiveActorCatalogTransactionsMsjRespond(STATUS status, String details, Integer lateNotificationsCounter) {
        super(status, details);
        this.lateNotificationsCounter = lateNotificationsCounter;
    }

    /**
     * Get the LateNotificationsCounter value
     * return java.lang.Integer
     */
    public Integer getLateNotificationsCounter() {
        return lateNotificationsCounter;
    }

    public static ReceiveActorCatalogTransactionsMsjRespond parseContent(String content) {

        return new Gson().fromJson(content, ReceiveActorCatalogTransactionsMsjRespond.class);
    }

}
