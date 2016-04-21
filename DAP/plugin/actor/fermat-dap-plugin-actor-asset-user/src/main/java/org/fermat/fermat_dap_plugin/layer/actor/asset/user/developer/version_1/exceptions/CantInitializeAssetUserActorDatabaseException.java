package org.fermat.fermat_dap_plugin.layer.actor.asset.user.developer.version_1.exceptions;


import org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException;

/**
 * Created by Nerio on 17/09/15.
 */
public class CantInitializeAssetUserActorDatabaseException extends DAPException {

    /**
     * Represent the default message
     */
    public static final String DEFAULT_MESSAGE = "CAN'T INTIALIZE REQUESTED ASSET USER ACTOR DATABASE EXCEPTION";

    /**
     * Constructor with parameters
     *
     * @param message
     * @param cause
     * @param context
     * @param possibleReason
     */
    public CantInitializeAssetUserActorDatabaseException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantInitializeAssetUserActorDatabaseException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantInitializeAssetUserActorDatabaseException(final String message) {
        this(message, null);
    }


    public CantInitializeAssetUserActorDatabaseException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    public CantInitializeAssetUserActorDatabaseException() {
        this(DEFAULT_MESSAGE);
    }
}
