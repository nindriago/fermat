package com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.crypto.util.CryptoHasher;
import com.bitdubai.fermat_api.layer.all_definition.enums.DeviceDirectory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;
import com.bitdubai.fermat_tky_api.all_definitions.enums.ExternalPlatform;
import com.bitdubai.fermat_tky_api.all_definitions.interfaces.User;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantGetFanIdentityException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.exceptions.CantUpdateFanIdentityException;
import com.bitdubai.fermat_tky_api.layer.identity.fan.interfaces.Fan;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.TokenlyFanIdentityPluginRoot;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.exceptions.CantCreateNewDeveloperException;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.exceptions.CantGetTokenlyFanIdentityPrivateKeyException;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.exceptions.CantGetTokenlyFanIdentityProfileImageException;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.exceptions.CantGetUserDeveloperIdentitiesException;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.exceptions.CantInitializeTokenlyFanIdentityDatabaseException;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.exceptions.CantPersistPrivateKeyException;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.exceptions.CantPersistProfileImageException;
import com.bitdubai.fermat_tky_plugin.layer.identity.fan_identity.developer.bitdubai.version_1.structure.TokenlyFanIdentityImp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gabriel Araujo 10/03/16.
 */
public class TokenlyFanIdentityDao implements DealsWithPluginDatabaseSystem {
    /**
     * Represent the Plugin Database.
     */
    private PluginDatabaseSystem pluginDatabaseSystem;

    private PluginFileSystem pluginFileSystem;

    private UUID pluginId;

    /**
     * Constructor with parameters
     *
     * @param pluginDatabaseSystem DealsWithPluginDatabaseSystem
     */

    public TokenlyFanIdentityDao(PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem, UUID pluginId) throws CantInitializeTokenlyFanIdentityDatabaseException {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;

        try {
            initializeDatabase();
        } catch (CantInitializeTokenlyFanIdentityDatabaseException e) {
            throw new CantInitializeTokenlyFanIdentityDatabaseException(e.getMessage());
        }
    }

