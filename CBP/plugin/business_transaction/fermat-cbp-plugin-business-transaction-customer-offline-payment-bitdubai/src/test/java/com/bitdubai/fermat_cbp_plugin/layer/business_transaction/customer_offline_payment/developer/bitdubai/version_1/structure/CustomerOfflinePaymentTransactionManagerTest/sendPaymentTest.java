package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_offline_payment.developer.bitdubai.version_1.structure.CustomerOfflinePaymentTransactionManagerTest;

import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.exceptions.CantSendPaymentException;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.common.mocks.PurchaseNegotiationOnlineMock;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchase;
import com.bitdubai.fermat_cbp_api.layer.contract.customer_broker_purchase.interfaces.CustomerBrokerContractPurchaseManager;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_purchase.interfaces.CustomerBrokerPurchaseNegotiationManager;
import com.bitdubai.fermat_cbp_api.layer.network_service.transaction_transmission.interfaces.TransactionTransmissionManager;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_offline_payment.developer.bitdubai.version_1.database.CustomerOfflinePaymentBusinessTransactionDao;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_offline_payment.developer.bitdubai.version_1.structure.CustomerOfflinePaymentTransactionManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by alexander jimenez (alex_jimenez76@hotmail.com) on 16/02/16.
 */
public class sendPaymentTest {
    CustomerOfflinePaymentTransactionManager customerOfflinePaymentTransactionManager;
    @Mock
    CustomerBrokerContractPurchaseManager customerBrokerContractPurchaseManager;
    @Mock
    CustomerOfflinePaymentBusinessTransactionDao customerOfflinePaymentBusinessTransactionDao;
    @Mock
    ErrorManager errorManager;
    @Mock
    TransactionTransmissionManager transactionTransmissionManager;
    @Mock
    CustomerBrokerPurchaseNegotiationManager customerBrokerPurchaseNegotiationManager;
    @Mock
    CustomerBrokerContractPurchase customerBrokerContractPurchase;
    PurchaseNegotiationOnlineMock purchaseNegotiationOnlineMock = new PurchaseNegotiationOnlineMock();
    //@Mock
    //Collection<Clause> collection;

    //@Mock
    //Iterator<Clause> iterator;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        customerOfflinePaymentTransactionManager = new CustomerOfflinePaymentTransactionManager(
                customerBrokerContractPurchaseManager,
                customerOfflinePaymentBusinessTransactionDao,
                errorManager);
        setUpGeneralMockitoRules();
    }
    public void setUpGeneralMockitoRules() throws Exception{
        when(customerBrokerContractPurchaseManager.getCustomerBrokerContractPurchaseForContractId(anyString())).
                thenReturn(customerBrokerContractPurchase);
        when(customerBrokerContractPurchase.getNegotiatiotId()).thenReturn("00000000-0000-0000-C000-000000000046");
        UUID negotiationUUID=UUID.fromString("00000000-0000-0000-C000-000000000046");
        when(customerBrokerPurchaseNegotiationManager.getNegotiationsByNegotiationId(negotiationUUID)).
                thenReturn(purchaseNegotiationOnlineMock);


    }

    @Test
    public void sendPaymentTest() throws Exception{
        customerOfflinePaymentTransactionManager.sendPayment("contractHash");
    }

    //ObjectNotSetException
    @Test(expected = CantSendPaymentException.class)
    public void sendPaymentTest_Should_Return_Exception() throws Exception{

        customerOfflinePaymentTransactionManager.sendPayment(null);
    }
    //Generic Exception
    @Test(expected = CantSendPaymentException.class)
    public void sendPaymentTest_Should_Throw_CantSendPaymentException() throws Exception{
        customerOfflinePaymentTransactionManager = new CustomerOfflinePaymentTransactionManager(null,
                customerOfflinePaymentBusinessTransactionDao,
                errorManager);
        customerOfflinePaymentTransactionManager.sendPayment("");
    }
}
