package org.fermat.fermat_dap_plugin.layer.digital_asset_transaction.issuer_appropriation.developer.version_1.exceptions;

import org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 06/11/15.
 */
public class CantLoadIssuerAppropriationEventListException extends DAPException {

    //VARIABLE DECLARATION
    public static final String DEFAULT_MESSAGE = "There was an error while searching the asset appropriation event list.";

    //CONSTRUCTORS

    public CantLoadIssuerAppropriationEventListException(Exception cause, String context, String possibleReason) {
        super(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

    //PUBLIC METHODS

    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}
