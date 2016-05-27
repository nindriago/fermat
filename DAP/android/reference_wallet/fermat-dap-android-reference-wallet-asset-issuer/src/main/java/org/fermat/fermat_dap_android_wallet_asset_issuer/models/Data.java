package org.fermat.fermat_dap_android_wallet_asset_issuer.models;

import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Resource;
import org.fermat.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetContractPropertiesConstants;
import org.fermat.fermat_dap_api.layer.all_definition.enums.AssetCurrentStatus;
import org.fermat.fermat_dap_api.layer.dap_actor.DAPActor;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.exceptions.CantGetAssetUserActorsException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.exceptions.CantGetAssetUserGroupException;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUser;
import org.fermat.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUserGroup;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.exceptions.CantGetAssetStatisticException;
import org.fermat.fermat_dap_api.layer.dap_module.wallet_asset_issuer.interfaces.AssetIssuerWalletSupAppModuleManager;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletList;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetIssuerWalletTransaction;
import org.fermat.fermat_dap_api.layer.dap_wallet.asset_issuer_wallet.interfaces.AssetStatistic;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.WalletUtilities;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.enums.TransactionType;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantGetTransactionsException;
import org.fermat.fermat_dap_api.layer.dap_wallet.common.exceptions.CantLoadWalletException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by frank on 12/9/15.
 */
public class Data {
    public static List<DigitalAsset> getAllDigitalAssets(AssetIssuerWalletSupAppModuleManager moduleManager) throws Exception {
        List<AssetIssuerWalletList> balances = moduleManager.getAssetIssuerWalletBalances(WalletUtilities.WALLET_PUBLIC_KEY);
        List<DigitalAsset> digitalAssets = new ArrayList<>();
        DigitalAsset digitalAsset;
        for (AssetIssuerWalletList balance : balances) {
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

            digitalAssets.add(digitalAsset);
        }
        return digitalAssets;
    }

    public static List<DigitalAsset> getAllDigitalAssetsDateSorted(AssetIssuerWalletSupAppModuleManager moduleManager) throws Exception {
        List<AssetIssuerWalletList> balances = moduleManager.getAssetIssuerWalletBalances(WalletUtilities.WALLET_PUBLIC_KEY);
        List<DigitalAsset> digitalAssets = new ArrayList<>();
        DigitalAsset digitalAsset;
        for (AssetIssuerWalletList balance : balances) {
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

            List<Transaction> transactions = getTransactions(moduleManager, digitalAsset);
            digitalAsset.setLastTransactionDate(transactions.get(0).getDate());

            digitalAssets.add(digitalAsset);
        }

        Collections.sort(digitalAssets, new Comparator<DigitalAsset>() {
            @Override
            public int compare(DigitalAsset lhs, DigitalAsset rhs) {
                if (lhs.getLastTransactionDate().getTime() > rhs.getLastTransactionDate().getTime()) return -1;
                else if (lhs.getLastTransactionDate().getTime() < rhs.getLastTransactionDate().getTime()) return 1;
                return 0;
            }
        });

        return digitalAssets;
    }

