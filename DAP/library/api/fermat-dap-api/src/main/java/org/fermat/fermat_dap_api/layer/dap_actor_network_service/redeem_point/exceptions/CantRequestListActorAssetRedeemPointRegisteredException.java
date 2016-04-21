package org.fermat.fermat_dap_api.layer.dap_actor_network_service.redeem_point.exceptions;

import org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException;

/**
 * Created by franklin on 15/10/15.
 */
public class CantRequestListActorAssetRedeemPointRegisteredException extends DAPException {


    public static final String DEFAULT_MESSAGE = "CAN'T REQUEST LIST ACTOR ASSET USER REGISTERED";


    public CantRequestListActorAssetRedeemPointRegisteredException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantRequestListActorAssetRedeemPointRegisteredException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantRequestListActorAssetRedeemPointRegisteredException(final String message) {
        this(message, null);
    }

    public CantRequestListActorAssetRedeemPointRegisteredException(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    public CantRequestListActorAssetRedeemPointRegisteredException() {
        this(DEFAULT_MESSAGE);
    }
}
