package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.respond;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalogTransaction;
import com.google.gson.Gson;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.respond.GetNodeCatalogTransactionsMsjRespond</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 05/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class GetNodeCatalogTransactionsMsjRespond extends MsgRespond {

    /**
     * Represent the list of transactions
     */
    private List<NodesCatalogTransaction> nodesCatalogTransactions;

    /**
     * Represent the count
     */
    private Long count;

    /**
     * Constructor with parameters
     *
     * @param status
     * @param details
     */
    public GetNodeCatalogTransactionsMsjRespond(STATUS status, String details, List<NodesCatalogTransaction> nodesCatalogTransactions, Long count) {
        super(status, details);
        this.nodesCatalogTransactions = nodesCatalogTransactions;
        this.count = count;
    }

    /**
     * Get the list of node catalog nodesCatalogTransactions
     * @return nodesCatalogTransactions
     */
    public List<NodesCatalogTransaction> getNodesCatalogTransactions() {
        return nodesCatalogTransactions;
    }

    /**
     * Get the Count
     * @return Long
     */
    public Long getCount() {
        return count;
    }

    public static GetNodeCatalogTransactionsMsjRespond parseContent(String content) {

        return new Gson().fromJson(content, GetNodeCatalogTransactionsMsjRespond.class);
    }
}