    public static DigitalAsset getDigitalAsset(AssetIssuerWalletSupAppModuleManager moduleManager, String digitalAssetPublicKey) throws CantLoadWalletException {
        List<AssetIssuerWalletList> balances = moduleManager.getAssetIssuerWalletBalances(WalletUtilities.WALLET_PUBLIC_KEY);
        DigitalAsset digitalAsset;
        String publicKey;
        for (AssetIssuerWalletList balance : balances) {
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

    public static List<UserDelivery> getStats(String walletPublicKey, DigitalAsset digitalAsset, AssetIssuerWalletSupAppModuleManager moduleManager) throws Exception {
        List<UserDelivery> users = new ArrayList<>();
        UserDelivery userDelivery;
        List<AssetStatistic> stats = moduleManager.getWalletStatisticsByAsset(walletPublicKey, digitalAsset.getName());
        for (AssetStatistic stat : stats) {
            if (!stat.getStatus().equals(AssetCurrentStatus.ASSET_CREATED)) {
                userDelivery = new UserDelivery(stat.getOwner().getProfileImage(), stat.getOwner().getName(), new Timestamp(stat.getDistributionDate().getTime()), stat.getStatus().getCode(), stat.getStatus().getDescription());
                users.add(userDelivery);
            }
        }
        return users;
    }

//    public static List<UserDelivery> getStats(String walletPublicKey, DigitalAsset digitalAsset, AssetIssuerWalletSupAppModuleManager moduleManager) {
//        List<UserDelivery> stats = new ArrayList<>();
//        Timestamp timestamp = new Timestamp(new Date().getTime());
//        UserDelivery u1 = new UserDelivery(null, "Frank Contreras", timestamp, "ASUN", "Unused");
//        UserDelivery u2 = new UserDelivery(null, "Flor Naveda", timestamp, "ASUN", "Unused");
//        UserDelivery u3 = new UserDelivery(null, "Victor Mars", timestamp, "ASUN", "Unused");
//        UserDelivery u4 = new UserDelivery(null, "Nerio Indriago", timestamp, "ASRE", "Redeemed");
//        UserDelivery u5 = new UserDelivery(null, "Francisco Rodriguez", timestamp, "ASRE", "Redeemed");
//        UserDelivery u6 = new UserDelivery(null, "Humberto Perdomo", timestamp, "ASRE", "Redeemed");
//        UserDelivery u7 = new UserDelivery(null, "Lisa Loeb", timestamp, "ASAP", "Appropriated");
//        UserDelivery u8 = new UserDelivery(null, "Armando Manzanero", timestamp, "ASAP", "Appropriated");
//        UserDelivery u9 = new UserDelivery(null, "Francisca Main", timestamp, "ASAP", "Appropriated");
//        UserDelivery u10 = new UserDelivery(null, "Superman Rodriguez", timestamp, "ASAP", "Appropriated");
//        stats.add(u1);
//        stats.add(u2);
//        stats.add(u3);
//        stats.add(u4);
//        stats.add(u5);
//        stats.add(u6);
//        stats.add(u7);
//        stats.add(u8);
//        stats.add(u9);
//        stats.add(u10);
//        return stats;
//    }

    public static List<UserDelivery> getUserDeliveryList(String walletPublicKey, DigitalAsset digitalAsset, AssetIssuerWalletSupAppModuleManager moduleManager) throws Exception {
        List<UserDelivery> users = new ArrayList<>();
        UserDelivery userDelivery;
        List<AssetStatistic> stats = moduleManager.getWalletStatisticsByAsset(walletPublicKey, digitalAsset.getName());
        for (AssetStatistic stat : stats) {
            if (stat.getStatus().equals(AssetCurrentStatus.ASSET_UNUSED)) {
                userDelivery = new UserDelivery(stat.getOwner().getName(), new Timestamp(stat.getDistributionDate().getTime()), stat.getStatus().getDescription());
                users.add(userDelivery);
            }
        }
        return users;
    }

    public static List<UserRedeemed> getUserRedeemedList(String walletPublicKey, DigitalAsset digitalAsset, AssetIssuerWalletSupAppModuleManager moduleManager) throws Exception {
        List<UserRedeemed> users = new ArrayList<>();
        UserRedeemed UserRedeemed;
        List<AssetStatistic> stats = moduleManager.getWalletStatisticsByAsset(walletPublicKey, digitalAsset.getName());
        for (AssetStatistic stat :
                stats) {
            if (stat.getStatus().equals(AssetCurrentStatus.ASSET_REDEEMED)) {
                UserRedeemed = new UserRedeemed(stat.getOwner().getName(), new Timestamp(stat.getDistributionDate().getTime()), stat.getStatus().getDescription(), stat.getRedeemPoint().getName());
                users.add(UserRedeemed);
            }
        }
        return users;
    }

    public static List<UserAppropiate> getUserAppropiateList(String walletPublicKey, DigitalAsset digitalAsset, AssetIssuerWalletSupAppModuleManager moduleManager) throws Exception {
        List<UserAppropiate> users = new ArrayList<>();
        UserAppropiate UserAppropiate;
        List<AssetStatistic> stats = moduleManager.getWalletStatisticsByAsset(walletPublicKey, digitalAsset.getName());
        for (AssetStatistic stat :
                stats) {
            if (stat.getStatus().equals(AssetCurrentStatus.ASSET_APPROPRIATED)) {
                UserAppropiate = new UserAppropiate(stat.getOwner().getName(), new Timestamp(stat.getAssetUsedDate().getTime()), stat.getStatus().getDescription());
                users.add(UserAppropiate);
            }
        }
        return users;
    }

    public static List<User> getConnectedUsers(AssetIssuerWalletSupAppModuleManager moduleManager, List<User> usersSelected) throws CantGetAssetUserActorsException {
        List<User> users = new ArrayList<>();
        List<ActorAssetUser> actorAssetUsers = moduleManager.getAllAssetUserActorConnected();
        for (ActorAssetUser actorAssetUser:actorAssetUsers) {
            User newUser = new User(actorAssetUser.getName(), actorAssetUser);
            users.add(newUser);
        }

        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User lhs, User rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        String lastLetter = "";
        for (User user : users) {
            String letter = user.getName().substring(0, 1);
            if (!letter.equals(lastLetter)) {
                user.setFirst(true);
                lastLetter = letter;
            }
        }

        return users;
    }

//    public static List<User> getConnectedUsers(AssetIssuerWalletSupAppModuleManager moduleManager, List<User> usersSelected) throws CantGetAssetUserActorsException {
//        List<User> users = new ArrayList<>();
//
//        User u1 = new User("Arnaldo Sequera", null);
//        User u2 = new User("Frank Contreras", null);
//        User u3 = new User("Jean Segura", null);
//        User u4 = new User("John Macdonalds", null);
//        User u5 = new User("Christian Contreras", null);
//        User u6 = new User("Francisco Cardenas", null);
//        User u7 = new User("Daniel Zamudio", null);
//        User u8 = new User("Carlos Lopez", null);
//        User u9 = new User("Juan Bimba", null);
//        User u10 = new User("Juan Bimbo", null);
//        users.add(u1);
//        users.add(u2);
//        users.add(u3);
//        users.add(u4);
//        users.add(u5);
//        users.add(u6);
//        users.add(u7);
//        users.add(u8);
//        users.add(u9);
//        users.add(u10);
//
//        Collections.sort(users, new Comparator<User>() {
//            @Override
//            public int compare(User lhs, User rhs) {
//                return lhs.getName().compareTo(rhs.getName());
//            }
//        });
//
//        String lastLetter = "";
//        for (User user : users) {
//            String letter = user.getName().substring(0, 1);
//            if (!letter.equals(lastLetter)) {
//                user.setFirst(true);
//                lastLetter = letter;
//            }
//        }
//
//        return users;
//    }

    public static List<Group> getGroups(AssetIssuerWalletSupAppModuleManager moduleManager, List<Group> groupsSelected) throws CantGetAssetUserGroupException, CantGetAssetUserActorsException {
        List<Group> groups = new ArrayList<>();
        List<ActorAssetUserGroup> actorAssetUserGroups = moduleManager.getAssetUserGroupsList();
        for (ActorAssetUserGroup actorAssetUserGroup:actorAssetUserGroups) {
            Group newGroup = new Group(actorAssetUserGroup.getGroupName(), actorAssetUserGroup);
            List<ActorAssetUser> actorAssetUsers = moduleManager.getListActorAssetUserByGroups(actorAssetUserGroup.getGroupId());
            List<User> users = new ArrayList<>();
            for (ActorAssetUser actorAssetUser : actorAssetUsers) {
                users.add(new User(actorAssetUser.getName(), actorAssetUser));
            }
            newGroup.setUsers(users);
            groups.add(newGroup);
        }

        Collections.sort(groups, new Comparator<Group>() {
            @Override
            public int compare(Group lhs, Group rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        String lastLetter = "";
        for (Group group : groups) {
            String letter = group.getName().substring(0, 1);
            if (!letter.equals(lastLetter)) {
                group.setFirst(true);
                lastLetter = letter;
            }
        }

        return groups;
    }

//    public static List<Group> getGroups(AssetIssuerWalletSupAppModuleManager moduleManager, List<Group> usersSelected) throws CantGetAssetUserActorsException {
//        List<Group> groups = new ArrayList<>();
//
//        Group u1 = new Group("Arnaldo Sequera Grupo", null);
//        Group u2 = new Group("Frank Contreras Grupo", null);
//        Group u3 = new Group("Jean Segura Grupo", null);
//        Group u4 = new Group("John Macdonalds Grupo", null);
//        Group u5 = new Group("Christian Contreras Grupo", null);
//        Group u6 = new Group("Francisco Cardenas Grupo", null);
//        Group u7 = new Group("Daniel Zamudio Grupo", null);
//        Group u8 = new Group("Carlos Lopez Grupo", null);
//        Group u9 = new Group("Juan Bimba Grupo", null);
//        Group u10 = new Group("Juan Bimbo Grupo", null);
//        groups.add(u1);
//        groups.add(u2);
//        groups.add(u3);
//        groups.add(u4);
//        groups.add(u5);
//        groups.add(u6);
//        groups.add(u7);
//        groups.add(u8);
//        groups.add(u9);
//        groups.add(u10);
//
//        Collections.sort(groups, new Comparator<Group>() {
//            @Override
//            public int compare(Group lhs, Group rhs) {
//                return lhs.getName().compareTo(rhs.getName());
//            }
//        });
//
//        String lastLetter = "";
//        for (Group group : groups) {
//            String letter = group.getName().substring(0, 1);
//            if (!letter.equals(lastLetter)) {
//                group.setFirst(true);
//                lastLetter = letter;
//            }
//        }
//
//        return groups;
//    }

    public static void setStatistics(String walletPublicKey, DigitalAsset digitalAsset, AssetIssuerWalletSupAppModuleManager moduleManager) throws CantGetAssetStatisticException, CantLoadWalletException {
        int unused = moduleManager.getWalletStatisticsByAssetAndStatus(walletPublicKey, digitalAsset.getName(), AssetCurrentStatus.ASSET_UNUSED).size();
        int appropriated = moduleManager.getWalletStatisticsByAssetAndStatus(walletPublicKey, digitalAsset.getName(), AssetCurrentStatus.ASSET_APPROPRIATED).size();
        int redeemed = moduleManager.getWalletStatisticsByAssetAndStatus(walletPublicKey, digitalAsset.getName(), AssetCurrentStatus.ASSET_REDEEMED).size();

        digitalAsset.setUnused(unused);
        digitalAsset.setAppropriated(appropriated);
        digitalAsset.setRedeemed(redeemed);
    }

    public static List<Transaction> getTransactions(AssetIssuerWalletSupAppModuleManager moduleManager, DigitalAsset digitalAsset) throws CantLoadWalletException, CantGetTransactionsException {
        List<Transaction> transactions = new ArrayList<>();
        List<AssetIssuerWalletTransaction> assetUserWalletTransactions = moduleManager.loadAssetIssuerWallet(WalletUtilities.WALLET_PUBLIC_KEY).getTransactionsForDisplay(digitalAsset.getAssetPublicKey());
        DAPActor dapActor;
        for (AssetIssuerWalletTransaction assetUserWalletTransaction :
                assetUserWalletTransactions) {
            if (assetUserWalletTransaction.getTransactionType().equals(TransactionType.CREDIT)) {
                dapActor = assetUserWalletTransaction.getActorFrom();
            } else {
                dapActor = assetUserWalletTransaction.getActorTo();
            }
            Transaction transaction = new Transaction(assetUserWalletTransaction, dapActor);
            transactions.add(transaction);
        }
        return transactions;
    }

    public static List<String> getStatsOptions() {
        List<String> arr = new ArrayList<>();
        arr.add("All");
        AssetCurrentStatus[] statuses = AssetCurrentStatus.values();
        for (AssetCurrentStatus status: statuses) {
            if (!status.equals(AssetCurrentStatus.ASSET_CREATED.ASSET_CREATED)) {
                arr.add(status.getDescription());
            }
        }
        return arr;
    }
}
