package com.bitdubai.fermat_wpd_plugin.layer.engine.wallet_runtime.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.enums.WalletCategory;
import com.bitdubai.fermat_api.layer.all_definition.enums.WalletType;
import com.bitdubai.fermat_api.layer.all_definition.enums.WalletsPublicKeys;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Activity;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Footer;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Fragment;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Header;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.MainMenu;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.MenuItem;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.SideMenu;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.StatusBar;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Tab;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.TabStrip;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.TitleBar;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.Wizard;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.WizardPage;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Fragments;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.WizardTypes;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatFooter;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatStructure;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.dmp_network_service.CantCheckResourcesException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;
import com.bitdubai.fermat_wpd_api.all_definition.WalletNavigationStructure;
import com.bitdubai.fermat_wpd_api.all_definition.enums.EventType;
import com.bitdubai.fermat_wpd_api.layer.wpd_engine.wallet_runtime.exceptions.CantRemoveWalletNavigationStructureException;
import com.bitdubai.fermat_wpd_api.layer.wpd_engine.wallet_runtime.exceptions.WalletRuntimeExceptions;
import com.bitdubai.fermat_wpd_api.layer.wpd_engine.wallet_runtime.interfaces.WalletRuntimeManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_engine.wallet_runtime.interfaces.XML;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_factory.exceptions.CantGetWalletFactoryProjectNavigationStructureException;
import com.bitdubai.fermat_wpd_plugin.layer.engine.wallet_runtime.developer.bitdubai.version_1.event_handlers.WalletClosedEventHandler;
import com.bitdubai.fermat_wpd_plugin.layer.engine.wallet_runtime.developer.bitdubai.version_1.event_handlers.WalletInstalledEventHandler;
import com.bitdubai.fermat_wpd_plugin.layer.engine.wallet_runtime.developer.bitdubai.version_1.event_handlers.WalletNavigationStructureDownloadedHandler;
import com.bitdubai.fermat_wpd_plugin.layer.engine.wallet_runtime.developer.bitdubai.version_1.event_handlers.WalletOpenedEventHandler;
import com.bitdubai.fermat_wpd_plugin.layer.engine.wallet_runtime.developer.bitdubai.version_1.event_handlers.WalletUnnInstalledEventHandler;
import com.bitdubai.fermat_wpd_plugin.layer.engine.wallet_runtime.developer.bitdubai.version_1.exceptions.CantFactoryReset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * Created by Matias Furszyfer on 23.07.15.
 */

