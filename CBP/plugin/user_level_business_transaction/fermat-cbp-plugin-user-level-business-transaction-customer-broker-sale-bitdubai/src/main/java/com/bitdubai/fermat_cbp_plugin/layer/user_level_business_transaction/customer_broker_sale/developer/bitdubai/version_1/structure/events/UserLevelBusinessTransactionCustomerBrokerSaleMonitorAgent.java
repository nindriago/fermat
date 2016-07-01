package com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.structure.events;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStopAgentException;
import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.BroadcasterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ClauseType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.MoneyType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.OriginTransaction;
import com.bitdubai.fermat_cbp_api.all_definition.negotiation.Clause;
import com.bitdubai.fermat_cbp_api.all_definition.negotiation.Negotiation;
import com.bitdubai.fermat_cbp_api.all_definition.util.NegotiationClauseHelper;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.close_contract.exceptions.CantCloseContractException;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.close_contract.interfaces.CloseContractManager;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.open_contract.interfaces.OpenContractManager;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.exceptions.CantGetListCustomerBrokerContractSaleException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.interfaces.CustomerBrokerContractSale;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_sale.interfaces.CustomerBrokerContractSaleManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.exceptions.CantGetListSaleNegotiationsException;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiation;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.stock_transactions.bank_money_restock.interfaces.BankMoneyRestockManager;
import com.bitdubai.fermat_cbp_api.layer.stock_transactions.cash_money_restock.interfaces.CashMoneyRestockManager;
import com.bitdubai.fermat_cbp_api.layer.stock_transactions.crypto_money_restock.interfaces.CryptoMoneyRestockManager;
import com.bitdubai.fermat_cbp_api.layer.user_level_business_transaction.common.enums.TransactionStatus;
import com.bitdubai.fermat_cbp_api.layer.user_level_business_transaction.customer_broker_sale.interfaces.CustomerBrokerSale;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.CryptoBrokerWallet;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.CryptoBrokerWalletManager;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.setting.CryptoBrokerWalletProviderSetting;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.setting.CryptoBrokerWalletSetting;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.database.UserLevelBusinessTransactionCustomerBrokerSaleConstants;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.database.UserLevelBusinessTransactionCustomerBrokerSaleDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.exceptions.DatabaseOperationException;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.exceptions.MissingCustomerBrokerSaleDataException;
import com.bitdubai.fermat_cbp_plugin.layer.user_level_business_transaction.customer_broker_sale.developer.bitdubai.version_1.utils.CustomerBrokerSaleImpl;
import com.bitdubai.fermat_cer_api.all_definition.interfaces.CurrencyPair;
import com.bitdubai.fermat_cer_api.all_definition.interfaces.ExchangeRate;
import com.bitdubai.fermat_cer_api.all_definition.utils.CurrencyPairImpl;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantGetExchangeRateException;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.UnsupportedCurrencyPairException;
import com.bitdubai.fermat_cer_api.layer.provider.interfaces.CurrencyExchangeRateProviderManager;
import com.bitdubai.fermat_cer_api.layer.search.exceptions.CantGetProviderException;
import com.bitdubai.fermat_cer_api.layer.search.interfaces.CurrencyExchangeProviderFilterManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;


/**
 * The Class <code>UserLevelBusinessTransactionCustomerBrokerSaleMonitorAgent</code>
 * contains the logic for handling agent transactional
 * Created by franklin on 15.12.15
 */
public class UserLevelBusinessTransactionCustomerBrokerSaleMonitorAgent extends FermatAgent {
    //TODO: Documentar y manejo de excepciones.
    private Thread agentThread;
    private final ErrorManager errorManager;
    private final CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager;
    private final UserLevelBusinessTransactionCustomerBrokerSaleDatabaseDao userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao;
    private final OpenContractManager openContractManager;
    private final CloseContractManager closeContractManager;
    private final CustomerBrokerContractSaleManager customerBrokerContractSaleManager;
    private final CurrencyExchangeProviderFilterManager currencyExchangeRateProviderFilter;
    private final CryptoBrokerWalletManager cryptoBrokerWalletManager;
    private final BankMoneyRestockManager bankMoneyRestockManager;
    private final CashMoneyRestockManager cashMoneyRestockManager;
    private final CryptoMoneyRestockManager cryptoMoneyRestockManager;
    private Broadcaster broadcaster;

    public final int SLEEP_TIME = 5000;
    public final int DELAY_HOURS = 2;
    public final int TIME_BETWEEN_NOTIFICATIONS = 600000; //10min
    private long lastNotificationTime = 0;

