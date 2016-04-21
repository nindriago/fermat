package com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.VaultType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.TransactionSender;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransaction;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoTransactionType;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.BlockchainDownloadProgress;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.BroadcastStatus;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.BlockchainConnectionStatus;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantBroadcastTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantCancellBroadcastTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetActiveBlockchainNetworkTypeException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetBlockchainConnectionStatusException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetBlockchainDownloadProgress;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetBroadcastStatusException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetCryptoTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetTransactionCryptoStatusException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantGetTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantMonitorBitcoinNetworkException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantStoreBitcoinTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.enums.CryptoVaults;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.UTXOProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

/**
 * Created by rodrigo on 9/30/15.
 */
public interface BitcoinNetworkManager extends TransactionSender<CryptoTransaction> {

    /**
     * Starts monitoring the network active networks with the list of keys passed.
     * @param keyList
     * @throws CantMonitorBitcoinNetworkException
     */
    void monitorNetworkFromKeyList(CryptoVaults vault, List<BlockchainNetworkType> blockchainNetworkTypes,List<ECKey> keyList) throws CantMonitorBitcoinNetworkException;

    /**
     * Gests all the CryptoTransactions that matchs this transaction Hash
     * @param txHash
     * @return
     * @throws CantGetCryptoTransactionException
     */
    List<CryptoTransaction> getCryptoTransactions(String txHash) throws CantGetCryptoTransactionException;

    /**
     * gets the current Crypto Status for the specified Transaction ID
     * @param txHash the Bitcoin transaction hash
     * @return the last crypto status
     * @throws CantGetTransactionCryptoStatusException
     */
    CryptoStatus getCryptoStatus(String txHash) throws CantGetTransactionCryptoStatusException;

    /**********************************************Broadcasting functionality ********************************************/

    /**
     * Stores a Bitcoin Transaction in the CryptoNetwork to be broadcasted later
     * @param blockchainNetworkType
     * @param tx
     * @param transactionId
     * @param commit specifies if we commit this transaction or there is a chance of rolling back.
     * @throws CantStoreBitcoinTransactionException
     */
    void storeBitcoinTransaction(BlockchainNetworkType blockchainNetworkType, Transaction tx, UUID transactionId, boolean commit) throws CantStoreBitcoinTransactionException;

    /**
     * Broadcast a well formed, commited and signed transaction into the network.
     * @param txHash
     * @throws CantBroadcastTransactionException
     */
    void broadcastTransaction (String txHash) throws CantBroadcastTransactionException;


    /**
     * Will mark the passed transaction as cancelled, and it won't be broadcasted again.
     * @param txHash
     * @throws CantCancellBroadcastTransactionException
     */
    void cancelBroadcast(String txHash) throws CantCancellBroadcastTransactionException;

    /**
     * Returns the broadcast Status for a specified transaction.
     * @param txHash
     * @return
     * @throws CantGetBroadcastStatusException
     */
    BroadcastStatus getBroadcastStatus (String txHash) throws CantGetBroadcastStatusException;

    /**
     * Get the bitcoin transaction stored by the CryptoNetwork
     * @param blockchainNetworkType the network type
     * @param transactionHash the transsaction hash
     * @return the bitcoin transaction
     */
    Transaction getBitcoinTransaction(BlockchainNetworkType blockchainNetworkType, String transactionHash);


    /**
     * Get the bitcoin transactions stored by the CryptoNetwork
     * @param blockchainNetworkType the network type
     * @return the bitcoin transaction
     */
    List<Transaction> getBitcoinTransactions(BlockchainNetworkType blockchainNetworkType);


    /**
     * Will get the BlockchainConnectionStatus for the specified network.
     * @param blockchainNetworkType the Network type we won't to get info from. If the passed network is not currently activated,
     * then we will receive null.
     * @return BlockchainConnectionStatus with information of amount of peers currently connected, etc.
     * @exception CantGetBlockchainConnectionStatusException
     */
    BlockchainConnectionStatus getBlockchainConnectionStatus(BlockchainNetworkType blockchainNetworkType)  throws CantGetBlockchainConnectionStatusException;

    /**
     * Gets the active networks running on the Crypto Network
     * @return the list of active networks {MainNet, TestNet and RegTest}
     * @throws CantGetActiveBlockchainNetworkTypeException
     */
    List<BlockchainNetworkType> getActivesBlockchainNetworkTypes() throws CantGetActiveBlockchainNetworkTypeException;

    /**
     * Gets a stored CryptoTransaction in whatever network.
     * @param txHash the transaction hash of the transaction
     * @param cryptoTransactionType the type of CryptoTransaction we are looking for
     * @param toAddress the address this transaction was sent to.
     * @return the CryptoTransaction with the latest cryptoStatus
     * @throws CantGetCryptoTransactionException
     */
    CryptoTransaction getCryptoTransaction(String txHash, @Nullable CryptoTransactionType cryptoTransactionType, @Nullable CryptoAddress toAddress) throws CantGetCryptoTransactionException;


    /**
     * Gets the list of stored CryptoTransactions for the specified network type
     * @param blockchainNetworkType the network type to get the transactions from.
     * @param addressTo the AddressTo of the transaction we are looking for.
     * @param cryptoTransactionType if it is an incoming or outgoing transaction
     * @return the list of Crypto Transaction that match the criteria
     * @throws CantGetCryptoTransactionException
     */
    List<CryptoTransaction> getCryptoTransactions(BlockchainNetworkType blockchainNetworkType, CryptoAddress addressTo, @Nullable CryptoTransactionType cryptoTransactionType) throws CantGetCryptoTransactionException;

    /**
     * Based on the passed transaction chain of Transactions hashes and Blocks hashes, determines the entire path
     * of the chain until the Genesis Transaction is reached.
     * The genesis Transaction will be the first transaction in the map.
     * @param blockchainNetworkType the active network we are getting this info from. Defaults to BlockchainNetworkType.getDefaultBlockchainNetworkType()
     * @param transactionChain a Map with the form TransactionHash / BlockHash
     * @return all the CryptoTransactions originated at the genesis transaction
     * @throws CantGetCryptoTransactionException
     */
    CryptoTransaction getGenesisCryptoTransaction(@Nullable BlockchainNetworkType blockchainNetworkType, LinkedHashMap<String, String> transactionChain) throws CantGetCryptoTransactionException;


    /**
     * Based on the Parent trasaction passed, will return all the CryptoTransactions that are a direct descendant of this parent.
     * Only if it is a locally stored transaction
     * @param parentTransactionHash the hash of the parent trasaction
     * @return the list of CryptoTransactions that are a direct child of the parent.
     * @throws CantGetCryptoTransactionException
     */
    List<CryptoTransaction> getChildTransactionsFromParent(String parentTransactionHash) throws CantGetCryptoTransactionException;


    /**
     * Gets the download progress from the specified network
     * @param blockchainNetworkType The network type we want to know the download progress
     * @return the BlockchainDownloadProgress class which includes information about pending blocks, total blocks, etc.
     * @throws CantGetBlockchainDownloadProgress
     */
    BlockchainDownloadProgress getBlockchainDownloadProgress(BlockchainNetworkType blockchainNetworkType) throws CantGetBlockchainDownloadProgress;


}