public class WalletRuntimeEnginePluginRoot extends AbstractPlugin implements
        WalletRuntimeManager,
        XML {


    /**
     * Path of xml files
     */
    final String NAVIGATION_STRUCTURE_FILE_PATH = "navigation_structure";
    List<FermatEventListener> listenersAdded = new ArrayList<>();
    /**
     * WalletRuntimeManager Interface member variables.
     */

    Map<String,WalletNavigationStructure> lstWalletNavigationStructureOpen;
    String lastWalletPublicKey;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;
    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    public WalletRuntimeEnginePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    @Override
    public void start() throws CantStartPluginException {
        /**
         * I will initialize the handling of com.bitdubai.platform events.
         */


        FermatEventListener fermatEventListener;
        FermatEventHandler fermatEventHandler;

        fermatEventListener = eventManager.getNewListener(EventType.WALLET_OPENED);
        fermatEventHandler = new WalletOpenedEventHandler();
        ((WalletOpenedEventHandler) fermatEventHandler).setWalletRuntimeManager(this);
        fermatEventListener.setEventHandler(fermatEventHandler);
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

        fermatEventListener = eventManager.getNewListener(EventType.WALLET_CLOSED);
        fermatEventHandler = new WalletClosedEventHandler();
        ((WalletClosedEventHandler) fermatEventHandler).setWalletRuntimeManager(this);
        fermatEventListener.setEventHandler(fermatEventHandler);
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

        fermatEventListener = eventManager.getNewListener(EventType.WALLET_INSTALLED);
        fermatEventHandler = new WalletInstalledEventHandler();
        ((WalletInstalledEventHandler) fermatEventHandler).setWalletRuntimeManager(this);
        fermatEventListener.setEventHandler(fermatEventHandler);
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

        fermatEventListener = eventManager.getNewListener(EventType.WALLET_UNINSTALLED);
        fermatEventHandler = new WalletUnnInstalledEventHandler();
        ((WalletUnnInstalledEventHandler) fermatEventHandler).setWalletRuntimeManager(this);
        fermatEventListener.setEventHandler(fermatEventHandler);
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

        FermatEventListener fermatEventListenerStructureDownloaded = eventManager.getNewListener(EventType.WALLET_RESOURCES_NAVIGATION_STRUCTURE_DOWNLOADED);
        FermatEventHandler fermatEventHandlerStructureDownloaded = new WalletNavigationStructureDownloadedHandler(this);
        fermatEventListenerStructureDownloaded.setEventHandler(fermatEventHandlerStructureDownloaded);
        eventManager.addListener(fermatEventListenerStructureDownloaded);
        listenersAdded.add(fermatEventListenerStructureDownloaded);

        lstWalletNavigationStructureOpen = new HashMap<>();
        /**
         * At this time the only thing I can do is a factory reset. Once there should be a possibility to add
         * functionality based on wallets downloaded by users this wont be an option.
         * * *
         *
         */
        try {

            loadLastWalletNavigationStructure();
            factoryReset();

        } catch (CantFactoryReset ex) {
            String message = CantStartPluginException.DEFAULT_MESSAGE;
            FermatException cause = ex;
            String context = "WalletNavigationStructure Runtime Start";

            String possibleReason = "Some null definition";
            throw new CantStartPluginException(message, cause, context, possibleReason);
        }

        this.serviceStatus = ServiceStatus.STARTED;

    }



    @Override
    public void stop() {

        /**
         * I will remove all the event listeners registered with the event manager.
         */

        for (FermatEventListener fermatEventListener : listenersAdded) {
            eventManager.removeListener(fermatEventListener);
        }

        listenersAdded.clear();
    }

    /**
     * WalletRuntime Interface implementation.
     */

    @Override
    public void recordNavigationStructure(String xmlText, String linkToRepo, String name, UUID skinId, String walletPublicKey) throws CantCheckResourcesException {
        //TODO: pido el navigationStrucutre del network service que sea y lo mando ahí
        //recordNavigationStructureIsNotExist(walletNavigationStructure);


        // For testing purpose

        WalletNavigationStructure walletNavigationStructure = new WalletNavigationStructure();

        //this.walletNavigationStructureOpen=(WalletNavigationStructure)XMLParser.parseXML(xmlText,walletNavigationStructure);

        PluginTextFile layoutFile = null;

        //String filename= skinId.toString()+"_"+name;
        String navigationStructureName = walletPublicKey + ".xml";

        try {

            layoutFile = pluginFileSystem.getTextFile(pluginId, NAVIGATION_STRUCTURE_FILE_PATH, navigationStructureName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);

        } catch (CantCreateFileException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            try {

                layoutFile = pluginFileSystem.createTextFile(pluginId, NAVIGATION_STRUCTURE_FILE_PATH, navigationStructureName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
                layoutFile.setContent(xmlText);
                layoutFile.persistToMedia();

            } catch (CantCreateFileException cantCreateFileException) {
                throw new CantCheckResourcesException("CAN'T CHECK REQUESTED RESOURCES", cantCreateFileException, "Error persist image file " + navigationStructureName, "");
            } catch (CantPersistFileException cantPersistFileException) {
                throw new CantCheckResourcesException("CAN'T CHECK REQUESTED RESOURCES", cantPersistFileException, "Error persist image file " + navigationStructureName, "");
            }
        }
    }

    @Override
    public boolean removeNavigationStructure(String publicKey) throws CantRemoveWalletNavigationStructureException {
        removeNavigationStructureXml(publicKey);
        return true;
    }

    @Override
    public WalletNavigationStructure getNavigationStructureFromWallet(String publicKey) throws WalletRuntimeExceptions {
        try {
            WalletNavigationStructure walletNavigationStructure = null;
            if (!lstWalletNavigationStructureOpen.containsKey(publicKey)){
                walletNavigationStructure = getNavigationStructure(publicKey);
                lstWalletNavigationStructureOpen.put(publicKey,walletNavigationStructure);
            }else{
                walletNavigationStructure = lstWalletNavigationStructureOpen.get(publicKey);
            }
            lastWalletPublicKey = publicKey;
            return walletNavigationStructure;
        } catch (Exception e) {
            throw new WalletRuntimeExceptions("WALLET RUNTIME GET WALLET", e, "wallet runtime not found the navigation structure for: " + publicKey, "");
        }
    }

    @Override
    public WalletNavigationStructure getLastWallet() {
        return lstWalletNavigationStructureOpen.get(lastWalletPublicKey);
    }


    @Override
    public WalletNavigationStructure getWallet(String publicKey) throws WalletRuntimeExceptions {
        //TODO: acá hay que poner una excepcion si no encuentra la wallet
        try {
            WalletNavigationStructure walletNavigationStructure = null;
            if (!lstWalletNavigationStructureOpen.containsKey(publicKey)){
                walletNavigationStructure = getNavigationStructure(publicKey);
                lstWalletNavigationStructureOpen.put(publicKey,walletNavigationStructure);
            }else{
                walletNavigationStructure = lstWalletNavigationStructureOpen.get(publicKey);
            }
            lastWalletPublicKey = publicKey;
            return walletNavigationStructure;
        } catch (Exception e) {
            throw new WalletRuntimeExceptions("WALLET RUNTIME GET WALLET", e, "wallet runtime not found the navigation structure for: " + publicKey, "");
        }

    }

    @Override
    public void recordNAvigationStructure(FermatStructure fermatStructure) {
        setNavigationStructureXml((WalletNavigationStructure)fermatStructure);
    }

    @Override
    public FermatStructure getLastApp() {
        return lstWalletNavigationStructureOpen.get(lastWalletPublicKey);
    }

    @Override
    public FermatStructure getAppByPublicKey(String appPublicKey) {
        return getNavigationStructure(appPublicKey);
    }

    @Override
    public Set<String> getListOfAppsPublicKey() {
        try{
            WalletNavigationStructure walletNavigationStructure = startWalletNavigationStructure();
            lstWalletNavigationStructureOpen.put (WalletsPublicKeys.CCP_REFERENCE_WALLET.getCode(),walletNavigationStructure);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            factoryReset();
        } catch (CantFactoryReset cantFactoryReset) {
            cantFactoryReset.printStackTrace();
        }
        return lstWalletNavigationStructureOpen.keySet();
    }

    /**
     * Here is where I actually generate the factory structure of the APP. This method is also useful to reset to the
     * factory structure.
     */
    private void factoryReset() throws CantFactoryReset {



        Activity runtimeActivity;
        Fragment runtimeFragment;
        WalletNavigationStructure runtimeWalletNavigationStructure;
        TitleBar runtimeTitleBar;
        SideMenu runtimeSideMenu;
        MainMenu runtimeMainMenu;
        MenuItem runtimeMenuItem;
        TabStrip runtimeTabStrip;
        Header runtimeHeader;
        StatusBar runtimeStatusBar;
        FermatFooter fermatFooter;

        Tab runtimeTab;

        String publicKey;

        runtimeWalletNavigationStructure = createAssetIssuerWalletNavigationStructure();
        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        //WalletNavigationStructure walletNavigationStructure= getNavigationStructure(publicKey);

        runtimeWalletNavigationStructure = createAssetUserWalletNavigationStructure();
        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        runtimeWalletNavigationStructure = createAssetRedeemPointWalletNavigationStructure();
        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        /**
         * CRYPTO BROKER WALLET
         */
        runtimeWalletNavigationStructure = createCryptoBrokerWalletNavigationStructure();
        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        /**
         * CRYPTO CUSTOMER WALLET
         */
        runtimeWalletNavigationStructure = createCryptoCustomerWalletNavigationStructure();
        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        /**
         * Banking Wallet
         * */
        runtimeWalletNavigationStructure = createBankMoneyWalletNavigationStructure();
        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        /**
         * Cash Wallet
         * */
        runtimeWalletNavigationStructure = createCashMoneyWalletNavigationStructure();
        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

         /**
         * tky Fan Wallet
         * */
        runtimeWalletNavigationStructure = createFanWalletNavigationStructure();
        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        /**
         * fin asset issuer
         */
        //try{

//            Activity runtimeActivity;
//            Fragment runtimeFragment;
//            WalletNavigationStructure runtimeWalletNavigationStructure;
//            TitleBar runtimeTitleBar;
//            SideMenu runtimeSideMenu;
//            MainMenu runtimeMainMenu;
//            MenuItem runtimeMenuItem;
//            TabStrip runtimeTabStrip;
//            StatusBar runtimeStatusBar;
//
//            Tab runtimeTab;
//
//            String publicKey;
//
//
//            /**
//             * WalletNavigationStructure Kids definition.
//             * */
//
//
//            runtimeWalletNavigationStructure = new WalletNavigationStructure();
//            //   runtimeSubApp.addWallet(runtimeWalletNavigationStructure);
//            publicKey="kids";
//            listWallets.put(publicKey, runtimeWalletNavigationStructure);
//            runtimeWalletNavigationStructure.setPublicKey(publicKey);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_VERSION_1_MAIN);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Kids Wallet");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#84DCF5");
//
//            runtimeSideMenu = new SideMenu();
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Menu item 1");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_VERSION_1_MAIN); // Solo es un ej.
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeActivity.setSideMenu(runtimeSideMenu);
//
//            runtimeTabStrip = new TabStrip();
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Profile");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_PROFILE);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Desktop");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_DESKTOP);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Contacts");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_CONTACTS);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Community");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_COMMUNITY);
//            runtimeTabStrip.addTab(runtimeTab);
//
//
//            runtimeTabStrip.setDividerColor(0xFFFFFFFF);
//
//
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_PROFILE);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_PROFILE,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_DESKTOP);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_DESKTOP,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_CONTACTS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_CONTACTS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_COMMUNITY);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_AGE_KIDS_ALL_BITDUBAI_COMMUNITY,runtimeFragment);
//
//
//            /**
//             * End of WalletNavigationStructure Kids fragments.
//             * */
//
//            /**
//             * Fermat Bitcoin Reference Walletc definition.
//             *
//             * Structure:
//             *
//             * TYPE: CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN
//             *
//             * TITLE: Fermat Bitcoin WalletNavigationStructure
//             *
//             * TAB STRIP:
//             *      *BALANCE - CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_BALANCE
//             *      *SEND - CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_SEND
//             *      *RECEIVE - CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_RECEIVE
//             *      *TRANSACTIONS - CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_TRANSACTIONS
//             *      *CONTACTS - CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_CONTACTS
//             *      *DATABASE TOOLS - CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_DATABASE_TOOLS
//             *      *LOG TOOLS - CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_LOG_TOOLS
//             */
//
//
//            runtimeWalletNavigationStructure = new WalletNavigationStructure();
//            publicKey="reference_wallet";
//            runtimeWalletNavigationStructure.setPublicKey(publicKey);
//            listWallets.put(publicKey, runtimeWalletNavigationStructure);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
//            runtimeActivity.setColor("#8bba9e");
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//            runtimeWalletNavigationStructure.addPosibleStartActivity(runtimeActivity.getType());
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Fermat Bitcoin Reference Wallet");
//
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#72af9c");
//            //runtimeActivity.setColor("#d07b62");
//
//
//            runtimeStatusBar = new com.bitdubai.fermat_api.layer.all_definition.navigation_structure.StatusBar();
//            runtimeStatusBar.setColor("#72af9c");
//
//            runtimeActivity.setStatusBar(runtimeStatusBar);
//
//
//            runtimeTabStrip = new TabStrip();
//
//            runtimeTabStrip.setTabsColor("#8bba9e");
//
//            runtimeTabStrip.setTabsTextColor("#FFFFFF");
//
//            runtimeTabStrip.setTabsIndicateColor("#72af9c");
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Balance");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_BALANCE);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Send");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_SEND);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Receive");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_RECEIVE);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Transactions");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_TRANSACTIONS);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Contacts");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_CONTACTS);
//            runtimeTabStrip.addTab(runtimeTab);
//
//
//
//
//
//            runtimeTabStrip.setDividerColor(0x72af9c);
//            //runtimeTabStrip.setBackgroundColor("#72af9c");
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_BALANCE);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_BALANCE,runtimeFragment);
//
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_SEND);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_SEND,runtimeFragment);
//
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_RECEIVE);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_RECEIVE,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_TRANSACTIONS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_TRANSACTIONS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_CONTACTS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_CONTACTS,runtimeFragment);
//
//
//            // Testing purpose Mati
//            //recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);
//
//            //getNavigationStructure("fasf");
//
//            //WalletRuntimeEngineDatabaseFactory walletRuntimeEngineDatabaseFactory = new WalletRuntimeEngineDatabaseFactory();
//
//            //WalletRuntimeNavigationStructureDao walletRuntimeNavigationStructureDao = new WalletRuntimeNavigationStructureDao();
//
//
//            /**
//             * End of WalletNavigationStructure basic fragments.
//             */
//
//
//
//
//
//            // WalletNavigationStructure adults
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_STORE_MAIN);
//            //   runtimeSubApp.addActivity(runtimeActivity);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_STORE_MAIN);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_STORE_MAIN,runtimeFragment);
//
//            runtimeWalletNavigationStructure = new WalletNavigationStructure();
//            // runtimeSubApp.addWallet(runtimeWalletNavigationStructure);
//            publicKey="adults_wallet";
//            runtimeWalletNavigationStructure.setPublicKey(publicKey);
//            listWallets.put(publicKey, runtimeWalletNavigationStructure);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_MAIN);
//            //runtimeSubApp.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Adults wallet");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeSideMenu = new SideMenu();
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Contacts");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Accounts");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_ACCOUNTS);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Banks");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_BANKS);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Coupons");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_COUPONS);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Discounts");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_DISCOUNTS);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Vouchers");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_VOUCHERS);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Gift Cards");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_GIFT_CARDS);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Clones");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CLONES);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Childs");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CHILDS);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeMenuItem = new MenuItem();
//            runtimeMenuItem.setLabel("Exit");
//            runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_MANAGER_MAIN);
//            runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//            runtimeActivity.setSideMenu(runtimeSideMenu);
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Home");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_HOME);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Balance");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_BALANCE);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Send");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SEND);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Receive");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_RECEIVE);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Shops");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOPS);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Refill");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_REFFIL);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Discounts");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_DISCOUNTS);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTabStrip.setDividerColor(0xFFFFFFFF);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_HOME);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_HOME,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_BALANCE);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_BALANCE,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SEND);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SEND,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_RECEIVE);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_RECEIVE,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOPS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOPS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_REFFIL);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_REFFIL,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_DISCOUNTS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_DISCOUNTS,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS_CHAT);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("");
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_CHAT);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_CHAT,runtimeFragment);
//
//
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Contacts");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("");
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS,runtimeFragment);
//
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_AVAILABLE_BALANCE);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Available balance");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("");
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_ADULTS_ALL_AVAILABLE_BALANCE);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_ADULTS_ALL_AVAILABLE_BALANCE,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_SEND_HISTORY);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Sent History");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("");
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_ADULTS_ALL_SEND_HISTORY);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_ADULTS_ALL_SEND_HISTORY,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS_SEND);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Send To Contact");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("");
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_SEND);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_SEND,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_CHAT_TRX);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("");
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CHAT_TRX);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CHAT_TRX,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED_HISTORY);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("");
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED_HISTORY);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED_HISTORY,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS_RECEIVE);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Receive From Contact");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("");
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_RECEIVE);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_RECEIVE,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_DAILY_DISCOUNT);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_ADULTS_ALL_DAILY_DISCOUNT);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_ADULTS_ALL_DAILY_DISCOUNT,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_DAILY_DISCOUNT);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_ADULTS_ALL_DAILY_DISCOUNT);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_ADULTS_ALL_DAILY_DISCOUNT,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_MONTHLY_DISCOUNT);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_ADULTS_ALL_MONTHLY_DISCOUNT);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_ADULTS_ALL_MONTHLY_DISCOUNT,runtimeFragment);
//
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS_NEW_SEND);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Send to new contact");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_NEW_SEND);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_NEW_SEND,runtimeFragment);
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_CONTACTS_NEW_RECEIVE);
//            runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Receive from new contact");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//            runtimeActivity.setColor("#F0E173");
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_NEW_RECEIVE);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_CONTACTS_NEW_RECEIVE,runtimeFragment);
//
//            /**
//             * End of WalletNavigationStructure Adults tabs.
//             */
//
//            runtimeActivity= new Activity();
//            runmeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_SHOPS);
//            runtimeActivity.setColor("#76dc4a");
//            //  runtimeSubApp.addActivity(runtimeActivity);
//
//
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("My Shop");
//
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Shop");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_SHOP);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Products");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_PRODUCTS);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Reviews");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_REVIEWS);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Chat");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_CHAT);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("History");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_HISTORY);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Map");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_MAP);
//            runtimeTabStrip.addTab(runtimeTab);
//
//
//            runtimeTabStrip.setDividerColor(0xFFFFFFFF);
//            runtimeTabStrip.setIndicatorColor(0xFFFFFFFF);
//            runtimeTabStrip.setIndicatorHeight(9);
//            runtimeTabStrip.setBackgroundColor(0xFF76dc4a);
//            runtimeTabStrip.setTextColor(0xFFFFFFFF);
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_SHOP);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_SHOP,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_PRODUCTS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_PRODUCTS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_REVIEWS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_REVIEWS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_CHAT);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_CHAT,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_HISTORY);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_HISTORY,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_MAP);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_SHOP_MAP,runtimeFragment);
//            /**
//             * End of SHOPS tabs.
//             */
//
//
//
//            //Account Details
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_ACCOUNT_DETAIL);
//            runtimeActivity.setColor("#F0E173");
//            //runtimeSubApp.addActivity(runtimeActivity);
//
//
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Account details");
//
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Debits");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_DEBITS);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Credits");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNT_CREDITS);
//            runtimeTabStrip.addTab(runtimeTab);
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("All");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_ALL);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_DEBITS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_DEBITS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNT_CREDITS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNT_CREDITS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_ALL);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_ALL,runtimeFragment);
//
//
//
//        /*------------------------------*/
//
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_REFFILS);
//            // runtimeSubApp.addActivity(runtimeActivity);
////-----------------------------------------------------------------------------------
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED);
//            //runtimeSubApp.addActivity(runtimeActivity);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_ADULTS_ALL_REQUESTS_RECEIVED,runtimeFragment);
////------------------------------------------------------------------------------------
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_ADULTS_ALL_REQUEST_SEND);
//            //runtimeSubApp.addActivity(runtimeActivity);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_ADULTS_ALL_REQUEST_SEND);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_ADULTS_ALL_REQUEST_SEND,runtimeFragment);
////-----------------------------------------------------------------------------------
//            runtimeActivity= new Activity();
//            runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_ADULTS_ALL_ACCOUNTS);
//            //runtimeSubApp.addActivity(runtimeActivity);
//
//            runtimeTitleBar = new TitleBar();
//            runtimeTitleBar.setLabel("Account details");
//            runtimeActivity.setTitleBar(runtimeTitleBar);
//
//            runtimeTabStrip = new TabStrip();
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Debits");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_DEBITS);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("Credits");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNT_CREDITS);
//            runtimeTabStrip.addTab(runtimeTab);
//
//            runtimeTab = new Tab();
//            runtimeTab.setLabel("All");
//            runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_ALL);
//            runtimeTabStrip.addTab(runtimeTab);
//
//
//            runtimeActivity.setTabStrip(runtimeTabStrip);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_DEBITS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_DEBITS,runtimeFragment);
//            //listFragments.put(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_DEBITS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNT_CREDITS);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNT_CREDITS,runtimeFragment);
//
//            runtimeFragment = new Fragment();
//            runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_ALL);
//            runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_ADULTS_ALL_BITDUBAI_ACCOUNTS_ALL,runtimeFragment);
//            //  listApps.put(Apps.CRYPTO_WALLET_PLATFORM,runtimeApp);
//            //lastApp = Apps.CRYPTO_WALLET_PLATFORM;
//            /**
//             * End of WalletNavigationStructure Accounts tabs.
//             */

        /*}catch(Exception e){
            String message = CantFactoryReset.DEFAULT_MESSAGE;
            FermatException cause = FermatException.wrapException(e);
            String context = "Error on method Factory Reset, setting the structure of the apps";
            String possibleReason = "some null definition";
            throw new CantFactoryReset(message, cause, context, possibleReason);

        }*/

    }

    //TODO remove this method