    /**
     * Represent de Database where i will be working with
     */
    Database database;

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * This method open or creates the database i'll be working with
     *
     * @throws CantInitializeTokenlyFanIdentityDatabaseException
     */
    private void initializeDatabase() throws CantInitializeTokenlyFanIdentityDatabaseException {
        try {

             /*
              * Open new database connection
              */

            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_DB_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

             /*
              * The database exists but cannot be open. I can not handle this situation.
              */
            throw new CantInitializeTokenlyFanIdentityDatabaseException(cantOpenDatabaseException.getMessage());

        } catch (DatabaseNotFoundException e) {

             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
            TokenlyFanIdentityDatabaseFactory tokenlyFanIdentityDatabaseFactory = new TokenlyFanIdentityDatabaseFactory(pluginDatabaseSystem);

            try {
                  /*
                   * We create the new database
                   */
                database = tokenlyFanIdentityDatabaseFactory.createDatabase(pluginId);

            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                throw new CantInitializeTokenlyFanIdentityDatabaseException(cantCreateDatabaseException.getMessage());
            }
        } catch (Exception e) {

            throw new CantInitializeTokenlyFanIdentityDatabaseException(e.getMessage());

        }
    }
    /**
     * first I persist private key on a file
     * second I insert the record in database
     * third I save the profile image file
     *
     * @param user
     * @param id
     * @param privateKey
     * @param deviceUser
     * @param profileImage
     * @throws CantCreateNewDeveloperException
     */
    public void createNewUser(User user, UUID id,String publicKey, String privateKey, DeviceUser deviceUser, byte[] profileImage, String password, ExternalPlatform externalPlatform) throws CantCreateNewDeveloperException {

        try {
            if (aliasExists(user.getUsername())) {
                throw new CantCreateNewDeveloperException("Cant create new Redeem Point Identity, alias exists.");
            }

            persistNewUserPrivateKeysFile(publicKey, privateKey);

            DatabaseTable table = this.database.getTable(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME);
            DatabaseTableRecord record = table.getEmptyRecord();

            record.setUUIDValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME, id);
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_ID_COLUMN_NAME, user.getTokenlyId());
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PUBLIC_KEY_COLUMN_NAME, publicKey);
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, deviceUser.getPublicKey());
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_SECRET_KEY_COLUMN_NAME, user.getApiSecretKey());
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PASSWORD_COLUMN_NAME, CryptoHasher.performSha256(password));
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_USER_NAME_COLUMN_NAME, user.getUsername());
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ACCESS_TOKEN_COLUMN_NAME, user.getApiToken());
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EMAIL_COLUMN_NAME,user.getEmail());
            record.setStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_PLATFORM_COLUMN_NAME, externalPlatform.getCode());
            //We don't persist any artist connected list in this moment, theoretically they don't exist at this point.

            table.insertRecord(record);

            if (profileImage != null)
                persistNewUserProfileImage(publicKey, profileImage);

        } catch (CantInsertRecordException e) {
            // Cant insert record.
            throw new CantCreateNewDeveloperException(e.getMessage(), e, "Redeem Point Identity", "Cant create new Redeem Point, insert database problems.");

        } catch (CantPersistPrivateKeyException e) {
            // Cant insert record.
            throw new CantCreateNewDeveloperException(e.getMessage(), e, "ARedeem Point Identity", "Cant create new Redeem Point, persist private key error.");

        } catch (Exception e) {
            // Failure unknown.

            throw new CantCreateNewDeveloperException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Identity", "Cant create new Redeem Point, unknown failure.");
        }
    }

    /**
     * This method updates the Fan Identity in database.
     * @param fan
     * @throws CantUpdateFanIdentityException
     */
    public void updateIdentityFanUser(Fan fan) throws CantUpdateFanIdentityException {
        User user = fan.getMusicUser();
        String password = fan.getUserPassword();
        UUID id = fan.getId();
        String publicKey = fan.getPublicKey();
        byte[] profileImage = fan.getProfileImage();
        ExternalPlatform externalPlatform = fan.getExternalPlatform();
        String xmlStringList = fan.getArtistsConnectedStringList();
        //Update the Fan identity
        updateIdentityFanUser(
                user,
                password,
                id,
                publicKey,
                profileImage,
                externalPlatform,
                xmlStringList);

    }

    /**
     * This method updates the Fan Identity in database.
     * @param user
     * @param password
     * @param id
     * @param publicKey
     * @param profileImage
     * @param externalPlatform
     * @throws CantUpdateFanIdentityException
     */
    public void updateIdentityFanUser(
            User user,
            String password,
            UUID id,
            String publicKey,
            byte[] profileImage,
            ExternalPlatform externalPlatform) throws CantUpdateFanIdentityException {
        /**
         * In this method we don't need to update the artist connected list.
         */
        updateIdentityFanUser(
                user,
                password,
                id,
                publicKey,
                profileImage,
                externalPlatform, "");
    }

    /**
     * This method updates the Fan Identity in database.
     * @param user
     * @param password
     * @param id
     * @param publicKey
     * @param profileImage
     * @param externalPlatform
     * @param xmlStringList
     * @throws CantUpdateFanIdentityException
     */
    private void updateIdentityFanUser(
            User user,
            String password,
            UUID id,
            String publicKey,
            byte[] profileImage,
            ExternalPlatform externalPlatform,
            String xmlStringList) throws CantUpdateFanIdentityException {
        try {
            /**
             * 1) Get the table.
             */
            DatabaseTable table = this.database.getTable(
                    TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException(
                        "Cant get Fan Point Identity list, table not found.");
            }

            // 2) Find the Fan users.
            table.addUUIDFilter(
                    TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME,
                    id,
                    DatabaseFilterType.EQUAL);
            table.loadToMemory();

            // 3) Get Fan users.
            for (DatabaseTableRecord record : table.getRecords()) {
                //set new values
                record.setUUIDValue(
                        TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME,
                        id);
                record.setStringValue(
                        TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_ID_COLUMN_NAME,
                        user.getTokenlyId());
                record.setStringValue(
                        TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PUBLIC_KEY_COLUMN_NAME,
                        publicKey);
                record.setStringValue(
                        TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_SECRET_KEY_COLUMN_NAME,
                        user.getApiSecretKey());
                record.setStringValue(
                        TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PASSWORD_COLUMN_NAME,
                        CryptoHasher.performSha256(password));
                record.setStringValue(
                        TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_USER_NAME_COLUMN_NAME,
                        user.getUsername());
                record.setStringValue(
                        TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ACCESS_TOKEN_COLUMN_NAME,
                        user.getApiToken());
                record.setStringValue(
                        TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_PLATFORM_COLUMN_NAME,
                        externalPlatform.getCode());
                //Set the artist connected list
                if(!(xmlStringList==null || xmlStringList.isEmpty())){
                    //This is to avoid remove the artist connected list from database if we get an empty string.
                    record.setStringValue(
                            TokenlyFanIdentityDatabaseConstants.
                                    TOKENLY_FAN_IDENTITY_ARTIST_CONNECTED_LIST_COLUMN_NAME,
                            xmlStringList);
                }

                table.updateRecord(record);
            }
            if (profileImage != null)
                persistNewUserProfileImage(publicKey, profileImage);
        } catch (CantUpdateRecordException e) {
            throw new CantUpdateFanIdentityException(
                    e.getMessage(), e,
                    "Fan Identity",
                    "Cant update Fan Identity, database problems.");
        } catch (CantPersistProfileImageException e) {
            throw new CantUpdateFanIdentityException(
                    e.getMessage(),
                    e,
                    "Fan Identity",
                    "Cant update Fan Identity, persist image error.");
        } catch (Exception e) {
            throw new CantUpdateFanIdentityException(
                    e.getMessage(),
                    FermatException.wrapException(e),
                    "Fan Identity",
                    "Cant update Fan Identity, unknown failure.");
        }
    }

    public List<Fan> getIdentityFansFromCurrentDeviceUser(DeviceUser deviceUser) throws CantListFanIdentitiesException {


        // Setup method.
        List<Fan> list = new ArrayList<>(); // Intra User list.
        DatabaseTable table; // Intra User table.

        // Get Redeem Point identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Asset Issuer identity list, table not found.");
            }


            // 2) Find the Redeem Point.
            table.addStringFilter(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, deviceUser.getPublicKey(), DatabaseFilterType.EQUAL);
            table.loadToMemory();


            // 3) Get Redeem Point.
            for (DatabaseTableRecord record : table.getRecords()) {

                // Add records to list.
                /*list.add(new IdentityAssetRedeemPointImpl(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_SECRET_KEY_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME),
                        getFanIdentityPrivateKey(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME)),
                        getFanProfileImagePrivateKey(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME)),
                        pluginFileSystem,
                        pluginId), );*/
                list.add(new TokenlyFanIdentityImp(record.getUUIDValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_ID_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        getFanProfileImagePrivateKey(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PUBLIC_KEY_COLUMN_NAME)),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_USER_NAME_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ACCESS_TOKEN_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_SECRET_KEY_COLUMN_NAME),
                        ExternalPlatform.getByCode(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_PLATFORM_COLUMN_NAME)),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EMAIL_COLUMN_NAME),
                        pluginFileSystem,
                        pluginId));
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantListFanIdentitiesException(e.getMessage(), e, "Asset Redeem Point Identity", "Cant load " + TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantListFanIdentitiesException(e.getMessage(), FermatException.wrapException(e), "Asset Redeem Point Identity", "Cant get Asset Issuer identity list, unknown failure.");
        }

        // Return the list values.
        return list;
    }

    public Fan getIdentityFan() throws CantGetFanIdentityException {

        // Setup method.
        Fan fan = null;
        DatabaseTable table; // Intra User table.

        // Get Fan identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Asset Issuer identity list, table not found.");
            }


            // 2) Find the Identity Issuers.

