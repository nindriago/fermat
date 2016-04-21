package org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.exceptions;

import org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException;

/**
 * Created by Nerio on 07/09/15.
 */
public class CantSingMessageException extends DAPException {

    public static final String DEFAULT_MESSAGE = "CAN'T SIGN MESSAGE REDEEM POINT";

    public CantSingMessageException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
