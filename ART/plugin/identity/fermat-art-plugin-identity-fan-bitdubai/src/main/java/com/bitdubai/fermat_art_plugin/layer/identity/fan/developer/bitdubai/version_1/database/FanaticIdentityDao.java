package com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.FermatException;
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
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantCreateFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantGetFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantListFanIdentitiesException;
import com.bitdubai.fermat_art_api.layer.identity.fan.exceptions.CantUpdateFanIdentityException;
import com.bitdubai.fermat_art_api.layer.identity.fan.interfaces.Fanatic;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.FanaticPluginRoot;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.exceptions.CantGetArtistIdentityProfileImageException;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.exceptions.CantGetFanaticIdentityPrivateKeyException;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.exceptions.CantInitializeFanaticIdentityDatabaseException;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.exceptions.CantPersistPrivateKeyException;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.exceptions.CantPersistProfileImageException;
import com.bitdubai.fermat_art_plugin.layer.identity.fan.developer.bitdubai.version_1.structure.FanaticIdentityImp;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gabriel Araujo 10/03/16.
 */
public class FanaticIdentityDao implements DealsWithPluginDatabaseSystem {
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

    public FanaticIdentityDao(PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem, UUID pluginId) throws CantInitializeFanaticIdentityDatabaseException {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;

        try {
            initializeDatabase();
        } catch (CantInitializeFanaticIdentityDatabaseException e) {
            throw new CantInitializeFanaticIdentityDatabaseException(e.getMessage());
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
     * @throws CantInitializeFanaticIdentityDatabaseException
     */
    private void initializeDatabase() throws CantInitializeFanaticIdentityDatabaseException {
        try {

             /*
              * Open new database connection
              */

            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_DB_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

             /*
              * The database exists but cannot be open. I can not handle this situation.
              */
            throw new CantInitializeFanaticIdentityDatabaseException(cantOpenDatabaseException.getMessage());

        } catch (DatabaseNotFoundException e) {

             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
            FanaticIdentityDatabaseFactory fanaticIdentityDatabaseFactory = new FanaticIdentityDatabaseFactory(pluginDatabaseSystem);

            try {
                  /*
                   * We create the new database
                   */
                database = fanaticIdentityDatabaseFactory.createDatabase(pluginId);

            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                throw new CantInitializeFanaticIdentityDatabaseException(cantCreateDatabaseException.getMessage());
            }
        } catch (Exception e) {

            throw new CantInitializeFanaticIdentityDatabaseException(e.getMessage());

        }
    }

    /**
     * first i persist private key on a file
     * second i insert the record in database
     * third i save the profile image file
     *
     * @param alias
     * @param publicKey
     * @param privateKey
     * @param deviceUser
     * @param profileImage
     * @param externalIdentityID
     * @throws CantCreateFanIdentityException
     */
    public void createNewUser(String alias, String publicKey, String privateKey, DeviceUser deviceUser, byte[] profileImage, UUID externalIdentityID) throws CantCreateFanIdentityException {

        try {
            if (aliasExists(alias)) {
                throw new CantCreateFanIdentityException("Cant create new Fanatic, alias exists.");
            }

            persistNewUserPrivateKeysFile(publicKey, privateKey);

            DatabaseTable table = this.database.getTable(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME);
            DatabaseTableRecord record = table.getEmptyRecord();

            record.setStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME, publicKey);
            record.setStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME, alias);
            record.setStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, deviceUser.getPublicKey());
            record.setUUIDValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_EXTERNAL_IDENTITY_ID_COLUMN_NAME, externalIdentityID);

            table.insertRecord(record);

