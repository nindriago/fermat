package org.fermat.fermat_dap_android_wallet_redeem_point.models;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.Genders;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Resource;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;

import org.fermat.fermat_dap_android_wallet_redeem_point.v3.models.DigitalAssetHistory;
import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetContractPropertiesConstants;
import org.fermat.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUser;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_redeem_point.interfaces.AssetRedeemPointWalletSubAppModule;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_redeem_point.interfaces.AssetRedeemPointWallet;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_redeem_point.interfaces.AssetRedeemPointWalletList;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_redeem_point.interfaces.AssetRedeemPointWalletTransaction;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_redeem_point.interfaces.RedeemPointStatistic;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.WalletUtilities;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.enums.TransactionType;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantGetTransactionsException;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by frank on 12/9/15.
 */
public class Data {
    public static List<DigitalAsset> getAllDigitalAssets(AssetRedeemPointWalletSubAppModule moduleManager) throws Exception {
        List<AssetRedeemPointWalletList> assets = moduleManager.getAssetRedeemPointWalletBalances(WalletUtilities.WALLET_PUBLIC_KEY);
        List<DigitalAsset> digitalAssets = new ArrayList<>();
        DigitalAsset digitalAsset;

        for (AssetRedeemPointWalletList asset : assets) {
            digitalAsset = new DigitalAsset();
            digitalAsset.setAssetPublicKey(asset.getDigitalAsset().getPublicKey());
            digitalAsset.setName(asset.getDigitalAsset().getName());
            digitalAsset.setAvailableBalanceQuantity(asset.getQuantityAvailableBalance());
            digitalAsset.setBookBalanceQuantity(asset.getQuantityBookBalance());
            digitalAsset.setAvailableBalance(asset.getAvailableBalance());
            digitalAsset.setExpDate((Timestamp) asset.getDigitalAsset().getContract().getContractProperty(DigitalAssetContractPropertiesConstants.EXPIRATION_DATE).getValue());

            digitalAssets.add(digitalAsset);

            List<Resource> resources = asset.getDigitalAsset().getResources();
            if (resources != null && !resources.isEmpty()) {
                digitalAsset.setImage(resources.get(0).getResourceBinayData());
            }
        }
        return digitalAssets;
    }

    public static List<UserRedeemed> getUserRedeemedPointList(String walletPublicKey, DigitalAsset digitalAsset, AssetRedeemPointWalletSubAppModule moduleManager) throws Exception {

        AssetRedeemPointWallet wallet = moduleManager.loadAssetRedeemPointWallet(walletPublicKey);
        List<RedeemPointStatistic> all = wallet.getStatisticsByAssetPublicKey(digitalAsset.getAssetPublicKey());
        List<UserRedeemed> userRedeemeds = new ArrayList<>();

        for (RedeemPointStatistic stadistic : all) {
            UserRedeemed user = new UserRedeemed(stadistic.userThatRedeemed().getName(), new Timestamp(stadistic.redemptionTime().getTime()));
            userRedeemeds.add(user);
        }

        /*List<UserRedeemed> userRedeemeds = new ArrayList<>();
        UserRedeemed user= new UserRedeemed("Penny Quintero",new Timestamp(new Date().getTime()));
        userRedeemeds.add(user);
        user= new UserRedeemed("Nerio Indriago",new Timestamp(new Date().getTime()));
        userRedeemeds.add(user);
        user= new UserRedeemed("Jinmy Bohorquez",new Timestamp(new Date().getTime()));
        userRedeemeds.add(user);*/

        return userRedeemeds;
    }

    public static List<Transaction> getTransactions(AssetRedeemPointWalletSubAppModule moduleManager, DigitalAsset digitalAsset) throws CantLoadWalletException, CantGetTransactionsException {
        List<Transaction> transactions = new ArrayList<>();
        List<AssetRedeemPointWalletTransaction> assetRedeemPointWalletTransactions = moduleManager.loadAssetRedeemPointWallet(WalletUtilities.WALLET_PUBLIC_KEY).getTransactionsForDisplay(digitalAsset.getAssetPublicKey());
        DAPActor dapActor;
        for (AssetRedeemPointWalletTransaction assetRedeemPointWalletTransaction :
                assetRedeemPointWalletTransactions) {
            if (assetRedeemPointWalletTransaction.getTransactionType().equals(TransactionType.CREDIT)) {
                dapActor = assetRedeemPointWalletTransaction.getActorFrom();
            } else {
                dapActor = assetRedeemPointWalletTransaction.getActorTo();
            }
            Transaction transaction = new Transaction(assetRedeemPointWalletTransaction, dapActor);
            transactions.add(transaction);
        }
        return transactions;
    }

