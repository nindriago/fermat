package com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces;

import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;

import java.util.List;
import java.util.UUID;

/**
* Created by Eleazar Oroño (eorono@protonmail.com) on 31/03/16.
 */
public interface ChatActorCommunityInformation  {

    String getPublicKey();

    String getAlias();

    byte[] getImage();

    List listAlias();

    ConnectionState getConnectionState();

    UUID getConnectionId();



}
