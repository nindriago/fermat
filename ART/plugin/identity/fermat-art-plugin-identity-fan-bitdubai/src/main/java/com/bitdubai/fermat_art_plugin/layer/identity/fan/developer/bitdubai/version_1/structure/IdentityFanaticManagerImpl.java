package com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_art_api.all_definition.exceptions.CantPublishIdentityException;
import com.bitdubai.fermat_art_api.all_definition.exceptions.IdentityNotFoundException;
import com.bitdubai.fermat_art_api.all_definition.interfaces.ArtIdentity;
import com.bitdubai.fermat_art_api.layer.actor_network_service.exceptions.CantExposeIdentityException;
import com.bitdubai.fermat_art_api.layer.actor_network_service.interfaces.fan.FanManager;
import com.bitdubai.fermat_art_api.layer.actor_network_service.interfaces.fan.util.FanExposingData;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantCreateFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantGetFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantUpdateFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.interfaces.Fanatic;
import com.bitdubai.fermat_art_api.layer.identity.fan.interfaces.FanaticIdentityManager;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.database.FanaticIdentityDao;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.exceptions.CantInitializeFanaticIdentityDatabaseException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.user.device_user.exceptions.CantGetLoggedInDeviceUserException;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;
import com.bitdubai.fermat_tky_api.all_definitions.enums.ExternalPlatform;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.Fan;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.TokenlyFanIdentityManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gabriel Araujo 10/03/16.
 */
public class IdentityFanaticManagerImpl implements DealsWithErrors, DealsWithLogger, DealsWithPluginDatabaseSystem, DealsWithPluginFileSystem,FanaticIdentityManager {
    /**
     * IdentityAssetIssuerManagerImpl member variables
     */
    UUID pluginId;

    /**
     * DealsWithErrors interface member variables
     */
    ErrorManager errorManager;

    /**
     * DealsWithLogger interface mmeber variables
     */
    LogManager logManager;

    /**
     * DealsWithPluginDatabaseSystem interface member variables
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * DealsWithPluginFileSystem interface member variables
     */
    PluginFileSystem pluginFileSystem;


    /**
     * DealsWithDeviceUsers Interface member variables.
     */
    private DeviceUserManager deviceUserManager;

    private FanManager fanManager;

    private TokenlyFanIdentityManager tokenlyFanIdentityManager;

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    @Override
    public void setPluginFileSystem(PluginFileSystem pluginFileSystem) {
        this.pluginFileSystem = pluginFileSystem;
    }

    /**
     * Constructor
     *
     * @param errorManager
     * @param logManager
     * @param pluginDatabaseSystem
     * @param pluginFileSystem
     */
    public IdentityFanaticManagerImpl(ErrorManager errorManager, LogManager logManager, PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem, UUID pluginId, DeviceUserManager deviceUserManager, FanManager fanManager, TokenlyFanIdentityManager tokenlyFanIdentityManager){
        this.errorManager = errorManager;
        this.logManager = logManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
        this.deviceUserManager = deviceUserManager;
        this.fanManager = fanManager;
        this.tokenlyFanIdentityManager = tokenlyFanIdentityManager;
    }

    private FanaticIdentityDao getFanaticIdentityDao() throws CantInitializeFanaticIdentityDatabaseException {
        return new FanaticIdentityDao(this.pluginDatabaseSystem, this.pluginFileSystem, this.pluginId);
    }

    public List<Fanatic> getIdentityArtistFromCurrentDeviceUser() throws CantListFanIdentitiesException {

        try {

            List<Fanatic> artists;


            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();
            artists = getFanaticIdentityDao().getIdentityFanaticsFromCurrentDeviceUser(loggedUser);


            return artists;

        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantListFanIdentitiesException("CAN'T GET ASSET NEW Fanatic IDENTITIES", e, "Error get logged user device", "");
        } catch (Exception e) {
            throw new CantListFanIdentitiesException("CAN'T GET ASSET NEW Fanatic IDENTITIES", FermatException.wrapException(e), "", "");
        }
    }

    public Fanatic getIdentitFanatic() throws CantGetFanIdentityException {
        Fanatic Fanatic = null;
        try {
            Fanatic = getFanaticIdentityDao().getIdentityFanatic();
        } catch (CantInitializeFanaticIdentityDatabaseException e) {
            e.printStackTrace();
        }
        return Fanatic;
    }
    public Fanatic getIdentitFanatic(String publicKey) throws CantGetFanIdentityException {
        Fanatic Fanatic = null;
        try {
            Fanatic = getFanaticIdentityDao().getIdentityFanatic(publicKey);
        } catch (CantInitializeFanaticIdentityDatabaseException e) {
            e.printStackTrace();
        }
        return Fanatic;
    }
    public Fanatic createNewIdentityArtist(String alias, byte[] profileImage, UUID externalIdentityID) throws CantCreateFanIdentityException {
        try {
            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();

            ECCKeyPair keyPair = new ECCKeyPair();
            String publicKey = keyPair.getPublicKey();
            String privateKey = keyPair.getPrivateKey();

            getFanaticIdentityDao().createNewUser(alias, publicKey, privateKey, loggedUser, profileImage, externalIdentityID);

            return new FanaticIdentityImp(alias, publicKey, profileImage,externalIdentityID, pluginFileSystem, pluginId);
        } catch (CantGetLoggedInDeviceUserException e) {
            throw new CantCreateFanIdentityException("CAN'T CREATE NEW Fanatic IDENTITY", e, "Error getting current logged in device user", "");
        } catch (Exception e) {
            throw new CantCreateFanIdentityException("CAN'T CREATE NEW Fanatic IDENTITY", FermatException.wrapException(e), "", "");
        }
    }