            if (profileImage != null)
                persistNewUserProfileImage(publicKey, profileImage);

        } catch (CantInsertRecordException e) {
            // Cant insert record.
            throw new CantCreateFanIdentityException(e.getMessage(), e, "Redeem Point Identity", "Cant create new Redeem Point, insert database problems.");

        } catch (CantPersistPrivateKeyException e) {
            // Cant insert record.
            throw new CantCreateFanIdentityException(e.getMessage(), e, "ARedeem Point Identity", "Cant create new Redeem Point, persist private key error.");

        } catch (Exception e) {
            // Failure unknown.

            throw new CantCreateFanIdentityException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Identity", "Cant create new Redeem Point, unknown failure.");
        }
    }

    public void updateIdentityFanaticUser(String publicKey, String alias, byte[] profileImage,
                                          UUID externalIdentityID) throws CantUpdateFanIdentityException {
        try {
            /**
             * 1) Get the table.
             */
            DatabaseTable table = this.database.getTable(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantUpdateFanIdentityException("Cant get Fanatic identity list, table not found.");
            }

            // 2) Find the Intra users.
            table.addStringFilter(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME, publicKey, DatabaseFilterType.EQUAL);
            table.loadToMemory();


            // 3) Get Intra users.
            for (DatabaseTableRecord record : table.getRecords()) {
                //set new values
                record.setStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME, publicKey);
                record.setStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME, alias);
                record.setUUIDValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_EXTERNAL_IDENTITY_ID_COLUMN_NAME, externalIdentityID);

                table.updateRecord(record);
            }

            if (profileImage != null)
                persistNewUserProfileImage(publicKey, profileImage);

        } catch (CantUpdateRecordException e) {
            throw new CantUpdateFanIdentityException(e.getMessage(), e, "Redeem Point Identity", "Cant update Redeem Point Identity, database problems.");
        } catch (CantPersistProfileImageException e) {
            throw new CantUpdateFanIdentityException(e.getMessage(), e, "Redeem Point Identity", "Cant update Redeem Point Identity, persist image error.");
        } catch (Exception e) {
            throw new CantUpdateFanIdentityException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Identity", "Cant update Redeem Point Identity, unknown failure.");
        }
    }

    public List<Fanatic> getIdentityFanaticsFromCurrentDeviceUser(DeviceUser deviceUser) throws CantListFanIdentitiesException {


        // Setup method.
        List<Fanatic> list = new ArrayList<>(); // Intra User list.
        DatabaseTable table; // Intra User table.

        // Get Redeem Point identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantUpdateFanIdentityException("Cant get Fanatic identity list, table not found.");
            }


            // 2) Find the Redeem Point.
            table.addStringFilter(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, deviceUser.getPublicKey(), DatabaseFilterType.EQUAL);
            table.loadToMemory();


            // 3) Get Redeem Point.
            for (DatabaseTableRecord record : table.getRecords()) {

                // Add records to list.
                /*list.add(new IdentityAssetRedeemPointImpl(record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME),
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        getArtistIdentityPrivateKey(record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME)),
                        getFanaticProfileImagePrivateKey(record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME)),
                        pluginFileSystem,
                        pluginId), );*/
                list.add(new FanaticIdentityImp(record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME),
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        record.getUUIDValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_EXTERNAL_IDENTITY_ID_COLUMN_NAME),
                        pluginFileSystem,
                        pluginId));
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantListFanIdentitiesException(e.getMessage(), e, "Asset Redeem Point Identity", "Cant load " + FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantListFanIdentitiesException(e.getMessage(), FermatException.wrapException(e), "Asset Redeem Point Identity", "Cant get Asset Issuer identity list, unknown failure.");
        }

        // Return the list values.
        return list;
    }

    public Fanatic getIdentityFanatic() throws CantGetFanIdentityException {

        // Setup method.
        Fanatic Fanatic = null;
        DatabaseTable table; // Intra User table.

        // Get Asset Issuers identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantUpdateFanIdentityException("Cant get Fanatic identity list, table not found.");
            }


            // 2) Find the Identity Issuers.