//    private WalletNavigationStructure createAssetIssuerWalletNavigationStructure1() {
//        /**
//         * Asset issuer
//         */
//        WalletNavigationStructure runtimeWalletNavigationStructure = new WalletNavigationStructure();
//        runtimeWalletNavigationStructure = new WalletNavigationStructure();
//        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
//        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
//        String publicKey = "asset_issuer";
//        runtimeWalletNavigationStructure.setPublicKey(publicKey);
//        //listWallets.put(publicKey, runtimeWalletNavigationStructure);
//
//
//        Activity runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
//        runtimeActivity.setColor("#8bba9e");
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//        runtimeWalletNavigationStructure.addPosibleStartActivity(runtimeActivity.getType());
//
//        TitleBar runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("asset issuer wallet");
//        runtimeTitleBar.setLabelSize(16);
//
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//        runtimeActivity.setColor("#72af9c");
//
//        StatusBar runtimeStatusBar = new com.bitdubai.fermat_api.layer.all_definition.navigation_structure.StatusBar();
//        runtimeStatusBar.setColor("#72af9c");
//
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//
//        Fragment runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment("DWAIMA");
//
//        return runtimeWalletNavigationStructure;
//    }

    private WalletNavigationStructure createAssetIssuerWalletNavigationStructure() {
        WalletNavigationStructure runtimeWalletNavigationStructure;
        Activity runtimeActivity;
        TitleBar runtimeTitleBar;
        TabStrip runtimeTabStrip;
        Tab runtimeTab;
        StatusBar runtimeStatusBar;
        Header runtimeHeader;
        Fragment runtimeFragment;

        final String publicKey = WalletsPublicKeys.DAP_ISSUER_WALLET.getCode();

        final String statusBarColor = "#5C6E81";
        final String titleBarLabelColor = "#ffffff";
        final int titleBarLabelSize = 20;


        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        lstWalletNavigationStructureOpen.put(publicKey,runtimeWalletNavigationStructure);

        // Activity: Home
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY.getCode());
//        runtimeActivity.setColor("#627284");
        runtimeActivity.setSideMenu(loadSideMenuAssetIssuerWallet(publicKey));

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY.getCode());

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("My Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor("#ffffff");
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setColor("#e2e4e6");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY.getKey());

        // Activity: Settings
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_SETTINGS_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_SETTINGS_ACTIVITY.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(loadSideMenuAssetIssuerWallet(publicKey));
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Settings");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor("#ffffff");
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setColor("#e2e4e6");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_MAIN_SETTINGS_ACTIVITY.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_MAIN_SETTINGS_ACTIVITY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_MAIN_SETTINGS_ACTIVITY.getKey());

        // Activity: Settings_Network
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_SETTINGS_MAIN_NETWORK_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_SETTINGS_MAIN_NETWORK_ACTIVITY.getCode());
//        runtimeActivity.setColor("#627284");
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Network");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIconName("back");
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setColor("#e2e4e6");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_SETTINGS_MAIN_NETWORK_ACTIVITY.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_SETTINGS_MAIN_NETWORK_ACTIVITY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_SETTINGS_MAIN_NETWORK_ACTIVITY.getKey());

        // Activity: Settings_Notifications
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_SETTINGS_NOTIFICATIONS_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_SETTINGS_NOTIFICATIONS_ACTIVITY.getCode());
//        runtimeActivity.setColor("#627284");
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Notifications");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIconName("back");
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setColor("#e2e4e6");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_SETTINGS_NOTIFICATIONS_ACTIVITY.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_SETTINGS_NOTIFICATIONS_ACTIVITY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_SETTINGS_NOTIFICATIONS_ACTIVITY.getKey());

//TODO en Deshuso por ahora
//        // Activity: History
//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_HISTORY_ACTIVITY);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_HISTORY_ACTIVITY.getCode());
//        runtimeActivity.setSideMenu(runtimeSideMenu);
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("History");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setTitleColor("#ffffff");
////        runtimeTitleBar.setIsTitleTextStatic(true);
//        runtimeTitleBar.setColor("#e2e4e6");
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        // Side Menu
//        runtimeSideMenu = new SideMenu();
//        runtimeSideMenu.setBackgroundColor("#e2e4e6");
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("My Assets");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("History");
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_ISSUER_HISTORY_ACTIVITY);
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("Statistics");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_ISSUER_STADISTICS_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_HISTORY_ACTIVITY.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_HISTORY_ACTIVITY.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_HISTORY_ACTIVITY.getKey());
//
//        // Activity: Stadistics
//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_STADISTICS_ACTIVITY);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_STADISTICS_ACTIVITY.getCode());
//        runtimeActivity.setSideMenu(runtimeSideMenu);
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("Statistics");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setTitleColor("#ffffff");
//        runtimeTitleBar.setColor("#e2e4e6");
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        // Side Menu
//        runtimeSideMenu = new SideMenu();
//        runtimeSideMenu.setBackgroundColor("#e2e4e6");
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("My Assets");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("History");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_ISSUER_HISTORY_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("Statistics");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_ISSUER_STADISTICS_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_STADISTICS_ACTIVITY.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_STADISTICS_ACTIVITY.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_STADISTICS_ACTIVITY.getKey());

        // Activity: Detail
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_ASSET_ISSUER_WALLET_ASSET_DETAIL);
        runtimeActivity.setActivityType(Activities.DAP_ASSET_ISSUER_WALLET_ASSET_DETAIL.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);
//        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Asset Detail");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeHeader = new Header();
        runtimeHeader.setLabel("root");
        runtimeActivity.setHeader(runtimeHeader);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DETAIL.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DETAIL.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DETAIL.getKey());

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_USER_DELIVERY_LIST);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_USER_DELIVERY_LIST.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_ASSET_ISSUER_WALLET_ASSET_DETAIL);
        runtimeActivity.setBackPublicKey(publicKey);
//        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Delivered Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_USER_DELIVERY_LIST.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_USER_DELIVERY_LIST.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_USER_DELIVERY_LIST.getKey());


        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_ASSET_ISSUER_WALLET_ASSET_DETAIL);
        runtimeActivity.setBackPublicKey(publicKey);
//        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Deliver Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY.getKey());

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_SELECT_USERS_GROUPS);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_SELECT_USERS_GROUPS.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY);
        runtimeActivity.setBackPublicKey(publicKey);
//        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTabStrip = new TabStrip();
        runtimeTabStrip.setTabsColor("#535C65");//808A96
        runtimeTabStrip.setTabsTextColor("#FFFFFF");
        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");
        runtimeTabStrip.setBackgroundColor(8424086);
//        runtimeTabStrip.setDividerColor(16777215);
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Users");
        runtimeTab.setFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TAB_SELECT_USERS);
        runtimeTabStrip.addTab(runtimeTab);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TAB_SELECT_USERS.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TAB_SELECT_USERS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TAB_SELECT_USERS.getKey());

        runtimeTab = new Tab();
        runtimeTab.setLabel("Groups");
        runtimeTab.setFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TAB_SELECT_GROUPS);
        runtimeTabStrip.addTab(runtimeTab);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TAB_SELECT_GROUPS.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TAB_SELECT_GROUPS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TAB_SELECT_GROUPS.getKey());

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Select Users/Groups");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TABS.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TABS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_ASSET_DELIVERY_TABS.getKey());


        //Assets Details Appropriate
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_USER_APPROPIATE_LIST);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_USER_APPROPIATE_LIST.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_ASSET_ISSUER_WALLET_ASSET_DETAIL);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Appropiated Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_USER_APPROPIATE_LIST.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_USER_APPROPIATE_LIST.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_USER_APPROPIATE_LIST.getKey());

        // -DAP Asset Issuer / Asset User Aproppiated List End

        /*  Activity DAP Asset Issuer / Assets User Redeemed List */
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_ISSUER_USER_REDEEMED_LIST);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_ISSUER_USER_REDEEMED_LIST.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_ASSET_ISSUER_WALLET_ASSET_DETAIL);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Redeemed Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_ISSUER_USER_REDEEMED_LIST.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_ISSUER_USER_REDEEMED_LIST.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_ISSUER_USER_REDEEMED_LIST.getKey());
/*   Activity DAP Asset Issuer / Assets User Redeemed List END*/

        return runtimeWalletNavigationStructure;
    }

    private WalletNavigationStructure createAssetUserWalletNavigationStructure() {
        WalletNavigationStructure runtimeWalletNavigationStructure;
        Activity runtimeActivity;
        TitleBar runtimeTitleBar;
        StatusBar runtimeStatusBar;
        Header runtimeHeader;
        Fragment runtimeFragment;
        TabStrip runtimeTabStrip;
        Tab runtimeTab;

        final String publicKey = WalletsPublicKeys.DAP_USER_WALLET.getCode();

        final String statusBarColor = "#1371A7";
        final String titleBarLabelColor = "#ffffff";
        final int titleBarLabelSize = 20;


        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        lstWalletNavigationStructureOpen.put(publicKey,runtimeWalletNavigationStructure);

        // Activity: Home
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_V3_HOME);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_V3_HOME.getCode());
//        runtimeActivity.setColor("#627284");
        runtimeActivity.setSideMenu(loadSideMenuAssetUserWallet(publicKey));

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(Activities.DAP_WALLET_ASSET_USER_V3_HOME.getCode());

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("My Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setTitleColor(titleBarLabelColor);
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

//        runtimeHeader = new Header();
//        runtimeHeader.setLabel("Header");
//        runtimeActivity.setHeader(runtimeHeader);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_V3_HOME.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_V3_HOME.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_V3_HOME.getKey());
//TODO en Deshuso por ahora
//        // Activity: History
//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_HISTORY_ACTIVITY);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_HISTORY_ACTIVITY.getCode());
//        runtimeActivity.setSideMenu(runtimeSideMenu);
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("History");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setTitleColor("#ffffff");
////        runtimeTitleBar.setIsTitleTextStatic(true);
//        runtimeTitleBar.setColor("#e2e4e6");
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        // Side Menu
//        runtimeSideMenu = new SideMenu();
//        runtimeSideMenu.setBackgroundColor("#e2e4e6");
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("My Assets");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_USER_MAIN_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("History");
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_USER_HISTORY_ACTIVITY);
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_HISTORY_ACTIVITY.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_HISTORY_ACTIVITY.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_HISTORY_ACTIVITY.getKey());

//         Activity: Detail
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_ASSET_DETAIL);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_ASSET_DETAIL.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_V3_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
//        runtimeActivity.setColor("#1189a5");

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Asset Detail");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeHeader = new Header();
        runtimeHeader.setLabel("root");
        runtimeActivity.setHeader(runtimeHeader);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_ASSET_DETAIL_TRANSACTIONS.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_DETAIL_TRANSACTIONS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_DETAIL_TRANSACTIONS.getKey());

        // BEG Settings activity from DAP WALLET USER
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_SETTINGS_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_SETTINGS_ACTIVITY.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_V3_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(loadSideMenuAssetUserWallet(publicKey));

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Settings");
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(statusBarColor);
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_ACTIVITY.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_ACTIVITY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_ACTIVITY.getKey());

        // BEG Settings Network activity from DAP WALLET USER
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_SETTINGS_MAIN_NETWORK);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_SETTINGS_MAIN_NETWORK.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Network");
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(statusBarColor);
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_MAIN_NETWORK.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_MAIN_NETWORK.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_MAIN_NETWORK.getKey());

        // END Settings Network activity from DAP WALLET USER

        // BEG Settings Notifications activity from DAP WALLET USER
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_SETTINGS_NOTIFICATIONS);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_SETTINGS_NOTIFICATIONS.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Notifications");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(statusBarColor);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_NOTIFICATIONS.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_NOTIFICATIONS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_SETTINGS_NOTIFICATIONS.getKey());

        // END Settings Notifications activity from DAP WALLET USER

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_ASSET_REDEEM);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_ASSET_REDEEM.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_V3_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
//        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Redeem Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIconName("back");
//        runtimeTitleBar.setTitleColor(titleBarLabelColor);
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_ASSET_REDEEM.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_REDEEM.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_REDEEM.getKey());

//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_ASSET_REDEEM_SELECT_REDEEMPOINTS);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_ASSET_REDEEM_SELECT_REDEEMPOINTS.getCode());
//        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_ASSET_REDEEM);
//        runtimeActivity.setBackPublicKey(publicKey);
////        runtimeActivity.setColor("#1189a5");
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("Select Redeem Points");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setIconName("back");
////        runtimeTitleBar.setTitleColor(titleBarLabelColor);
////        runtimeTitleBar.setIsTitleTextStatic(true);
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_ASSET_REDEEM_SELECT_REDEEMPOINTS.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_REDEEM_SELECT_REDEEMPOINTS.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_REDEEM_SELECT_REDEEMPOINTS.getKey());

        //INI ASSET SELL ACTIVITY

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_ASSET_SELL_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_ASSET_SELL_ACTIVITY.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_V3_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
//        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Sell Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIconName("back");
//        runtimeTitleBar.setTitleColor(titleBarLabelColor);
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_ASSET_SELL_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_SELL_FRAGMENT.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_SELL_FRAGMENT.getKey());

        //INI SELECT USERS FRAGMENT

//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_ASSET_SELL_SELECT_USERS_ACTIVITY);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_ASSET_SELL_SELECT_USERS_ACTIVITY.getCode());
//        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_ASSET_SELL_ACTIVITY);
//        runtimeActivity.setBackPublicKey(publicKey);
////        runtimeActivity.setColor("#1189a5");
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("Select User");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setIconName("back");
////        runtimeTitleBar.setTitleColor(titleBarLabelColor);
////        runtimeTitleBar.setIsTitleTextStatic(true);
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_ASSET_SELL_SELECT_USERS_FRAGMENT.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_SELL_SELECT_USERS_FRAGMENT.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_SELL_SELECT_USERS_FRAGMENT.getKey());

        //INI ASSET TRANSFER ACTIVITY

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_ACTIVITY.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_V3_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
//        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Transfer Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIconName("back");
//        runtimeTitleBar.setTitleColor(titleBarLabelColor);
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_FRAGMENT.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_FRAGMENT.getKey());

        //INI TRANSFER SELECT USERS FRAGMENT

