package com.bitdubai.fermat_api.layer.osa_android.location_system;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 *
 *  <p>The public enum <code>LocationManager</code>
 *     define the type of network location.
 *
 *
 *  @author  Natalia
 *  @version 1.0.0
 *  @since   07/05/15.
 * */

public enum LocationSource implements FermatEnum {

    GPS           ("GPS"),
    NETWORK       ("NET"),
    IP_CALCULATED ("IPC"),

    ;

    private final String code;

    LocationSource(final String code){

        this.code = code;
    }

    public static LocationSource getByCode(String code) throws InvalidParameterException {

        switch (code){

            case "GPS": return GPS;
            case "NET": return NETWORK;
            case "IPC": return IP_CALCULATED;

            default:
                throw new InvalidParameterException(
                        "Code Received: " + code,
                        "This Code Is Not Valid for the LocationSource enum"
                );
        }
    }

    @Override
    public String getCode(){
        return this.code;
    }

}
