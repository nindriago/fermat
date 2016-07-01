package com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.close_contract.interfaces.CloseContractManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.open_contract.interfaces.OpenContractManager;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.interfaces.CustomerBrokerContractSaleManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.stock_transactions.bank_money_restock.interfaces.BankMoneyRestockManager;
import com.bitdubai.fermat_cbp_api.layer.stock_transactions.cash_money_restock.interfaces.CashMoneyRestockManager;
import com.bitdubai.fermat_cbp_api.layer.stock_transactions.crypto_money_restock.interfaces.CryptoMoneyRestockManager;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.CryptoBrokerWalletManager;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.database.UserLevelBusinessTransactionCustomerBrokerSaleDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.database.UserLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.exceptions.CantInitializeCustomerBrokerSaleDatabaseException;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.structure.UserLevelBusinessTransactionCustomerBrokerSaleManager;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.structure.events.UserLevelBusinessTransactionCustomerBrokerSaleMonitorAgent;
import com.bitdubai.fermat_cer_api.layer.search.interfaces.CurrencyExchangeProviderFilterManager;
import com.bitdubai.fermat_pip_api.layer.module.notification.interfaces.NotificationManagerMiddleware;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Yordin Alayn on 16.09.15.
 * Modified by Franklin Marcano 15.12.2015
 */
