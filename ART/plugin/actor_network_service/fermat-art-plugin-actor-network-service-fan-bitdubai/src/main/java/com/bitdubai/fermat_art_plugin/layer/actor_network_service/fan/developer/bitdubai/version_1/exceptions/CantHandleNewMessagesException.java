package com.bitdubai.fermat_art_plugin.layer.actor_network_service.fan.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_art_api.all_definition.exceptions.ARTException;

/**
 * The exception <code>com.bitdubai.fermat_cbp_plugin.layer.actor_network_service.crypto_broker.developer.bitdubai.version_1.exceptions.CantHandleNewMessagesException</code>
 * is thrown when there is an error trying to handle new messages.
 * <p>
 * Created by Gabriel Araujo.
 */
public class CantHandleNewMessagesException extends ARTException {

    private static final String DEFAULT_MESSAGE = "CAN'T HANDLE NEW MESSAGES EXCEPTION";

    public CantHandleNewMessagesException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantHandleNewMessagesException(Exception cause, String context, String possibleReason) {
        this(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

    public CantHandleNewMessagesException(String context, String possibleReason) {
        this(DEFAULT_MESSAGE, null, context, possibleReason);
    }

}
