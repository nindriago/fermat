package com.bitdubai.reference_niche_wallet.loss_protected_wallet.app_connection;

import com.bitdubai.fermat_android_api.engine.NotificationPainter;
import com.bitdubai.fermat_ccp_api.all_definition.util.WalletUtils;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CantListReceivePaymentRequestException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedPaymentRequest;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletTransaction;

import java.util.UUID;

/**
 * Created by natalia on 22/02/16.
 */
public class LossProtectedWalletBuildNotificationPainter {

    public static NotificationPainter getNotification(LossProtectedWallet moduleManager,String code,String walletPublicKey)
    {
        NotificationPainter notification = null;
        try {


            LossProtectedWalletTransaction transaction;
            LossProtectedPaymentRequest paymentRequest;
            String loggedIntraUserPublicKey;

                String[] params = code.split("_");
                String notificationType = params[0];
                String transactionId = params[1];
                //find last transaction
                switch (notificationType){
                    case "TRANSACTIONARRIVE":
                        if(moduleManager != null){
                            loggedIntraUserPublicKey = moduleManager.getActiveIdentities().get(0).getPublicKey();
                                transaction= moduleManager.getTransaction(UUID.fromString(transactionId), walletPublicKey,loggedIntraUserPublicKey);

                            notification = new LossProtectedWalletNotificationPainter("Received money", transaction.getInvolvedActor().getName() + " send "+ WalletUtils.formatBalanceString(transaction.getAmount()) + " BTC","","",true,walletPublicKey);

                        }else{
                            notification = new LossProtectedWalletNotificationPainter("Received money", "BTC Arrived","","",true,walletPublicKey);
                        }
                        break;
                    case "TRANSACTION_REVERSE":
                        if(moduleManager != null) {
                            loggedIntraUserPublicKey = moduleManager.getActiveIdentities().get(0).getPublicKey();
                            transaction = moduleManager.getTransaction(UUID.fromString(transactionId), walletPublicKey, loggedIntraUserPublicKey);
                            notification = new LossProtectedWalletNotificationPainter("Sent Transaction reversed", "Sending " + WalletUtils.formatBalanceString(transaction.getAmount()) + " BTC could not be completed.", "", "",true,walletPublicKey);
                        }else
                        {
                            notification = new LossProtectedWalletNotificationPainter("Sent Transaction reversed","Your last Sending could not be completed.","","",true,walletPublicKey);
                        }
                        break;


                    case "PAYMENTREQUEST":
                        if(moduleManager != null){

                            paymentRequest = moduleManager.getPaymentRequest(UUID.fromString(transactionId));
                            notification = new LossProtectedWalletNotificationPainter("Received new Payment Request","You have received a Payment Request, for" + WalletUtils.formatBalanceString(paymentRequest.getAmount()) + " BTC","","",true,walletPublicKey);
                        }
                        else
                        {
                            notification = new LossProtectedWalletNotificationPainter("Received new Payment Request","You have received a new Payment Request.","","",true,walletPublicKey);
                        }
                        break;

                    case "PAYMENTDENIED":
                        if(moduleManager != null){
                            paymentRequest = moduleManager.getPaymentRequest(UUID.fromString(transactionId));
                            notification = new LossProtectedWalletNotificationPainter("Payment Request deny","Your Payment Request, for " + WalletUtils.formatBalanceString(paymentRequest.getAmount()) + " BTC was deny.","","",true,walletPublicKey);
                        }
                        else
                        {
                            notification = new LossProtectedWalletNotificationPainter("Payment Request deny","Your Payment Request was deny.","","",true,walletPublicKey);
                        }
                        break;

                    case "PAYMENTERROR":
                        if(moduleManager != null){
                            paymentRequest = moduleManager.getPaymentRequest(UUID.fromString(transactionId));
                            notification = new LossProtectedWalletNotificationPainter("Payment Request reverted","Your Payment Request, for " + WalletUtils.formatBalanceString(paymentRequest.getAmount()) + " BTC was reverted.","","",true,walletPublicKey);
                        }
                        else
                        {
                            notification = new LossProtectedWalletNotificationPainter("Payment Request reverted","Your Last Payment Request was reverted.","","",true,walletPublicKey);
                        }
                        break;

                }

        } catch (CantListReceivePaymentRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notification;
    }
}
