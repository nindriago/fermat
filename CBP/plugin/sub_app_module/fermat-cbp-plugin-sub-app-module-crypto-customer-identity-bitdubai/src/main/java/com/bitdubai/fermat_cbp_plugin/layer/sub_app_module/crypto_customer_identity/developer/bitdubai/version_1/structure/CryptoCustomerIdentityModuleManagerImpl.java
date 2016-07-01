package com.bitdubai.fermat_cbp_plugin.layer.sub_app_module.crypto_customer_identity.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_broker.exceptions.CantUpdateCustomerIdentityException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.exceptions.CantCreateCryptoCustomerIdentityException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.exceptions.CantListCryptoCustomerIdentityException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.exceptions.CantPublishIdentityException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.exceptions.IdentityNotFoundException;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.interfaces.CryptoCustomerIdentity;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.interfaces.CryptoCustomerIdentityManager;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer.exceptions.CantGetCryptoCustomerListException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.IdentityCustomerPreferenceSettings;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.exceptions.CouldNotCreateCryptoCustomerException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.exceptions.CouldNotPublishCryptoCustomerException;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.interfaces.CryptoCustomerIdentityInformation;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.interfaces.CryptoCustomerIdentityModuleManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Yordin Alayn on 19.04.16.
 */
public class CryptoCustomerIdentityModuleManagerImpl implements CryptoCustomerIdentityModuleManager {

    private CryptoCustomerIdentityManager   identityManager;
    private PluginFileSystem                pluginFileSystem;
    private UUID                            pluginId;
    private ErrorManager errorManager;
    private PluginVersionReference pluginVersionReference;

    public CryptoCustomerIdentityModuleManagerImpl(
            CryptoCustomerIdentityManager   identityManager,
            PluginFileSystem                pluginFileSystem,
            UUID                            pluginId,
            ErrorManager                    errorManager,
            PluginVersionReference          pluginVersionReference
    ){
        this.identityManager        = identityManager;
        this.pluginFileSystem       = pluginFileSystem;
        this.pluginId               = pluginId;
        this.errorManager           = errorManager;
        this.pluginVersionReference = pluginVersionReference;
    }

    @Override
    public CryptoCustomerIdentityInformation createCryptoCustomerIdentity(String cryptoCustomerName, byte[] profileImage) throws CouldNotCreateCryptoCustomerException {
        try {
            CryptoCustomerIdentity identity = this.identityManager.createCryptoCustomerIdentity(cryptoCustomerName, profileImage);
            return converIdentityToInformation(identity);
        } catch (CantCreateCryptoCustomerIdentityException e) {
            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CouldNotCreateCryptoCustomerException(e, "Crypto Customer Identity Module Manager", "Cant Create Crypto Customer Identity");
        }
    }

    @Override
    public void updateCryptoCustomerIdentity(CryptoCustomerIdentityInformation cryptoBrokerIdentity) throws CantUpdateCustomerIdentityException {
        try {
            this.identityManager.updateCryptoCustomerIdentity(cryptoBrokerIdentity.getAlias(), cryptoBrokerIdentity.getPublicKey(), cryptoBrokerIdentity.getProfileImage());
        } catch (CantUpdateCustomerIdentityException e) {
            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantUpdateCustomerIdentityException(e, "Crypto Customer Identity Module Manager", "Cant Update Crypto Customer Identity");
        }
    }

    @Override
    public void publishCryptoCustomerIdentity(String cryptoCustomerPublicKey) throws CouldNotPublishCryptoCustomerException {

        try {
            System.out.println("************* voy al identity manager publicar la identidad");
            this.identityManager.publishIdentity(cryptoCustomerPublicKey);
        } catch (CantPublishIdentityException | IdentityNotFoundException e) {
            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CouldNotPublishCryptoCustomerException(e, "Crypto Customer Identity Module Manager", "Cant Publish Crypto Customer Identity");
        }
    }

    @Override
    public void unPublishCryptoCustomerIdentity(String cryptoCustomerPublicKey) throws com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.exceptions.CouldNotUnPublishCryptoCustomerException {

    }

    @Override
    public List<CryptoCustomerIdentityInformation> getAllCryptoCustomersIdentities(int max, int offset) throws com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.exceptions.CantGetCryptoCustomerListException {
        try {
            List<CryptoCustomerIdentityInformation> cryptoCustomers = new ArrayList<>();
            for(CryptoCustomerIdentity identity : this.identityManager.listAllCryptoCustomerFromCurrentDeviceUser()){
                cryptoCustomers.add(converIdentityToInformation(identity));
            }
            return cryptoCustomers;
        } catch (CantListCryptoCustomerIdentityException e) {
            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_identity.exceptions.CantGetCryptoCustomerListException(CantGetCryptoCustomerListException.DEFAULT_MESSAGE, e, "Crypto Customer Identity Module Manager","Cant Get List All Crypto Customer Identity");
        }
    }

    private CryptoCustomerIdentityInformation converIdentityToInformation(final CryptoCustomerIdentity identity){
        return new CryptoCustomerIdentityInformationImpl(identity.getAlias(), identity.getPublicKey(), identity.getProfileImage(), identity.isPublished());
    }

    private SettingsManager<IdentityCustomerPreferenceSettings> settingsManager;

    @Override
    public SettingsManager<IdentityCustomerPreferenceSettings> getSettingsManager() {
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
}
