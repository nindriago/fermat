package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients;

import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.request.CheckInProfileMsgRequest;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.CheckInProfileMsjRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.MessageContentType;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.PackageProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInNetworkService;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedNetworkServicesHistory;

import org.jboss.logging.Logger;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInNetworkServiceRequestProcessor</code>
 * process all packages received the type <code>MessageType.CHECK_IN_NETWORK_SERVICE_REQUEST</code><p/>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 06/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CheckInNetworkServiceRequestProcessor extends PackageProcessor {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(CheckInNetworkServiceRequestProcessor.class.getName());

    /**
     * Constructor  whit parameter
     *
     * @param fermatWebSocketChannelEndpoint register
     */
    public CheckInNetworkServiceRequestProcessor(FermatWebSocketChannelEndpoint fermatWebSocketChannelEndpoint) {
        super(fermatWebSocketChannelEndpoint, PackageType.CHECK_IN_NETWORK_SERVICE_REQUEST);
    }

    /**
     * (non-javadoc)
     *
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived) {

        LOG.info("Processing new package received");

        String channelIdentityPrivateKey = getChannel().getChannelIdentity().getPrivateKey();
        String destinationIdentityPublicKey = (String) session.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);
        NetworkServiceProfile networkServiceProfile = null;

        try {

            CheckInProfileMsgRequest messageContent = CheckInProfileMsgRequest.parseContent(packageReceived.getContent());

            /*
             * Create the method call history
             */
            methodCallsHistory(getGson().toJson(messageContent.getProfileToRegister()), destinationIdentityPublicKey);

            /*
             * Validate if content type is the correct
             */
            if (messageContent.getMessageContentType() == MessageContentType.JSON) {

                /*
                 * Obtain the profile of the network service
                 */
                networkServiceProfile = (NetworkServiceProfile) messageContent.getProfileToRegister();

                /*
                 * CheckedInNetworkService into data base
                 */
                insertCheckedInNetworkService(networkServiceProfile);

                /*
                 * CheckedInNetworkServiceHistory into data base
                 */
                insertCheckedInNetworkServiceHistory(networkServiceProfile);

                /*
                 * If all ok, respond whit success message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.SUCCESS, CheckInProfileMsjRespond.STATUS.SUCCESS.toString(), networkServiceProfile.getIdentityPublicKey());
                Package packageRespond = Package.createInstance(respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_NETWORK_SERVICE_RESPOND, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getBasicRemote().sendObject(packageRespond);

            }

        } catch (Exception exception) {

            try {

                LOG.error(exception.getMessage());

                /*
                 * Respond whit fail message
                 */
                CheckInProfileMsjRespond respondProfileCheckInMsj = new CheckInProfileMsjRespond(CheckInProfileMsjRespond.STATUS.FAIL, exception.getLocalizedMessage(), networkServiceProfile.getIdentityPublicKey());
                Package packageRespond = Package.createInstance(respondProfileCheckInMsj.toJson(), packageReceived.getNetworkServiceTypeSource(), PackageType.CHECK_IN_CLIENT_RESPOND, channelIdentityPrivateKey, destinationIdentityPublicKey);

                /*
                 * Send the respond
                 */
                session.getBasicRemote().sendObject(packageRespond);

            } catch (IOException iOException) {
                LOG.error(iOException.getMessage());
            } catch (EncodeException encodeException) {
                LOG.error(encodeException.getMessage());
            }

        }

    }

    /**
     * Create a new row into the data base
     *
     * @param networkServiceProfile
     * @throws CantInsertRecordDataBaseException
     */
    private void insertCheckedInNetworkService(NetworkServiceProfile networkServiceProfile) throws CantInsertRecordDataBaseException {

        /*
        * Create the checkedInNetworkService
        */
        CheckedInNetworkService checkedInNetworkService = new CheckedInNetworkService();
        checkedInNetworkService.setIdentityPublicKey(networkServiceProfile.getIdentityPublicKey());
        checkedInNetworkService.setClientIdentityPublicKey(networkServiceProfile.getClientIdentityPublicKey());
        checkedInNetworkService.setNetworkServiceType(networkServiceProfile.getNetworkServiceType().getCode());

        //Validate if location are available
        if (networkServiceProfile.getLocation() != null) {
            checkedInNetworkService.setLatitude(networkServiceProfile.getLocation().getLatitude());
            checkedInNetworkService.setLongitude(networkServiceProfile.getLocation().getLongitude());
        }

        /*
         * Save into the data base
         */
        getDaoFactory().getCheckedInNetworkServiceDao().create(checkedInNetworkService);
    }

    /**
     * Create a new row into the data base
     *
     * @param networkServiceProfile
     * @throws CantInsertRecordDataBaseException
     */
    private void insertCheckedInNetworkServiceHistory(NetworkServiceProfile networkServiceProfile) throws CantInsertRecordDataBaseException {

        /*
         * Create the CheckedClientsHistory
         */
        CheckedNetworkServicesHistory checkedNetworkServicesHistory = new CheckedNetworkServicesHistory();
        checkedNetworkServicesHistory.setIdentityPublicKey(networkServiceProfile.getIdentityPublicKey());
        checkedNetworkServicesHistory.setClientIdentityPublicKey(networkServiceProfile.getClientIdentityPublicKey());
        checkedNetworkServicesHistory.setNetworkServiceType(networkServiceProfile.getNetworkServiceType().getCode());
        checkedNetworkServicesHistory.setCheckType(CheckedNetworkServicesHistory.CHECK_TYPE_IN);

        //Validate if location are available
        if (networkServiceProfile.getLocation() != null) {
            checkedNetworkServicesHistory.setLastLatitude(networkServiceProfile.getLocation().getLatitude());
            checkedNetworkServicesHistory.setLastLongitude(networkServiceProfile.getLocation().getLongitude());
        }

        /*
         * Save into the data base
         */
        getDaoFactory().getCheckedNetworkServicesHistoryDao().create(checkedNetworkServicesHistory);

    }

}