//            table.addStringFilter(AssetIssuerIdentityDatabaseConstants.ASSET_ISSUER_IDENTITY_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, deviceUser.getPublicKey(), DatabaseFilterType.EQUAL);
            table.loadToMemory();

            // 3) Get Identity Issuers.

            for (DatabaseTableRecord record : table.getRecords()) {

                // Add records to list.
                /*Fanatic = new IdentityAssetRedeemPointImpl(
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME),
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        getFanaticProfileImagePrivateKey(record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME)));*/
                Fanatic = new FanaticIdentityImp(
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME),
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        getFanaticProfileImagePrivateKey(record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME)),
                        record.getUUIDValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_EXTERNAL_IDENTITY_ID_COLUMN_NAME),
                        pluginFileSystem,
                        pluginId);

            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetFanIdentityException(e.getMessage(), e, "Asset Redeem Point Identity", "Cant load " + FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantGetFanIdentityException(e.getMessage(), FermatException.wrapException(e), "Asset Redeem Point Identity", "Cant get Asset Redeem Point identity list, unknown failure.");
        }

        // Return the list values.
        return Fanatic;
    }
    public Fanatic getIdentityFanatic(String publicKey) throws CantGetFanIdentityException {

        // Setup method.
        Fanatic Fanatic = null;
        DatabaseTable table; // Intra User table.

        // Get Asset Issuers identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantUpdateFanIdentityException("Cant get Fanatic identity list, table not found.");
            }


            // 2) Find the Identity Issuers.

            table.addStringFilter(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME, publicKey, DatabaseFilterType.EQUAL);
            table.loadToMemory();

            // 3) Get Identity Issuers.

            for (DatabaseTableRecord record : table.getRecords()) {

                // Add records to list.
                /*Fanatic = new IdentityAssetRedeemPointImpl(
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME),
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        getFanaticProfileImagePrivateKey(record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME)));*/
                Fanatic = new FanaticIdentityImp(
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME),
                        record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME),
                        getFanaticProfileImagePrivateKey(record.getStringValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_PUBLIC_KEY_COLUMN_NAME)),
                        record.getUUIDValue(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_EXTERNAL_IDENTITY_ID_COLUMN_NAME),
                        pluginFileSystem,
                        pluginId);

            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetFanIdentityException(e.getMessage(), e, "Asset Redeem Point Identity", "Cant load " + FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantGetFanIdentityException(e.getMessage(), FermatException.wrapException(e), "Asset Redeem Point Identity", "Cant get Asset Redeem Point identity list, unknown failure.");
        }

        // Return the list values.
        return Fanatic;
    }

    public byte[] getFanaticProfileImagePrivateKey(String publicKey) throws CantGetArtistIdentityProfileImageException {
        byte[] profileImage;
        try {
            PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    FanaticPluginRoot.FANATIC_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.loadFromMedia();

            profileImage = file.getContent();

        } catch (CantLoadFileException e) {
            throw new CantGetArtistIdentityProfileImageException("CAN'T GET IMAGE PROFILE ", e, "Error loaded file.", null);
        } catch (FileNotFoundException | CantCreateFileException e) {
            profileImage = new byte[0];
            // throw new CantGetIntraWalletUserIdentityProfileImageException("CAN'T GET IMAGE PROFILE ", e, "Error getting developer identity private keys file.", null);
        } catch (Exception e) {
            throw new CantGetArtistIdentityProfileImageException("CAN'T GET IMAGE PROFILE ", FermatException.wrapException(e), "", "");
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
                    FanaticPluginRoot.FANATIC_PRIVATE_KEYS_FILE_NAME + "_" + publicKey,
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
                    FanaticPluginRoot.FANATIC_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
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
     * @throws CantCreateFanIdentityException
     */
    private boolean aliasExists(String alias) throws CantCreateFanIdentityException {


        DatabaseTable table;
        /**
         * Get developers identities list.
         * I select records on table
         */

        try {
            table = this.database.getTable(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME);

            if (table == null) {
                throw new CantUpdateFanIdentityException("Cant check if alias exists");
            }

            table.addStringFilter(FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_ALIAS_COLUMN_NAME, alias, DatabaseFilterType.EQUAL);
            table.loadToMemory();

            return table.getRecords().size() > 0;


        } catch (CantLoadTableToMemoryException em) {
            throw new CantCreateFanIdentityException(em.getMessage(), em, "Asset Issuer  Identity", "Cant load " + FanaticIdentityDatabaseConstants.FANATIC_IDENTITY_TABLE_NAME + " table in memory.");

        } catch (Exception e) {
            throw new CantCreateFanIdentityException(e.getMessage(), FermatException.wrapException(e), "Asset Issuer  Identity", "unknown failure.");
        }
    }

    public String getArtistIdentityPrivateKey(String publicKey) throws CantGetFanaticIdentityPrivateKeyException {
        String privateKey = "";
        try {
            PluginTextFile file = this.pluginFileSystem.getTextFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    FanaticPluginRoot.FANATIC_PRIVATE_KEYS_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.loadFromMedia();

            privateKey = file.getContent();

        } catch (CantLoadFileException e) {
            throw new CantGetFanaticIdentityPrivateKeyException("CAN'T GET PRIVATE KEY ", e, "Error loaded file.", null);
        } catch (FileNotFoundException | CantCreateFileException e) {
            throw new CantGetFanaticIdentityPrivateKeyException("CAN'T GET PRIVATE KEY ", e, "Error getting developer identity private keys file.", null);
        } catch (Exception e) {
            throw new CantGetFanaticIdentityPrivateKeyException("CAN'T GET PRIVATE KEY ", FermatException.wrapException(e), "", "");
        }

        return privateKey;
    }
}
