/*
 * @#NetworkNodePluginRoot.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1;


import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
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
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.NetworkNodeManager;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.FermatEmbeddedNodeServer;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContext;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.context.NodeContextItem;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseConstants;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDatabaseFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.CommunicationsNetworkNodeP2PDeveloperDatabaseFactoryTemp;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.database.daos.DaoFactory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.exceptions.CantInitializeCommunicationsNetworkNodeP2PDatabaseException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.jboss.logging.Logger;


/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.NetworkNodePluginRoot</code> is
 * responsible of initialize all the component to work together
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 11/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NetworkNodePluginRoot extends AbstractPlugin implements NetworkNodeManager {

    /**
     * Represent the LOG
     */
    private static final Logger LOG = Logger.getLogger(NetworkNodePluginRoot.class.getName());

    /**
     * ErrorManager references definition.
     */
    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    /**
     * EventManager references definition.
     */
    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    /**
     * PluginFileSystem references definition.
     */
    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    protected PluginFileSystem pluginFileSystem;

    /**
     * PluginDatabaseSystem references definition.
     */
    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * Represent the daoFactory instance
     */
    private DaoFactory daoFactory;

    /**
     * Represent the node identity
     */
    private ECCKeyPair identity;

    /**
     * Represent the database of the node
     */
    private Database dataBase;

    /**
     * Represent the communicationsNetworkNodeP2PDatabaseFactory of the node
     */
    private CommunicationsNetworkNodeP2PDatabaseFactory communicationsNetworkNodeP2PDatabaseFactory;

    /**
     * Represent the fermatEmbeddedNodeServer instance
     */
    private FermatEmbeddedNodeServer fermatEmbeddedNodeServer;

    /**
     * Constructor
     */
    public NetworkNodePluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    /**
     * (non-javadoc)
     *
     * @see AbstractPlugin#start()
     */
    @Override
    public void start() throws CantStartPluginException {

        LOG.info("Calling the method - start() ");
        LOG.info("pluginId = " + pluginId);

        /*
         * Validate required resources
         */
        validateInjectedResources();

        try {

            /*
             * Initialize the identity of the node
             */
            initializeIdentity();

             /*
             * Initialize the Data Base of the node
             */
            initializeDb();

            CommunicationsNetworkNodeP2PDeveloperDatabaseFactoryTemp developerDatabaseFactory = new CommunicationsNetworkNodeP2PDeveloperDatabaseFactoryTemp(pluginDatabaseSystem, pluginId);

            /*
             * Create and start the internal server
             */
            fermatEmbeddedNodeServer = new FermatEmbeddedNodeServer();
            fermatEmbeddedNodeServer.start();

            LOG.info("Add references to the node context...");

            /*
             * Add references to the node context
             */
            NodeContext.add(NodeContextItem.DAO_FACTORY                , daoFactory              );
            NodeContext.add(NodeContextItem.DEVELOPER_DATABASE_FACTORY , developerDatabaseFactory);
            NodeContext.add(NodeContextItem.ERROR_MANAGER              , errorManager            );
            NodeContext.add(NodeContextItem.EVENT_MANAGER              , eventManager            );
            NodeContext.add(NodeContextItem.FERMAT_EMBEDDED_NODE_SERVER, fermatEmbeddedNodeServer);
            NodeContext.add(NodeContextItem.PLUGIN_DATABASE_SYSTEM     , pluginDatabaseSystem    );
            NodeContext.add(NodeContextItem.PLUGIN_FILE_SYSTEM         , pluginFileSystem        );

        } catch (CantInitializeCommunicationsNetworkNodeP2PDatabaseException exception) {


            exception.printStackTrace();

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Error trying to initialize the network node database.");
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("Plugin ID: ");
            contextBuffer.append(pluginId.toString());
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("Database Name: " + CommunicationsNetworkNodeP2PDatabaseConstants.DATA_BASE_NAME);

            String context = contextBuffer.toString();
            String possibleCause = "The  Network Node Service triggered an unexpected problem that wasn't able to solve by itself";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, exception, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.NETWORK_NODE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;
        } catch (Exception exception) {


            exception.printStackTrace();

            String context = "Plugin ID: " + pluginId;
            String possibleCause = "The  Network Node Service triggered an unexpected problem that wasn't able to solve by itself";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, exception, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.NETWORK_NODE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;
        }

    }

    /**
     * This method validate is all required resource are injected into
     * the plugin root by the platform
     *
     * @throws CantStartPluginException
     */
    private void validateInjectedResources() throws CantStartPluginException {

         /*
         * If all resources are inject
         */
        if (pluginDatabaseSystem == null ||
                errorManager == null ||
                eventManager == null) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("errorManager: " + errorManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("eventManager: " + eventManager);

            String context = contextBuffer.toString();
            String possibleCause = "No all required resource are injected";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, null, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.NETWORK_NODE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);
            throw pluginStartException;

        }

    }

    private static final String IDENTITY_FILE_DIRECTORY = "private";
    private static final String IDENTITY_FILE_NAME      = "nodeIdentity";

    /**
     * Initialize the identity of this plugin
     */
    private void initializeIdentity() throws CantInitializeCommunicationsNetworkNodeP2PDatabaseException {

        System.out.println("Calling the method - initializeIdentity() ");

        try {

            System.out.println("Loading identity");

         /*
          * Load the file with the identity
          */
            PluginTextFile pluginTextFile = pluginFileSystem.getTextFile(pluginId, IDENTITY_FILE_DIRECTORY, IDENTITY_FILE_NAME, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
            String content = pluginTextFile.getContent();

            System.out.println("content = " + content);

            identity = new ECCKeyPair(content);

        } catch (FileNotFoundException e) {

            /*
             * The file no exist may be the first time the plugin is running on this device,
             * We need to create the new identity
             */
            try {

                System.out.println("No previous identity found - Proceed to create new one");

                /*
                 * Create the new identity
                 */
                identity = new ECCKeyPair();

                System.out.println("identity.getPrivateKey() = " + identity.getPrivateKey());
                System.out.println("identity.getPublicKey() = " + identity.getPublicKey());

                /*
                 * save into the file
                 */
                PluginTextFile pluginTextFile = pluginFileSystem.createTextFile(pluginId, IDENTITY_FILE_DIRECTORY, IDENTITY_FILE_NAME, FilePrivacy.PRIVATE, FileLifeSpan.PERMANENT);
                pluginTextFile.setContent(identity.getPrivateKey());
                pluginTextFile.persistToMedia();

            } catch (Exception exception) {
                /*
                 * The file cannot be created. I can not handle this situation.
                 */
                errorManager.reportUnexpectedPluginException(Plugins.NETWORK_NODE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, exception);
                throw new CantInitializeCommunicationsNetworkNodeP2PDatabaseException(exception.getLocalizedMessage());
            }


        } catch (CantCreateFileException cantCreateFileException) {

            /*
             * The file cannot be load. I can not handle this situation.
             */
            errorManager.reportUnexpectedPluginException(Plugins.NETWORK_NODE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantCreateFileException);
            throw new CantInitializeCommunicationsNetworkNodeP2PDatabaseException(cantCreateFileException.getLocalizedMessage());

        }

    }

    /**
     * This method initialize the database
     *
     * @throws CantInitializeCommunicationsNetworkNodeP2PDatabaseException
     */
    private void initializeDb() throws CantInitializeCommunicationsNetworkNodeP2PDatabaseException {

        System.out.println("Calling the method - initializeDb() ");

        try {

            System.out.println("Loading database");
            /*
             * Open new database connection
             */
            this.dataBase = this.pluginDatabaseSystem.openDatabase(pluginId, CommunicationsNetworkNodeP2PDatabaseConstants.DATA_BASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            /*
             * The database exists but cannot be open. I can not handle this situation.
             */
            errorManager.reportUnexpectedPluginException(Plugins.NETWORK_NODE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantOpenDatabaseException);
            throw new CantInitializeCommunicationsNetworkNodeP2PDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

        } catch (DatabaseNotFoundException e) {

            /*
             * The database no exist may be the first time the plugin is running on this device,
             * We need to create the new database
             */
            try {

                System.out.println("No previous data base found - Proceed to create new one");

                /*
                 * We create the new database
                 */
                this.communicationsNetworkNodeP2PDatabaseFactory = new CommunicationsNetworkNodeP2PDatabaseFactory(pluginDatabaseSystem);
                this.dataBase = communicationsNetworkNodeP2PDatabaseFactory.createDatabase(pluginId, CommunicationsNetworkNodeP2PDatabaseConstants.DATA_BASE_NAME);


            } catch (CantCreateDatabaseException cantOpenDatabaseException) {

                /*
                 * The database cannot be created. I can not handle this situation.
                 */
                errorManager.reportUnexpectedPluginException(Plugins.NETWORK_NODE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantOpenDatabaseException);
                throw new CantInitializeCommunicationsNetworkNodeP2PDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

            }
        }

        //Validate if database is ok
        if (dataBase != null) {

            /*
             * Create the daoFactory
             */
            this.daoFactory = new DaoFactory(dataBase);

        }

    }

}
