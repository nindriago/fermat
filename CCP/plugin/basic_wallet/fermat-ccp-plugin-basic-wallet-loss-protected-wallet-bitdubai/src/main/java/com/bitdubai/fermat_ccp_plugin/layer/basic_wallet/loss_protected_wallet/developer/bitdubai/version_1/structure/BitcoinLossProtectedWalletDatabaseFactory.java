package com.bitdubai.fermat_ccp_plugin.layer.basic_wallet.loss_protected_wallet.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseDataType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateTableException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.InvalidOwnerIdException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eze on 2015.06.23..
 */
public class BitcoinLossProtectedWalletDatabaseFactory implements DealsWithPluginDatabaseSystem {

    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */
    private PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * DealsWithPluginDatabaseSystem Interface implementation.
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    public Database createDatabase(UUID ownerId, UUID walletId) throws CantCreateDatabaseException {
        Database database = null;
        try {
            database = this.pluginDatabaseSystem.createDatabase(ownerId, walletId.toString());
            createLossProtectedWalletTable(ownerId, database.getDatabaseFactory());
            createLossProtectedWalletBalancesTable(ownerId, database.getDatabaseFactory());
            createLossProtectedSpendWalletTable(ownerId, database.getDatabaseFactory());
            insertInitialBalancesRecord(database);
            database.closeDatabase();
            return database;
        } catch(CantCreateTableException | CantInsertRecordException exception){
            if(database != null)
                database.closeDatabase();
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, exception, null, "Check the cause");
        } catch(CantCreateDatabaseException exception){
            throw exception;
        } catch(Exception exception){
            if(database != null)
                database.closeDatabase();
            throw new CantCreateDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    private void createLossProtectedWalletTable(final UUID ownerId, final DatabaseFactory databaseFactory) throws CantCreateTableException{
        try{
            DatabaseTableFactory tableFactory = createLossProtectedWalletTableFactory(ownerId, databaseFactory);
            databaseFactory.createTable(tableFactory);
        } catch(InvalidOwnerIdException exception){
            throw new CantCreateTableException(CantCreateTableException.DEFAULT_MESSAGE, exception, null, "The ownerId of the database factory didn't match with the given owner id");
        }
    }

    private void createLossProtectedSpendWalletTable(final UUID ownerId, final DatabaseFactory databaseFactory) throws CantCreateTableException{
        try{
            DatabaseTableFactory tableFactory = createLossProtectedWalletSpentTableFactory(ownerId, databaseFactory);
            databaseFactory.createTable(tableFactory);
        } catch(InvalidOwnerIdException exception){
            throw new CantCreateTableException(CantCreateTableException.DEFAULT_MESSAGE, exception, null, "The ownerId of the database factory didn't match with the given owner id");
        }
    }

    private DatabaseTableFactory createLossProtectedWalletTableFactory(final UUID ownerId, final DatabaseFactory databaseFactory) throws InvalidOwnerIdException{
        DatabaseTableFactory table = databaseFactory.newTableFactory(ownerId, BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_NAME);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_ID_COLUMN_NAME, DatabaseDataType.STRING, 36,true);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_VERIFICATION_ID_COLUMN_NAME, DatabaseDataType.STRING,36,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_ADDRESS_FROM_COLUMN_NAME, DatabaseDataType.STRING, 36,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_ADDRESS_TO_COLUMN_NAME, DatabaseDataType.STRING, 36,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_ACTOR_FROM_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_ACTOR_TO_COLUMN_NAME, DatabaseDataType.STRING, 150, false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_ACTOR_FROM_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 36, false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_ACTOR_TO_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 36, false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_AMOUNT_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 20, false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_BALANCE_TYPE_COLUMN_NAME, DatabaseDataType.STRING, 20, false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_TIME_STAMP_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_MEMO_COLUMN_NAME, DatabaseDataType.STRING, 200,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_TRANSACTION_HASH_COLUMN_NAME, DatabaseDataType.STRING, 100,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_RUNNING_BOOK_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_RUNNING_AVAILABLE_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0, false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_RUNNING_NETWORK_TYPE, DatabaseDataType.STRING, 10,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_TRANSACTION_STATE_COLUMN_NAME, DatabaseDataType.STRING, 10,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_TABLE_EXCHANGE_RATE_COLUMN_NAME, DatabaseDataType.REAL, 0,false);