//            table.addStringFilter(AssetIssuerIdentityDatabaseConstants.ASSET_ISSUER_IDENTITY_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, deviceUser.getPublicKey(), DatabaseFilterType.EQUAL);
            table.loadToMemory();

            // 3) Get Identity Issuers.

            for (DatabaseTableRecord record : table.getRecords()) {

                // Add records to list.
                /*fan = new IdentityAssetRedeemPointImpl(
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_SECRET_KEY_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME),
                        getFanProfileImagePrivateKey(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME)));*/
                fan = new TokenlyFanIdentityImp(record.getUUIDValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_ID_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        getFanProfileImagePrivateKey(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PUBLIC_KEY_COLUMN_NAME)),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_USER_NAME_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ACCESS_TOKEN_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_SECRET_KEY_COLUMN_NAME),
                        ExternalPlatform.getByCode(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_PLATFORM_COLUMN_NAME)),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EMAIL_COLUMN_NAME),
                        pluginFileSystem,
                        pluginId);
                //Include in Fan identity the artist connected from database
                fan.addArtistConnectedList(
                        record.getStringValue(
                                TokenlyFanIdentityDatabaseConstants.
                                        TOKENLY_FAN_IDENTITY_ARTIST_CONNECTED_LIST_COLUMN_NAME));

            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetFanIdentityException(e.getMessage(), e, "Asset Redeem Point Identity", "Cant load " + TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantGetFanIdentityException(e.getMessage(), FermatException.wrapException(e), "Asset Redeem Point Identity", "Cant get Asset Redeem Point identity list, unknown failure.");
        }

        // Return the list values.
        return fan;
    }
    public Fan getIdentityFan(UUID id) throws CantGetFanIdentityException {

        // Setup method.
        Fan fan = null;
        DatabaseTable table; // Intra User table.

        // Get Asset Issuers identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Asset Issuer identity list, table not found.");
            }


            // 2) Find the Identity Issuers.

            table.addUUIDFilter(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME, id, DatabaseFilterType.EQUAL);
            table.loadToMemory();

            // 3) Get Identity Issuers.

            for (DatabaseTableRecord record : table.getRecords()) {

                // Add records to list.
                /*fan = new IdentityAssetRedeemPointImpl(
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_SECRET_KEY_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME),
                        getFanProfileImagePrivateKey(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME)));*/
                fan =  new TokenlyFanIdentityImp(record.getUUIDValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ID_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_ID_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        getFanProfileImagePrivateKey(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_PUBLIC_KEY_COLUMN_NAME)),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_USER_NAME_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_ACCESS_TOKEN_COLUMN_NAME),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_SECRET_KEY_COLUMN_NAME),
                        ExternalPlatform.getByCode(record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EXTERNAL_PLATFORM_COLUMN_NAME)),
                        record.getStringValue(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_EMAIL_COLUMN_NAME),
                        pluginFileSystem,
                        pluginId);
                //Include in Fan identity the artist connected from database
                fan.addArtistConnectedList(
                        record.getStringValue(
                                TokenlyFanIdentityDatabaseConstants.
                                        TOKENLY_FAN_IDENTITY_ARTIST_CONNECTED_LIST_COLUMN_NAME));
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetFanIdentityException(e.getMessage(), e, "Asset Redeem Point Identity", "Cant load " + TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantGetFanIdentityException(e.getMessage(), FermatException.wrapException(e), "Asset Redeem Point Identity", "Cant get Asset Redeem Point identity list, unknown failure.");
        }

        // Return the list values.
        return fan;
    }

    public byte[] getFanProfileImagePrivateKey(String publicKey) throws CantGetTokenlyFanIdentityProfileImageException {
        byte[] profileImage;
        try {
            PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    TokenlyFanIdentityPluginRoot.TOKENLY_FAN_IDENTITY_PROFILE_IMAGE + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.loadFromMedia();

            profileImage = file.getContent();

        } catch (CantLoadFileException e) {
            throw new CantGetTokenlyFanIdentityProfileImageException("CAN'T GET IMAGE PROFILE ", e, "Error loaded file.", null);
        } catch (FileNotFoundException | CantCreateFileException e) {
            profileImage = new byte[0];
            // throw new CantGetIntraWalletUserIdentityProfileImageException("CAN'T GET IMAGE PROFILE ", e, "Error getting developer identity private keys file.", null);
        } catch (Exception e) {
            throw new CantGetTokenlyFanIdentityProfileImageException("CAN'T GET IMAGE PROFILE ", FermatException.wrapException(e), "", "");
        }

        return profileImage;
    }

    /**
     * Private Methods
     */

    private void persistNewUserPrivateKeysFile(String publicKey, String privateKey) throws CantPersistPrivateKeyException {
        try {
            PluginTextFile file = this.pluginFileSystem.createTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    TokenlyFanIdentityPluginRoot.TOKENLY_FAN_IDENTITY_PRIVATE_KEY + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.setContent(privateKey);

            file.persistToMedia();
        } catch (CantPersistFileException e) {
            throw new CantPersistPrivateKeyException("CAN'T PERSIST PRIVATE KEY ", e, "Error persist file.", null);
        } catch (CantCreateFileException e) {
            throw new CantPersistPrivateKeyException("CAN'T PERSIST PRIVATE KEY ", e, "Error creating file.", null);
        } catch (Exception e) {
            throw new CantPersistPrivateKeyException("CAN'T PERSIST PRIVATE KEY ", FermatException.wrapException(e), "", "");
        }
    }

    private void persistNewUserProfileImage(String publicKey, byte[] profileImage) throws CantPersistProfileImageException {
        try {
            PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    TokenlyFanIdentityPluginRoot.TOKENLY_FAN_IDENTITY_PROFILE_IMAGE + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.setContent(profileImage);

            file.persistToMedia();
        } catch (CantPersistFileException e) {
            throw new CantPersistProfileImageException("CAN'T PERSIST PROFILE IMAGE ", e, "Error persist file.", null);
        } catch (CantCreateFileException e) {
            throw new CantPersistProfileImageException("CAN'T PERSIST PROFILE IMAGE ", e, "Error creating file.", null);
        } catch (Exception e) {
            throw new CantPersistProfileImageException("CAN'T PERSIST PROFILE IMAGE ", FermatException.wrapException(e), "", "");
        }
    }

    /**
     * <p>Method that check if alias exists.
     *
     * @param alias
     * @return boolean exists
     * @throws CantCreateNewDeveloperException
     */
    private boolean aliasExists(String alias) throws CantCreateNewDeveloperException {


        DatabaseTable table;
        /**
         * Get developers identities list.
         * I select records on table
         */

        try {
            table = this.database.getTable(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME);

            if (table == null) {
                throw new CantGetUserDeveloperIdentitiesException("Cant check if alias exists");
            }

            table.addStringFilter(TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_USER_NAME_COLUMN_NAME, alias, DatabaseFilterType.EQUAL);
            table.loadToMemory();

            return table.getRecords().size() > 0;


        } catch (CantLoadTableToMemoryException em) {
            throw new CantCreateNewDeveloperException(em.getMessage(), em, "Asset Issuer  Identity", "Cant load " + TokenlyFanIdentityDatabaseConstants.TOKENLY_FAN_IDENTITY_TABLE_NAME + " table in memory.");

        } catch (Exception e) {
            throw new CantCreateNewDeveloperException(e.getMessage(), FermatException.wrapException(e), "Asset Issuer  Identity", "unknown failure.");
        }
    }

    public String getFanIdentityPrivateKey(String publicKey) throws CantGetTokenlyFanIdentityPrivateKeyException {
        String privateKey = "";
        try {
            PluginTextFile file = this.pluginFileSystem.getTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    TokenlyFanIdentityPluginRoot.TOKENLY_FAN_IDENTITY_PRIVATE_KEY + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.loadFromMedia();

            privateKey = file.getContent();

        } catch (CantLoadFileException e) {
            throw new CantGetTokenlyFanIdentityPrivateKeyException("CAN'T GET PRIVATE KEY ", e, "Error loaded file.", null);
        } catch (FileNotFoundException | CantCreateFileException e) {
            throw new CantGetTokenlyFanIdentityPrivateKeyException("CAN'T GET PRIVATE KEY ", e, "Error getting developer identity private keys file.", null);
        } catch (Exception e) {
            throw new CantGetTokenlyFanIdentityPrivateKeyException("CAN'T GET PRIVATE KEY ", FermatException.wrapException(e), "", "");
        }

        return privateKey;
    }
}
