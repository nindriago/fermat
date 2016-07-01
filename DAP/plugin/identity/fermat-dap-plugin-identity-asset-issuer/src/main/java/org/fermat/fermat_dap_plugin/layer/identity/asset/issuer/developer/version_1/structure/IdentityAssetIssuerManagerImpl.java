package org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.exceptions.CantCreateActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import org.fermat.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.exceptions.CantRegisterActorAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.exceptions.CantCreateNewIdentityAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.exceptions.CantGetAssetIssuerIdentitiesException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.exceptions.CantListAssetIssuersException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.exceptions.CantUpdateIdentityAssetIssuerException;
import org.fermat.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;
import org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.AssetIssuerIdentityPluginRoot;
import org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantInitializeAssetIssuerIdentityDatabaseException;

import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.user.device_user.exceptions.CantGetLoggedInDeviceUserException;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by franklin on 02/11/15.
 */
public class IdentityAssetIssuerManagerImpl implements DealsWithLogger, DealsWithPluginDatabaseSystem, DealsWithPluginFileSystem {
    //TODO: Documentar
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

    AssetIssuerIdentityPluginRoot assetIssuerIdentityPluginRoot;

    /**
     * DealsWithDeviceUsers Interface member variables.
     */
    private DeviceUserManager deviceUserManager;

    /**
     * DealsWithActorAssetIssuer Interface member variables.
     */
    private ActorAssetIssuerManager actorAssetIssuerManager;

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
     * @param logManager
     * @param pluginDatabaseSystem
     * @param pluginFileSystem
     */
    public IdentityAssetIssuerManagerImpl(LogManager logManager,
                                          PluginDatabaseSystem pluginDatabaseSystem,
                                          PluginFileSystem pluginFileSystem,
                                          UUID pluginId,
                                          DeviceUserManager deviceUserManager,
                                          ActorAssetIssuerManager actorAssetIssuerManager,
                                          AssetIssuerIdentityPluginRoot assetIssuerIdentityPluginRoot) {
        this.logManager = logManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
        this.deviceUserManager = deviceUserManager;
        this.actorAssetIssuerManager = actorAssetIssuerManager;
        this.assetIssuerIdentityPluginRoot = assetIssuerIdentityPluginRoot;
    }

    private org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.database.AssetIssuerIdentityDao getAssetIssuerIdentityDao() throws CantInitializeAssetIssuerIdentityDatabaseException {
        return new org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.database.AssetIssuerIdentityDao(this.pluginDatabaseSystem, this.pluginFileSystem, this.pluginId);
    }

    public List<IdentityAssetIssuer> getIdentityAssetIssuersFromCurrentDeviceUser() throws CantListAssetIssuersException {

        try {

            List<IdentityAssetIssuer> assetIssuerList = new ArrayList<IdentityAssetIssuer>();


            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();
            assetIssuerList = getAssetIssuerIdentityDao().getIdentityAssetIssuersFromCurrentDeviceUser(loggedUser);

            return assetIssuerList;

        } catch (CantGetLoggedInDeviceUserException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantListAssetIssuersException("CAN'T GET ASSET ISSUER IDENTITIES", e, "Error get logged user device", "");
        } catch (org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantListAssetIssuersException("CAN'T GET ASSET ISSUER  IDENTITIES", e, "", "");
        } catch (Exception e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantListAssetIssuersException("CAN'T GET ASSET ISSUER IDENTITIES", FermatException.wrapException(e), "", "");
        }
    }

    public IdentityAssetIssuer createNewIdentityAssetIssuer(String alias, byte[] profileImage) throws CantCreateNewIdentityAssetIssuerException {
        try {
            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();

            ECCKeyPair keyPair = new ECCKeyPair();
            String publicKey = keyPair.getPublicKey();
            String privateKey = keyPair.getPrivateKey();

            getAssetIssuerIdentityDao().createNewUser(alias, publicKey, privateKey, loggedUser, profileImage);

            IdentityAssetIssuerImpl identityAssetIssuer = new IdentityAssetIssuerImpl(alias, publicKey, privateKey, profileImage, pluginFileSystem, pluginId);

            registerIdentities();

            return identityAssetIssuer;
        } catch (CantGetLoggedInDeviceUserException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCreateNewIdentityAssetIssuerException("CAN'T CREATE NEW ASSET ISSUER IDENTITY", e, "Error getting current logged in device user", "");
        } catch (Exception e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCreateNewIdentityAssetIssuerException("CAN'T CREATE NEW ASSET ISSUER IDENTITY", FermatException.wrapException(e), "", "");
        }
    }

