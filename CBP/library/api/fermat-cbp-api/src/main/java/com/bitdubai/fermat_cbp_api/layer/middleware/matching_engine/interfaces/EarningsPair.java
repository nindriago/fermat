package com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces;

import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.enums.EarningPairState;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.exceptions.CantChangeEarningsWalletException;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.utils.WalletReference;

import java.io.Serializable;
import java.util.UUID;

/**
 * The interface <code>com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces.EarningsPair</code>
 * represents an earnings pair object and containts all the methods needed to get their information.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 28/01/2016.
 */
public interface EarningsPair extends Serializable {

    /**
     * @return the id of the earning pair.
     */
    UUID getId();

    /**
     * @return the currency which we decided to extract the earnings.
     */
    Currency getEarningCurrency();

    /**
     * @return the currency that we're linking to the previous selected currency to conform the pair.
     */
    Currency getLinkedCurrency();

    /**
     * @return the wallet information referencing the wallet where the plug-in will deposit the earnings.
     */
    WalletReference getEarningsWallet();

    /**
     * @return the state of the earning pair.
     */
    EarningPairState getState();

    /**
     * Through the method <code>getSearch</code> you can get a search object with the search functionality encapsulated.
     *
     * @return an instance of earning search for the selected wallet.
     */
    EarningsSearch getSearch();

    /**
     * Through the method <code>changeEarningsWallet</code> you can change the earnigs wallet of the respective pair.
     *
     * @param earningsWallet the information of the wallet that we're associating and where we will deposit the earnings.
     *
     * @throws CantChangeEarningsWalletException if something goes wrong.
     */
    void changeEarningsWallet(

            WalletReference earningsWallet

    ) throws CantChangeEarningsWalletException;

}
