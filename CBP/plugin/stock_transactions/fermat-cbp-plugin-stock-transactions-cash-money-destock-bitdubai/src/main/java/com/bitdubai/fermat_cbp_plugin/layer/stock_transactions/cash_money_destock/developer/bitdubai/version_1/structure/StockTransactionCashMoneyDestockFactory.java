package com.bitdubai.fermat_cbp_plugin.layer.stock_transactions.cash_money_destock.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_cbp_api.all_definition.business_transaction.CashMoneyTransaction;
import com.bitdubai.fermat_cbp_plugin.layer.stock_transactions.cash_money_destock.developer.bitdubai.version_1.database.StockTransactionsCashMoneyDestockDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.stock_transactions.cash_money_destock.developer.bitdubai.version_1.exceptions.DatabaseOperationException;
import com.bitdubai.fermat_cbp_plugin.layer.stock_transactions.cash_money_destock.developer.bitdubai.version_1.exceptions.MissingCashMoneyDestockDataException;

import java.util.List;
import java.util.UUID;


/**
 * Created by franklin on 11/12/15.
 */
public class StockTransactionCashMoneyDestockFactory {
    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;
    private StockTransactionsCashMoneyDestockDatabaseDao dao;

    /**
     * Constructor with params.
     *
     * @param pluginDatabaseSystem database system reference.
     * @param pluginId             of this module.
     */
    public StockTransactionCashMoneyDestockFactory(final PluginDatabaseSystem pluginDatabaseSystem,
                                                   final UUID pluginId) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;
    }


    private StockTransactionsCashMoneyDestockDatabaseDao getStockTransactionCashMoneyDestockDao() {
        if (dao == null)
            dao = new StockTransactionsCashMoneyDestockDatabaseDao(pluginDatabaseSystem, pluginId);

        return dao;
    }

    public void saveCashMoneyDestockTransactionData(CashMoneyTransaction cashMoneyTransaction) throws DatabaseOperationException, MissingCashMoneyDestockDataException {
        getStockTransactionCashMoneyDestockDao().saveCashMoneyDestockTransactionData(cashMoneyTransaction);
    }

    public List<CashMoneyTransaction> getCashMoneyTransactionList(DatabaseTableFilter filter) throws DatabaseOperationException, InvalidParameterException {
        return getStockTransactionCashMoneyDestockDao().getCashMoneyTransactionList(filter);
    }
}