//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_SELECT_USERS_ACTIVITY);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_SELECT_USERS_ACTIVITY.getCode());
//        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_ACTIVITY);
//        runtimeActivity.setBackPublicKey(publicKey);
////        runtimeActivity.setColor("#1189a5");
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("Select Users");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setIconName("back");
////        runtimeTitleBar.setTitleColor(titleBarLabelColor);
////        runtimeTitleBar.setIsTitleTextStatic(true);
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_SELECT_USERS_FRAGMENT.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_SELECT_USERS_FRAGMENT.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_TRANSFER_SELECT_USERS_FRAGMENT.getKey());
//        //INI ASSET NEGOTIATION DETAIL
//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_ASSET_USER_ASSET_NEGOTIATION_DETAIL_ACTIVITY);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_ASSET_USER_ASSET_NEGOTIATION_DETAIL_ACTIVITY.getCode());
//        runtimeActivity.setBackActivity(Activities.DAP_WALLET_ASSET_USER_MAIN_ACTIVITY);
//        runtimeActivity.setBackPublicKey(publicKey);
////        runtimeActivity.setColor("#1189a5");
//
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("Asset Negotiation Detail");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setIconName("back");
////        runtimeTitleBar.setTitleColor(titleBarLabelColor);
////        runtimeTitleBar.setIsTitleTextStatic(true);
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        /*runtimeHeader = new Header();
//        runtimeHeader.setLabel("root");
//        runtimeActivity.setHeader(runtimeHeader);*/
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_ASSET_USER_ASSET_NEGOTIATION_DETAIL_FRAGMENT.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_NEGOTIATION_DETAIL_FRAGMENT.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_ASSET_USER_ASSET_NEGOTIATION_DETAIL_FRAGMENT.getKey());
//        //END ASSET NEGOTIATION DETAIL

        return runtimeWalletNavigationStructure;
    }

    private WalletNavigationStructure createAssetRedeemPointWalletNavigationStructure() {
        WalletNavigationStructure runtimeWalletNavigationStructure;
        SideMenu runtimeSideMenu;
        MenuItem runtimeMenuItem;
        Activity runtimeActivity;
        TitleBar runtimeTitleBar;
        StatusBar runtimeStatusBar;
        Header runtimeHeader;
        Fragment runtimeFragment;
        TabStrip runtimeTabStrip;
        Tab runtimeTab;

        final String publicKey = WalletsPublicKeys.DAP_REDEEM_WALLET.getCode();

        final String statusBarColor = "#005580";
        final String titleBarLabelColor = "#ffffff";
        final int titleBarLabelSize = 20;


        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        lstWalletNavigationStructureOpen.put(publicKey,runtimeWalletNavigationStructure);

        // Activity: Home
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY.getCode());
//        runtimeActivity.setColor("#627284");
        runtimeActivity.setSideMenu(loadSideMenuAssetRedeemPoint(publicKey));
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(Activities.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY.getCode());

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("My Assets");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor("#ffffff");
//        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setColor("#005580");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

//        runtimeHeader = new Header();
//        runtimeHeader.setLabel("Header");
//        runtimeActivity.setHeader(runtimeHeader);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY.getKey());

        // INI Settings activity from DAP REDEEMPOINT
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_ACTIVITY.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY);
        runtimeActivity.setSideMenu(loadSideMenuAssetRedeemPoint(publicKey));
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(loadSideMenuAssetRedeemPoint(publicKey));
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Settings");
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(statusBarColor);
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_ACTIVITY.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_ACTIVITY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_ACTIVITY.getKey());

        // INI Settings Network activity from redeemPoint

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_REDEEM_POINT_SETTINGS_MAIN_NETWORK);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_REDEEM_POINT_SETTINGS_MAIN_NETWORK.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Network");
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(statusBarColor);
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_REDEEM_POINT_SETTINGS_MAIN_NETWORK.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_REDEEM_POINT_SETTINGS_MAIN_NETWORK.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_REDEEM_POINT_SETTINGS_MAIN_NETWORK.getKey());

        // END Settings Network activity from redeempoint

        // INI Settings Notifications activity from redeempoint
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_NOTIFICATIONS);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_NOTIFICATIONS.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Notifications");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(statusBarColor);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_NOTIFICATIONS.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_NOTIFICATIONS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_NOTIFICATIONS.getKey());

//TODO en Deshuso por ahora
//        // Activity: History
//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_REDEEM_POINT_HISTORY_ACTIVITY);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_REDEEM_POINT_HISTORY_ACTIVITY.getCode());
//        runtimeActivity.setSideMenu(runtimeSideMenu);
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("History");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setTitleColor("#ffffff");
////        runtimeTitleBar.setIsTitleTextStatic(true);
//        runtimeTitleBar.setColor("#005580");
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        // Side Menu
//        runtimeSideMenu = new SideMenu();
//        runtimeSideMenu.setBackgroundColor("#e2e4e6");
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("My Assets");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("History");
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_REDEEM_POINT_HISTORY_ACTIVITY);
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("Statistics");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_REDEEM_POINT_STADISTICS_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_REDEEM_POINT_HISTORY_ACTIVITY.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_REDEEM_POINT_HISTORY_ACTIVITY.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_REDEEM_POINT_HISTORY_ACTIVITY.getKey());
//
//        // Activity: Stadistics
//        runtimeActivity = new Activity();
//        runtimeActivity.setType(Activities.DAP_WALLET_REDEEM_POINT_STADISTICS_ACTIVITY);
//        runtimeActivity.setActivityType(Activities.DAP_WALLET_REDEEM_POINT_STADISTICS_ACTIVITY.getCode());
//        runtimeActivity.setSideMenu(runtimeSideMenu);
//        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//
//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setLabel("Statistics");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setTitleColor("#ffffff");
//        runtimeTitleBar.setColor("#005580");
//        runtimeActivity.setTitleBar(runtimeTitleBar);
//
//        runtimeStatusBar = new StatusBar();
//        runtimeStatusBar.setColor(statusBarColor);
//        runtimeActivity.setStatusBar(runtimeStatusBar);
//
//        // Side Menu
//        runtimeSideMenu = new SideMenu();
//        runtimeSideMenu.setBackgroundColor("#e2e4e6");
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("My Assets");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("History");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_REDEEM_POINT_HISTORY_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeMenuItem = new MenuItem();
//        runtimeMenuItem.setLabel("Statistics");
//        runtimeMenuItem.setAppLinkPublicKey(publicKey);
//        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_REDEEM_POINT_STADISTICS_ACTIVITY);
//        runtimeSideMenu.addMenuItem(runtimeMenuItem);
//
//        runtimeFragment = new Fragment();
//        runtimeFragment.setType(Fragments.DAP_WALLET_REDEEM_POINT_STADISTICS_ACTIVITY.getKey());
//        runtimeActivity.addFragment(Fragments.DAP_WALLET_REDEEM_POINT_STADISTICS_ACTIVITY.getKey(), runtimeFragment);
//        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_REDEEM_POINT_STADISTICS_ACTIVITY.getKey());

        // -- Redeem Point Detail Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.DAP_WALLET_REDEEM_POINT_DETAILS_ACTIVITY);
        runtimeActivity.setActivityType(Activities.DAP_WALLET_REDEEM_POINT_DETAILS_ACTIVITY.getCode());
        runtimeActivity.setBackActivity(Activities.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Asset Detail");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setColor("#005580");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeHeader = new Header();
        runtimeHeader.setLabel("root");
        runtimeActivity.setHeader(runtimeHeader);

        runtimeTitleBar.setLabelSize(30);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeTabStrip = new TabStrip();
        runtimeTabStrip.setTabsColor("#1278a6");
        runtimeTabStrip.setTabsTextColor("#FFFFFF");
        runtimeTabStrip.setTabsIndicateColor("#3ec8e8");
        runtimeTabStrip.setDividerColor(0x72af9c);
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Transactions");
        runtimeTab.setFragment(Fragments.DAP_WALLET_REDEEM_POINT_DETAILS_TRANSACTIONS_TAB);
        runtimeTabStrip.addTab(runtimeTab);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_REDEEM_POINT_DETAILS_TRANSACTIONS_TAB.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_REDEEM_POINT_DETAILS_TRANSACTIONS_TAB.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.DAP_WALLET_REDEEM_POINT_DETAILS_TRANSACTIONS_TAB.getKey());

        runtimeTab = new Tab();
        runtimeTab.setLabel("Redeemed Users");
        runtimeTab.setFragment(Fragments.DAP_WALLET_REDEEM_POINT_DETAILS_USERS_TAB);
        runtimeTabStrip.addTab(runtimeTab);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.DAP_WALLET_REDEEM_POINT_DETAILS_USERS_TAB.getKey());
        runtimeActivity.addFragment(Fragments.DAP_WALLET_REDEEM_POINT_DETAILS_USERS_TAB.getKey(), runtimeFragment);
        // -- Redeem Point Detail Activity END

        return runtimeWalletNavigationStructure;
    }

    private SideMenu loadSideMenuAssetIssuerWallet(String publicKey) {

        SideMenu runtimeSideMenu = new SideMenu();
        runtimeSideMenu.setBackgroundColor("#5C6E81");
//        runtimeSideMenu.setNavigationIconColor("#ffffff");
        runtimeSideMenu.setHasFooter(true);

        MenuItem runtimeMenuItem = new MenuItem();

        runtimeMenuItem.setLabel("Home");
        runtimeMenuItem.setSelected(true);
        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_ACTIVITY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Settings");
        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_ISSUER_MAIN_SETTINGS_ACTIVITY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        return runtimeSideMenu;
    }

    private SideMenu loadSideMenuAssetUserWallet(String publicKey) {

        SideMenu runtimeSideMenu = new SideMenu();
        runtimeSideMenu.setBackgroundColor("#1371A7");
//        runtimeSideMenu.setNavigationIconColor("#ffffff");
        runtimeSideMenu.setHasFooter(true);

        MenuItem runtimeMenuItem = new MenuItem();

        runtimeMenuItem.setLabel("Home");
        runtimeMenuItem.setSelected(true);
        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_USER_V3_HOME);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Settings");
        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_ASSET_USER_SETTINGS_ACTIVITY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        return runtimeSideMenu;
    }

    private SideMenu loadSideMenuAssetRedeemPoint(String publicKey) {

        SideMenu runtimeSideMenu = new SideMenu();
        runtimeSideMenu.setBackgroundColor("#005580");
//        runtimeSideMenu.setNavigationIconColor("#ffffff");
        runtimeSideMenu.setHasFooter(true);

        MenuItem runtimeMenuItem = new MenuItem();

        runtimeMenuItem.setLabel("Home");
        runtimeMenuItem.setSelected(true);
        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_REDEEM_POINT_MAIN_ACTIVITY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Settings");
        runtimeMenuItem.setLinkToActivity(Activities.DAP_WALLET_REDEEM_POINT_ASSET_SETTINGS_ACTIVITY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        return runtimeSideMenu;
    }

    private WalletNavigationStructure createCryptoBrokerWalletNavigationStructure() {
        WalletNavigationStructure runtimeWalletNavigationStructure;
        SideMenu runtimeSideMenu;
        MenuItem runtimeMenuItem;
        Activity runtimeActivity;
        TitleBar runtimeTitleBar;
        StatusBar runtimeStatusBar;
        Header runtimeHeader;
        Fragment runtimeFragment;
        TabStrip runtimeTabStrip;
        Tab runtimeTab;

        final String publicKey = WalletsPublicKeys.CBP_CRYPTO_BROKER_WALLET.getCode();

        final String statusBarColor = "#1375a7";
        final String titleBarColor = "#254478";
        final String titleBarTitleColor = "#ffffff";
        final int titleBarTextSize = 16;

        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setPlatform(Platforms.CRYPTO_BROKER_PLATFORM);
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        lstWalletNavigationStructureOpen.put(publicKey,runtimeWalletNavigationStructure);


        // Side Menu
        runtimeSideMenu = new SideMenu();
        runtimeSideMenu.setHasFooter(true);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Home");
        runtimeMenuItem.setLinkToActivity(Activities.CBP_CRYPTO_BROKER_WALLET_HOME);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Contracts History");
        runtimeMenuItem.setLinkToActivity(Activities.CBP_CRYPTO_BROKER_WALLET_CONTRACTS_HISTORY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Earnings");
        runtimeMenuItem.setLinkToActivity(Activities.CBP_CRYPTO_BROKER_WALLET_EARNINGS);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Settings");
        runtimeMenuItem.setLinkToActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        // WIZARD

        // step 1 - Set Merchandises
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES.getCode());
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES.getCode());

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES.getKey());

        // step 2 - Set Earnings
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_EARNINGS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_EARNINGS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_MERCHANDISES);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_EARNINGS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_EARNINGS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_EARNINGS.getKey());

        // step 4 - Set Providers
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_PROVIDERS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_PROVIDERS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_EARNINGS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_PROVIDERS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_PROVIDERS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_PROVIDERS.getKey());

        // step 5 - Set Locations
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_LOCATIONS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_LOCATIONS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_EARNINGS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_LOCATIONS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_LOCATIONS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_LOCATIONS.getKey());

        // step 6 - Set Bank Accounts (If bank wallet has been set as Stock)
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_BANK_ACCOUNT);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SET_BANK_ACCOUNT.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_LOCATIONS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_BANK_ACCOUNT.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_BANK_ACCOUNT.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SET_BANK_ACCOUNT.getKey());

        // Create New Location in Wizard Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SET_LOCATIONS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Create Location");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setColor(titleBarColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD.getKey());


        // Activity: Home
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_HOME);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_HOME.getCode());
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Broker Wallet");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeHeader = new Header();
        runtimeHeader.setLabel("Market rate");
        runtimeHeader.setHasExpandable(true);
        runtimeActivity.setHeader(runtimeHeader);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_MARKET_RATE_STATISTICS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_MARKET_RATE_STATISTICS.getKey(), runtimeFragment);

        runtimeTabStrip = new TabStrip();
        runtimeTabStrip.setTabsColor("#1278a6");
        runtimeTabStrip.setTabsTextColor("#FFFFFF");
        runtimeTabStrip.setTabsIndicateColor("#3ec8e8");
        runtimeTabStrip.setDividerColor(0x72af9c);
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Negotiations");
        runtimeTab.setFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATIONS_TAB);
        runtimeTabStrip.addTab(runtimeTab);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATIONS_TAB.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATIONS_TAB.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATIONS_TAB.getKey());

        runtimeTab = new Tab();
        runtimeTab.setLabel("Contracts");
        runtimeTab.setFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_CONTRACTS_TAB);
        runtimeTabStrip.addTab(runtimeTab);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_CONTRACTS_TAB.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_CONTRACTS_TAB.getKey(), runtimeFragment);

        Footer runtimeFooter = new Footer();
        runtimeFooter.setBackgroundColor("#AAAAAA");
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_STOCK_STATISTICS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_STOCK_STATISTICS.getKey(), runtimeFragment);
        runtimeFooter.setFragmentCode(Fragments.CBP_CRYPTO_BROKER_WALLET_STOCK_STATISTICS.getKey());
        runtimeActivity.setFooter(runtimeFooter);


        // Activity: Open Negotiation details
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATION_DETAILS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATION_DETAILS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Negotiation Details");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATION_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATION_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_OPEN_NEGOTIATION_DETAILS.getKey());


        // Activity: Close Negotiation details - Open Contract
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS_OPEN_CONTRACT);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS_OPEN_CONTRACT.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_CONTRACT_DETAILS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Negotiation Details");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey());

        // Activity: Close Negotiation details - Close Contract
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS_CLOSE_CONTRACT);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS_CLOSE_CONTRACT.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_CLOSE_CONTRACT_DETAILS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Negotiation Details");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey());


        // Activity: Open Contract Details
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_CONTRACT_DETAILS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_CONTRACT_DETAILS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contract Details");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_CONTRACT_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CONTRACT_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CONTRACT_DETAILS.getKey());


        // Activity: Close Contract Details
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_CLOSE_CONTRACT_DETAILS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_CLOSE_CONTRACT_DETAILS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_CONTRACTS_HISTORY);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contract Details");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_CONTRACT_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_CONTRACT_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CLOSE_CONTRACT_DETAILS.getKey());


        // Activity: Contracts History
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_CONTRACTS_HISTORY);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_CONTRACTS_HISTORY.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contracts History");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_CONTRACTS_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CONTRACTS_HISTORY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CONTRACTS_HISTORY.getKey());


        // Activity: Earnings
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_EARNINGS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_EARNINGS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Earnings");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_EARNINGS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_EARNINGS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_EARNINGS.getKey());


        // Activity: Settings
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Settings");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS.getKey());

        // Activity: Settings -> My Locations
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS_MY_LOCATIONS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS_MY_LOCATIONS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("My Locations");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_MY_LOCATIONS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_MY_LOCATIONS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_MY_LOCATIONS.getKey());

        //Create new locations in settings activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS_MY_LOCATIONS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Create Location");
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setColor(titleBarColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS.getKey());

        // Activity: Settings -> Bank Accounts
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS_BANK_ACCOUNT);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS_BANK_ACCOUNT.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Bank Accounts");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_BANK_ACCOUNT.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_BANK_ACCOUNT.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_BANK_ACCOUNT.getKey());

        // Activity: Settings -> Stock Management
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS_STOCK_MERCHANDISES);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS_STOCK_MERCHANDISES.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_BROKER_WALLET_SETTINGS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Stock Management");
        runtimeTitleBar.setLabelSize(titleBarTextSize);
        runtimeTitleBar.setTitleColor(titleBarTitleColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_STOCK_MERCHANDISES.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_STOCK_MERCHANDISES.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_BROKER_WALLET_SETTINGS_STOCK_MERCHANDISES.getKey());

        return runtimeWalletNavigationStructure;

    }

    private WalletNavigationStructure createCryptoCustomerWalletNavigationStructure() {
        WalletNavigationStructure runtimeWalletNavigationStructure;
        SideMenu runtimeSideMenu;
        MenuItem runtimeMenuItem;
        Activity runtimeActivity;
        TitleBar runtimeTitleBar;
        StatusBar runtimeStatusBar;
        Header runtimeHeader;
        Fragment runtimeFragment;
        TabStrip runtimeTabStrip;
        Tab runtimeTab;

        final String publicKey = WalletsPublicKeys.CBP_CRYPTO_CUSTOMER_WALLET.getCode();

        final String statusBarColor = "#492781";
        final String titleBarColor = "#492781";
        final String titleBarLabelColor = "#ffffff";
        final int titleBarLabelSize = 16;


        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        runtimeWalletNavigationStructure.setPlatform(Platforms.CRYPTO_BROKER_PLATFORM);
        lstWalletNavigationStructureOpen.put(publicKey, runtimeWalletNavigationStructure);



        // Side Menu
        runtimeSideMenu = new SideMenu();
        runtimeSideMenu.setHasFooter(true);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Home");
        runtimeMenuItem.setLinkToActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Contracts History");
        runtimeMenuItem.setLinkToActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACTS_HISTORY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Broker List");
        runtimeMenuItem.setLinkToActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_BROKER_LIST);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Settings");
        runtimeMenuItem.setLinkToActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        // WIZARD

        // step 1 - Set Bitcoin Wallet and Providers
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_BITCOIN_WALLET_AND_PROVIDERS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_BITCOIN_WALLET_AND_PROVIDERS.getCode());
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_BITCOIN_WALLET_AND_PROVIDERS.getCode());

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_BITCOIN_WALLET_AND_PROVIDERS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_BITCOIN_WALLET_AND_PROVIDERS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_BITCOIN_WALLET_AND_PROVIDERS.getKey());

        // step 2 - Set Locations
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_LOCATIONS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_LOCATIONS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_BITCOIN_WALLET_AND_PROVIDERS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_LOCATIONS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_LOCATIONS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_LOCATIONS.getKey());

        // step 3 - Set Bank Accounts
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_BANK_ACCOUNT);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_BANK_ACCOUNT.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_LOCATIONS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_BANK_ACCOUNT.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_BANK_ACCOUNT.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SET_BANK_ACCOUNT.getKey());

        // Create New Location in Wizard Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_LOCATIONS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Create Location");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(titleBarColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_WIZARD.getKey());

        // Create New Bank Account in Wizard Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_WIZARD);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_WIZARD.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SET_BANK_ACCOUNT);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Create Bank Account");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(titleBarColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_WIZARD.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_WIZARD.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_WIZARD.getKey());


        // Activity: Home
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME.getCode());
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Customer Wallet");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeHeader = new Header();
        runtimeHeader.setLabel("Market rate");
        runtimeHeader.setHasExpandable(true);
        runtimeActivity.setHeader(runtimeHeader);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_MARKET_RATE_STATISTICS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_MARKET_RATE_STATISTICS.getKey(), runtimeFragment);


        runtimeTabStrip = new TabStrip();
