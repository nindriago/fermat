package com.bitdubai.fermat_art_core.layer.actor_network_service;

import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_art_core.layer.actor_network_service.artist.ArtistActorNetworkServicePluginSubsystem;
import com.bitdubai.fermat_art_core.layer.actor_network_service.fan.FanActorNetworkServicePluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractLayer;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantRegisterPluginException;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartLayerException;

/**
 * Created by Gabriel Araujo (gabe_512@hotmail.com) on 08/03/16.
 */
public class ActorNetworkServiceLayer  extends AbstractLayer {

    public ActorNetworkServiceLayer() {
        super(Layers.ACTOR_NETWORK_SERVICE);
    }

    public void start() throws CantStartLayerException {

        try {

            registerPlugin(new ArtistActorNetworkServicePluginSubsystem());
            registerPlugin(new FanActorNetworkServicePluginSubsystem());

        } catch (CantRegisterPluginException e) {

            throw new CantStartLayerException(
                    e,
                    "",
                    "Problem trying to register a plugin."
            );
        }
    }

}