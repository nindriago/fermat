package com.bitdubai.fermat_cer_plugin.layer.provider.yahoo.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_cer_api.all_definition.utils.ExchangeRateImpl;
import com.bitdubai.fermat_cer_api.layer.provider.utils.CurrencyPairHelper;
import com.bitdubai.fermat_cer_api.layer.provider.utils.HttpReader;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantGetExchangeRateException;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantGetProviderInfoException;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantSaveExchangeRateException;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.UnsupportedCurrencyPairException;
import com.bitdubai.fermat_cer_api.layer.provider.interfaces.CurrencyExchangeRateProviderManager;
import com.bitdubai.fermat_cer_api.all_definition.interfaces.CurrencyPair;
import com.bitdubai.fermat_cer_api.all_definition.interfaces.ExchangeRate;
import com.bitdubai.fermat_cer_plugin.layer.provider.yahoo.developer.bitdubai.version_1.database.YahooProviderDao;
import com.bitdubai.fermat_cer_plugin.layer.provider.yahoo.developer.bitdubai.version_1.database.YahooProviderDeveloperDatabaseFactory;
import com.bitdubai.fermat_cer_plugin.layer.provider.yahoo.developer.bitdubai.version_1.exceptions.CantInitializeYahooProviderDatabaseException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Alejandro Bicelis on 11/2/2015.
 */
@PluginInfo(createdBy = "abicelis", maintainerMail = "abicelis@gmail.com", platform = Platforms.CURRENCY_EXCHANGE_RATE_PLATFORM, layer = Layers.PROVIDER, plugin = Plugins.YAHOO)
public class ProviderYahooPluginRoot extends AbstractPlugin implements DatabaseManagerForDevelopers, CurrencyExchangeRateProviderManager {

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    YahooProviderDao dao;
    List<CurrencyPair> supportedCurrencyPairs = new ArrayList<>();


    /*
     * PluginRoot Constructor
     */
    public ProviderYahooPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }


    /*
     * Service interface implementation
     */
    @Override
    public void start() throws CantStartPluginException {
        System.out.println("PROVIDERYAHOO - PluginRoot START");

        //Yahoo Provider supports all FiatCurrencies
        supportedCurrencyPairs = CurrencyPairHelper.permuteAllFiatCurrencies();


        try {
            dao = new YahooProviderDao(pluginDatabaseSystem, pluginId, errorManager);
            dao.initialize();
            dao.initializeProvider("Yahoo");
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.YAHOO, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
            throw new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, FermatException.wrapException(e), null, null);
        }
        serviceStatus = ServiceStatus.STARTED;
    }




    /*
     * CurrencyExchangeRateProviderManager interface implementation
     */

    @Override
    public String getProviderName() throws CantGetProviderInfoException {
        return dao.getProviderName();
    }

    @Override
    public UUID getProviderId() throws CantGetProviderInfoException {
        return dao.getProviderId();
    }

    @Override
    public Collection<CurrencyPair> getSupportedCurrencyPairs() {
        return supportedCurrencyPairs;
    }

    @Override
    public boolean isCurrencyPairSupported(CurrencyPair currencyPair) throws IllegalArgumentException {
        for (CurrencyPair cp : supportedCurrencyPairs) {
            if (cp.equals(currencyPair))
                return true;
        }
        return false;
    }

    @Override
    public ExchangeRate getCurrentExchangeRate(CurrencyPair currencyPair) throws UnsupportedCurrencyPairException, CantGetExchangeRateException {

        if(!isCurrencyPairSupported(currencyPair))
            throw new UnsupportedCurrencyPairException();

        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22" +
                currencyPair.getFrom().getCode() + currencyPair.getTo().getCode() +
                "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        double purchasePrice = 0;
        double salePrice = 0;
        String aux;

        try{
            JSONObject json = new JSONObject(HttpReader.getHTTPContent(url));

            aux = json.getJSONObject("query").getJSONObject("results").getJSONObject("rate").get("Ask").toString();
            salePrice =  Double.valueOf(aux);

            aux = json.getJSONObject("query").getJSONObject("results").getJSONObject("rate").get("Bid").toString();
            purchasePrice =  Double.valueOf(aux);

        }catch (JSONException e) {
            errorManager.reportUnexpectedPluginException(Plugins.YAHOO, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
            throw new CantGetExchangeRateException(CantGetExchangeRateException.DEFAULT_MESSAGE,e,"Yahoo CER Provider","Cant Get exchange rate for" + currencyPair.getFrom().getCode() +  "-" + currencyPair.getTo().getCode());
        }


        ExchangeRateImpl exchangeRate = new ExchangeRateImpl(currencyPair.getFrom(), currencyPair.getTo(), purchasePrice, salePrice, (new Date().getTime() / 1000));
        try {
            dao.saveExchangeRate(exchangeRate);
        }catch (CantSaveExchangeRateException e) {
            errorManager.reportUnexpectedPluginException(Plugins.YAHOO, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRate getExchangeRateFromDate(CurrencyPair currencyPair, Calendar calendar) throws UnsupportedCurrencyPairException, CantGetExchangeRateException {
        throw new CantGetExchangeRateException("This provider does not support fetching non-current exchange rates");
    }

    @Override
    public Collection<ExchangeRate> getDailyExchangeRatesForPeriod(CurrencyPair currencyPair, Calendar startCalendar, Calendar endCalendar) throws UnsupportedCurrencyPairException, CantGetExchangeRateException {
        throw new CantGetExchangeRateException("This provider does not support fetching non-current exchange rates");
    }

    @Override
    public Collection<ExchangeRate> getQueriedExchangeRates(CurrencyPair currencyPair) throws UnsupportedCurrencyPairException, CantGetExchangeRateException {
        if(!isCurrencyPairSupported(currencyPair))
            throw new UnsupportedCurrencyPairException();

        return dao.getQueriedExchangeRateHistory(currencyPair);
    }



    /*
     * DatabaseManagerForDevelopers interface implementation
     */
    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        YahooProviderDeveloperDatabaseFactory factory = new YahooProviderDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId);
        return factory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        YahooProviderDeveloperDatabaseFactory factory = new YahooProviderDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId);
        return factory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        YahooProviderDeveloperDatabaseFactory factory = new YahooProviderDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId);
        List<DeveloperDatabaseTableRecord> tableRecordList = null;
        try {
            factory.initializeDatabase();
            tableRecordList = factory.getDatabaseTableContent(developerObjectFactory, developerDatabaseTable);
        } catch (CantInitializeYahooProviderDatabaseException e) {
            errorManager.reportUnexpectedPluginException(Plugins.YAHOO, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }
        return tableRecordList;
    }
}