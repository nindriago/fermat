package com.bitdubai.fermat_cht_api.all_definition.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by franklin on 02/11/15.
 */
public class CantListIdentitiesException extends FermatException {


    public CantListIdentitiesException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