    public static DigitalAsset getDigitalAsset(AssetRedeemPointWalletSubAppModule moduleManager, String digitalAssetPublicKey) throws CantLoadWalletException {
        List<AssetRedeemPointWalletList> balances = moduleManager.getAssetRedeemPointWalletBalances(WalletUtilities.WALLET_PUBLIC_KEY);
        DigitalAsset digitalAsset;
        String publicKey;
        for (AssetRedeemPointWalletList balance : balances) {
            publicKey = balance.getDigitalAsset().getPublicKey();
            if (publicKey.equals(digitalAssetPublicKey)) {
                digitalAsset = new DigitalAsset();
                digitalAsset.setAssetPublicKey(balance.getDigitalAsset().getPublicKey());
                digitalAsset.setName(balance.getDigitalAsset().getName());
                digitalAsset.setAvailableBalanceQuantity(balance.getQuantityAvailableBalance());
                digitalAsset.setBookBalanceQuantity(balance.getQuantityBookBalance());
                digitalAsset.setAvailableBalance(balance.getAvailableBalance());
                Timestamp expirationDate = (Timestamp) balance.getDigitalAsset().getContract().getContractProperty(DigitalAssetContractPropertiesConstants.EXPIRATION_DATE).getValue();
                digitalAsset.setExpDate(expirationDate);

                List<Resource> resources = balance.getDigitalAsset().getResources();
                if (resources != null && resources.size() > 0) {
                    digitalAsset.setImage(balance.getDigitalAsset().getResources().get(0).getResourceBinayData());
                }
                return digitalAsset;
            }
        }
        return null;
    }
    
//    public static List<DigitalAsset> getAssets(AssetRedeemPointWalletSubAppModule moduleManager) throws CantLoadWalletException {
//        List<AssetRedeemPointWalletList> assetRedeemPointWalletList = moduleManager.
//                getAssetRedeemPointWalletBalances(WalletUtilities.WALLET_PUBLIC_KEY);
//        AssetRedeemPointWallet redeemWallet = moduleManager.loadAssetRedeemPointWallet(WalletUtilities.WALLET_PUBLIC_KEY);
//        List<DigitalAsset> assets = new ArrayList<>();
//        for (AssetRedeemPointWalletList assetRedeemWalletList : assetRedeemPointWalletList) {
//            List<CryptoAddress> addresses = assetRedeemWalletList.getAddresses();
//            for (int i = 0; i < assetRedeemWalletList.getQuantityBookBalance(); i++) {
//                try {
//                    CryptoAddress address = addresses.get(i);
//                    assets.add(new DigitalAsset(assetRedeemWalletList, redeemWallet.getActorTransactionSummary(), address));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        Collections.sort(assets, new Comparator<DigitalAsset>() {
//            @Override
//            public int compare(DigitalAsset lhs, DigitalAsset rhs) {
//                if (lhs.getDate().getTime() > rhs.getDate().getTime()) return -1;
//                else if (lhs.getDate().getTime() < rhs.getDate().getTime()) return 1;
//                return 0;
//            }
//        });
//
//        return assets;
//    }
    public static List<DigitalAsset> getAllTestAssets(AssetRedeemPointWalletSubAppModule moduleManager) throws Exception{
        List<DigitalAsset> digitalAssets = new ArrayList<>();
        DigitalAsset digitalAsset;


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, 0);

        digitalAsset = new DigitalAsset();
        digitalAsset.setAssetPublicKey(UUID.randomUUID().toString());
        digitalAsset.setName("Combo " + (0 + 1) + "x1");
        digitalAsset.setExpDate(new Timestamp(calendar.getTime().getTime()));
        digitalAsset.setDate(new Timestamp(calendar.getTime().getTime()));
        digitalAsset.setActorUserNameFrom("Penelope Quintero");
        digitalAsset.setImageActorUserFrom(null);
        digitalAsset.setImage(null);
        digitalAsset.setStatus(DigitalAsset.Status.PENDING);

