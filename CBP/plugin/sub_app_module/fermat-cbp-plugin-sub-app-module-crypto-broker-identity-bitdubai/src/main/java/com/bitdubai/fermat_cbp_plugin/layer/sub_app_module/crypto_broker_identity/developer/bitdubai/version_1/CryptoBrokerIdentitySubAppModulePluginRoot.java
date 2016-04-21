package com.bitdubai.fermat_cbp_plugin.layer.sub_app_module.crypto_broker_identity.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractModule;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetModuleManagerException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.modules.interfaces.FermatSettings;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.exceptions.CantCreateCryptoBrokerIdentityException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.exceptions.CantHideIdentityException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.exceptions.CantListCryptoBrokerIdentitiesException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.exceptions.CantPublishIdentityException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.exceptions.CantUpdateBrokerIdentityException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.exceptions.CryptoBrokerIdentityAlreadyExistsException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.exceptions.IdentityNotFoundException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.interfaces.CryptoBrokerIdentity;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.interfaces.CryptoBrokerIdentityManager;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.IdentityBrokerPreferenceSettings;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.exceptions.CantCreateCryptoBrokerException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.exceptions.CantHideCryptoBrokerException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.exceptions.CantListCryptoBrokersException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.exceptions.CantPublishCryptoBrokerException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.exceptions.CryptoBrokerNotFoundException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.interfaces.CryptoBrokerIdentityInformation;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.interfaces.CryptoBrokerIdentityModuleManager;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_broker_identity.utils.CryptoBrokerIdentityInformationImpl;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel 16/10/2015
 */
//public class CryptoBrokerIdentitySubAppModulePluginRoot extends AbstractPlugin implements CryptoBrokerIdentityModuleManager {
public class CryptoBrokerIdentitySubAppModulePluginRoot extends AbstractModule<IdentityBrokerPreferenceSettings,ActiveActorIdentityInformation> implements
        CryptoBrokerIdentityModuleManager {

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.IDENTITY, plugin = Plugins.CRYPTO_BROKER)
    private CryptoBrokerIdentityManager identityManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

    public CryptoBrokerIdentitySubAppModulePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    @Override
    public CryptoBrokerIdentityInformation createCryptoBrokerIdentity(String alias,
                                                                      byte[] image) throws CantCreateCryptoBrokerException {

        try {

            CryptoBrokerIdentity identity = this.identityManager.createCryptoBrokerIdentity(alias, image);
            return converIdentityToInformation(identity);

        } catch (CantCreateCryptoBrokerIdentityException e) {

            throw new CantCreateCryptoBrokerException(e, "", "There was an error trying to create an identity.");
        } catch (CryptoBrokerIdentityAlreadyExistsException e) {

            throw new CantCreateCryptoBrokerException(e, "", "A similar alias already exists.");
        }
    }

    @Override
    public void updateCryptoBrokerIdentity(CryptoBrokerIdentityInformation cryptoBrokerIdentity)  throws CantUpdateBrokerIdentityException {
        this.identityManager.updateCryptoBrokerIdentity(cryptoBrokerIdentity.getAlias(), cryptoBrokerIdentity.getPublicKey(), cryptoBrokerIdentity.getProfileImage());
    }

    @Override
    public void publishIdentity(String publicKey) throws CantPublishCryptoBrokerException, CryptoBrokerNotFoundException {

        try {

            this.identityManager.publishIdentity(publicKey);

        } catch (CantPublishIdentityException e) {

            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantPublishCryptoBrokerException(e, "", "Problem publishing the identity.");
        } catch (IdentityNotFoundException e) {

            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantPublishCryptoBrokerException(e, "", "Cannot find the identity.");
        } catch (Exception e) {

            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantPublishCryptoBrokerException(e, "", "Unhandled Exception.");
        }
    }

    @Override
    public void hideIdentity(String publicKey) throws CantHideCryptoBrokerException, CryptoBrokerNotFoundException {

        try {

            this.identityManager.hideIdentity(publicKey);

        } catch (CantHideIdentityException e) {

            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantHideCryptoBrokerException(e, "", "Problem hiding the identity.");
        } catch (IdentityNotFoundException e) {

            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantHideCryptoBrokerException(e, "", "Cannot find the identity.");
        } catch (Exception e) {

            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantHideCryptoBrokerException(e, "", "Unhandled Exception.");
        }
    }

    @Override
    public List<CryptoBrokerIdentityInformation> listIdentities(int max, int offset) throws CantListCryptoBrokersException {
        try {
            List<CryptoBrokerIdentityInformation> cryptoBrokers = new ArrayList<>();
            for(CryptoBrokerIdentity identity : this.identityManager.listIdentitiesFromCurrentDeviceUser()){
                cryptoBrokers.add(converIdentityToInformation(identity));
            }
            return cryptoBrokers;
        } catch (CantListCryptoBrokerIdentitiesException e) {
            throw new CantListCryptoBrokersException(CantListCryptoBrokersException.DEFAULT_MESSAGE, e, "","");
        }
    }

    private CryptoBrokerIdentityInformation converIdentityToInformation(final CryptoBrokerIdentity identity){
        return new CryptoBrokerIdentityInformationImpl(identity.getAlias(), identity.getPublicKey(), identity.getProfileImage(), identity.getExposureLevel());
    }

    private SettingsManager<IdentityBrokerPreferenceSettings> settingsManager;

    @Override
    public SettingsManager<IdentityBrokerPreferenceSettings> getSettingsManager() {
        if (this.settingsManager != null)
            return this.settingsManager;

        this.settingsManager = new SettingsManager<>(
                pluginFileSystem,
                pluginId
        );
        return this.settingsManager;
    }

    @Override
    public ActiveActorIdentityInformation getSelectedActorIdentity() throws CantGetSelectedActorIdentityException, ActorIdentityNotSelectedException {
        return null;
    }

    @Override
    public void createIdentity(String name, String phrase, byte[] profile_img) throws Exception {

    }

    @Override
    public void setAppPublicKey(String publicKey) {

    }

    @Override
    public int[] getMenuNotifications() {
        return new int[0];
    }

    @Override
    public ModuleManager<IdentityBrokerPreferenceSettings, ActiveActorIdentityInformation> getModuleManager() throws CantGetModuleManagerException {
        return this;
    }
}