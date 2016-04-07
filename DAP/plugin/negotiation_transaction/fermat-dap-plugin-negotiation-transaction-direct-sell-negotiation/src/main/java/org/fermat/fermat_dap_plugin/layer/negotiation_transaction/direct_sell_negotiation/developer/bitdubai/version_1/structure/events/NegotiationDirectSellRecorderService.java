package org.fermat.fermat_dap_plugin.layer.negotiation_transaction.direct_sell_negotiation.developer.bitdubai.version_1.structure.events;

import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;

import org.fermat.fermat_dap_api.layer.all_definition.enums.EventType;
import org.fermat.fermat_dap_api.layer.all_definition.exceptions.CantSetObjectException;
import org.fermat.fermat_dap_api.layer.all_definition.util.Validate;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantStartServiceException;
import org.fermat.fermat_dap_api.layer.dap_transaction.common.interfaces.AssetTransactionService;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 06/11/15.
 */
public class NegotiationDirectSellRecorderService implements AssetTransactionService {

    //VARIABLE DECLARATION
    private ServiceStatus serviceStatus;

    {
        serviceStatus = ServiceStatus.CREATED;
    }

    private final EventManager eventManager;
    private ErrorManager errorManager;
    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;
    private List<FermatEventListener> listenersAdded;

    {
        listenersAdded = new ArrayList<>();
    }

    //CONSTRUCTORS

    public NegotiationDirectSellRecorderService(UUID pluginId, EventManager eventManager, PluginDatabaseSystem pluginDatabaseSystem) throws CantSetObjectException {
        this.pluginId = Validate.verifySetter(pluginId, "pluginId is null");
        this.eventManager = Validate.verifySetter(eventManager, "eventManager is null");
        this.pluginDatabaseSystem = Validate.verifySetter(pluginDatabaseSystem, "pluginDatabaseSystem is null");
    }


    //PUBLIC METHODS

    @Override
    public void start() throws CantStartServiceException {
        String context = "PluginDatabaseSystem: " + pluginDatabaseSystem + " - Plugin ID: " + pluginId + " Event Manager: " + eventManager;

        try {
            FermatEventListener newNetworkServiceMessageListener = eventManager.getNewListener(EventType.NEW_RECEIVE_MESSAGE_ACTOR);
            newNetworkServiceMessageListener.setEventHandler(new NegotiationDirectSellEventHandler(this));
            addListener(newNetworkServiceMessageListener);

        } catch (Exception e) {
            throw new CantStartServiceException(e, context, "An unexpected exception happened while trying to start the NegotiationDirectSellRecorderService.");
        }
        serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {
        removeRegisteredListeners();
        serviceStatus = ServiceStatus.STOPPED;
    }


    //PRIVATE METHODS

    private void addListener(FermatEventListener listener) {
        eventManager.addListener(listener);
        listenersAdded.add(listener);
    }


    private void removeRegisteredListeners() {
        for (FermatEventListener fermatEventListener : listenersAdded) {
            eventManager.removeListener(fermatEventListener);
        }
        listenersAdded.clear();
    }
    //GETTER AND SETTERS

    @Override
    public ServiceStatus getStatus() {
        return serviceStatus;
    }

    public List<FermatEventListener> getListenersAdded() {
        return Validate.verifyGetter(listenersAdded);
    }
    //INNER CLASSES

}