    public void updateIdentityArtist(String alias,String publicKey, byte[] profileImage, UUID externalIdentityID) throws CantUpdateFanIdentityException {
        try {
            getFanaticIdentityDao().updateIdentityFanaticUser(publicKey, alias, profileImage, externalIdentityID);

        } catch (CantInitializeFanaticIdentityDatabaseException e) {
            e.printStackTrace();
        }
    }

    public void registerIdentitiesANS(String publicKey) throws CantPublishIdentityException, IdentityNotFoundException {
        try {
            Fanatic Fanatic = getIdentitFanatic(publicKey);
            FanExposingData fanExposingData = new FanExposingData(Fanatic.getPublicKey(),Fanatic.getAlias(),Fanatic.getProfileImage());
            fanManager.exposeIdentity(fanExposingData);
        } catch (CantGetFanIdentityException | CantExposeIdentityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Fanatic> listIdentitiesFromCurrentDeviceUser() throws CantListFanIdentitiesException {
        return getIdentityArtistFromCurrentDeviceUser();
    }

    @Override
    public HashMap<ExternalPlatform, HashMap<UUID, String>> listExternalIdentitiesFromCurrentDeviceUser() throws CantListFanIdentitiesException {

        /*
            We'll return a HashMap based on the external platform containing another hashmap with the user and the id to that platform
         */
        HashMap<ExternalPlatform, HashMap<UUID,String>> externalArtistIdentities = new HashMap<>();
        HashMap<UUID,String> externalArtist = new HashMap<>();
        for (ExternalPlatform externalPlatform:
                ExternalPlatform.values()) {
            //Future platform will need to be added manually to the switch
            switch (externalPlatform){
                case TOKENLY:
                    try {
                        final List<Fan> tokenlyArtists = tokenlyFanIdentityManager.listIdentitiesFromCurrentDeviceUser();

                        for (Fan Fanatic:
                                tokenlyArtists) {
                            externalArtist.put(Fanatic.getId(),Fanatic.getUsername());
                        }
                        if(externalArtist.size()>0)
                            externalArtistIdentities.put(externalPlatform,externalArtist);
                    } catch (com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantListFanIdentitiesException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
        return externalArtistIdentities;
    }

    @Override
    public ArtIdentity getLinkedIdentity(String publicKey) {
        ArtIdentity artIdentity = null;
        try {
            Fanatic Fanatic = getIdentitFanatic(publicKey);
            if(Fanatic != null){
                for (ExternalPlatform externalPlatform:
                        ExternalPlatform.values()) {
                    //Future platform will need to be added manually to the switch
                    switch (externalPlatform){
                        case TOKENLY:
                            final Fan tokenlyArtist = tokenlyFanIdentityManager.getFanIdentity(Fanatic.getExternalIdentityID());
                            if(tokenlyArtist != null){
                                artIdentity = new FanaticIdentityImp(tokenlyArtist.getPublicKey(),tokenlyArtist.getProfileImage(),tokenlyArtist.getUsername(),tokenlyArtist.getId(),externalPlatform);
                            }
                            break;
                    }
                }
            }
        } catch (CantGetFanIdentityException e) {
            errorManager.reportUnexpectedPluginException(Plugins.FANATIC_IDENTITY, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
        } catch (com.bitdubai.fermat_tky_api.all_definitions.exceptions.IdentityNotFoundException e) {
            errorManager.reportUnexpectedPluginException(Plugins.FANATIC_IDENTITY, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
        } catch (com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantGetFanIdentityException e) {
            errorManager.reportUnexpectedPluginException(Plugins.FANATIC_IDENTITY, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
        }
        return artIdentity;
    }


    @Override
    public Fanatic createFanaticIdentity(String alias, byte[] imageBytes, UUID externalIdentityId) throws CantCreateFanIdentityException {
        return createNewIdentityArtist(alias, imageBytes, externalIdentityId);
    }

    @Override
    public void updateFanIdentity(String alias, String publicKey, byte[] imageProfile, UUID externalIdentityID) throws CantUpdateFanIdentityException {
        updateIdentityArtist(alias, publicKey, imageProfile, externalIdentityID);
    }

    @Override
    public Fanatic getFanIdentity(String publicKey) throws CantGetFanIdentityException, IdentityNotFoundException {
        return getIdentitFanatic(publicKey);
    }

    @Override
    public void publishIdentity(String publicKey) throws CantPublishIdentityException, IdentityNotFoundException {
        registerIdentitiesANS(publicKey);
    }
}
