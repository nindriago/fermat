package org.fermat.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions;

import org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 14/09/15.
 */
public class CantIssueDigitalAssetsException extends DAPException {

    static final String DEFAULT_MESSAGE = "There was an error Issuing Digital Assets.";

    public CantIssueDigitalAssetsException(Exception cause, String context, String possibleReason) {
        super(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

    public CantIssueDigitalAssetsException(Exception cause) {
        super(DEFAULT_MESSAGE, cause, null, null);
    }
}
