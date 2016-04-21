package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles;

import com.google.gson.Gson;

/**
 * The Class <code>ClientProfile</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 02/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ClientProfile extends Profile {

    /**
     * Represent the deviceType
     */
    private String deviceType;

    /**
     * Constructor
     */
    public ClientProfile(){
        super();
    }

    /**
     * Gets the value of deviceType and returns
     *
     * @return deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the deviceType
     *
     * @param deviceType to set
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * (no-javadoc)
     * @see Profile#toJson()
     */
    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * (no-javadoc)
     * @see Profile#fromJson(String)
     */
    @Override
    public ClientProfile fromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, this.getClass());
    }


}
