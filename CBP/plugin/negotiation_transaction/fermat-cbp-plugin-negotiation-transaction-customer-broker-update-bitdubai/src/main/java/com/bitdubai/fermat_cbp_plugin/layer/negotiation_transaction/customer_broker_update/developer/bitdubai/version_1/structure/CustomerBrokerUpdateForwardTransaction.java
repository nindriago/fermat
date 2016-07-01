package com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationTransactionStatus;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_update.interfaces.CustomerBrokerUpdate;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.database.CustomerBrokerUpdateNegotiationTransactionDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.exceptions.CantProcessPendingConfirmTransactionException;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.exceptions.CantRegisterCustomerBrokerUpdateNegotiationTransactionException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Yordin Alayn on 03.04.16.
 */
public class CustomerBrokerUpdateForwardTransaction {

    CustomerBrokerUpdateNegotiationTransactionDatabaseDao   customerBrokerUpdateNegotiationTransactionDatabaseDao;
    ErrorManager                                            errorManager;
    PluginVersionReference                                  pluginVersionReference;
    Map<UUID,Integer>                                       transactionSend;

    boolean                                                 isValidateSend = Boolean.FALSE;

    public CustomerBrokerUpdateForwardTransaction(
            CustomerBrokerUpdateNegotiationTransactionDatabaseDao   customerBrokerUpdateNegotiationTransactionDatabaseDao,
            ErrorManager                                            errorManager,
            PluginVersionReference                                  pluginVersionReference,
            Map<UUID,Integer>                                       transactionSend
    ){
        this.customerBrokerUpdateNegotiationTransactionDatabaseDao  = customerBrokerUpdateNegotiationTransactionDatabaseDao;
        this.errorManager                                           = errorManager;
        this.pluginVersionReference                                 = pluginVersionReference;
        this.transactionSend                                        = transactionSend;
    }

    public void pendingToConfirmtTransaction() throws CantProcessPendingConfirmTransactionException{

        try {

//            System.out.print("\n\n**** X) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER UPDATE - FORWARD TRANSACTION****\n");

            UUID transactionId;
            int numberSend;

            List<CustomerBrokerUpdate> negotiationList = customerBrokerUpdateNegotiationTransactionDatabaseDao.getPendingToConfirmTransactionNegotiation();
            if(!negotiationList.isEmpty()) {
                for (CustomerBrokerUpdate negotiationTransaction : negotiationList) {

                    transactionId = negotiationTransaction.getTransactionId();

                    System.out.print("\n\n**** X) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER UPDATE - AGENT - transactionSend ****\n" +
                            "\n - transaction id:"+transactionId+
                            "\n - numberSend: "+Integer.toString(getNumberSend(transactionSend, transactionId))+
                            "\n");

                    if (negotiationTransaction.getStatusTransaction().getCode().equals(NegotiationTransactionStatus.SENDING_NEGOTIATION.getCode())) {

                        System.out.print("\n\n**** X) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER UPDATE - FORWARD TRANSACTION - pendingToConfirmtTransaction" + transactionId + " ****\n");

                        numberSend = getNumberSend(transactionSend, transactionId);

                        isValidateSend(transactionId, numberSend);

                        if(isValidateSend) {
                            System.out.print("\n\n**** X) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER UPDATE - FORWARD TRANSACTION - pendingToConfirmtTransaction - SEND AGAIN: " + numberSend + " ****\n");
                            customerBrokerUpdateNegotiationTransactionDatabaseDao.updateStatusRegisterCustomerBrokerUpdateNegotiationTranasction(
                                    transactionId,
                                    NegotiationTransactionStatus.PENDING_SUBMIT);

                        }

                        transactionSend.put(transactionId, numberSend);

                    }
                }
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantProcessPendingConfirmTransactionException(e.getMessage(), FermatException.wrapException(e),"Sending Negotiation","UNKNOWN FAILURE.");
        }
    }

    public Map<UUID,Integer> getTransactionSend(){ return transactionSend; }

    private int getNumberSend(Map<UUID,Integer> transactionSend, UUID transactionId){

        int numberSend = 0;

        if (transactionSend.get(transactionId) != null) numberSend = transactionSend.get(transactionId);

        numberSend++;

        return numberSend;

    }

    private void isValidateSend(UUID transactionId, int numberSend) throws CantProcessPendingConfirmTransactionException{

        try {

            isValidateSend = Boolean.FALSE;
            int numberToSend = 3;

            if ((numberSend <= numberToSend) ||
                    (numberSend > numberToSend*2 && numberSend <= numberToSend*3) ||
                    (numberSend > numberToSend*4 && numberSend <= numberToSend*5))
                isValidateSend = Boolean.TRUE;

            if (numberSend > numberToSend*5) {

                customerBrokerUpdateNegotiationTransactionDatabaseDao.updateStatusRegisterCustomerBrokerUpdateNegotiationTranasction(
                        transactionId,
                        NegotiationTransactionStatus.REJECTED_NEGOTIATION);

            }

        } catch (CantRegisterCustomerBrokerUpdateNegotiationTransactionException e){
            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantProcessPendingConfirmTransactionException(e.getMessage(), FermatException.wrapException(e),"Sending Negotiation","UNKNOWN FAILURE.");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(pluginVersionReference, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantProcessPendingConfirmTransactionException(e.getMessage(), FermatException.wrapException(e),"Sending Negotiation","UNKNOWN FAILURE.");
        }

    }

}