//        runtimeTabStrip.setTabsColor("#502681");
//        runtimeTabStrip.setTabsTextColor("#FFFFFF");
        runtimeTabStrip.setTabsColor("#222222");
        runtimeTabStrip.setTabsTextColor("#F9F9F9");
        runtimeTabStrip.setTabsIndicateColor("#dbdbdb");
        runtimeTabStrip.setDividerColor(0x72af9c);
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Negotiations");
        runtimeTab.setFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATIONS_TAB);
        runtimeTabStrip.addTab(runtimeTab);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATIONS_TAB.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATIONS_TAB.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATIONS_TAB.getKey());

        runtimeTab = new Tab();
        runtimeTab.setLabel("Contracts");
        runtimeTab.setFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_CONTRACTS_TAB);
        runtimeTabStrip.addTab(runtimeTab);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_CONTRACTS_TAB.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_CONTRACTS_TAB.getKey(), runtimeFragment);


        // Activity: Start Negotiation
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_START_NEGOTIATION);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_START_NEGOTIATION.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_BROKER_LIST);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Start a Negotiation");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_START_NEGOTIATION.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_START_NEGOTIATION.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_START_NEGOTIATION.getKey());


        // Activity: Open Negotiation details
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_DETAILS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_DETAILS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Negotiation Details");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_DETAILS.getKey());


        // Activity: Open Negotiation Add Note
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_ADD_NOTE);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_ADD_NOTE.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_DETAILS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Negotiation Note");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_ADD_NOTE.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_ADD_NOTE.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_ADD_NOTE.getKey());


        // Activity: Close Negotiation details - Open Contract
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS_OPEN_CONTRACT);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS_OPEN_CONTRACT.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACT_DETAILS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Negotiation Details");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey());

        // Activity: Close Negotiation details - Close Contract
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS_CLOSE_CONTRACT);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS_CLOSE_CONTRACT.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_CONTRACT_DETAILS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Negotiation Details");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_NEGOTIATION_DETAILS.getKey());


        // Activity: Close Contract Details
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_CONTRACT_DETAILS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_CONTRACT_DETAILS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACTS_HISTORY);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contract Details");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_CONTRACT_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_CONTRACT_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CLOSE_CONTRACT_DETAILS.getKey());


        // Activity: Contracts History
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACTS_HISTORY);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACTS_HISTORY.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contract History");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACTS_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACTS_HISTORY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACTS_HISTORY.getKey());


        // Activity: Broker List
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_BROKER_LIST);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_BROKER_LIST.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Broker List");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_BROKER_LIST.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_BROKER_LIST.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_BROKER_LIST.getKey());


        // Activity: Settings
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Settings");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS.getKey());

        // Activity: Settings -> My Locations
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_MY_LOCATIONS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_MY_LOCATIONS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("My Locations");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_MY_LOCATIONS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_MY_LOCATIONS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_MY_LOCATIONS.getKey());

        //Create new locations in settings activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_MY_LOCATIONS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Create Location");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(titleBarColor);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_LOCATION_IN_SETTINGS.getKey());

        // Activity: Settings -> Bank Accounts
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_BANK_ACCOUNTS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_BANK_ACCOUNTS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Bank Accounts");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_BANK_ACCOUNTS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_BANK_ACCOUNTS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_BANK_ACCOUNTS.getKey());

        //Create New Bank Accounts in settings activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_SETTINGS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_SETTINGS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_BANK_ACCOUNTS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Create Bank Account");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(titleBarColor);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_SETTINGS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_SETTINGS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CREATE_NEW_BANK_ACCOUNT_IN_SETTINGS.getKey());

        // Activity: Settings -> Providers
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_PROVIDERS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_PROVIDERS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setColor("#1189a5");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Bitcoin Wallet and Providers");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_PROVIDERS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_PROVIDERS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS_PROVIDERS.getKey());

        // Activity: Contracts Details
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACT_DETAILS);
        runtimeActivity.setActivityType(Activities.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACT_DETAILS.getCode());
        runtimeActivity.setBackActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeActivity.setSideMenu(runtimeSideMenu);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contracts Details");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACT_DETAILS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACT_DETAILS.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_CONTRACT_DETAILS.getKey());

        return runtimeWalletNavigationStructure;
    }

    public WalletNavigationStructure createBankMoneyWalletNavigationStructure() {

        WalletNavigationStructure runtimeWalletNavigationStructure;
        Activity runtimeActivity;
        Fragment runtimeFragment;
        SideMenu runtimeSideMenu;
        MenuItem runtimeMenuItem;
        TitleBar runtimeTitleBar;
        StatusBar runtimeStatusBar;
        Header runtimeHeader;
        TabStrip runtimeTabStrip;
        Tab runtimeTab;

        final String publicKey = WalletsPublicKeys.BNK_BANKING_WALLET.getCode();
        final String statusBarColor = "#0e0719";
        final String titleBarLabelColor = "#FFFFFF";
        final int titleBarLabelSize = 16;


        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        lstWalletNavigationStructureOpen.put(publicKey,runtimeWalletNavigationStructure);

        //Setup activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.BNK_BANK_MONEY_WALLET_SETUP);
        runtimeActivity.setActivityType(Activities.BNK_BANK_MONEY_WALLET_SETUP.getCode());
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(Activities.BNK_BANK_MONEY_WALLET_SETUP.getCode());

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.BNK_BANK_MONEY_WALLET_SETUP.getKey());
        runtimeActivity.addFragment(Fragments.BNK_BANK_MONEY_WALLET_SETUP.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.BNK_BANK_MONEY_WALLET_SETUP.getKey());


        //Home Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.BNK_BANK_MONEY_WALLET_HOME);
        runtimeActivity.setActivityType(Activities.BNK_BANK_MONEY_WALLET_HOME.getCode());
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        //runtimeWalletNavigationStructure.addPosibleStartActivity(Activities.BNK_BANK_MONEY_WALLET_HOME);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("BANK WALLET");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.BNK_BANK_MONEY_WALLET_ACCOUNTS_LIST.getKey());
        runtimeActivity.addFragment(Fragments.BNK_BANK_MONEY_WALLET_ACCOUNTS_LIST.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.BNK_BANK_MONEY_WALLET_ACCOUNTS_LIST.getKey());

        //account detail activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.BNK_BANK_MONEY_WALLET_ACCOUNT_DETAILS);
        runtimeActivity.setActivityType(Activities.BNK_BANK_MONEY_WALLET_ACCOUNT_DETAILS.getCode());
        runtimeActivity.setBackActivity(Activities.BNK_BANK_MONEY_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Account Detail");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.BNK_BANK_MONEY_WALLET_ACCOUNT_DETAIL.getKey());
        runtimeActivity.addFragment(Fragments.BNK_BANK_MONEY_WALLET_ACCOUNT_DETAIL.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.BNK_BANK_MONEY_WALLET_ACCOUNT_DETAIL.getKey());

        //Update Record Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.BNK_BANK_MONEY_WALLET_UPDATE_RECORD);
        runtimeActivity.setActivityType(Activities.BNK_BANK_MONEY_WALLET_UPDATE_RECORD.getCode());
        runtimeActivity.setBackActivity(Activities.BNK_BANK_MONEY_WALLET_ACCOUNT_DETAILS);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Cancel");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.BNK_BANK_MONEY_WALLET_UPDATE_RECORD.getKey());
        runtimeActivity.addFragment(Fragments.BNK_BANK_MONEY_WALLET_UPDATE_RECORD.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.BNK_BANK_MONEY_WALLET_UPDATE_RECORD.getKey());

        //add account activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.BNK_BANK_MONEY_WALLET_ADD_ACCOUNT);
        runtimeActivity.setActivityType(Activities.BNK_BANK_MONEY_WALLET_ADD_ACCOUNT.getCode());
        runtimeActivity.setBackActivity(Activities.BNK_BANK_MONEY_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Cancel");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.BNK_BANK_MONEY_WALLET_ADD_ACCOUNT.getKey());
        runtimeActivity.addFragment(Fragments.BNK_BANK_MONEY_WALLET_ADD_ACCOUNT.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.BNK_BANK_MONEY_WALLET_ADD_ACCOUNT.getKey());


        return runtimeWalletNavigationStructure;
    }

    public WalletNavigationStructure createCashMoneyWalletNavigationStructure() {
        WalletNavigationStructure runtimeWalletNavigationStructure;
        Activity runtimeActivity;
        Fragment runtimeFragment;
        StatusBar runtimeStatusBar;
        TitleBar runtimeTitleBar;

        final String publicKey = WalletsPublicKeys.CSH_MONEY_WALLET.getCode();

        //final String statusBarColor = "#00b9ff";
        final String statusBarColor = "#11516F";
        final String titleBarLabelColor = "#FFFFFF";
        final String titleBarColor = "#00b9ff";
        final int titleBarLabelSize = 20;

        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        lstWalletNavigationStructureOpen.put(publicKey,runtimeWalletNavigationStructure);


        //Setup Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CSH_CASH_MONEY_WALLET_SETUP);
        runtimeActivity.setActivityType(Activities.CSH_CASH_MONEY_WALLET_SETUP.getCode());
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(Activities.CSH_CASH_MONEY_WALLET_SETUP.getCode());

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CSH_CASH_MONEY_WALLET_SETUP.getKey());
        runtimeActivity.addFragment(Fragments.CSH_CASH_MONEY_WALLET_SETUP.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CSH_CASH_MONEY_WALLET_SETUP.getKey());


        //Home Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CSH_CASH_MONEY_WALLET_HOME);
        runtimeActivity.setActivityType(Activities.CSH_CASH_MONEY_WALLET_HOME.getCode());
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CSH_CASH_MONEY_WALLET_BALANCE_SUMMARY.getKey());
        runtimeActivity.addFragment(Fragments.CSH_CASH_MONEY_WALLET_BALANCE_SUMMARY.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CSH_CASH_MONEY_WALLET_BALANCE_SUMMARY.getKey());


        //Transaction detail Activity
        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CSH_CASH_MONEY_WALLET_TRANSACTION_DETAIL);
        runtimeActivity.setActivityType(Activities.CSH_CASH_MONEY_WALLET_TRANSACTION_DETAIL.getCode());
        runtimeActivity.setBackActivity(Activities.CSH_CASH_MONEY_WALLET_HOME);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

