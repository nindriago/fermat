package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.close_contract.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Specialist;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.Transaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantConfirmTransactionException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.exceptions.CantDeliverPendingTransactionsException;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_cbp_api.all_definition.agent.CBPTransactionAgent;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractTransactionStatus;
import com.bitdubai.fermat_cbp_api.all_definition.events.enums.EventStatus;
import com.bitdubai.fermat_cbp_api.all_definition.events.enums.EventType;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CantInitializeCBPAgent;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CantSetObjectException;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.UnexpectedResultReturnedFromDatabaseException;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.close_contract.events.NewContractClosed;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.exceptions.CannotSendContractHashException;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.exceptions.CantGetContractListException;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.open_contract.enums.ContractType;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.close_contract.interfaces.ContractPurchaseRecord;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.close_contract.interfaces.ContractSaleRecord;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.exceptions.CantUpdateCustomerBrokerContractPurchaseException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchaseManager;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.exceptions.CantUpdateCustomerBrokerContractSaleException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.interfaces.CustomerBrokerContractSaleManager;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.exceptions.CantConfirmNotificationReceptionException;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.exceptions.CantSendContractNewStatusNotificationException;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.interfaces.BusinessTransactionMetadata;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.interfaces.TransactionTransmissionManager;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.close_contract.developer.bitdubai.version_1.CloseContractPluginRoot;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.close_contract.developer.bitdubai.version_1.database.CloseContractBusinessTransactionDao;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.close_contract.developer.bitdubai.version_1.database.CloseContractBusinessTransactionDatabaseConstants;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.close_contract.developer.bitdubai.version_1.database.CloseContractBusinessTransactionDatabaseFactory;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 04/12/15.
 */
