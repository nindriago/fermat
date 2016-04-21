package org.fermat.fermat_dap_plugin.layer.wallet.asset.issuer.developer.version_1.structure.functional;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAsset;
import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import org.fermat.fermat_dap_api.layer.all_definition.enums.AssetCurrentStatus;
import org.fermat.fermat_dap_api.layer.all_definition.enums.AssetMovementType;
import org.fermat.fermat_dap_api.layer.all_definition.util.ActorUtils;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuerManager;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUserManager;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPoint;
import org.fermat.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPointManager;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.exceptions.CantGetAssetStatisticException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantGetDigitalAssetFromLocalStorageException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.RecordsNotFoundException;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.exceptions.CantInitializeAssetIssuerWalletException;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.exceptions.CantSaveStatisticException;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWallet;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletBalance;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletTransaction;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletTransactionSummary;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetStatistic;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.WalletUtilities;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.enums.BalanceType;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.enums.TransactionType;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantCreateWalletException;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantFindTransactionException;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantGetActorTransactionSummaryException;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantGetTransactionsException;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantStoreMemoException;

import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by franklin on 27/09/15.
 */
public class AssetIssuerWalletImpl implements AssetIssuerWallet {
    public static final String PATH_DIRECTORY = "asset-issuer-swap/";
    private static final String ASSET_ISSUER_WALLET_FILE_NAME = "walletsIds";

    /**
     * AssetIssuerWallet member variables.
     */
    private Database database;
    private List<UUID> createdWallets;

    {
        createdWallets = new ArrayList<>();
    }

    private org.fermat.fermat_dap_plugin.layer.wallet.asset.issuer.developer.version_1.structure.database.AssetIssuerWalletDao assetIssuerWalletDao;
    private ErrorManager errorManager;

    private PluginDatabaseSystem pluginDatabaseSystem;

    private PluginFileSystem pluginFileSystem;

    private UUID pluginId;

    private final ActorAssetUserManager actorAssetUserManager;

    private final ActorAssetRedeemPointManager actorAssetRedeemPointManager;

    private final ActorAssetIssuerManager assetIssuerManager;

    private Broadcaster broadcaster;

    public AssetIssuerWalletImpl(ErrorManager errorManager,
                                 PluginDatabaseSystem pluginDatabaseSystem,
                                 PluginFileSystem pluginFileSystem,
                                 UUID pluginId,
                                 ActorAssetUserManager actorAssetUserManager,
                                 ActorAssetRedeemPointManager actorAssetRedeemPointManager,
                                 ActorAssetIssuerManager assetIssuerManager,
                                 Broadcaster broadcaster) {
        this.errorManager = errorManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
        this.actorAssetUserManager = actorAssetUserManager;
        this.actorAssetRedeemPointManager = actorAssetRedeemPointManager;
        this.assetIssuerManager = assetIssuerManager;
        this.broadcaster = broadcaster;
    }

