package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_online_payment.developer.bitdubai.version_1.structure.CustomerOnlinePaymentTransactionManagerTest;

import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractTransactionStatus;
import com.bitdubai.fermat_cbp_api.all_definition.exceptions.UnexpectedResultReturnedFromDatabaseException;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchaseManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_purchase.interfaces.CustomerBrokerPurchaseNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.interfaces.TransactionTransmissionManager;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_online_payment.developer.bitdubai.version_1.database.CustomerOnlinePaymentBusinessTransactionDao;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_online_payment.developer.bitdubai.version_1.structure.CustomerOnlinePaymentTransactionManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by alexander jimenez (alex_jimenez76@hotmail.com) on 18/02/16.
 */
public class getContractTransactionStatusTest {

    CustomerOnlinePaymentTransactionManager customerOnlinePaymentTransactionManager;
    @Mock
    CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager;
    @Mock
    CustomerOnlinePaymentBusinessTransactionDao customerOnlinePaymentBusinessTransactionDao;
    @Mock
    ErrorManager errorManager;
    @Mock
    TransactionTransmissionManager transactionTransmissionManager;
    @Mock
    CustomerBrokerPurchaseNegotiationManager customerBrokerPurchaseNegotiationManager;
    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        when(customerOnlinePaymentBusinessTransactionDao.getContractTransactionStatus(anyString())).
                thenReturn(ContractTransactionStatus.ACK_OFFLINE_PAYMENT);
    }

    @Test
    public void getContractTransactionStatusTest_Should_Return_Not_Null() throws Exception{
        customerOnlinePaymentTransactionManager = new CustomerOnlinePaymentTransactionManager(customerBrokerContractPurchaseManager,
                customerOnlinePaymentBusinessTransactionDao,
                transactionTransmissionManager,
                customerBrokerPurchaseNegotiationManager,
                errorManager);
        assertEquals(ContractTransactionStatus.ACK_OFFLINE_PAYMENT,
                customerOnlinePaymentTransactionManager.getContractTransactionStatus("Test"));
    }

    @Test(expected = UnexpectedResultReturnedFromDatabaseException.class)
    public void getContractTransactionStatusTest_Should_Throw_UnexpectedResultReturnedFromDatabaseException() throws Exception{
        customerOnlinePaymentTransactionManager = new CustomerOnlinePaymentTransactionManager(
                customerBrokerContractPurchaseManager,
                customerOnlinePaymentBusinessTransactionDao,
                transactionTransmissionManager,
                customerBrokerPurchaseNegotiationManager,
                errorManager);
        customerOnlinePaymentTransactionManager.getContractTransactionStatus(null);
    }

    @Test(expected = UnexpectedResultReturnedFromDatabaseException.class)
    public void getContractTransactionStatusTest_Should_Throw_Generic_UnexpectedResultReturnedFromDatabaseException() throws Exception{
        customerOnlinePaymentTransactionManager = new CustomerOnlinePaymentTransactionManager(
                null,null,null,null,errorManager);
        customerOnlinePaymentTransactionManager.getContractTransactionStatus("Test");
    }
}