    public UserLevelBusinessTransactionCustomerBrokerSaleMonitorAgent(ErrorManager errorManager,
                                                                      CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager,
                                                                      PluginDatabaseSystem pluginDatabaseSystem,
                                                                      UUID pluginId,
                                                                      OpenContractManager openContractManager,
                                                                      CloseContractManager closeContractManager,
                                                                      CustomerBrokerContractSaleManager customerBrokerContractSaleManager,
                                                                      CurrencyExchangeProviderFilterManager currencyExchangeRateProviderFilter,
                                                                      CryptoBrokerWalletManager cryptoBrokerWalletManager,
                                                                      BankMoneyRestockManager bankMoneyRestockManager,
                                                                      CashMoneyRestockManager cashMoneyRestockManager,
                                                                      CryptoMoneyRestockManager cryptoMoneyRestockManager,
                                                                      Broadcaster broadcaster) {

        this.errorManager = errorManager;
        this.customerBrokerSaleNegotiationManager = customerBrokerSaleNegotiationManager;
        this.openContractManager = openContractManager;
        this.closeContractManager = closeContractManager;
        this.customerBrokerContractSaleManager = customerBrokerContractSaleManager;
        this.currencyExchangeRateProviderFilter = currencyExchangeRateProviderFilter;
        this.cryptoBrokerWalletManager = cryptoBrokerWalletManager;
        this.bankMoneyRestockManager = bankMoneyRestockManager;
        this.cashMoneyRestockManager = cashMoneyRestockManager;
        this.cryptoMoneyRestockManager = cryptoMoneyRestockManager;
        this.broadcaster = broadcaster;
        this.userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao = new UserLevelBusinessTransactionCustomerBrokerSaleDatabaseDao(pluginDatabaseSystem, pluginId);

        createAndStartThread();
    }

    @Override
    public void start() throws CantStartAgentException {
        Logger LOG = Logger.getGlobal();
        LOG.info("Customer Broker Sale monitor agent starting");

        this.agentThread.start();
        super.start();
    }

    @Override
    public void stop() throws CantStopAgentException {
        this.agentThread.interrupt();
        super.stop();
    }

    private void createAndStartThread() {
        final Runnable target = new Runnable() {
            @Override
            public void run() {
                while (isRunning()) {
                    process();
                }
            }
        };

        this.agentThread = new Thread(target, this.getClass().getSimpleName());
    }

