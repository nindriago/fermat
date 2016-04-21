package com.bitdubai.fermat_cht_core.layer.sup_app_module.identity;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;

/**
 * Created by franklin on 06/01/16.
 */
public class ChatSupAppIdentityModulePluginSubsystem extends AbstractPluginSubsystem {

    public ChatSupAppIdentityModulePluginSubsystem() {
        super(new PluginReference(Plugins.CHAT_IDENTITY_SUP_APP_MODULE));
    }

    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new com.fermat_cht_plugin.layer.sub_app_module.chat.identity.bitdubai.DeveloperBitDubai());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
}