@PluginInfo(createdBy = "franklinmarcano1970", maintainerMail = "franklinmarcano1970@gmail.com", platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.USER_LEVEL_BUSINESS_TRANSACTION, plugin = Plugins.CUSTOMER_BROKER_SALE)
public class UserLevelBusinessTransactionCustomerBrokerSalePluginRoot extends AbstractPlugin implements
        DatabaseManagerForDevelopers,
        LogManagerForDevelopers  {

    public UserLevelBusinessTransactionCustomerBrokerSalePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    private UserLevelBusinessTransactionCustomerBrokerSaleManager customerBrokerSaleManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    EventManager eventManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.LOG_MANAGER)
    LogManager logManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    PluginDatabaseSystem pluginDatabaseSystem;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.NEGOTIATION, plugin = Plugins.NEGOTIATION_SALE)
    CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.OPEN_CONTRACT)
    OpenContractManager openContractManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.BUSINESS_TRANSACTION, plugin = Plugins.CLOSE_CONTRACT)
    CloseContractManager closeContractManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.CONTRACT, plugin = Plugins.CONTRACT_SALE)
    CustomerBrokerContractSaleManager customerBrokerContractSaleManager;

    @NeededPluginReference(platform = Platforms.CURRENCY_EXCHANGE_RATE_PLATFORM, layer = Layers.SEARCH, plugin = Plugins.FILTER)
    private CurrencyExchangeProviderFilterManager currencyExchangeRateProviderFilter;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.WALLET, plugin = Plugins.CRYPTO_BROKER_WALLET)
    CryptoBrokerWalletManager cryptoBrokerWalletManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.STOCK_TRANSACTIONS, plugin = Plugins.BANK_MONEY_RESTOCK)
    BankMoneyRestockManager bankMoneyRestockManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.STOCK_TRANSACTIONS, plugin = Plugins.CASH_MONEY_RESTOCK)
    CashMoneyRestockManager cashMoneyRestockManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_BROKER_PLATFORM, layer = Layers.STOCK_TRANSACTIONS, plugin = Plugins.CRYPTO_MONEY_RESTOCK)
    CryptoMoneyRestockManager cryptoMoneyRestockManager;

    @NeededPluginReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.SUB_APP_MODULE, plugin = Plugins.NOTIFICATION)
    NotificationManagerMiddleware notificationManagerMiddleware;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_BROADCASTER_SYSTEM)
    Broadcaster broadcaster;

    UserLevelBusinessTransactionCustomerBrokerSaleDatabaseDao userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao;

    static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    public static EventSource EVENT_SOURCE = EventSource.USER_LEVEL_CUSTOMER_BROKER_SALE_MANAGER;

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_purchase.developer.bitdubai.version_1.UserLevelBusinessTransactionCustomerBrokerSalePluginRoot");
        return returnedClasses;
    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        /*
         * I will check the current values and update the LogLevel in those which is different
         */
        for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {

            /*
             * if this path already exists in the Root.bewLoggingLevel I'll update the value, else, I will put as new
             */
            if (UserLevelBusinessTransactionCustomerBrokerSalePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                UserLevelBusinessTransactionCustomerBrokerSalePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                UserLevelBusinessTransactionCustomerBrokerSalePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                UserLevelBusinessTransactionCustomerBrokerSalePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }
    }

    ServiceStatus serviceStatus = ServiceStatus.CREATED;

    @Override
    public void start() throws CantStartPluginException {
        try {
            customerBrokerSaleManager = new UserLevelBusinessTransactionCustomerBrokerSaleManager();
            startMonitorAgent();
            this.serviceStatus = ServiceStatus.STARTED;
            System.out.print("***** Init User Level Bussines Customer Broker Sale *****");
        } catch (CantStartAgentException e) {
            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, FermatException.wrapException(e), null, null);
        }
    }

    @Override
    public void pause() {
        this.serviceStatus = ServiceStatus.PAUSED;
    }

    @Override
    public void resume() {
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public FermatManager getManager() {
        return customerBrokerSaleManager;
    }

    public static LogLevel getLogLevelByClass(String className) {
        try{
            /**
             * sometimes the classname may be passed dynamically with an $moretext
             * I need to ignore whats after this.
             */
            String[] correctedClass = className.split((Pattern.quote("$")));
            return UserLevelBusinessTransactionCustomerBrokerSalePluginRoot.newLoggingLevel.get(correctedClass[0]);
        } catch (Exception e){
            /**
             * If I couldn't get the correct logging level, then I will set it to minimal.
             */
            return DEFAULT_LOG_LEVEL;
        }
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        UserLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory userLevelBusinessTransactionCustomerBrokerUserDeveloperFactory = new UserLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory(pluginDatabaseSystem, pluginId);
        return userLevelBusinessTransactionCustomerBrokerUserDeveloperFactory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        UserLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory userLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory = new UserLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory(pluginDatabaseSystem, pluginId);
        return userLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        UserLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory userLevelBusinessTransactionCustomerBrokerUserDeveloperFactory = new UserLevelBusinessTransactionCustomerBrokerSaleDeveloperFactory(pluginDatabaseSystem, pluginId);
        List<DeveloperDatabaseTableRecord> developerDatabaseTableRecordList = null;
        try {
            userLevelBusinessTransactionCustomerBrokerUserDeveloperFactory.initializeDatabase();
            developerDatabaseTableRecordList = userLevelBusinessTransactionCustomerBrokerUserDeveloperFactory.getDatabaseTableContent(developerObjectFactory, developerDatabaseTable);
        } catch (CantInitializeCustomerBrokerSaleDatabaseException e) {
            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
        return developerDatabaseTableRecordList;
    }

    UserLevelBusinessTransactionCustomerBrokerSaleMonitorAgent agent;
    /**
     * This method will start the Monitor Agent that watches the asyncronic process registered in the Customer Broker Sale plugin
     * @throws CantStartAgentException
     */
    private void startMonitorAgent() throws CantStartAgentException {
        if(agent == null) {
            agent = new UserLevelBusinessTransactionCustomerBrokerSaleMonitorAgent(
                    errorManager,
                    customerBrokerSaleNegotiationManager,
                    pluginDatabaseSystem,
                    pluginId,
                    openContractManager,
                    closeContractManager,
                    customerBrokerContractSaleManager,
                    currencyExchangeRateProviderFilter,
                    cryptoBrokerWalletManager,
                    bankMoneyRestockManager,
                    cashMoneyRestockManager,
                    cryptoMoneyRestockManager,
                    broadcaster);

            agent.start();

        }else agent.start();
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}