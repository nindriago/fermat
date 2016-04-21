/*
 * @#PackageType.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums;

/**
 * The enum <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType</code> represent
 * all type can be a <code>Package</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public enum PackageType {

    // Definition types Client channel

    /*
     * Request packet types
     */
    CHECK_IN_CLIENT_REQUEST,
    CHECK_IN_NETWORK_SERVICE_REQUEST,
    CHECK_IN_ACTOR_REQUEST,

    CHECK_OUT_CLIENT_REQUEST,
    CHECK_OUT_NETWORK_SERVICE_REQUEST,
    CHECK_OUT_ACTOR_REQUEST,

    NETWORK_SERVICE_LIST_REQUEST,
    ACTOR_LIST_REQUEST,
    NEAR_NODE_LIST_REQUEST,

    CHECK_IN_PROFILE_DISCOVERY_QUERY_REQUEST,
    ACTOR_TRACE_DISCOVERY_QUERY_REQUEST,

    /*
     * Respond packet types
     */
    CHECK_IN_CLIENT_RESPOND,
    CHECK_IN_NETWORK_SERVICE_RESPOND,
    CHECK_IN_ACTOR_RESPOND,

    CHECK_OUT_CLIENT_RESPOND,
    CHECK_OUT_NETWORK_SERVICE_RESPOND,
    CHECK_OUT_ACTOR_RESPOND,

    NETWORK_SERVICE_LIST_RESPOND,
    ACTOR_LIST_RESPOND,
    NEAR_NODE_LIST_RESPOND,

    CHECK_IN_PROFILE_DISCOVERY_QUERY_RESPOND,
    ACTOR_TRACE_DISCOVERY_QUERY_RESPOND,

    // Definition types Client channel

    /*
     * Request packet types
     */
    ADD_NODE_TO_CATALOG_REQUEST,
    UPDATE_NODE_IN_CATALOG_REQUEST,
    GET_NODE_CATALOG_REQUEST,
    GET_NODE_CATALOG_TRANSACTIONS_REQUEST,
    RECEIVE_NODE_CATALOG_TRANSACTIONS_REQUEST,
    RECEIVE_ACTOR_CATALOG_TRANSACTIONS_REQUEST,

    /*
     * Respond packet types
     */
    ADD_NODE_TO_CATALOG_RESPOND,
    UPDATE_NODE_IN_CATALOG_RESPOND,
    GET_NODE_CATALOG_RESPOND,
    GET_NODE_CATALOG_TRANSACTIONS_RESPOND,
    RECEIVE_NODE_CATALOG_TRANSACTIONS_RESPOND,
    RECEIVE_ACTOR_CATALOG_TRANSACTIONS_RESPOND

    ;


}
