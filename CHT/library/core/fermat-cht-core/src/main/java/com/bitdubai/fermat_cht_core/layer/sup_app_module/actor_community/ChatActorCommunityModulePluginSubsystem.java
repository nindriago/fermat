package com.bitdubai.fermat_cht_core.layer.sup_app_module.actor_community;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_cht_plugin.layer.sub_app_module.chat_community.developer.bitdubai.DeveloperBitDubai;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;


/**
 * Created by root on 14/04/16.
 */
public class ChatActorCommunityModulePluginSubsystem extends AbstractPluginSubsystem{

    public ChatActorCommunityModulePluginSubsystem() {
        super(new PluginReference(Plugins.CHAT_COMMUNITY_SUP_APP_MODULE));
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
