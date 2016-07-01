package unit.com.bitdubai.fermat_cbp_plugin.layer.network_service.negotiation_transmission.developer.bitdubai.version_1.structure.NegotiationTransmissioAgent;


import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.PlatformComponentProfile;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_cbp_plugin.layer.network_service.negotiation_transmission.developer.bitdubai.version_1.NetworkServiceNegotiationTransmissionPluginRoot;
import com.bitdubai.fermat_cbp_plugin.layer.network_service.negotiation_transmission.developer.bitdubai.version_1.communication.structure.CommunicationNetworkServiceConnectionManager;
import com.bitdubai.fermat_cbp_plugin.layer.network_service.negotiation_transmission.developer.bitdubai.version_1.database.NegotiationTransmissionNetworkServiceConnectionsDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.network_service.negotiation_transmission.developer.bitdubai.version_1.database.NegotiationTransmissionNetworkServiceDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.network_service.negotiation_transmission.developer.bitdubai.version_1.structure.NegotiationTransmissionAgent;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.WsCommunicationsCloudClientManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Yordin Alayn on 23.10.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConstructionTest {

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM,       layer = Layers.PLATFORM_SERVICE,    addon = Addons.ERROR_MANAGER)
    @Mock
    private ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM,       layer = Layers.PLATFORM_SERVICE,    addon = Addons.EVENT_MANAGER)
    @Mock
    private EventManager eventManager;

    @NeededPluginReference(platform = Platforms.COMMUNICATION_PLATFORM, layer = Layers.COMMUNICATION,       plugin = Plugins.WS_CLOUD_CLIENT)
    @Mock
    private WsCommunicationsCloudClientManager wsCommunicationsCloudClientManager;

    @Mock
    private NegotiationTransmissionNetworkServiceDatabaseDao databaseDao;

    @Mock
    private NegotiationTransmissionNetworkServiceConnectionsDatabaseDao databaseConnectionsDao;

    @Mock
    private NetworkServiceNegotiationTransmissionPluginRoot pluginRoot;

    @Mock
    private PlatformComponentProfile platformComponentProfileRegistered;

    private CommunicationNetworkServiceConnectionManager communicationNetworkServiceConnectionManager;

    private NegotiationTransmissionAgent testMonitorAgent;

    @Test
    public void Construction_ValidParameters_NewObjectCreated() {
        ECCKeyPair identity = new ECCKeyPair();
        testMonitorAgent = new NegotiationTransmissionAgent(
                pluginRoot,
                databaseConnectionsDao,
                databaseDao,
                communicationNetworkServiceConnectionManager,
                wsCommunicationsCloudClientManager,
                platformComponentProfileRegistered,
                errorManager,
                new ArrayList<PlatformComponentProfile>(),
                identity,
                eventManager
        );
        assertThat(testMonitorAgent).isNotNull();
    }
}