    public void updateIdentityAssetIssuer(String identityPublicKey, String identityAlias, byte[] profileImage) throws CantUpdateIdentityAssetIssuerException {
        try {
            getAssetIssuerIdentityDao().updateIdentityAssetIssuer(identityPublicKey, identityAlias, profileImage);

            registerIdentities();
        } catch (CantInitializeAssetIssuerIdentityDatabaseException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            e.printStackTrace();
        } catch (org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            e.printStackTrace();
        }
    }

    public IdentityAssetIssuer getIdentityAssetIssuer() throws CantGetAssetIssuerIdentitiesException {
        IdentityAssetIssuer identityAssetIssuer = null;
        try {
            identityAssetIssuer = getAssetIssuerIdentityDao().getIdentityAssetIssuer();
        } catch (CantInitializeAssetIssuerIdentityDatabaseException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            e.printStackTrace();
        }
        return identityAssetIssuer;
    }

    public boolean hasIntraIssuerIdentity() throws CantListAssetIssuersException {
        try {

            DeviceUser loggedUser = deviceUserManager.getLoggedInDeviceUser();
            return getAssetIssuerIdentityDao().getIdentityAssetIssuersFromCurrentDeviceUser(loggedUser).size() > 0;
        } catch (CantGetLoggedInDeviceUserException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantListAssetIssuersException("CAN'T GET IF ASSET ISSUER IDENTITIES  EXISTS", e, "Error get logged user device", "");
        } catch (org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantListAssetIssuersException("CAN'T GET IF ASSET ISSUER IDENTITIES EXISTS", e, "", "");
        } catch (Exception e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantListAssetIssuersException("CAN'T GET ASSET ISSUER ISSUER IDENTITY EXISTS", FermatException.wrapException(e), "", "");
        }
    }

    public void registerIdentities() throws org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException {
        try {
            List<IdentityAssetIssuer> identityAssetIssuers = getAssetIssuerIdentityDao().getIdentityAssetIssuersFromCurrentDeviceUser(deviceUserManager.getLoggedInDeviceUser());
            if (identityAssetIssuers.size() > 0) {
                for (IdentityAssetIssuer identityAssetIssuer : identityAssetIssuers) {
                    actorAssetIssuerManager.createActorAssetIssuerFactory(identityAssetIssuer.getPublicKey(), identityAssetIssuer.getAlias(), identityAssetIssuer.getImage());
                }
            }
        } catch (CantGetLoggedInDeviceUserException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException("CAN'T GET IF ASSET ISSUER IDENTITIES  EXISTS", e, "Cant Get Logged InDevice User", "");
        } catch (org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException("CAN'T GET IF ASSET ISSUER IDENTITIES  EXISTS", e, "Cant List Asset Issuer Identities", "");
        } catch (CantCreateActorAssetIssuerException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException("CAN'T GET IF ASSET ISSUER IDENTITIES  EXISTS", e, "Cant Create ActorAsset Issuer", "");
        } catch (CantInitializeAssetIssuerIdentityDatabaseException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new org.fermat.fermat_dap_plugin.layer.identity.asset.issuer.developer.version_1.exceptions.CantListAssetIssuerIdentitiesException("CAN'T GET IF ASSET ISSUER IDENTITIES  EXISTS", e, "Cant Initialize Asset Issuer Identity Database", "");
        }
    }

    public void registerIdentitiesANS() throws CantRegisterActorAssetIssuerException {
        try {
            actorAssetIssuerManager.registerActorInActorNetworkService();
        } catch (CantRegisterActorAssetIssuerException e) {
            assetIssuerIdentityPluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantRegisterActorAssetIssuerException("CAN'T REGISTER IDENTITY TO ACTOR NETWORK SERVICE", FermatException.wrapException(e), "", "");
        }
    }
}
