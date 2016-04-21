package org.fermat.fermat_dap_api.layer.all_definition.contracts.exceptions;

import org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException;

/**
 * Created by rodrigo on 9/4/15.
 */
public class CantDefineContractPropertyException extends DAPException {
    public CantDefineContractPropertyException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
