package com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.interfaces;

import com.bitdubai.fermat_api.layer.world.interfaces.Currency;

/**
 * The class <code>com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.interfaces.CryptoBrokerQuote</code>
 * contain the information about quotes.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 06/02/2016.
 */
public final class CryptoBrokerQuote implements CryptoBrokerInfo {

    private final Currency merchandise;
    private final Currency paymentCurrency;
    private final Float    price;

    public CryptoBrokerQuote(final Currency merchandise    ,
                             final Currency paymentCurrency,
                             final Float    price          ) {

        this.merchandise     = merchandise    ;
        this.paymentCurrency = paymentCurrency;
        this.price           = price          ;
    }

    public final Currency getMerchandise() {
        return merchandise;
    }

    public final Currency getPaymentCurrency() {
        return paymentCurrency;
    }

    public final Float getPrice() {
        return price;
    }

}
