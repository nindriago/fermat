package com.bitdubai.fermat_cht_api.all_definition.events.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 05/01/16.
 */
public enum EventStatus implements FermatEnum {
    NOTIFIED("NOTD"),
    PENDING("PEND");

    String code;

    EventStatus(String code){
        this.code=code;
    }

    @Override
    public String getCode() { return this.code ; }

    public static EventStatus getByCode(String code) throws InvalidParameterException {
        switch (code){
            case "NOTD":
                return EventStatus.NOTIFIED;
            case "PEND":
                return EventStatus.PENDING;
            default:
                throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE, null, "Code Received: " + code, "This Code Is Not Valid for the EventStatus enum.");
        }
    }
}