        return table;
    }

    private DatabaseTableFactory createLossProtectedWalletSpentTableFactory(final UUID ownerId, final DatabaseFactory databaseFactory) throws InvalidOwnerIdException{
        DatabaseTableFactory table = databaseFactory.newTableFactory(ownerId, BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_SPENT_TABLE_NAME);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_SPENT_TABLE_ID_COLUMN_NAME, DatabaseDataType.STRING, 36,true);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_SPENT_TRANSACTION_ID_COLUMN_NAME, DatabaseDataType.STRING,100,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_SPENT_TABLE_BTC_SPENT_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_SPENT_TABLE_TIME_STAMP_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_SPENT_TABLE_EXCHANGE_RATE_COLUMN_NAME, DatabaseDataType.REAL, 0,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_SPENT_TABLE_RUNNING_NETWORK_TYPE, DatabaseDataType.STRING, 10,false);


        return table;
    }

    private void createLossProtectedWalletBalancesTable(final UUID ownerId, final DatabaseFactory databaseFactory) throws CantCreateTableException{
        try{
            DatabaseTableFactory tableFactory = createLossProtectedWalletBalancesTableFactory(ownerId, databaseFactory);
            databaseFactory.createTable(tableFactory);
        }catch(InvalidOwnerIdException exception){
            throw new CantCreateTableException(CantCreateTableException.DEFAULT_MESSAGE, exception, null, "The ownerId of the database factory didn't match with the given owner id");
        }
    }

    private DatabaseTableFactory createLossProtectedWalletBalancesTableFactory(final UUID ownerId, final DatabaseFactory databaseFactory) throws InvalidOwnerIdException{
        DatabaseTableFactory table = databaseFactory.newTableFactory(ownerId, BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_NAME);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_ID_COLUMN_NAME, DatabaseDataType.STRING, 36,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_AVAILABLE_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_BOOK_BALANCE_COLUMN_NAME, DatabaseDataType.LONG_INTEGER, 0,false);
        table.addColumn(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_RUNNING_NETWORK_TYPE, DatabaseDataType.STRING, 36,false);
        return table;
    }

    private void insertInitialBalancesRecord(final Database database) throws CantInsertRecordException{
        DatabaseTable balancesTable = database.getTable(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_NAME);
        List<DatabaseTableRecord> initialListRecord = constructBalanceInitialRecord(balancesTable);
        for(DatabaseTableRecord initalRecord : initialListRecord) {
            balancesTable.insertRecord(initalRecord);
        }
    }

    private List<DatabaseTableRecord> constructBalanceInitialRecord(final DatabaseTable balancesTable){

        List<DatabaseTableRecord> list = new ArrayList<>();
        DatabaseTableRecord balancesRecord1 = balancesTable.getEmptyRecord();
        UUID balanceRecordId = UUID.randomUUID();
        balancesRecord1.setUUIDValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_ID_COLUMN_NAME, balanceRecordId);
        balancesRecord1.setLongValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_AVAILABLE_BALANCE_COLUMN_NAME, 0);
        balancesRecord1.setLongValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_BOOK_BALANCE_COLUMN_NAME, 0);
        balancesRecord1.setStringValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_RUNNING_NETWORK_TYPE, BlockchainNetworkType.PRODUCTION.getCode());

        DatabaseTableRecord balancesRecord2 = balancesTable.getEmptyRecord();
        UUID balanceRecordId2 = UUID.randomUUID();
        balancesRecord2.setUUIDValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_ID_COLUMN_NAME, balanceRecordId2);
        balancesRecord2.setLongValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_AVAILABLE_BALANCE_COLUMN_NAME, 0);
        balancesRecord2.setLongValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_BOOK_BALANCE_COLUMN_NAME, 0);
        balancesRecord2.setStringValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_RUNNING_NETWORK_TYPE, BlockchainNetworkType.REG_TEST.getCode());

        DatabaseTableRecord balancesRecord3 = balancesTable.getEmptyRecord();
        UUID balanceRecordId3 = UUID.randomUUID();
        balancesRecord3.setUUIDValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_ID_COLUMN_NAME, balanceRecordId3);
        balancesRecord3.setLongValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_AVAILABLE_BALANCE_COLUMN_NAME, 0);
        balancesRecord3.setLongValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_BOOK_BALANCE_COLUMN_NAME, 0);
        balancesRecord3.setStringValue(BitcoinLossProtectedWalletDatabaseConstants.LOSS_PROTECTED_WALLET_BALANCE_TABLE_RUNNING_NETWORK_TYPE, BlockchainNetworkType.TEST_NET.getCode());

       list.add(balancesRecord1);
        list.add(balancesRecord2);
        list.add(balancesRecord3);
        return list;
    }

}
