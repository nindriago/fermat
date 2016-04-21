package org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.exceptions;

/**
 * Created by Nerio on 07/09/15.
 */
public class CantCreateActorRedeemPointException extends org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException {

    public static final String DEFAULT_MESSAGE = "CAN'T CREATE NEW REDEEM POINT";

    public CantCreateActorRedeemPointException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantCreateActorRedeemPointException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantCreateActorRedeemPointException(final String message) {
        this(message, null);
    }

    public CantCreateActorRedeemPointException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    public CantCreateActorRedeemPointException() {
        this(DEFAULT_MESSAGE);
    }
}