        digitalAssets.add(digitalAsset);

        digitalAsset = new DigitalAsset();
        digitalAsset.setAssetPublicKey(UUID.randomUUID().toString());
        digitalAsset.setName("Combo " + (2) + "pizza");
        digitalAsset.setExpDate(new Timestamp(calendar.getTime().getTime()));
        digitalAsset.setDate(new Timestamp(calendar.getTime().getTime()));
        digitalAsset.setActorUserNameFrom("Jinmy Bohorquez");
        digitalAsset.setImageActorUserFrom(null);
        digitalAsset.setImage(null);
        digitalAsset.setStatus(DigitalAsset.Status.CONFIRMED);

        digitalAssets.add(digitalAsset);

        digitalAsset = new DigitalAsset();
        digitalAsset.setAssetPublicKey(UUID.randomUUID().toString());
        digitalAsset.setName("tequeños " + (2) + "mix");
        digitalAsset.setExpDate(new Timestamp(calendar.getTime().getTime()));
        digitalAsset.setDate(new Timestamp(calendar.getTime().getTime()));
        digitalAsset.setActorUserNameFrom("Nerio Indriago");
        digitalAsset.setImageActorUserFrom(null);
        digitalAsset.setImage(null);
        digitalAsset.setStatus(DigitalAsset.Status.ACEPTED);

        digitalAssets.add(digitalAsset);


        return digitalAssets;
    }
    public static List<DigitalAssetHistory> getAllAcceptedDigitalAssets(AssetRedeemPointWalletSubAppModule moduleManager) throws Exception {
        List<DigitalAssetHistory> digitalAssets = new ArrayList<>();
        DigitalAssetHistory digitalAsset;

        for (int i = 0; i < 5; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE,-i);

            digitalAsset = new DigitalAssetHistory();
            digitalAsset.setAssetPublicKey(UUID.randomUUID().toString());
            digitalAsset.setHistoryNameAsset("Combo " + (i + 1) + "x1");
            digitalAsset.setExpDate(new Timestamp(calendar.getTime().getTime()));
            digitalAsset.setAcceptedDate(new Timestamp(calendar.getTime().getTime()));



            digitalAsset.setHistoryNameUser("Penelope Quintero");
            digitalAsset.setImageActorUserFrom(null);
            digitalAsset.setImageAsset(null);
            digitalAsset.setActorUserPublicKey(UUID.randomUUID().toString());

            digitalAssets.add(digitalAsset);

        }

        for (int i = 0; i < 5; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE,-i);

            digitalAsset = new DigitalAssetHistory();
            digitalAsset.setAssetPublicKey(UUID.randomUUID().toString());
            digitalAsset.setHistoryNameAsset("Combo " + (i + 1) + "x1");
            digitalAsset.setExpDate(new Timestamp(calendar.getTime().getTime()));
            digitalAsset.setAcceptedDate(new Timestamp(calendar.getTime().getTime()));



            digitalAsset.setHistoryNameUser("Jinmy Bohorquez");
            digitalAsset.setImageActorUserFrom(null);
            digitalAsset.setImageAsset(null);
            digitalAsset.setActorUserPublicKey(UUID.randomUUID().toString());

            digitalAssets.add(digitalAsset);

        }

        for (int i = 0; i < 5; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE,i);

            digitalAsset = new DigitalAssetHistory();
            digitalAsset.setAssetPublicKey(UUID.randomUUID().toString());
            digitalAsset.setHistoryNameAsset("Hamburguesa " + (i + 1) + "x1");
            digitalAsset.setExpDate(new Timestamp(calendar.getTime().getTime()));
            digitalAsset.setAcceptedDate(new Timestamp(calendar.getTime().getTime()));


            digitalAsset.setHistoryNameUser("Nerio Indriago");
            digitalAsset.setImageActorUserFrom(null);
            digitalAsset.setImageAsset(null);
            digitalAsset.setActorUserPublicKey(UUID.randomUUID().toString());

            digitalAssets.add(digitalAsset);

        }
        Collections.sort(digitalAssets);

        return digitalAssets;
    }

}