//        runtimeTitleBar = new TitleBar();
//        runtimeTitleBar.setIconName("back");
//        runtimeTitleBar.setLabel("Transaction detail");
//        runtimeTitleBar.setLabelSize(titleBarLabelSize);
//        runtimeTitleBar.setTitleColor(titleBarLabelColor);
//        runtimeTitleBar.setColor(titleBarColor);
//        runtimeTitleBar.setIsTitleTextStatic(true);
//        runtimeActivity.setTitleBar(runtimeTitleBar);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CSH_CASH_MONEY_WALLET_TRANSACTION_DETAIL.getKey());
        runtimeActivity.addFragment(Fragments.CSH_CASH_MONEY_WALLET_TRANSACTION_DETAIL.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.CSH_CASH_MONEY_WALLET_TRANSACTION_DETAIL.getKey());


        return runtimeWalletNavigationStructure;
    }

    //TKYFanWallet
    public WalletNavigationStructure createFanWalletNavigationStructure() {
        WalletNavigationStructure runtimeWalletNavigationStructure;
        Activity runtimeActivity;
        Fragment runtimeFragment;
        StatusBar runtimeStatusBar;
        TitleBar runtimeTitleBar;
        TabStrip runtimeTabStrip;
        Tab runtimeTab;
        final String publicKey = WalletsPublicKeys.TKY_FAN_WALLET.getCode();
        final String statusBarColor = "#000000";
        final String titleBarLabelColor = "#000000";
        final String BackgroundtitleBarLabelColor = "#f5f5f5";
        final int titleBarLabelSize = 20;


        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        lstWalletNavigationStructureOpen.put(publicKey, runtimeWalletNavigationStructure);

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.TKY_FAN_WALLET_MAIN_ACTIVITY);
        runtimeActivity.setActivityType(Activities.TKY_FAN_WALLET_MAIN_ACTIVITY.getCode());
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(Activities.TKY_FAN_WALLET_MAIN_ACTIVITY.getCode());

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Fan Wallet");
        runtimeTitleBar.setLabelSize(titleBarLabelSize);
        runtimeTitleBar.setTitleColor(titleBarLabelColor);
        runtimeTitleBar.setColor(BackgroundtitleBarLabelColor);
        runtimeTitleBar.setIsTitleTextStatic(false);
        runtimeActivity.setTitleBar(runtimeTitleBar);


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor(statusBarColor);
        runtimeActivity.setStatusBar(runtimeStatusBar);

        /*runtimeHeader = new Header();
        runtimeHeader.setLabel("Market rate");
        runtimeHeader.setHasExpandable(true);
        runtimeActivity.setHeader(runtimeHeader);*/

        /*runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_MARKET_RATE_STATISTICS.getKey());
        runtimeActivity.addFragment(Fragments.CBP_CRYPTO_CUSTOMER_WALLET_MARKET_RATE_STATISTICS.getKey(), runtimeFragment);*/


        runtimeTabStrip = new TabStrip();
        runtimeTabStrip.setTabsColor("#f5f5f5");
        runtimeTabStrip.setTabsTextColor("#b9b9b9");
        runtimeTabStrip.setTabsIndicateColor("#0c6908");
        runtimeTabStrip.setSelectTabTextColor("#000000");
        runtimeTabStrip.setDividerColor(0x72af9c);
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Song");
        runtimeTab.setFragment(Fragments.TKY_FAN_WALLET_SONGS_TAB_FRAGMENT);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.TKY_FAN_WALLET_SONGS_TAB_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.TKY_FAN_WALLET_SONGS_TAB_FRAGMENT.getKey(), runtimeFragment);
        runtimeActivity.setStartFragment(Fragments.TKY_FAN_WALLET_SONGS_TAB_FRAGMENT.getKey());

        runtimeTab = new Tab();
        runtimeTab.setLabel("Following");
        runtimeTab.setFragment(Fragments.TKY_FAN_WALLET_FOLLOWING_TAB_FRAGMENT);
        runtimeTabStrip.addTab(runtimeTab);


        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.TKY_FAN_WALLET_FOLLOWING_TAB_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.TKY_FAN_WALLET_FOLLOWING_TAB_FRAGMENT.getKey(), runtimeFragment);


        return runtimeWalletNavigationStructure;
    }

    private void loadLastWalletNavigationStructure() throws CantFactoryReset {
        String walletCategory = null;
        String walletType = null;
        String screenSize = null;
        String screenDensity = null;
        String skinName = null;
        String languageName = null;

        final String publicKey = WalletsPublicKeys.CCP_REFERENCE_WALLET.getCode();

//        try {
//
//            walletResourcesManger.installCompleteWallet(WalletCategory.REFERENCE_WALLET.getCode(),WalletType.REFERENCE.getCode(), "bitDubai", "medium", "default", "en", "1.0.0",publicKey);
//
//        } catch (WalletResourcesInstalationException e) {
//            e.printStackTrace();
//        }


        try {
            /**
             * Esto es hasta que tengamos las cosas andando y conectadas
             */


//            WalletNavigationStructure walletNavigationStructure = getNavigationStructure(publicKey);
            //          if(walletNavigationStructure==null){
            recordNavigationStructureIsNotExist(startWalletNavigationStructure());
            WalletNavigationStructure walletNavigationStructure = getNavigationStructure(publicKey);

            //LOSS PROTECTED WALLET
            recordNavigationStructureIsNotExist(this.startLossProtectedWalletNavigationStructure());
             walletNavigationStructure = getNavigationStructure("loss_protected_wallet");

            //        }
            //listWallets.put(publicKey, walletNavigationStructure);
            //walletNavigationStructureOpen=walletNavigationStructure;
        } catch (Exception e) {
            String message = CantFactoryReset.DEFAULT_MESSAGE;
            FermatException cause = FermatException.wrapException(e);
            String context = "Error on method Factory Reset, setting the structure of the apps";
            String possibleReason = "some null definition";
            throw new CantFactoryReset(message, cause, context, possibleReason);

        }
    }

    private void removeNavigationStructureXml(String publicKey) {
        if (publicKey != null) {
            String navigationStructureName = publicKey + ".xml";
            try {
                PluginTextFile pluginTextFile = pluginFileSystem.getTextFile(pluginId, NAVIGATION_STRUCTURE_FILE_PATH, navigationStructureName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
                pluginTextFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param walletPublicKey
     * @return
     * @throws CantGetWalletFactoryProjectNavigationStructureException
     */
    @Override
    public WalletNavigationStructure getNavigationStructure(String walletPublicKey) {
        WalletNavigationStructure fermatStructure = null;
        if (walletPublicKey != null) {
            if (lstWalletNavigationStructureOpen.containsKey(walletPublicKey)) {
                fermatStructure = lstWalletNavigationStructureOpen.get(walletPublicKey);
            } else {
                String navigationStructureName = walletPublicKey + ".xml";
                try {
                    PluginTextFile pluginTextFile = pluginFileSystem.getTextFile(pluginId, NAVIGATION_STRUCTURE_FILE_PATH, navigationStructureName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
                    pluginTextFile.loadFromMedia();
                    String xml = pluginTextFile.getContent();
                    fermatStructure = (WalletNavigationStructure) XMLParser.parseXML(xml, fermatStructure);
                    lstWalletNavigationStructureOpen.put(walletPublicKey,fermatStructure);
                } catch (FileNotFoundException e) {
                    try {
                        PluginTextFile layoutFile = pluginFileSystem.createTextFile(pluginId, NAVIGATION_STRUCTURE_FILE_PATH, navigationStructureName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
                        layoutFile.setContent("");

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }


                } catch (CantCreateFileException e) {
                    e.printStackTrace();
                } catch (CantLoadFileException e) {
                    e.printStackTrace();
                }
            }
        }
    return fermatStructure;
    }

    @Override
    public String parseNavigationStructureXml(WalletNavigationStructure walletNavigationStructure) {
        String xml = null;
        if (walletNavigationStructure != null) {
            xml = XMLParser.parseObject(walletNavigationStructure);
        }
        return xml;
    }




    @Override
    public void setNavigationStructureXml(WalletNavigationStructure walletNavigationStructure) {
        String publiKey = walletNavigationStructure.getPublicKey();
        try {
            String navigationStructureXml = parseNavigationStructureXml(walletNavigationStructure);
            String navigationStructureName = publiKey + ".xml";
            try {
                PluginTextFile newFile = pluginFileSystem.createTextFile(pluginId, NAVIGATION_STRUCTURE_FILE_PATH, navigationStructureName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
                newFile.setContent(navigationStructureXml);
                newFile.persistToMedia();
            } catch (CantPersistFileException e) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WPD_WALLET_FACTORY_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                //throw new CantSetWalletFactoryProjectNavigationStructureException(CantSetWalletFactoryProjectNavigationStructureException.DEFAULT_MESSAGE, e, "Can't create or overwrite navigation structure file.", "");
            } catch (CantCreateFileException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WPD_WALLET_FACTORY_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            //throw new CantSetWalletFactoryProjectNavigationStructureException(CantSetWalletFactoryProjectNavigationStructureException.DEFAULT_MESSAGE, e, "Can't convert navigation structure to xml format", "");
        }
    }

    private void recordNavigationStructureIsNotExist(WalletNavigationStructure walletNavigationStructure){
        String publiKey = walletNavigationStructure.getPublicKey();
        try {
            String navigationStructureXml = parseNavigationStructureXml(walletNavigationStructure);
            String navigationStructureName = publiKey + ".xml";
            if (!pluginFileSystem.isTextFileExist(pluginId, NAVIGATION_STRUCTURE_FILE_PATH, navigationStructureName, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT)){
                setNavigationStructureXml(walletNavigationStructure);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Meanwhile
     *
     * @return
     */

    private WalletNavigationStructure startWalletNavigationStructure() {

        Activity runtimeActivity;
        Fragment runtimeFragment;
        WalletNavigationStructure runtimeWalletNavigationStructure;
        TitleBar runtimeTitleBar;
        SideMenu runtimeSideMenu;
        MainMenu runtimeMainMenu;
        MenuItem runtimeMenuItem;
        TabStrip runtimeTabStrip;
        StatusBar runtimeStatusBar;
        Header runtimeHeader;
        Footer fermatFooter;

        Tab runtimeTab;

        final String publicKey = WalletsPublicKeys.CCP_REFERENCE_WALLET.getCode();

        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());
        runtimeWalletNavigationStructure.setPublicKey(publicKey);
        //listWallets.put(publicKey, runtimeWalletNavigationStructure);
        lastWalletPublicKey = publicKey;
        lstWalletNavigationStructureOpen.put(publicKey,runtimeWalletNavigationStructure);

        runtimeActivity = new Activity();
        runtimeActivity.setActivityType(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN.getCode());
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setColor("#12aca1");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(runtimeActivity.getType().getCode());

        runtimeHeader = new Header();
        runtimeHeader.setLabel("Balance");
        runtimeActivity.setHeader(runtimeHeader);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Bitcoin wallet");
        runtimeTitleBar.setLabelSize(18);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setFont("Roboto-Regular.ttf");
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");

        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeActivity.setStatusBar(runtimeStatusBar);


        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#06356f");

        runtimeTabStrip.setTabsTextColor("#b9bfcd");

        runtimeTabStrip.setTabsIndicateColor("#12aca1");


        runtimeTab = new Tab();
        runtimeTab.setLabel("Sent");
        runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_SEND);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Received");
        runtimeTab.setFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_RECEIVE);
        runtimeTabStrip.addTab(runtimeTab);


        runtimeTabStrip.setDividerColor(0x72af9c);
        //runtimeTabStrip.setBackgroundColor("#72af9c");
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_BALANCE.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_BALANCE.getKey(), runtimeFragment);


        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_SEND.getKey());
        runtimeFragment.setBack(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_CONTACTS.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_SEND.getKey(), runtimeFragment);


        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_RECEIVE.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_RECEIVE.getKey(), runtimeFragment);


        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuBitcoinWallet(publicKey));

        //fin navigation


//        /**
//         * Menu
//         */
//
        runtimeMainMenu = new MainMenu();
        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setIcon("send");
        runtimeMainMenu.addMenuItem(runtimeMenuItem);


        runtimeActivity.setMainMenu(runtimeMainMenu);
//
//        /**
//         *  Fin de menu
//         */

        /**
         * Wizard
         */
         /* Adding WizardTypes */
//        Wizard runtimeWizard = new Wizard();
//        // step 1 wizard create from scratch
//        WizardPage runtimeWizardPage = new WizardPage();
//        runtimeWizardPage.setFragment(Fragments.CCP_BITCOIN_WALLET_NO_IDENTITY_FRAGMENT.getKey());
//        runtimeWizard.addPage(runtimeWizardPage);
//            /* Adding wizard */
//        runtimeActivity.addWizard(WizardTypes.CCP_WALLET_BITCOIN_START_WIZARD.getKey(), runtimeWizard);

        /**
         * Transaction Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_TRANSACTIONS);
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("bitdubai bitcoin Wallet");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackgroundColor("#ffffff");
        //runtimeActivity.setColor("#d07b62");


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeTab = new Tab();
        runtimeTab.setLabel("Sent");
        runtimeTab.setFragment(Fragments.CCP_BITCOIN_WALLET_TRANSACTIONS_SENT_HISTORY);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Received");
        runtimeTab.setFragment(Fragments.CCP_BITCOIN_WALLET_TRANSACTIONS_RECEIVED_HISTORY);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTabStrip.setDividerColor(0x72af9c);
        //runtimeTabStrip.setBackgroundColor("#72af9c");
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_TRANSACTIONS_SENT_HISTORY.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_TRANSACTIONS_SENT_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_TRANSACTIONS_SENT_HISTORY.getKey(), runtimeFragment);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_TRANSACTIONS_RECEIVED_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_TRANSACTIONS_RECEIVED_HISTORY.getKey(), runtimeFragment);
//Navigation

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuBitcoinWallet(publicKey));

        //fin navigation

        //fin navigation


        /**
         * Payment request Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_PAYMENT_REQUEST);
        runtimeActivity.setColor("#8bba9e");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Requests");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeTab = new Tab();
        runtimeTab.setLabel("sent");
        runtimeTab.setFragment(Fragments.CCP_BITCOIN_WALLET_REQUEST_SENT_HISTORY);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Received");
        runtimeTab.setFragment(Fragments.CCP_BITCOIN_WALLET_REQUEST_RECEIVED_HISTORY);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTabStrip.setDividerColor(0x72af9c);
        //runtimeTabStrip.setBackgroundColor("#72af9c");
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_REQUEST_SENT_HISTORY.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_REQUEST_SENT_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_REQUEST_SENT_HISTORY.getKey(), runtimeFragment);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_REQUEST_RECEIVED_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_REQUEST_RECEIVED_HISTORY.getKey(), runtimeFragment);

        //Navigation

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuBitcoinWallet(publicKey));

        //fin navigation

        //fin navigation
        /**
         * Contacts Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_CONTACTS);
        runtimeActivity.setActivityType(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_CONTACTS.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contacts");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_CONTACTS.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_CONTACTS.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_CONTACTS.getKey(), runtimeFragment);

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuBitcoinWallet(publicKey));

        //fin navigation

        //fin navigation


        /**
         * Send form Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_SEND_FORM_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_SEND_FORM_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Send to contact");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_SEND_FORM_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_SEND_FORM_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_SEND_FORM_FRAGMENT.getKey(), runtimeFragment);

        /**
         * Request form Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_REQUEST_FORM_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_REQUEST_FORM_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CCP_BITCOIN_WALLET_CONTACT_DETAIL_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Send Request To Contact");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setColor("#12aca1");
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_REQUEST_FORM_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_REQUEST_FORM_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_REQUEST_FORM_FRAGMENT.getKey(), runtimeFragment);

        /**
         * No identity Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_NO_IDENTITY_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_NO_IDENTITY_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//        runtimeWalletNavigationStructure.addPosibleStartActivity(Activities.CCP_BITCOIN_WALLET_NO_IDENTITY_ACTIVITY);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_NO_IDENTITY_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_NO_IDENTITY_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_NO_IDENTITY_FRAGMENT.getKey(), runtimeFragment);

        /**
         * Contact detail Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_CONTACT_DETAIL_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_CONTACT_DETAIL_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_CONTACTS);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contact detail");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_DETAIL_CONTACTS.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_DETAIL_CONTACTS.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_DETAIL_CONTACTS.getKey(), runtimeFragment);

        /**
         * Contacts Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Settings");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT.getKey(), runtimeFragment);

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuBitcoinWallet(publicKey));

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY_MAIN_NETWORK);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY_MAIN_NETWORK.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Network");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT_MAIN_NETWORK.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT_MAIN_NETWORK.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT_MAIN_NETWORK.getKey(), runtimeFragment);


        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY_NOTIFICATIONS);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY_NOTIFICATIONS.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Notifications");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();


        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT_NOTIFICATIONS.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT_NOTIFICATIONS.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_SETTINGS_FRAGMENT_NOTIFICATIONS.getKey(), runtimeFragment);

        //fin navigation

        //fin navigation
        /**
         * Contact detail Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_CONTACT_DETAIL_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_CONTACT_DETAIL_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_CONTACTS);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contact detail");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setColor("#12aca1");
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_DETAIL_CONTACTS.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_DETAIL_CONTACTS.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_ALL_BITDUBAI_DETAIL_CONTACTS.getKey(), runtimeFragment);

        /**
         * Add connection Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_ADD_CONNECTION_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_ADD_CONNECTION_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_CONTACTS);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Add Fermat user");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_ADD_CONNECTION_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_ADD_CONNECTION_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_ADD_CONNECTION_FRAGMENT.getKey(), runtimeFragment);

        //Export Mnemonic key activity


        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_MNEMONIC_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_MNEMONIC_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Export Private key");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_MNEMONIC_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_MNEMONIC_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_MNEMONIC_FRAGMENT.getKey(), runtimeFragment);

        /**
         * Send Error Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_WALLET_OPEN_SEND_ERROR_REPORT);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_WALLET_OPEN_SEND_ERROR_REPORT.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Send Error Report");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#12aca1");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#12aca1");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_WALLET_SEND_ERROR_REPORT_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_WALLET_SEND_ERROR_REPORT_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_WALLET_SEND_ERROR_REPORT_FRAGMENT.getKey(), runtimeFragment);


        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        return runtimeWalletNavigationStructure;
    }

    private SideMenu loadSideMenuBitcoinWallet(String publicKey) {
        //Navigation

        SideMenu runtimeSideMenu = new SideMenu();
        runtimeSideMenu.setBackgroundColor("#ffffff");
        runtimeSideMenu.setNavigationIconColor("#ffffff");
        runtimeSideMenu.setHasFooter(true);

        MenuItem runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Home");
        runtimeMenuItem.setIcon("home");
        runtimeMenuItem.setSelected(true);
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Contacts");
        runtimeMenuItem.setIcon("contacts");
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_CONTACTS);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Payment requests");
        runtimeMenuItem.setIcon("request");
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_PAYMENT_REQUEST);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Settings");
        runtimeMenuItem.setIcon("settings");
        runtimeMenuItem.setLinkToActivity(Activities.CCP_BITCOIN_WALLET_SETTINGS_ACTIVITY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

       /* runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Logout");
        runtimeMenuItem.setLinkToActivity(Activities.DEVELOP_MODE);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);*/

        return runtimeSideMenu;
    }


    private WalletNavigationStructure startLossProtectedWalletNavigationStructure() {

        Activity runtimeActivity;
        Fragment runtimeFragment;
        WalletNavigationStructure runtimeWalletNavigationStructure;
        TitleBar runtimeTitleBar;
        SideMenu runtimeSideMenu;
        MainMenu runtimeMainMenu;
        MenuItem runtimeMenuItem;
        TabStrip runtimeTabStrip;
        StatusBar runtimeStatusBar;
        Header runtimeHeader;

        Tab runtimeTab;

        String publicKey = WalletsPublicKeys.CWP_LOSS_PROTECTED_WALLET.getCode();

        runtimeWalletNavigationStructure = new WalletNavigationStructure();
        runtimeWalletNavigationStructure.setWalletCategory(WalletCategory.REFERENCE_WALLET.getCode());
        runtimeWalletNavigationStructure.setWalletType(WalletType.REFERENCE.getCode());

        runtimeWalletNavigationStructure.setPublicKey(publicKey);

        lastWalletPublicKey = publicKey;
        lstWalletNavigationStructureOpen.put(publicKey, runtimeWalletNavigationStructure);

        /*
        * START HOME ACTIVITY
        * */

        runtimeActivity = new Activity();
        runtimeActivity.setActivityType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN.getCode());
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setColor("#073487");
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
        runtimeWalletNavigationStructure.changeActualStartActivity(runtimeActivity.getType().getCode());

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Loss Protected wallet");
        runtimeTitleBar.setLabelSize(18);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setFont("Roboto-Regular.ttf");
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");

        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeActivity.setStatusBar(runtimeStatusBar);


        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_HOME.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_HOME.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_HOME.getKey(), runtimeFragment);


        /*
        * END HOME ACTIVITY
        * */


        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuLossProtectedWallet(publicKey));

        //fin navigation


//        /**
//         * Menu
//         */
//
        runtimeMainMenu = new MainMenu();
        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setIcon("send");
        runtimeMainMenu.addMenuItem(runtimeMenuItem);


        runtimeActivity.setMainMenu(runtimeMainMenu);
//
//        /**
//         *  Fin de menu
//         */

        /**
         * Wizard
         */
         /* Adding WizardTypes */
        Wizard runtimeWizard = new Wizard();
        // step 1 wizard create from scratch
        WizardPage runtimeWizardPage = new WizardPage();
        runtimeWizardPage.setFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_NO_IDENTITY_FRAGMENT.getKey());
        runtimeWizard.addPage(runtimeWizardPage);
            /* Adding wizard */
        runtimeActivity.addWizard(WizardTypes.CCP_WALLET_BITCOIN_START_WIZARD.getKey(), runtimeWizard);

        /**
         * Transaction Activity
         */


        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_TRANSACTIONS);
        runtimeActivity.setColor("#8bba9e");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Transactions");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();


        runtimeTabStrip.setTabsColor("#06356f");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#DC5A0F");

        runtimeTab = new Tab();
        runtimeTab.setLabel("Sent");
        runtimeTab.setFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_TRANSACTIONS_SENT_HISTORY);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Received");
        runtimeTab.setFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_TRANSACTIONS_RECEIVED_HISTORY);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTabStrip.setDividerColor(0x72af9c);
        //runtimeTabStrip.setBackgroundColor("#72af9c");
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_TRANSACTIONS_SENT_HISTORY.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_TRANSACTIONS_SENT_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_TRANSACTIONS_SENT_HISTORY.getKey(), runtimeFragment);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_TRANSACTIONS_RECEIVED_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_TRANSACTIONS_RECEIVED_HISTORY.getKey(), runtimeFragment);

        //Navigation

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuLossProtectedWallet(publicKey));

        //fin navigation




        /**
         * Payment request Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_PAYMENT_REQUEST);
        runtimeActivity.setColor("#8bba9e");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);
        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Requests");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();


        runtimeTabStrip.setTabsColor("#06356f");

        runtimeTabStrip.setTabsTextColor("#b9bfcd");

        runtimeTabStrip.setTabsIndicateColor("#DC5A0F");

        runtimeTab = new Tab();
        runtimeTab.setLabel("sent");
        runtimeTab.setFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_SENT_HISTORY);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTab = new Tab();
        runtimeTab.setLabel("Received");
        runtimeTab.setFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_RECEIVED_HISTORY);
        runtimeTabStrip.addTab(runtimeTab);

        runtimeTabStrip.setDividerColor(0x72af9c);
        //runtimeTabStrip.setBackgroundColor("#72af9c");
        runtimeActivity.setTabStrip(runtimeTabStrip);

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_SENT_HISTORY.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_SENT_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_SENT_HISTORY.getKey(), runtimeFragment);
        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_RECEIVED_HISTORY.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_RECEIVED_HISTORY.getKey(), runtimeFragment);

        //Navigation

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuLossProtectedWallet(publicKey));

        //fin navigation

        //fin navigation
        /**
         * Contacts Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CONTACTS);
        runtimeActivity.setActivityType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CONTACTS.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contacts");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeActivity.setTitleBar(runtimeTitleBar);


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_CONTACTS.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_CONTACTS.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_CONTACTS.getKey(), runtimeFragment);

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuLossProtectedWallet(publicKey));

        //fin navigation

        //fin navigation


        /**
         * Send form Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_FORM_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_FORM_ACTIVITY.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Send to contact");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_FORM_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_FORM_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_FORM_FRAGMENT.getKey(), runtimeFragment);


        /**
         * Send to wallet form Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_SEND_WALLET);
        runtimeActivity.setActivityType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_SEND_WALLET.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Send to wallet");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_WALLET_FORM_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_WALLET_FORM_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_WALLET_FORM_FRAGMENT.getKey(), runtimeFragment);

        /**
         * Chunk Values Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CHUNCK_VALUES);
        runtimeActivity.setActivityType(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CHUNCK_VALUES.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Chunk Value");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");


        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        /*runtimeHeader = new Header();
        runtimeHeader.setLabel("Balance");
        runtimeActivity.setHeader(runtimeHeader);*/

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_CHUNCK_VALUES.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_CHUNCK_VALUES.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_CHUNCK_VALUES.getKey(), runtimeFragment);

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuLossProtectedWallet(publicKey));


        /**
         * Chunck Values Detalil
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CHUNCK_VALUE_DETAIL_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CHUNCK_VALUE_DETAIL_ACTIVITY.getCode());
        runtimeActivity.setColor("#12aca1");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CHUNCK_VALUES);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Chunck Value detail");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setColor("#073487");
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setTitleColor("#ffffff");
        //runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        /*runtimeHeader = new Header();
        runtimeHeader.setLabel("Chunck Detail Header");
        runtimeActivity.setHeader(runtimeHeader);*/


        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CHUNCK_VALUES_DETAIL_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CHUNCK_VALUES_DETAIL_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CHUNCK_VALUES_DETAIL_FRAGMENT.getKey(), runtimeFragment);

        /**
         * Request form Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_FORM_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_FORM_ACTIVITY.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CONTACT_DETAIL_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Send Request To Contact");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setColor("#073487");
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_FORM_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_FORM_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_REQUEST_FORM_FRAGMENT.getKey(), runtimeFragment);

        /**
         * No identity Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_NO_IDENTITY_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_NO_IDENTITY_ACTIVITY.getCode());
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);
//        runtimeWalletNavigationStructure.addPosibleStartActivity(Activities.CCP_BITCOIN_WALLET_NO_IDENTITY_ACTIVITY);

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_NO_IDENTITY_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_NO_IDENTITY_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_NO_IDENTITY_FRAGMENT.getKey(), runtimeFragment);

        /**
         * Contact detail Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CONTACT_DETAIL_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CONTACT_DETAIL_ACTIVITY.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CONTACTS);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contact detail");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#12aca1");
        runtimeActivity.setTitleBar(runtimeTitleBar);

        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_DETAIL_CONTACTS.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_DETAIL_CONTACTS.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_DETAIL_CONTACTS.getKey(), runtimeFragment);

        /**
         * Contacts Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Settings");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setColor("#073487");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT.getKey(), runtimeFragment);

        //Side Menu
        runtimeActivity.setSideMenu(loadSideMenuLossProtectedWallet(publicKey));

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY_MAIN_NETWORK);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY_MAIN_NETWORK.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Network");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");


        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT_MAIN_NETWORK.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT_MAIN_NETWORK.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT_MAIN_NETWORK.getKey(), runtimeFragment);


        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY_NOTIFICATIONS);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY_NOTIFICATIONS.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Notifications");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeTitleBar.setIconName("back");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();


        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT_NOTIFICATIONS.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT_NOTIFICATIONS.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_FRAGMENT_NOTIFICATIONS.getKey(), runtimeFragment);

        //fin navigation

        //fin navigation
        /**
         * Contact detail Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CONTACT_DETAIL_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_CONTACT_DETAIL_ACTIVITY.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CONTACTS);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Contact detail");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setColor("#073487");
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_DETAIL_CONTACTS.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_DETAIL_CONTACTS.getKey());
        runtimeActivity.addFragment(Fragments.CWP_WALLET_RUNTIME_WALLET_BITCOIN_LOSS_PROTECTED_ALL_BITDUBAI_DETAIL_CONTACTS.getKey(), runtimeFragment);

        /**
         * Add connection Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_ADD_CONNECTION_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_ADD_CONNECTION_ACTIVITY.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CONTACTS);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Add Fermat user");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setColor("#073487");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_ADD_CONNECTION_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_ADD_CONNECTION_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_ADD_CONNECTION_FRAGMENT.getKey(), runtimeFragment);


        //Export Mnemonic key activity


        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_MNEMONIC_ACTIVITY);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_MNEMONIC_ACTIVITY.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeActivity.setBackPublicKey(publicKey);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Export Private key");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setColor("#073487");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_MNEMONIC_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_MNEMONIC_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_MNEMONIC_FRAGMENT.getKey(), runtimeFragment);

        //---

        /**
         * Send Error Activity
         */

        runtimeActivity = new Activity();
        runtimeActivity.setType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_OPEN_SEND_ERROR_REPORT);
        runtimeActivity.setActivityType(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_OPEN_SEND_ERROR_REPORT.getCode());
        runtimeActivity.setColor("#073487");
        runtimeActivity.setBackActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);

        runtimeWalletNavigationStructure.addActivity(runtimeActivity);

        runtimeTitleBar = new TitleBar();
        runtimeTitleBar.setLabel("Send Error Report");
        runtimeTitleBar.setLabelSize(16);
        runtimeTitleBar.setIconName("back");
        runtimeTitleBar.setIsTitleTextStatic(true);
        runtimeTitleBar.setTitleColor("#ffffff");
        runtimeTitleBar.setColor("#073487");
        runtimeActivity.setTitleBar(runtimeTitleBar);
        runtimeActivity.setColor("#073487");
        //runtimeActivity.setColor("#d07b62");

        runtimeStatusBar = new StatusBar();
        runtimeStatusBar.setColor("#073487");

        runtimeTabStrip = new TabStrip();

        runtimeTabStrip.setTabsColor("#1173aa");

        runtimeTabStrip.setTabsTextColor("#FFFFFF");

        runtimeTabStrip.setTabsIndicateColor("#FFFFFF");

        runtimeActivity.setStatusBar(runtimeStatusBar);

        runtimeActivity.setStartFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_ERROR_REPORT_FRAGMENT.getKey());

        runtimeFragment = new Fragment();
        runtimeFragment.setType(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_ERROR_REPORT_FRAGMENT.getKey());
        runtimeActivity.addFragment(Fragments.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SEND_ERROR_REPORT_FRAGMENT.getKey(), runtimeFragment);
        //---------------------

        recordNavigationStructureIsNotExist(runtimeWalletNavigationStructure);

        return runtimeWalletNavigationStructure;
    }

    private SideMenu loadSideMenuLossProtectedWallet(String publicKey) {
        //Navigation

        SideMenu runtimeSideMenu = new SideMenu();
        runtimeSideMenu.setBackgroundColor("#ffffff");
        runtimeSideMenu.setNavigationIconColor("#ffffff");
        runtimeSideMenu.setHasFooter(false);

        MenuItem runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Home");
        runtimeMenuItem.setIcon("home");
        runtimeMenuItem.setSelected(true);
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_MAIN);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Contacts");
        runtimeMenuItem.setIcon("contacts");
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CONTACTS);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

       /* runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Send Btc to wallets");
        runtimeMenuItem.setIcon("request");
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_SEND_WALLET);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);*/

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Payment requests");
        runtimeMenuItem.setIcon("request");
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_PAYMENT_REQUEST);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Transactions");
        runtimeMenuItem.setIcon("request");
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_TRANSACTIONS);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Chunck Values");
        runtimeMenuItem.setIcon("request");
        runtimeMenuItem.setLinkToActivity(Activities.CWP_WALLET_RUNTIME_WALLET_LOSS_PROTECTED_WALLET_BITDUBAI_VERSION_1_CHUNCK_VALUES);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

        runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Settings");
        runtimeMenuItem.setIcon("settings");
        runtimeMenuItem.setLinkToActivity(Activities.CCP_BITCOIN_LOSS_PROTECTED_WALLET_SETTINGS_ACTIVITY);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);

      /*  runtimeMenuItem = new MenuItem();
        runtimeMenuItem.setLabel("Logout");
        runtimeMenuItem.setLinkToActivity(Activities.DEVELOP_MODE);
        runtimeMenuItem.setAppLinkPublicKey(publicKey);
        runtimeSideMenu.addMenuItem(runtimeMenuItem);*/

        return runtimeSideMenu;
    }
}