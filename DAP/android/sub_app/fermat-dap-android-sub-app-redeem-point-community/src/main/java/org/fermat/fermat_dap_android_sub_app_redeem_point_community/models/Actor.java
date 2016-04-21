package org.fermat.fermat_dap_android_sub_app_redeem_point_community.models;

import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.RedeemPointActorRecord;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPoint;

/**
 * Actor Model
 */
public class Actor extends RedeemPointActorRecord
        implements ActorAssetRedeemPoint {

    public boolean selected;

    public Actor() {
        super();
    }

    public Actor(RedeemPointActorRecord record) {
        super(
                record.getActorPublicKey(),
                record.getName(),
                record.getDapConnectionState(),
                record.getLocationLatitude(),
                record.getLocationLongitude(),
                record.getCryptoAddress(),
                record.getRegistrationDate(),
                record.getLastConnectionDate(),
                record.getType(),
                record.getBlockchainNetworkType(),
                record.getProfileImage()
        );
    }

}
