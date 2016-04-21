package org.fermat.fermat_dap_plugin.layer.wallet.asset.issuer.developer.version_1.structure.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateTableException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.InvalidOwnerIdException;

import java.util.UUID;

/**
 * Created by franklin on 27/09/15.
 */
public class AssetIssuerWalletDatabaseFactory {
    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */
    private PluginDatabaseSystem pluginDatabaseSystem;

    public AssetIssuerWalletDatabaseFactory(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    public Database createDatabase(UUID ownerId, UUID walletId) throws CantCreateDatabaseException {
        Database database = null;
        try {
            database = this.pluginDatabaseSystem.createDatabase(ownerId, walletId.toString());
            createAssetIssuerWalletTable(ownerId, database.getDatabaseFactory());
            createAssetIssuerWalletBalancesTable(ownerId, database.getDatabaseFactory());
            createAssetStatisticTable(ownerId, database.getDatabaseFactory());
            createAssetMovementsTable(ownerId, database.getDatabaseFactory());
            return database;
        } catch (CantCreateTableException exception) {
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, exception, null, "Check the cause");
        } catch (CantCreateDatabaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    private void createAssetIssuerWalletTable(final UUID ownerId, final DatabaseFactory databaseFactory) throws CantCreateTableException {
        try {
            DatabaseTableFactory tableFactory = createAssetIssuerWalletTableFactory(ownerId, databaseFactory);
            databaseFactory.createTable(tableFactory);
        } catch (InvalidOwnerIdException exception) {
            throw new CantCreateTableException(CantCreateTableException.DEFAULT_MESSAGE, exception, null, "The ownerId of the database factory didn't match with the given owner id");
        }
    }


    private void createAssetIssuerWalletBalancesTable(final UUID ownerId, final DatabaseFactory databaseFactory) throws CantCreateTableException {
        try {
            DatabaseTableFactory tableFactory = createAssetIssuerWalletBalanceTableFactory(ownerId, databaseFactory);
            databaseFactory.createTable(tableFactory);
        } catch (InvalidOwnerIdException exception) {
            throw new CantCreateTableException(CantCreateTableException.DEFAULT_MESSAGE, exception, null, "The ownerId of the database factory didn't match with the given owner id");
        }
    }

    private void createAssetStatisticTable(final UUID ownerId, final DatabaseFactory databaseFactory) throws CantCreateTableException {
        try {
            DatabaseTableFactory tableFactory = createAssetStatisticTableFactory(ownerId, databaseFactory);
            databaseFactory.createTable(tableFactory);
        } catch (InvalidOwnerIdException exception) {
            throw new CantCreateTableException(CantCreateTableException.DEFAULT_MESSAGE, exception, null, "The ownerId of the database factory didn't match with the given owner id");
        }
    }

    private void createAssetMovementsTable(final UUID ownerId, final DatabaseFactory databaseFactory) throws CantCreateTableException {
        try {
            DatabaseTableFactory tableFactory = createAssetMovementTableFactory(ownerId, databaseFactory);
            databaseFactory.createTable(tableFactory);
        } catch (InvalidOwnerIdException exception) {
            throw new CantCreateTableException(CantCreateTableException.DEFAULT_MESSAGE, exception, null, "The ownerId of the database factory didn't match with the given owner id");
        }
    }

    private DatabaseTableFactory createAssetIssuerWalletTableFactory(final UUID ownerId, final DatabaseFactory databaseFactory) throws InvalidOwnerIdException {
        DatabaseTableFactory table = databaseFactory.newTableFactory(ownerId, AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_TABLE_NAME);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_TABLE_ID_COLUMN_NAME, DatabaseDataType.STRING, 36, true);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_ASSET_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 255, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_AMOUNT_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_VERIFICATION_ID_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_ADDRESS_FROM_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_ADDRESS_TO_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_ACTOR_FROM_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_ACTOR_TO_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_ACTOR_FROM_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_ACTOR_TO_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 20, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 20, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_TIME_STAMP_COLUMN_NAME, DatabaseDataType.STRING, 30, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_MEMO_COLUMN_NAME, DatabaseDataType.STRING, 200, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_TRANSACTION_HASH_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_RUNNING_BOOK_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_RUNNING_AVAILABLE_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);


        return table;
    }

    private DatabaseTableFactory createAssetIssuerWalletBalanceTableFactory(final UUID ownerId, final DatabaseFactory databaseFactory) throws InvalidOwnerIdException {
        DatabaseTableFactory table = databaseFactory.newTableFactory(ownerId, AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TABLE_NAME);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TABLE_ASSET_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 255, true);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TABLE_DESCRIPTION_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TABLE_NAME_COLUMN_NAME, DatabaseDataType.STRING, 100, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TABLE_AVAILABLE_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TABLE_BOOK_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TABLE_QUANTITY_AVAILABLE_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_WALLET_ISSUER_BALANCE_TABLE_QUANTITY_BOOK_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);

        return table;
    }

    private DatabaseTableFactory createAssetStatisticTableFactory(final UUID ownerId, final DatabaseFactory databaseFactory) throws InvalidOwnerIdException {
        DatabaseTableFactory table = databaseFactory.newTableFactory(ownerId, AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_TABLE_NAME);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_TRANSACTION_ID_COLUMN_NAME, DatabaseDataType.STRING, 255, true);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_TRANSACTION_HASH_COLUMN_NAME, DatabaseDataType.STRING, 255, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_ASSET_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 255, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_ACTOR_USER_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME, DatabaseDataType.STRING, 100, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_DISTRIBUTION_DATE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_ASSET_USAGE_DATE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_ASSET_CURRENT_STATUS_COLUMN_NAME, DatabaseDataType.STRING, 15, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_STATISTIC_ASSET_NAME_COLUMN_NAME, DatabaseDataType.STRING, 255, false);
        return table;
    }

    private DatabaseTableFactory createAssetMovementTableFactory(final UUID ownerId, final DatabaseFactory databaseFactory) throws InvalidOwnerIdException {
        DatabaseTableFactory table = databaseFactory.newTableFactory(ownerId, AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_TABLE_NAME);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_ENTRY_ID, DatabaseDataType.STRING, 255, true);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_ASSET_PUBLIC_KEY, DatabaseDataType.STRING, 255, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_ACTOR_FROM_PUBLIC_KEY, DatabaseDataType.STRING, 255, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_ACTOR_FROM_TYPE, DatabaseDataType.STRING, 15, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_ACTOR_TO_PUBLIC_KEY, DatabaseDataType.STRING, 255, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_ACTOR_TO_TYPE, DatabaseDataType.STRING, 15, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_TIMESTAMP, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(AssetWalletIssuerDatabaseConstant.ASSET_MOVEMENTS_TYPE, DatabaseDataType.STRING, 255, false);

        return table;
    }
}
