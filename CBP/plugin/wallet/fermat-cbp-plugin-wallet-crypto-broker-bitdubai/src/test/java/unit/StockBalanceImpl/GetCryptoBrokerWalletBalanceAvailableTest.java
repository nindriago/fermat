package unit.StockBalanceImpl;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.exceptions.CantGetBookedBalanceCryptoBrokerWalletException;
import com.bitdubai.fermat_cbp_api.layer.wallet.crypto_broker.interfaces.CryptoBrokerWalletBalanceRecord;
import com.bitdubai.fermat_cbp_plugin.layer.wallet.crypto_broker.developer.bitdubai.version_1.structure.util.StockBalanceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by José Vilchez on 22/01/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCryptoBrokerWalletBalanceAvailableTest {

    @Test
    public void getCryptoBrokerWalletBalanceAvailable() throws CantGetBookedBalanceCryptoBrokerWalletException, CantStartPluginException {
        StockBalanceImpl stockBalance = mock(StockBalanceImpl.class);
        when(stockBalance.getCryptoBrokerWalletBalanceAvailable()).thenReturn(new ArrayList<CryptoBrokerWalletBalanceRecord>());
        assertThat(stockBalance.getCryptoBrokerWalletBalanceAvailable()).isNotNull();
    }

}