    private void process() {

        while (isRunning()) {

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException interruptedException) {
                cleanResources();
                return;
            }

            doTheMainTask();

            if (agentThread.isInterrupted()) {
                cleanResources();
                return;
            }
        }
    }

    private void doTheMainTask() {
        try {
            final String transactionStatusColumnName = UserLevelBusinessTransactionCustomerBrokerSaleConstants.
                    CUSTOMER_BROKER_SALE_TRANSACTION_STATUS_COLUMN_NAME;

            // TODO: Esto es provisorio. hay que obtenerlo del Wallet Manager de WPD hasta que matias haga los cambios para que no sea necesario enviar esto
            final String brokerWalletPublicKey = "crypto_broker_wallet";

            // NegotiationStatus.CLOSED -> TransactionStatus.IN_PROCESS
            takeCloseNegotiationsAndCreateTransactionWithStatusInProgress();

            // IN_PROCESS -> IN_OPEN_CONTRACT
            changeTransactionStatusFromInProcessToInOpenContract(transactionStatusColumnName, brokerWalletPublicKey);

            // IN_OPEN_CONTRACT -> IN_CONTRACT_SUBMIT
            changeTransactionStatusFromInOpenContractToInContractSubmit(transactionStatusColumnName);

            // IN_CONTRACT_SUBMIT -> Update Contract Expiration Time and notify
            updateContractExpirationDateWhitStatusInContractSubmitAndNotify(transactionStatusColumnName, brokerWalletPublicKey);

            // IN_CONTRACT_SUBMIT -> Update Contract Status to CANCELLED for expiration time in payment submit
            changeTransactionStatusFromInContractSubmitToCancelledIfExpirationTimeReached(transactionStatusColumnName, brokerWalletPublicKey);

            // IN_CONTRACT_SUBMIT -> IN_PAYMENT_SUBMIT: apply the credit (restock) from the payment to the wallets
            changeTransactionStatusFromInContractSubmitToInPaymentSubmitAndApplyCredit(transactionStatusColumnName, brokerWalletPublicKey);

            // IN_PAYMENT_SUBMIT -> IN_PENDING_MERCHANDISE
            changeTransactionStatusFromInPaymentSubmitToInPendingMerchandise(transactionStatusColumnName);

            // IN_PENDING_MERCHANDISE -> Update Contract Expiration Time and notify
            updateContractExpirationDateWhitInPendingMerchandiseStatusAndNotify(transactionStatusColumnName, brokerWalletPublicKey);

            // IN_PENDING_MERCHANDISE -> Update Contract Status to CANCELLED for expiration time in merchandise
            changeTransactionStatusFromInPendingMerchandiseToCancelledIfExpirationTimeReached(transactionStatusColumnName, brokerWalletPublicKey);

            // IN_PENDING_MERCHANDISE -> MERCHANDISE_SUBMIT
            changeTransactionStatusInPendingMerchandiseFromToMerchandiseSubmit(transactionStatusColumnName);

            // IN_MERCHANDISE_SUBMIT -> COMPLETED
            changeTransactionStatusFromInMerchandiseSubmitToCompleted(transactionStatusColumnName, brokerWalletPublicKey);

        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.CRYPTO_BROKER_SALE,
                    UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
    }

    /**
     * NegotiationStatus.CLOSED -> TransactionStatus.IN_PROCESS
     *
     * @throws CantGetListSaleNegotiationsException
     * @throws DatabaseOperationException
     * @throws InvalidParameterException
     * @throws MissingCustomerBrokerSaleDataException
     */
    private void takeCloseNegotiationsAndCreateTransactionWithStatusInProgress() throws FermatException {
        final Collection<CustomerBrokerSaleNegotiation> negotiationsClosed = customerBrokerSaleNegotiationManager.
                getNegotiationsByStatus(NegotiationStatus.CLOSED);

        final String customerBrokerSaleTransactionIdColumnName = UserLevelBusinessTransactionCustomerBrokerSaleConstants.
                CUSTOMER_BROKER_SALE_TRANSACTION_ID_COLUMN_NAME;

        for (CustomerBrokerSaleNegotiation records : negotiationsClosed) {
            String negotiationId = records.getNegotiationId().toString(); //Buscar que la transaccion no se encuentre ya registrada

            DatabaseTableFilter filterTable = getFilterTable(negotiationId, customerBrokerSaleTransactionIdColumnName);

            List<CustomerBrokerSale> customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);

            if (customerBrokerSales.isEmpty()) {
                CustomerBrokerSale customerBrokerSale = new CustomerBrokerSaleImpl(negotiationId, negotiationId, 0, null, null, TransactionStatus.IN_PROCESS, null, null, null);
                userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);
            }
        }
    }

    /**
     * IN_PROCESS -> IN_OPEN_CONTRACT:
     * <p/>
     * Registra el Open Contract siempre y cuando el Transaction Status de la CustomerBrokerSale este IN_PROCESS
     * Se obtiene el customerCurrency de la negociacion para obtener el marketExchangeRate de ese currency vs. USD
     * <p/>
     * Se envia un Broadcast para actualizar la UI y enviar una notificacion
     *
     * @param transactionStatusColumnName the Transaction Status column name
     * @param brokerWalletPublicKey       the broker wallet public key
     *
     * @throws FermatException
     */
    private void changeTransactionStatusFromInProcessToInOpenContract(String transactionStatusColumnName, String brokerWalletPublicKey) throws FermatException {
        List<CustomerBrokerSale> customerBrokerSales;
        DatabaseTableFilter filterTable = getFilterTable(TransactionStatus.IN_PROCESS.getCode(), transactionStatusColumnName);
        customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);

        //Registra el Open Contract siempre y cuando el Transaction_Status de la Transaction Customer Broker Sale este IN_PROCESS
        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation = customerBrokerSaleNegotiationManager.
                    getNegotiationsByNegotiationId(UUID.fromString(customerBrokerSale.getTransactionId()));

            //Find the negotiation's customerCurrency, to find the marketExchangeRate of that currency vs. USD
            String customerCurrency = "";
            for (Clause clause : customerBrokerSaleNegotiation.getClauses())
                if (clause.getType() == ClauseType.CUSTOMER_CURRENCY)
                    customerCurrency = clause.getValue();

            float marketExchangeRate = 1;
            if (customerCurrency.isEmpty()) {
                try {
                    marketExchangeRate = getMarketExchangeRate(customerCurrency);
                } catch (CantGetExchangeRateException e) {
                    marketExchangeRate = 1;
                }
            }
            openContractManager.openSaleContract(customerBrokerSaleNegotiation, marketExchangeRate);

            //Actualiza el Transaction_Status de la Transaction Customer Broker Sale a IN_OPEN_CONTRACT
            customerBrokerSale.setTransactionStatus(TransactionStatus.IN_OPEN_CONTRACT);
            userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);
            broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, brokerWalletPublicKey, CBPBroadcasterConstants.CBW_NEW_CONTRACT_NOTIFICATION);
            broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW);
        }
    }

    /**
     * IN_CONTRACT_SUBMIT -> Update Contract Status to CANCELLED for expiration time in payment submit:
     * <p/>
     * If Expiration Time is done, Update the contract status to CANCELLED.
     *
     * @param transactionStatusColumnName the Transaction Status column name
     * @param brokerWalletPublicKey       the broker wallet public key
     *
     * @throws FermatException
     */
    private void changeTransactionStatusFromInContractSubmitToCancelledIfExpirationTimeReached(String transactionStatusColumnName, String brokerWalletPublicKey) throws FermatException {
        DatabaseTableFilter filterTable = getFilterTable(TransactionStatus.IN_CONTRACT_SUBMIT.getCode(), transactionStatusColumnName);
        List<CustomerBrokerSale> customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        Collection<CustomerBrokerContractSale> contractSalesPendingPayment = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.PENDING_PAYMENT);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesPendingPayment) {
                String negotiationId = customerBrokerContractSale.getNegotiatiotId();

                if (customerBrokerSale.getTransactionId().equals(negotiationId)) {

                    long timeToDelivery = 0;
                    long timeStampToday = new Date().getTime();
                    Negotiation negotiation = customerBrokerSaleNegotiationManager.getNegotiationsByNegotiationId(UUID.fromString(negotiationId));
                    Collection<Clause> negotiationClause = negotiation.getClauses();
                    String clauseValue = NegotiationClauseHelper.getNegotiationClauseValue(negotiationClause, ClauseType.CUSTOMER_DATE_TIME_TO_DELIVER);

                    if (clauseValue != null)
                        timeToDelivery = Long.parseLong(clauseValue);

                    /*System.out.println("\n*** TEST USER LEVEL - IN PAYMENT SUMIT - CANCELLED CONTRACT FOR EXPIRATION TIME IN PAYMENT ***\n" +
                                    "\n - Contract: " + customerBrokerContractSale.getContractId() +
                                    "\n - timeStampToday: " + timeStampToday +
                                    "\n - dateTimeToDelivery: " + timeToDelivery
                    );*/

                    if (timeStampToday >= timeToDelivery) {

                        //UPDATE CONTRACT STATUS
                        customerBrokerContractSaleManager.cancelContract(customerBrokerContractSale.getContractId(),
                                "CANCELLATION CONTRACT BY EXPIRATION IN DATE OF SUBMIT PAYMENT.");

                        //UPDATE STATUS USER LEVEL BUSINESS TRANSACTION
                        customerBrokerSale.setTransactionStatus(TransactionStatus.CANCELLED);
                        userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);

                        //BROADCASTER
                        broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, brokerWalletPublicKey, CBPBroadcasterConstants.CCW_CONTRACT_CANCELLED_NOTIFICATION);
                        broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CCW_CONTRACT_UPDATE_VIEW);

                    }
                }
            }
        }
    }

    /**
     * IN_OPEN_CONTRACT -> IN_CONTRACT_SUBMIT:
     * <p/>
     * Se debe enviar un Broadcast para actualizar la UI
     *
     * @param transactionStatusColumnName the Transaction Status column name
     *
     * @throws FermatException
     */
    private void changeTransactionStatusFromInOpenContractToInContractSubmit(String transactionStatusColumnName) throws FermatException {
        DatabaseTableFilter filterTable = getFilterTable(TransactionStatus.IN_OPEN_CONTRACT.getCode(), transactionStatusColumnName);
        List<CustomerBrokerSale> customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        Collection<CustomerBrokerContractSale> contractSalesPendingPayment = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.PENDING_PAYMENT);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesPendingPayment) {
                if (Objects.equals(customerBrokerSale.getTransactionId(), customerBrokerContractSale.getNegotiatiotId())) {
                    customerBrokerSale.setTransactionStatus(TransactionStatus.IN_CONTRACT_SUBMIT);
                    userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);
                    broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW);
                }
            }
        }
    }

    /**
     * IN_CONTRACT_SUBMIT -> Update Contract Expiration Time and notify:
     * <p/>
     * Si la fecha del contracto se acerca al dia y 2 horas antes de vencerse debo de elevar un evento de notificacion
     * siempre y cuando el ContractStatus sea igual a PENDING_PAYMENT
     *
     * @param transactionStatusColumnName the Transaction Status column name
     * @param brokerWalletPublicKey       the broker wallet public key
     *
     * @throws FermatException
     */
    private void updateContractExpirationDateWhitStatusInContractSubmitAndNotify(String transactionStatusColumnName, String brokerWalletPublicKey) throws FermatException {
        DatabaseTableFilter filterTable = getFilterTable(TransactionStatus.IN_CONTRACT_SUBMIT.getCode(), transactionStatusColumnName);
        List<CustomerBrokerSale> customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        Collection<CustomerBrokerContractSale> contractSalesPendingPayment = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.PENDING_PAYMENT);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesPendingPayment) {
                if (Objects.equals(customerBrokerSale.getTransactionId(), customerBrokerContractSale.getNegotiatiotId())) {
                    // Si la fecha del contracto se acerca al dia y 2 horas antes de vencerse debo de elevar un evento
                    // de notificacion siempre y cuando el ContractStatus sea igual a PENDING_PAYMENT

                    long timeStampToday = ((customerBrokerContractSale.getDateTime() - new Date().getTime()) / 3600000);
                    if (timeStampToday <= DELAY_HOURS) {
                        customerBrokerContractSaleManager.updateContractNearExpirationDatetime(customerBrokerContractSale.getContractId(), true);

                        if (new Date().getTime() - lastNotificationTime > TIME_BETWEEN_NOTIFICATIONS) {
                            lastNotificationTime = new Date().getTime();
                            broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, brokerWalletPublicKey, CBPBroadcasterConstants.CBW_CONTRACT_EXPIRATION_NOTIFICATION);
                            broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW);
                        }
                    }
                }
            }
        }
    }

    /**
     * * IN_PENDING_MERCHANDISE -> Update Contract Expiration Time and notify:
     * <p/>
     * Si se acerca la tiempo límite para recibir la mercadería y esta no ha sido registrada como recibida,
     * se eleva un evento de notificación
     *
     * @param transactionStatusColumnName the Transaction Status column name
     * @param brokerWalletPublicKey       the broker wallet public key
     *
     * @throws FermatException
     */
    private void updateContractExpirationDateWhitInPendingMerchandiseStatusAndNotify(String transactionStatusColumnName, String brokerWalletPublicKey) throws FermatException {
        DatabaseTableFilter filterTable = getFilterTable(TransactionStatus.IN_PENDING_MERCHANDISE.getCode(), transactionStatusColumnName);
        List<CustomerBrokerSale> customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        Collection<CustomerBrokerContractSale> contractSalesPendingMerchandise = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.PENDING_MERCHANDISE);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesPendingMerchandise) {
                if (Objects.equals(customerBrokerSale.getTransactionId(), customerBrokerContractSale.getNegotiatiotId())) {
                    //Si se acerca la tiempo límite para recibir la mercadería y esta no ha sido registrada como recibida,
                    // se eleva un evento de notificación
                    long timeStampToday = ((customerBrokerContractSale.getDateTime() - new Date().getTime()) / 3600000);
                    if (timeStampToday <= DELAY_HOURS) {
                        customerBrokerContractSaleManager.updateContractNearExpirationDatetime(customerBrokerContractSale.getContractId(), true);

                        if (new Date().getTime() - lastNotificationTime > TIME_BETWEEN_NOTIFICATIONS) {
                            lastNotificationTime = new Date().getTime();
                            broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, brokerWalletPublicKey, CBPBroadcasterConstants.CBW_CONTRACT_EXPIRATION_NOTIFICATION);
                            broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW);
                        }
                    }
                }
            }
        }
    }

    /**
     * IN_PENDING_MERCHANDISE -> MERCHANDISE_SUBMIT:
     * <p/>
     * Se debe enviar un Broadcast para actualizar la UI
     *
     * @param transactionStatusColumnName the Transaction Status column name
     *
     * @throws FermatException
     */
    private void changeTransactionStatusInPendingMerchandiseFromToMerchandiseSubmit(String transactionStatusColumnName) throws FermatException {
        DatabaseTableFilter filterTable = getFilterTable(TransactionStatus.IN_PENDING_MERCHANDISE.getCode(), transactionStatusColumnName);
        List<CustomerBrokerSale> customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        Collection<CustomerBrokerContractSale> contractSalesMerchandiseSubmit = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.MERCHANDISE_SUBMIT);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesMerchandiseSubmit) {
                if (Objects.equals(customerBrokerSale.getTransactionId(), customerBrokerContractSale.getNegotiatiotId())) {
                    customerBrokerSale.setTransactionStatus(TransactionStatus.IN_MERCHANDISE_SUBMIT);
                    userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);
                    broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW);
                }
            }
        }
    }

    /**
     * IN_PENDING_MERCHANDISE -> Update Contract Status to CANCELLED for expiration time in merchandise:
     * <p/>
     * If Expiration Time is done, Update the contract status to CANCELLED.
     *
     * @param transactionStatusColumnName the Transaction Status column name
     * @param brokerWalletPublicKey       the broker wallet public key
     *
     * @throws FermatException
     */
    private void changeTransactionStatusFromInPendingMerchandiseToCancelledIfExpirationTimeReached(String transactionStatusColumnName, String brokerWalletPublicKey) throws FermatException {
        DatabaseTableFilter filterTable;
        List<CustomerBrokerSale> customerBrokerSales;
        Collection<CustomerBrokerContractSale> contractSalesPendingMerchandise;
        filterTable = getFilterTable(TransactionStatus.IN_PENDING_MERCHANDISE.getCode(), transactionStatusColumnName);
        customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        contractSalesPendingMerchandise = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.PENDING_MERCHANDISE);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesPendingMerchandise) {
                String negotiationId = customerBrokerContractSale.getNegotiatiotId();

                if (customerBrokerSale.getTransactionId().equals(negotiationId)) {

                    long timeToDelivery = 0;
                    long timeStampToday = new Date().getTime();
                    Negotiation negotiation = customerBrokerSaleNegotiationManager.getNegotiationsByNegotiationId(UUID.fromString(negotiationId));
                    Collection<Clause> negotiationClause = negotiation.getClauses();
                    String clauseValue = NegotiationClauseHelper.getNegotiationClauseValue(negotiationClause, ClauseType.BROKER_DATE_TIME_TO_DELIVER);

                    if (clauseValue != null)
                        timeToDelivery = Long.parseLong(clauseValue);

                    // System.out.println("\n*** TEST USER LEVEL - IN PAYMENT SUMIT - CANCELLED CONTRACT FOR EXPIRATION TIME IN MERCHANDISE ***\n" +
                    // "\n - Contract: " + customerBrokerContractSale.getContractId() +
                    // "\n - timeStampToday: " + timeStampToday +
                    // "\n - dateTimeToDelivery: " + timeToDelivery);

                    if (timeStampToday >= timeToDelivery) {

                        //UPDATE CONTRACT STATUS
                        customerBrokerContractSaleManager.cancelContract(customerBrokerContractSale.getContractId(),
                                "CANCELLATION CONTRACT BY EXPIRATION IN DATE OF SUBMIT MERCHANDISE.");

                        //UPDATE STATUS USER LEVEL BUSINESS TRANSACTION
                        customerBrokerSale.setTransactionStatus(TransactionStatus.CANCELLED);
                        userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);

                        //BROADCASTER
                        broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, brokerWalletPublicKey, CBPBroadcasterConstants.CCW_CONTRACT_CANCELLED_NOTIFICATION);
                        broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CCW_CONTRACT_UPDATE_VIEW);

                    }
                }
            }
        }
    }

    /**
     * IN_PAYMENT_SUBMIT -> IN_PENDING_MERCHANDISE:
     * Se debe enviar un Broadcast para actualizar la UI
     *
     * @param transactionStatusColumnName the Transaction Status column name
     *
     * @throws FermatException
     */
    private void changeTransactionStatusFromInPaymentSubmitToInPendingMerchandise(String transactionStatusColumnName) throws FermatException {
        DatabaseTableFilter filterTable;
        List<CustomerBrokerSale> customerBrokerSales;
        filterTable = getFilterTable(TransactionStatus.IN_PAYMENT_SUBMIT.getCode(), transactionStatusColumnName);
        customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        Collection<CustomerBrokerContractSale> contractSalesPendingMerchandise = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.PENDING_MERCHANDISE);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesPendingMerchandise) {
                if (Objects.equals(customerBrokerSale.getTransactionId(), customerBrokerContractSale.getNegotiatiotId())) {
                    customerBrokerSale.setTransactionStatus(TransactionStatus.IN_PENDING_MERCHANDISE);
                    userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);
                    broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW);
                }
            }
        }
    }

    /**
     * IN_MERCHANDISE_SUBMIT -> COMPLETED
     * <p/>
     * Se debe enviar un Broadcast para actualizar la UI
     *
     * @param transactionStatusColumnName the Transaction Status column name
     * @param brokerWalletPublicKey       the broker wallet public key
     *
     * @throws DatabaseOperationException
     * @throws InvalidParameterException
     * @throws CantGetListCustomerBrokerContractSaleException
     * @throws CantCloseContractException
     * @throws MissingCustomerBrokerSaleDataException
     */
    private void changeTransactionStatusFromInMerchandiseSubmitToCompleted(String transactionStatusColumnName, String brokerWalletPublicKey) throws FermatException {
        DatabaseTableFilter filterTable = getFilterTable(TransactionStatus.IN_MERCHANDISE_SUBMIT.getCode(), transactionStatusColumnName);
        List<CustomerBrokerSale> customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        Collection<CustomerBrokerContractSale> contractSalesCompleted;
        contractSalesCompleted = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.READY_TO_CLOSE);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesCompleted) {
                if (Objects.equals(customerBrokerSale.getTransactionId(), customerBrokerContractSale.getNegotiatiotId())) {

                    System.out.print("\nTEST CONTRACT - USER LEVEL SALE - AGENT - getCustomerBrokerSales()\n");
                    closeContractManager.closeSaleContract(customerBrokerContractSale.getContractId());

                    customerBrokerSale.setTransactionStatus(TransactionStatus.COMPLETED);
                    userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);

                    broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW);
                    broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, brokerWalletPublicKey, CBPBroadcasterConstants.CBW_CONTRACT_COMPLETED_NOTIFICATION);
                }
            }
        }
    }

    /**
     * IN_CONTRACT_SUBMIT -> IN_PAYMENT_SUBMIT:
     * <p/>
     * Se sigue verificando el estatus del contrato hasta que se consiga la realización de un pago.
     * Si se detecta la realización de un pago se procede actulizar el estatus de la transacción y a monitorear la llegada de la mercadería.
     * Se verifica si el broker configuró procesar Restock de manera automática.
     * <p/>
     * Se debe enviar un Broadcast para actualizar la UI
     *
     * @param transactionStatusColumnName the Transaction Status column name
     * @param brokerWalletPublicKey       the broker wallet public key
     *
     * @throws FermatException
     * @throws ParseException
     */
    private void changeTransactionStatusFromInContractSubmitToInPaymentSubmitAndApplyCredit(String transactionStatusColumnName, String brokerWalletPublicKey) throws FermatException, ParseException {
        final DatabaseTableFilter filterTable = getFilterTable(TransactionStatus.IN_CONTRACT_SUBMIT.getCode(), transactionStatusColumnName);
        final List<CustomerBrokerSale> customerBrokerSales = userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.getCustomerBrokerSales(filterTable);
        final Collection<CustomerBrokerContractSale> contractSalesPaymentSubmit = customerBrokerContractSaleManager.getCustomerBrokerContractSaleForStatus(ContractStatus.PAYMENT_SUBMIT);

        for (CustomerBrokerSale customerBrokerSale : customerBrokerSales) {
            for (CustomerBrokerContractSale customerBrokerContractSale : contractSalesPaymentSubmit) {
                final String transactionId = customerBrokerSale.getTransactionId();
                final String negotiationId = customerBrokerContractSale.getNegotiatiotId();

                if (transactionId.equals(negotiationId)) {
                    /* Si se detecta la realización de un pago se procede actulizar el estatus de la transacción y a monitorear
                    la llegada de la mercadería. Se verifica si el broker configuró procesar Restock de manera automática. */

                    applySalePaymentCredit(brokerWalletPublicKey, customerBrokerContractSale, negotiationId);

                    customerBrokerSale.setTransactionStatus(TransactionStatus.IN_PAYMENT_SUBMIT);
                    userLevelBusinessTransactionCustomerBrokerSaleDatabaseDao.saveCustomerBrokerSaleTransactionData(customerBrokerSale);

                    broadcaster.publish(BroadcasterType.UPDATE_VIEW, CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW);
                    broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, brokerWalletPublicKey, CBPBroadcasterConstants.
                            CBW_CONTRACT_CUSTOMER_SUBMITTED_PAYMENT_NOTIFICATION);
                }
            }
        }
    }

    /**
     * Apply a sale credit coming from a payment to the broker wallet and the crypto, bank o cash wallet
     *
     * @param brokerWalletPublicKey the broker wallet public key
     * @param contractSale          the sale contract information
     * @param negotiationId         the negotiation ID associated with the contract
     *
     * @throws FermatException
     * @throws ParseException
     */
    private void applySalePaymentCredit(String brokerWalletPublicKey, CustomerBrokerContractSale contractSale, String negotiationId) throws FermatException, ParseException {

        final NumberFormat numberFormat = NumberFormat.getInstance();
        final CustomerBrokerSaleNegotiation saleNegotiation = customerBrokerSaleNegotiationManager.
                getNegotiationsByNegotiationId(UUID.fromString(negotiationId));

        // Obtengo info de las clausulas de la negociacion
        final Collection<Clause> saleNegotiationClauses = saleNegotiation.getClauses();

        String clauseValue;

        clauseValue = NegotiationClauseHelper.getNegotiationClauseValue(saleNegotiationClauses, ClauseType.EXCHANGE_RATE);
        final BigDecimal priceReference = new BigDecimal(numberFormat.parse(clauseValue).doubleValue());

        clauseValue = NegotiationClauseHelper.getNegotiationClauseValue(saleNegotiationClauses, ClauseType.BROKER_CURRENCY_QUANTITY);
        final BigDecimal amount = new BigDecimal(numberFormat.parse(clauseValue).doubleValue());

        clauseValue = NegotiationClauseHelper.getNegotiationClauseValue(saleNegotiationClauses, ClauseType.BROKER_BANK_ACCOUNT);
        final String bankAccount = NegotiationClauseHelper.getAccountNumberFromString(clauseValue);

        clauseValue = NegotiationClauseHelper.getNegotiationClauseValue(saleNegotiationClauses, ClauseType.CUSTOMER_PAYMENT_METHOD);
        final MoneyType paymentMethod = MoneyType.getByCode(clauseValue);

        final String currencyCode = NegotiationClauseHelper.getNegotiationClauseValue(saleNegotiationClauses, ClauseType.BROKER_CURRENCY);

        //Ejecuto el restock dependiendo del tipo de transferencia a realizar
        switch (paymentMethod) {
            case CRYPTO:
                cryptoMoneyRestockManager.createTransactionRestock(
                        contractSale.getPublicKeyBroker(),
                        CryptoCurrency.getByCode(currencyCode),
                        brokerWalletPublicKey,
                        "reference_wallet", // TODO: obtenerlo de installed wallets
                        amount,
                        "Payment from a Customer",
                        priceReference,
                        OriginTransaction.SALE,
                        contractSale.getContractId(),
                        BlockchainNetworkType.getDefaultBlockchainNetworkType()); //TODO: Revisar de donde saco esto
                break;
            case BANK:
                bankMoneyRestockManager.createTransactionRestock(
                        contractSale.getPublicKeyBroker(),
                        FiatCurrency.getByCode(currencyCode),
                        brokerWalletPublicKey,
                        "banking_wallet", // TODO: obtenerlo de installed wallets
                        bankAccount,
                        amount,
                        "Payment from a Customer",
                        priceReference,
                        OriginTransaction.SALE,
                        contractSale.getContractId());
                break;
            case CASH_ON_HAND:
                cashMoneyRestockManager.createTransactionRestock(
                        contractSale.getPublicKeyBroker(),
                        FiatCurrency.getByCode(currencyCode),
                        brokerWalletPublicKey,
                        "cash_wallet",  // TODO: obtenerlo de installed wallets
                        "cashReference",
                        amount,
                        "Cash on Hand Payment from a Customer",
                        priceReference,
                        OriginTransaction.SALE,
                        contractSale.getContractId());
                break;
            case CASH_DELIVERY:
                cashMoneyRestockManager.createTransactionRestock(
                        contractSale.getPublicKeyBroker(),
                        FiatCurrency.getByCode(currencyCode),
                        brokerWalletPublicKey,
                        "cash_wallet",  // TODO: obtenerlo de installed wallets
                        "cashReference",
                        amount,
                        "Cash Delivery Payment from a Customer",
                        priceReference,
                        OriginTransaction.SALE,
                        contractSale.getContractId());
                break;
        }
    }

    /**
     * Create a table filter object
     *
     * @param valueFilter the value of the filter
     * @param columnValue the column name which is going to be queried
     *
     * @return the filter object
     */
    private DatabaseTableFilter getFilterTable(final String valueFilter, final String columnValue) {
        // I define the filter to search for the public Key
        return new DatabaseTableFilter() {
            @Override
            public void setColumn(String column) {
            }

            @Override
            public void setType(DatabaseFilterType type) {
            }

            @Override
            public void setValue(String value) {
            }

            @Override
            public String getColumn() {
                return columnValue;
            }

            @Override
            public String getValue() {
                return valueFilter;
            }

            @Override
            public DatabaseFilterType getType() {
                return DatabaseFilterType.EQUAL;
            }
        };
    }

    /**
     * Disconnect from database and explicitly set all references to null.
     */
    private void cleanResources() {

    }

    /**
     * Return the market exchange rate for the currency to sell vs USD
     *
     * @param customerCurrency the code of the currency to sell
     *
     * @return the market exchange rate fot this currency vs USD
     *
     * @throws CantGetExchangeRateException
     */
    private float getMarketExchangeRate(String customerCurrency) throws CantGetExchangeRateException {
        //Find out if customerCurrency parameter is a FiatCurrency or a CryptoCurrency
        Currency currency = null;
        try {
            if (FiatCurrency.codeExists(customerCurrency))
                currency = FiatCurrency.getByCode(customerCurrency);
            else if (CryptoCurrency.codeExists(customerCurrency))
                currency = CryptoCurrency.getByCode(customerCurrency);
        } catch (Exception e) {
            throw new CantGetExchangeRateException();
        }

        if (currency == null)
            throw new CantGetExchangeRateException();


        CurrencyPair currencyPair = new CurrencyPairImpl(currency, FiatCurrency.US_DOLLAR);


        //Get saved CER providers in broker wallet
        final String publicKeyWalletCryptoBrokerInstall = "walletPublicKeyTest"; //TODO: Quitar este hardcode luego que se implemente la instalacion de la wallet

        try {
            final CryptoBrokerWallet cryptoBrokerWallet = cryptoBrokerWalletManager.loadCryptoBrokerWallet(publicKeyWalletCryptoBrokerInstall);
            final CryptoBrokerWalletSetting cryptoWalletSetting = cryptoBrokerWallet.getCryptoWalletSetting();
            final List<CryptoBrokerWalletProviderSetting> providerSettings = cryptoWalletSetting.getCryptoBrokerWalletProviderSettings();

            for (CryptoBrokerWalletProviderSetting providerSetting : providerSettings) {

                UUID providerId = providerSetting.getPlugin();
                CurrencyExchangeRateProviderManager providerReference = currencyExchangeRateProviderFilter.getProviderReference(providerId);
                if (providerReference.isCurrencyPairSupported(currencyPair)) {
                    ExchangeRate currentExchangeRate = providerReference.getCurrentExchangeRate(currencyPair);
                    return (float) currentExchangeRate.getPurchasePrice();
                }
            }
        } catch (Exception e) { /*Continue*/ }

        //Find any CER provider which can obtain the needed currencyPair, regardless of it not being set up in the broker wallet
        try {
            final Collection<CurrencyExchangeRateProviderManager> providers = currencyExchangeRateProviderFilter.
                    getProviderReferencesFromCurrencyPair(currencyPair);

            for (CurrencyExchangeRateProviderManager provider : providers) {
                try {
                    ExchangeRate currentExchangeRate = provider.getCurrentExchangeRate(currencyPair);
                    return (float) currentExchangeRate.getPurchasePrice();

                } catch (UnsupportedCurrencyPairException ignore) { /*Continue*/ }
            }
        } catch (CantGetProviderException ignore) { /*Continue*/ }

        throw new CantGetExchangeRateException();
    }
}
