package org.fermat.fermat_dap_android_wallet_asset_issuer.models;

import org.fermat.fermat_dap_android_wallet_asset_issuer.util.Utils;
import org.fermat.fermat_dap_api.layer.all_definition.util.DAPStandardFormats;

import java.sql.Timestamp;

/**
 * Created by frank on 12/22/15.
 */
public class UserDelivery {
    private byte[] userImage;
    private String userName;
    private Timestamp deliveryDate;
    private String deliveryStatus;

    public UserDelivery() {
    }

    public UserDelivery(byte[] profileImage, String name, Timestamp timestamp, String description) {
        this(name, timestamp, description);
        this.userImage = profileImage;
    }

    public UserDelivery(String name, Timestamp timestamp, String description) {
        this.userName = userName;
        this.deliveryDate = deliveryDate;
        this.deliveryStatus = deliveryStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public String getFormattedDeliveryDate() {
        if (deliveryDate == null) return "No date";
        return Utils.getTimeAgo(deliveryDate.getTime());
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }
}