    public void initialize(UUID walletId) throws CantInitializeAssetIssuerWalletException {
        if (walletId == null)
            throw new CantInitializeAssetIssuerWalletException("InternalId is null", null, "Parameter walletId is null", "loadWallet didn't find the asociated id");

        try {
            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, walletId.toString());
            assetIssuerWalletDao = new org.fermat.fermat_dap_plugin.layer.wallet.asset.issuer.developer.version_1.structure.database.AssetIssuerWalletDao(database, pluginFileSystem, pluginId, actorAssetUserManager, assetIssuerManager, actorAssetRedeemPointManager);
        } catch (CantOpenDatabaseException cantOpenDatabaseException) {
            throw new CantInitializeAssetIssuerWalletException("I can't open database", cantOpenDatabaseException, "WalletId: " + walletId.toString(), "");
        } catch (DatabaseNotFoundException databaseNotFoundException) {
            throw new CantInitializeAssetIssuerWalletException("Database does not exists", databaseNotFoundException, "WalletId: " + walletId.toString(), "");
        } catch (Exception exception) {
            throw new CantInitializeAssetIssuerWalletException(CantInitializeAssetIssuerWalletException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    public UUID create(String walletPublicKey, BlockchainNetworkType networkType) throws CantCreateWalletException {
        try {
            // TODO: Until the Wallet MAnager create the wallets, we will use this internal id
            //       We need to change this in the near future
            UUID internalWalletId = WalletUtilities.constructWalletId(walletPublicKey, networkType);
            createWalletDatabase(internalWalletId);
            PluginTextFile walletAssetIssuerFile = createAssetIssuerWalletFile();
            loadAssetIssuerWalletList(walletAssetIssuerFile);
            createdWallets.add(internalWalletId);
            persistAssetIssuerWallet(walletAssetIssuerFile);
            return internalWalletId;
        } catch (CantCreateWalletException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new CantCreateWalletException(CantCreateWalletException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }

    }

    private PluginTextFile createAssetIssuerWalletFile() throws CantCreateWalletException {
        try {
            return pluginFileSystem.getTextFile(pluginId, "", ASSET_ISSUER_WALLET_FILE_NAME, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
        } catch (CantCreateFileException cantCreateFileException) {
            throw new CantCreateWalletException("File could not be created (?)", cantCreateFileException, "File Name: " + ASSET_ISSUER_WALLET_FILE_NAME, "");
        } catch (FileNotFoundException e) {
            throw new CantCreateWalletException("File could not be found", e, "File Name: " + ASSET_ISSUER_WALLET_FILE_NAME, "");
        }
    }

    private void loadAssetIssuerWalletList(final PluginTextFile walletListFile) throws CantCreateWalletException {
        try {
            walletListFile.loadFromMedia();
            for (String stringWalletId : walletListFile.getContent().split(";")) {
                if (!stringWalletId.isEmpty()) {
                    createdWallets.add(UUID.fromString(stringWalletId));
                }
            }
        } catch (CantLoadFileException exception) {
            throw new CantCreateWalletException("Can't load file content from media", exception, "", "");
        }
    }

    private void createWalletDatabase(final UUID internalWalletId) throws CantCreateWalletException {
        try {
            org.fermat.fermat_dap_plugin.layer.wallet.asset.issuer.developer.version_1.structure.database.AssetIssuerWalletDatabaseFactory databaseFactory = new org.fermat.fermat_dap_plugin.layer.wallet.asset.issuer.developer.version_1.structure.database.AssetIssuerWalletDatabaseFactory(pluginDatabaseSystem);
            database = databaseFactory.createDatabase(this.pluginId, internalWalletId);
        } catch (CantCreateDatabaseException cantCreateDatabaseException) {
            throw new CantCreateWalletException("Database could not be created", cantCreateDatabaseException, "internalWalletId: " + internalWalletId.toString(), "");
        }
    }

    private void persistAssetIssuerWallet(final PluginTextFile pluginTextFile) throws CantCreateWalletException {
        StringBuilder stringBuilder = new StringBuilder(createdWallets.size() * 72);
        for (UUID walletId : createdWallets) {
            stringBuilder.append(walletId).append(";");
        }
        pluginTextFile.setContent(stringBuilder.toString());
        try {
            pluginTextFile.persistToMedia();
        } catch (CantPersistFileException cantPersistFileException) {
            throw new CantCreateWalletException("Could not persist in file", cantPersistFileException, "stringBuilder: " + stringBuilder.toString(), "");
        }
    }

    @Override
    public AssetIssuerWalletBalance getBalance() throws CantGetTransactionsException {
        try {
            return new AssetIssuerWallletBalanceImpl(assetIssuerWalletDao, broadcaster);
        } catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw new CantGetTransactionsException(CantGetTransactionsException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public List<AssetIssuerWalletTransaction> getTransactionsAll(BalanceType balanceType, TransactionType transactionType, String assetPublicKey) throws CantGetTransactionsException {
        try {
            return assetIssuerWalletDao.listsTransactionsByAssetsAll(balanceType, transactionType, assetPublicKey);
        } catch (CantGetTransactionsException exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw exception;
        } catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw new CantGetTransactionsException(CantGetTransactionsException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public List<AssetIssuerWalletTransaction> getTransactions(BalanceType balanceType, TransactionType transactionType, int max, int offset, String assetPublicKey) throws CantGetTransactionsException {
        try {
            return assetIssuerWalletDao.listsTransactionsByAssets(balanceType, transactionType, max, offset, assetPublicKey);
        } catch (CantGetTransactionsException exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw exception;
        } catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw new CantGetTransactionsException(CantGetTransactionsException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public List<AssetIssuerWalletTransaction> getAvailableTransactions(String assetPublicKey) throws CantGetTransactionsException {
        List<AssetIssuerWalletTransaction> allCreditAvailable = getTransactionsAll(BalanceType.AVAILABLE, TransactionType.CREDIT, assetPublicKey);
        List<AssetIssuerWalletTransaction> alldebitAvailable = getTransactionsAll(BalanceType.AVAILABLE, TransactionType.DEBIT, assetPublicKey);
        for (AssetIssuerWalletTransaction transaction : alldebitAvailable) {
            allCreditAvailable.remove(transaction);
        }
        return allCreditAvailable;
    }

    @Override
    public List<AssetIssuerWalletTransaction> getTransactionsByActor(String actorPublicKey, BalanceType balanceType, int max, int offset) throws CantGetTransactionsException {
        try {
            return assetIssuerWalletDao.getTransactionsByActor(actorPublicKey, balanceType, max, offset);
        } catch (CantGetTransactionsException exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw exception;
        } catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw new CantGetTransactionsException(CantGetTransactionsException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public List<AssetIssuerWalletTransaction> gettLastActorTransactionsByTransactionType(BalanceType balanceType, TransactionType transactionType, int max, int offset) throws CantGetTransactionsException {
        try {
            return assetIssuerWalletDao.getTransactionsByTransactionType(transactionType, max, offset);
        } catch (CantGetTransactionsException exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw exception;
        } catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw new CantGetTransactionsException(CantGetTransactionsException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public void setTransactionDescription(UUID transactionID, String description) throws CantFindTransactionException, CantStoreMemoException {
        try {
            assetIssuerWalletDao.updateMemoField(transactionID, description);
        } catch (CantStoreMemoException exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw exception;
        } catch (Exception exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(exception));
            throw new CantStoreMemoException(CantStoreMemoException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public AssetIssuerWalletTransactionSummary getActorTransactionSummary(String actorPublicKey, BalanceType balanceType) throws CantGetActorTransactionSummaryException {
        return null;
    }

    @Override
    public List<AssetIssuerWalletTransaction> getTransactionsAssetAll(String assetPublicKey) throws CantGetTransactionsException {
        List<AssetIssuerWalletTransaction> assetIssuerWalletTransactions;
        assetIssuerWalletTransactions = assetIssuerWalletDao.distributeAssets(assetPublicKey);
        return assetIssuerWalletTransactions;
    }


    @Override
    public List<AssetIssuerWalletTransaction> getTransactionsForDisplay(String assetPublicKey) throws CantGetTransactionsException {
        List<AssetIssuerWalletTransaction> creditAvailable = getTransactionsAll(BalanceType.AVAILABLE, TransactionType.CREDIT, assetPublicKey);
        List<AssetIssuerWalletTransaction> creditBook = getTransactionsAll(BalanceType.BOOK, TransactionType.CREDIT, assetPublicKey);
        List<AssetIssuerWalletTransaction> debitAvailable = getTransactionsAll(BalanceType.AVAILABLE, TransactionType.DEBIT, assetPublicKey);
        List<AssetIssuerWalletTransaction> debitBook = getTransactionsAll(BalanceType.BOOK, TransactionType.DEBIT, assetPublicKey);
        List<AssetIssuerWalletTransaction> toReturn = new ArrayList<>();
        toReturn.addAll(getCreditsForDisplay(creditAvailable, creditBook));
        toReturn.addAll(getDebitsForDisplay(debitAvailable, debitBook));
        Collections.sort(toReturn, new Comparator<AssetIssuerWalletTransaction>() {
            @Override
            public int compare(AssetIssuerWalletTransaction o1, AssetIssuerWalletTransaction o2) {
                return (int) (o2.getTimestamp() - o1.getTimestamp());
            }
        });
        return toReturn;
    }

    private List<AssetIssuerWalletTransaction> getCreditsForDisplay(List<AssetIssuerWalletTransaction> available, List<AssetIssuerWalletTransaction> book) {
        for (AssetIssuerWalletTransaction transaction : book) {
            if (!available.contains(transaction)) {
                available.add(transaction);
            }
        }
        return available;
    }

    private List<AssetIssuerWalletTransaction> getDebitsForDisplay(List<AssetIssuerWalletTransaction> available, List<AssetIssuerWalletTransaction> book) {
        Collections.reverse(available);
        for (AssetIssuerWalletTransaction transaction : book) {
            if (available.contains(transaction)) {
                available.remove(transaction); //YES, THIS IS NECESSARY.
            }
            available.add(transaction);
        }
        return available;
    }

    @Override
    public DigitalAssetMetadata getDigitalAssetMetadata(String transactionHash) throws CantGetDigitalAssetFromLocalStorageException {
        DigitalAssetMetadata digitalAssetMetadata = new DigitalAssetMetadata();
        try {
            PluginTextFile pluginTextFile = null;
            pluginTextFile = pluginFileSystem.getTextFile(pluginId, PATH_DIRECTORY, transactionHash, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
            String content = pluginTextFile.getContent();
            digitalAssetMetadata = (DigitalAssetMetadata) XMLParser.parseXML(content, digitalAssetMetadata);


        } catch (FileNotFoundException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(e));
            throw new CantGetDigitalAssetFromLocalStorageException(FermatException.wrapException(e), "File no found", null);
        } catch (CantCreateFileException e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(e));
            throw new CantGetDigitalAssetFromLocalStorageException(FermatException.wrapException(e), "Cannot create file", null);
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_WALLET_ISSUER, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, FermatException.wrapException(e));
            throw new CantGetDigitalAssetFromLocalStorageException(FermatException.wrapException(e), null, null);
        }
        return digitalAssetMetadata;
    }

    @Override
    public DigitalAsset getAssetByPublicKey(String assetPublicKey) {
        return assetIssuerWalletDao.getAssetByPublicKey(assetPublicKey);
    }

    @Override
    public String getUserDeliveredToPublicKey(UUID transactionId) throws RecordsNotFoundException, CantGetAssetStatisticException {
        return assetIssuerWalletDao.getUserPublicKey(transactionId);
    }

    @Override
    public List<DigitalAssetMetadata> getAllUnusedAssets() throws CantGetDigitalAssetFromLocalStorageException {
        List<AssetStatistic> allUsedAssets = new ArrayList<>();
        List<DigitalAssetMetadata> toReturn = new ArrayList<>();
        try {
            allUsedAssets.addAll(constructListFromAssetPublicKey(assetIssuerWalletDao.getAllAssetPublicKeyForStatus(AssetCurrentStatus.ASSET_UNUSED)));

            for (AssetStatistic statistic : allUsedAssets) {
                toReturn.add(getDigitalAssetMetadata(statistic.genesisTransaction()));
            }
        } catch (CantGetAssetStatisticException e) {
            throw new CantGetDigitalAssetFromLocalStorageException();
        }
        return toReturn;
    }

    @Override
    public void createdNewAsset(DigitalAssetMetadata metadata) throws CantSaveStatisticException {
        assetIssuerWalletDao.createdNewAsset(metadata);
    }

    @Override
    public void newMovement(DAPActor actorFrom, DAPActor actorTo, String assetPk, AssetMovementType type) throws CantSaveStatisticException {
        String fromPk = actorFrom.getActorPublicKey();
        Actors fromType = ActorUtils.getActorType(actorFrom);
        String toPk = actorTo.getActorPublicKey();
        Actors toType = ActorUtils.getActorType(actorTo);
        assetIssuerWalletDao.newMovement(assetPk, fromPk, fromType, toPk, toType, type);
    }


    @Override
    public void assetDistributed(UUID transactionId, String actorAssetUserPublicKey) throws RecordsNotFoundException, CantGetAssetStatisticException {
        assetIssuerWalletDao.assetDistributed(transactionId, actorAssetUserPublicKey);
    }

    @Override
    public void assetRedeemed(UUID transactionId, String userPublicKey, String redeemPointPublicKey) throws RecordsNotFoundException, CantGetAssetStatisticException {
        assetIssuerWalletDao.assetRedeemed(transactionId, userPublicKey, redeemPointPublicKey);
    }

    @Override
    public void assetAppropriated(UUID transactionId, String userPublicKey) throws RecordsNotFoundException, CantGetAssetStatisticException {
        assetIssuerWalletDao.assetAppropriated(transactionId, userPublicKey);
    }

    @Override
    public List<AssetStatistic> getAllStatisticForAllAssets() throws CantGetAssetStatisticException {
        return constructListFromAssetPublicKey(assetIssuerWalletDao.getAllTransactionIds());
    }

    @Override
    public List<AssetStatistic> getStatisticForAllAssetsByStatus(AssetCurrentStatus status) throws CantGetAssetStatisticException {
        return constructListFromAssetPublicKey(assetIssuerWalletDao.getAllAssetPublicKeyForStatus(status));
    }

    @Override
    public List<AssetStatistic> getStatisticForGivenAssetByStatus(String assetName, AssetCurrentStatus status) throws CantGetAssetStatisticException {
        return constructListFromAssetPublicKey(assetIssuerWalletDao.getAllAssetPublicKeyForAssetNameAndStatus(assetName, status));
    }

    @Override
    public List<AssetStatistic> getAllStatisticForGivenAsset(String assetName) throws CantGetAssetStatisticException {
        return constructListFromAssetPublicKey(assetIssuerWalletDao.getAllAssetPublicKeyForAssetName(assetName));
    }

    //TODO: THE FOLLOWING THREE METHODS SHOULD BE CHANGED SO THEY QUERY THE AMOUNT USING A
    //SELECT COUNT(*) FROM THE TABLE. OR THIS WALLET WILL SUFFER A LOT OF PERFORMANCE ISSUES.

    @Override
    public int getUnusedAmountForAssetByStatus(AssetCurrentStatus status, String assetName) throws CantGetAssetStatisticException {
        return getStatisticForGivenAssetByStatus(assetName, status).size();
    }

    private List<AssetStatistic> constructListFromAssetPublicKey(List<UUID> transactionIds) {
        if (transactionIds.isEmpty()) return Collections.EMPTY_LIST;

        List<AssetStatistic> returnList = new ArrayList<>();
        for (UUID txId : transactionIds) {
            returnList.add(constructAssetStatisticByTransactionId(txId));
        }
        return returnList;
    }

    private AssetStatistic constructAssetStatisticByTransactionId(UUID transactionId) {
        AssetStatisticImpl assetStatistic = new AssetStatisticImpl();
        assetStatistic.setTransactionId(transactionId);
        assetStatistic.setGenesisTransaction(assetIssuerWalletDao.getGenesisTransaction(transactionId));
        assetStatistic.setAssetPublicKey(assetIssuerWalletDao.getAssetPublicKey(transactionId));
        assetStatistic.setAssetName(assetIssuerWalletDao.getAssetName(transactionId));

        try {
            assetStatistic.setOwner(actorAssetUserManager.getActorByPublicKey(assetIssuerWalletDao.getUserPublicKey(transactionId)));
        } catch (Exception e) {
            e.printStackTrace();
            //If this happen it means we couldn't get the user or there were none. So we'll keep it as null.
        }
        AssetCurrentStatus status = assetIssuerWalletDao.getStatus(transactionId);
        assetStatistic.setStatus(status);
        assetStatistic.setDistributionDate(assetIssuerWalletDao.getDistributionDate(transactionId));

        if (status == AssetCurrentStatus.ASSET_REDEEMED || status == AssetCurrentStatus.ASSET_APPROPRIATED) {
            Date assetUsageDate = assetIssuerWalletDao.getUsageDate(transactionId);
            assetStatistic.setUsageDate(assetUsageDate);
            if (status == AssetCurrentStatus.ASSET_REDEEMED) {
                try {
                    ActorAssetRedeemPoint redeemPoint = actorAssetRedeemPointManager.getActorByPublicKey(assetIssuerWalletDao.getRedeemPointPublicKey(transactionId));
                    assetStatistic.setRedeemPoint(redeemPoint);
                } catch (Exception e) {
                    e.printStackTrace();
                    //If this happen it means we couldn't get the redeem point or there were none. So we'll keep it as null.
                }
            }
        }

        return assetStatistic;
    }
}