public class CloseContractMonitorAgent implements
        CBPTransactionAgent,
        DealsWithLogger,
        DealsWithEvents,
        DealsWithErrors,
        DealsWithPluginDatabaseSystem,
        DealsWithPluginIdentity {

    Database database;
    MonitorAgent monitorAgent;
    Thread agentThread;
    LogManager logManager;
    EventManager eventManager;
    ErrorManager errorManager;
    PluginDatabaseSystem pluginDatabaseSystem;
    UUID pluginId;
    TransactionTransmissionManager transactionTransmissionManager;
    CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager;
    CustomerBrokerContractSaleManager customerBrokerContractSaleManager;

    public CloseContractMonitorAgent(PluginDatabaseSystem pluginDatabaseSystem,
                                     LogManager logManager,
                                     ErrorManager errorManager,
                                     EventManager eventManager,
                                     UUID pluginId,
                                     TransactionTransmissionManager transactionTransmissionManager,
                                     CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager,
                                     CustomerBrokerContractSaleManager customerBrokerContractSaleManager) throws CantSetObjectException {
        this.eventManager = eventManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.errorManager = errorManager;
        this.pluginId = pluginId;
        this.logManager = logManager;
        this.transactionTransmissionManager = transactionTransmissionManager;
        this.customerBrokerContractPurchaseManager = customerBrokerContractPurchaseManager;
        this.customerBrokerContractSaleManager = customerBrokerContractSaleManager;
    }

    @Override
    public void start() throws CantStartAgentException {

        //Logger LOG = Logger.getGlobal();
        //LOG.info("Close contract monitor agent starting");
        monitorAgent = new MonitorAgent();

        this.monitorAgent.setPluginDatabaseSystem(this.pluginDatabaseSystem);
        this.monitorAgent.setErrorManager(this.errorManager);

        try {
            this.monitorAgent.Initialize();
        } catch (CantInitializeCBPAgent exception) {
            errorManager.reportUnexpectedPluginException(Plugins.CLOSE_CONTRACT, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);
        }

        this.agentThread = new Thread(monitorAgent, this.getClass().getSimpleName());
        this.agentThread.start();

    }

    @Override
    public void stop() {
        this.agentThread.interrupt();
    }

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
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
    public void setPluginId(UUID pluginId) {
        this.pluginId = pluginId;
    }

    /**
     * Private class which implements runnable and is started by the Agent
     * Based on MonitorAgent created by Rodrigo Acosta
     */
    private class MonitorAgent implements DealsWithPluginDatabaseSystem, DealsWithErrors, Runnable {

        ErrorManager errorManager;
        PluginDatabaseSystem pluginDatabaseSystem;
        public final int SLEEP_TIME = 5000;
        int iterationNumber = 0;
        CloseContractBusinessTransactionDao closeContractBusinessTransactionDao;
        boolean threadWorking;

        @Override
        public void setErrorManager(ErrorManager errorManager) {
            this.errorManager = errorManager;
        }

        @Override
        public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
            this.pluginDatabaseSystem = pluginDatabaseSystem;
        }

        @Override
        public void run() {

            threadWorking = true;
            logManager.log(CloseContractPluginRoot.getLogLevelByClass(this.getClass().getName()),
                    "Close Contract Monitor Agent: running...", null, null);
            while (threadWorking) {
                /**
                 * Increase the iteration counter
                 */
                iterationNumber++;
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {


                    errorManager.reportUnexpectedPluginException(
                            Plugins.CLOSE_CONTRACT,
                            UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,
                            interruptedException);
                    return;
                }

                /**
                 * now I will check if there are pending transactions to raise the event
                 */
                try {

                    logManager.log(CloseContractPluginRoot.getLogLevelByClass(this.getClass().getName()), "Iteration number " + iterationNumber, null, null);
                    doTheMainTask();
                } catch (CannotSendContractHashException | CantUpdateRecordException e) {
                    errorManager.reportUnexpectedPluginException(
                            Plugins.CLOSE_CONTRACT,
                            UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                            e);
                }

            }

        }

        public void Initialize() throws CantInitializeCBPAgent {
            try {
                database = this.pluginDatabaseSystem.openDatabase(pluginId, CloseContractBusinessTransactionDatabaseConstants.DATABASE_NAME);
            } catch (DatabaseNotFoundException databaseNotFoundException) {

                errorManager.reportUnexpectedPluginException(
                        Plugins.CLOSE_CONTRACT,
                        UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,
                        databaseNotFoundException);

                CloseContractBusinessTransactionDatabaseFactory closeContractBusinessTransactionDatabaseFactory =
                        new CloseContractBusinessTransactionDatabaseFactory(this.pluginDatabaseSystem);
                try {
                    database = closeContractBusinessTransactionDatabaseFactory.createDatabase(pluginId,
                            CloseContractBusinessTransactionDatabaseConstants.DATABASE_NAME);

                } catch (CantCreateDatabaseException exception) {
                    errorManager.reportUnexpectedPluginException(Plugins.CLOSE_CONTRACT,
                            UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);
                    throw new CantInitializeCBPAgent(exception, "Initialize Monitor Agent - trying to create the plugin database",
                            "Please, check the cause");
                }

            } catch (CantOpenDatabaseException exception) {
                errorManager.reportUnexpectedPluginException(Plugins.CLOSE_CONTRACT,
                        UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);
                throw new CantInitializeCBPAgent(exception, "Initialize Monitor Agent - trying to open the plugin database",
                        "Please, check the cause");
            }
        }

        private void doTheMainTask() throws CannotSendContractHashException, CantUpdateRecordException {
            try {
                closeContractBusinessTransactionDao = new CloseContractBusinessTransactionDao(pluginDatabaseSystem, pluginId, database, errorManager);

                ContractType contractType;
                String contractXML;
                String transactionId;
                ContractPurchaseRecord purchaseContract = new ContractPurchaseRecord();
                ContractSaleRecord saleContract = new ContractSaleRecord();
                /**
                 * Check if exist in database new close contracts to send
                 */
                List<String> contractToCloseList = closeContractBusinessTransactionDao.getNewContractToCloseList();
                for (String hashToSubmit : contractToCloseList) {

                    contractType = closeContractBusinessTransactionDao.getContractType(hashToSubmit);
                    contractXML = closeContractBusinessTransactionDao.getContractXML(hashToSubmit);
//                    transactionId = closeContractBusinessTransactionDao.getTransactionId(hashToSubmit);
                    UUID transmissionId = UUID.randomUUID();

                    System.out.println("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - doTheMainTask() - getNewContractToCloseList() \n" +
                            "- contractType: " +contractType+"\n" +
                            "- contracXML: \n"+contractXML);

                    switch (contractType) {
                        case PURCHASE:

                            purchaseContract = (ContractPurchaseRecord) XMLParser.parseXML(contractXML, purchaseContract);
                            System.out.println("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - doTheMainTask() - getNewContractToCloseList() - PURCHASE\n" +
                                    " - purchaseContract.getPublicKeyCustomer(): "+purchaseContract.getPublicKeyCustomer()+"\n" +
                                    " - purchaseContract.getPublicKeyBroker(): "+purchaseContract.getPublicKeyBroker()+"\n"
                            );

                            transactionTransmissionManager.sendContractStatusNotification(
                                    purchaseContract.getPublicKeyCustomer(),
                                    purchaseContract.getPublicKeyBroker(),
                                    hashToSubmit,
                                    transmissionId.toString(),
                                    ContractTransactionStatus.CHECKING_CLOSING_CONTRACT,
                                    Plugins.CLOSE_CONTRACT,
                                    PlatformComponentType.ACTOR_CRYPTO_CUSTOMER,
                                    PlatformComponentType.ACTOR_CRYPTO_BROKER);
                            break;
                        case SALE:

                            saleContract = (ContractSaleRecord) XMLParser.parseXML(contractXML, saleContract);
                            System.out.println("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - doTheMainTask() - getNewContractToCloseList() - SALE\n" +
                                    " - saleContract.getPublicKeyCustomer(): "+saleContract.getPublicKeyCustomer()+"\n" +
                                    " - saleContract.getPublicKeyBroker(): "+saleContract.getPublicKeyBroker()+"\n"
                            );

                            transactionTransmissionManager.sendContractStatusNotification(
                                    saleContract.getPublicKeyBroker(),
                                    saleContract.getPublicKeyCustomer(),
                                    hashToSubmit,
                                    transmissionId.toString(),
                                    ContractTransactionStatus.CHECKING_CLOSING_CONTRACT,
                                    Plugins.CLOSE_CONTRACT,
                                    PlatformComponentType.ACTOR_CRYPTO_BROKER,
                                    PlatformComponentType.ACTOR_CRYPTO_CUSTOMER);
                            break;
                    }

                    closeContractBusinessTransactionDao.updateContractTransactionStatus(hashToSubmit, ContractTransactionStatus.CHECKING_CLOSING_CONTRACT);
                    transactionTransmissionManager.confirmReception(transmissionId);
                }

                /**
                 * Check if exists a new closed contract to confirm
                 */
                List<String> contractToConfirmList = closeContractBusinessTransactionDao.getClosingConfirmContractToCloseList();
                for (String hashToSubmit : contractToConfirmList) {

                    contractType = closeContractBusinessTransactionDao.getContractType(hashToSubmit);
                    contractXML = closeContractBusinessTransactionDao.getContractXML(hashToSubmit);
                    transactionId = closeContractBusinessTransactionDao.getTransactionId(hashToSubmit);
                    UUID transmissionId = UUID.randomUUID();

                    System.out.println("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - doTheMainTask() - getClosingConfirmContractToCloseList()\n");

                    switch (contractType) {
                        case PURCHASE:

                            System.out.println("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - doTheMainTask() - getClosingConfirmContractToCloseList() - PURCHASE\n");
                            purchaseContract = (ContractPurchaseRecord) XMLParser.parseXML(contractXML, purchaseContract);

                            transactionTransmissionManager.confirmNotificationReception(
                                    purchaseContract.getPublicKeyCustomer(),
                                    purchaseContract.getPublicKeyBroker(),
                                    hashToSubmit,
                                    transmissionId.toString(),
                                    Plugins.CLOSE_CONTRACT,
                                    PlatformComponentType.ACTOR_CRYPTO_CUSTOMER,
                                    PlatformComponentType.ACTOR_CRYPTO_BROKER);
                            break;
                        case SALE:

                            System.out.println("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - doTheMainTask() - getClosingConfirmContractToCloseList() - SALE\n");
                            saleContract = (ContractSaleRecord) XMLParser.parseXML(contractXML, saleContract);

                            transactionTransmissionManager.confirmNotificationReception(
                                    saleContract.getPublicKeyBroker(),
                                    saleContract.getPublicKeyCustomer(),
                                    hashToSubmit,
                                    transmissionId.toString(),
                                    Plugins.CLOSE_CONTRACT,
                                    PlatformComponentType.ACTOR_CRYPTO_BROKER,
                                    PlatformComponentType.ACTOR_CRYPTO_CUSTOMER);
                            break;
                    }

                    closeContractBusinessTransactionDao.updateContractTransactionStatus(hashToSubmit, ContractTransactionStatus.SUBMIT_CLOSING_CONTRACT_CONFIRMATION);
                    transactionTransmissionManager.confirmReception(transmissionId);
                }

                /**
                 * Check if pending events
                 */
                List<String> pendingEventsIdList = closeContractBusinessTransactionDao.getPendingEvents();
                for (String eventId : pendingEventsIdList) {
                    checkPendingEvent(eventId);
                }

            } catch (CantGetContractListException e) {
                throw new CannotSendContractHashException(e, "Sending contract hash", "Cannot get the contract list from database");
            } catch (UnexpectedResultReturnedFromDatabaseException e) {
                throw new CannotSendContractHashException(e, "Sending contract hash", "Unexpected result in database");
            }   catch (CantSendContractNewStatusNotificationException e) {
                throw new CannotSendContractHashException(e, "Sending contract hash", "Cannot send notification");
            } catch (CantConfirmNotificationReceptionException e) {
                throw new CannotSendContractHashException(e, "Sending contract hash", "Cannot send confirmation");
            } catch (CantConfirmTransactionException e) {
                throw new CannotSendContractHashException(e, "Sending contract hash", "Cannot send confirmation");
            }
        }

        private void raiseNewContractClosedEvent() {
            FermatEvent fermatEvent = eventManager.getNewEvent(EventType.NEW_CONTRACT_CLOSED);
            NewContractClosed newContractOpened = (NewContractClosed) fermatEvent;
            newContractOpened.setSource(EventSource.BUSINESS_TRANSACTION_CLOSE_CONTRACT);
            eventManager.raiseEvent(newContractOpened);
        }

        private void checkPendingEvent(String eventId) throws UnexpectedResultReturnedFromDatabaseException {

            try {
                String eventTypeCode = closeContractBusinessTransactionDao.getEventType(eventId);
                BusinessTransactionMetadata businessTransactionMetadata;
                String contractHash;
                ContractTransactionStatus contractTransactionStatus;
                ContractType contractType;

                if (eventTypeCode.equals(EventType.INCOMING_NEW_CONTRACT_STATUS_UPDATE.getCode())) {

                    System.out.print("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - checkPendingEvent() - INCOMING_NEW_CONTRACT_STATUS_UPDATE\n");

                    List<Transaction<BusinessTransactionMetadata>> pendingTransactionList = transactionTransmissionManager.getPendingTransactions(Specialist.UNKNOWN_SPECIALIST);
                    for (Transaction<BusinessTransactionMetadata> record : pendingTransactionList) {
                        businessTransactionMetadata = record.getInformation();
                        contractHash = businessTransactionMetadata.getContractHash();
                        contractType = closeContractBusinessTransactionDao.getContractType(contractHash);
                        contractTransactionStatus = closeContractBusinessTransactionDao.getContractTransactionStatus(contractHash);

                        System.out.print("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - checkPendingEvent() - INCOMING_NEW_CONTRACT_STATUS_UPDATE" +
                                "\n - "+contractTransactionStatus.getCode()+".equals("+ContractTransactionStatus.CHECKING_CLOSING_CONTRACT.getCode()+")");

                        if (contractTransactionStatus.getCode().equals(ContractTransactionStatus.CHECKING_CLOSING_CONTRACT.getCode())) {

                            System.out.print("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - checkPendingEvent() - INCOMING_NEW_CONTRACT_STATUS_UPDATE - VAL\n");

                            switch (contractType) {
                                case PURCHASE:
                                    customerBrokerContractPurchaseManager.updateStatusCustomerBrokerPurchaseContractStatus(contractHash, ContractStatus.COMPLETED);
                                    break;
                                case SALE:
                                    customerBrokerContractSaleManager.updateStatusCustomerBrokerSaleContractStatus(contractHash, ContractStatus.COMPLETED);
                                    break;
                            }
                            closeContractBusinessTransactionDao.updateContractTransactionStatus(contractHash, ContractTransactionStatus.CONFIRM_CLOSED_CONTRACT);
                            closeContractBusinessTransactionDao.setCompletionDateByContractHash(contractHash, (new Date()).getTime());
                            closeContractBusinessTransactionDao.updateEventStatus(eventId, EventStatus.NOTIFIED);
                            transactionTransmissionManager.confirmReception(record.getTransactionID());
                        }
                    }
                }

                //TODO: check new confirmed closed contract... raise an event.
                if (eventTypeCode.equals(EventType.INCOMING_CONFIRM_BUSINESS_TRANSACTION_RESPONSE.getCode())) {

                    System.out.print("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - checkPendingEvent() - INCOMING_CONFIRM_BUSINESS_TRANSACTION_RESPONSE\n");

                    List<Transaction<BusinessTransactionMetadata>> pendingTransactionList = transactionTransmissionManager.getPendingTransactions(Specialist.UNKNOWN_SPECIALIST);
                    for (Transaction<BusinessTransactionMetadata> record : pendingTransactionList) {
                        businessTransactionMetadata = record.getInformation();
                        contractHash = businessTransactionMetadata.getContractHash();
                        contractTransactionStatus = closeContractBusinessTransactionDao.getContractTransactionStatus(contractHash);

                        System.out.print("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - checkPendingEvent() - INCOMING_NEW_CONTRACT_STATUS_UPDATE" +
                                "\n - "+contractTransactionStatus.getCode()+".equals("+ContractTransactionStatus.CHECKING_CLOSING_CONTRACT.getCode()+")");
                        if (contractTransactionStatus.getCode().equals(ContractTransactionStatus.SUBMIT_CLOSING_CONTRACT_CONFIRMATION.getCode()) ||
                            contractTransactionStatus.getCode().equals(ContractTransactionStatus.CHECKING_CLOSING_CONTRACT.getCode())) {

                            System.out.print("\nTEST CONTRACT - CLOSE CONTRACT - AGENT - checkPendingEvent() - INCOMING_CONFIRM_BUSINESS_TRANSACTION_RESPONSE - VAL\n");
                            closeContractBusinessTransactionDao.updateContractTransactionStatus(contractHash, ContractTransactionStatus.CONTRACT_COMPLETED);
                            closeContractBusinessTransactionDao.updateEventStatus(eventId, EventStatus.NOTIFIED);
                            transactionTransmissionManager.confirmReception(record.getTransactionID());
                            raiseNewContractClosedEvent();
                        }
                    }
                }

            } catch (CantUpdateRecordException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(exception, "Checking pending events", "Cannot update the database");
            } catch (CantConfirmTransactionException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(exception, "Checking pending events", "Cannot confirm the transaction");
            } catch (CantUpdateCustomerBrokerContractSaleException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(exception, "Checking pending events", "Cannot update the contract sale status");
            } catch (CantDeliverPendingTransactionsException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(exception, "Checking pending events", "Cannot get the pending transactions from transaction transmission plugin");
            } catch (CantUpdateCustomerBrokerContractPurchaseException exception) {
                throw new UnexpectedResultReturnedFromDatabaseException(exception, "Checking pending events", "Cannot update the contract purchase status");
            }
        }

    }

}
