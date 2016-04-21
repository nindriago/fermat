package com.bitdubai.fermat_art_core.layer.sub_app_module.artist_community;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_art_plugin.layer.sub_app_module.artist_community.developer.bitdubai.DeveloperBitDubai;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 29/03/16.
 */
public class ArtSubAppModuleArtistCommunityPluginSubsystem extends AbstractPluginSubsystem {

    public ArtSubAppModuleArtistCommunityPluginSubsystem() {
        super(new PluginReference(Plugins.ARTIST_COMMUNITY_SUB_APP_MODULE));
    }

    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new DeveloperBitDubai());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
}
