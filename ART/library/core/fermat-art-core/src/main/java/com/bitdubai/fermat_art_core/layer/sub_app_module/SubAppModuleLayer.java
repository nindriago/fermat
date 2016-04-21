package com.bitdubai.fermat_art_core.layer.sub_app_module;

import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_art_core.layer.sub_app_module.artist_community.ArtSubAppModuleArtistCommunityPluginSubsystem;
import com.bitdubai.fermat_art_core.layer.sub_app_module.artist_identity.ArtSubAppModuleArtistIdentityPluginSubsystem;
import com.bitdubai.fermat_art_core.layer.sub_app_module.fan_community.ArtSubAppModuleFanCommunityPluginSubsystem;
import com.bitdubai.fermat_art_core.layer.sub_app_module.fan_identity.ArtSubAppModuleFanIdentityPluginSubsystem;
import com.bitdubai.fermat_art_core.layer.sub_app_module.music_player.ArtSubAppModuleMusicPlayerPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractLayer;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantRegisterPluginException;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartLayerException;

/**
 * Created by Alexander Jimenez (alex_jimenez76@hotmail.com) on 3/23/16.
 */
public class SubAppModuleLayer extends AbstractLayer {

    public SubAppModuleLayer() {
        super(Layers.SUB_APP_MODULE);
    }

    @Override
    public void start() throws CantStartLayerException {
        try{
            registerPlugin(new ArtSubAppModuleArtistCommunityPluginSubsystem());
            registerPlugin(new ArtSubAppModuleArtistIdentityPluginSubsystem());
            registerPlugin(new ArtSubAppModuleFanCommunityPluginSubsystem());
            registerPlugin(new ArtSubAppModuleFanIdentityPluginSubsystem());
            registerPlugin(new ArtSubAppModuleMusicPlayerPluginSubsystem());
        }catch (CantRegisterPluginException e) {

            throw new CantStartLayerException(
                    e,
                    "",
                    "Problem trying to register a plugin."
            );
        }
    }
}
